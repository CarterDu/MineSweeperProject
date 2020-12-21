package minesweeper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MineSweeperWindow implements ActionListener {
    JFrame frame = new JFrame();
    ImageIcon bannerIcon = new ImageIcon("src/image/banner.png");
    ImageIcon guessIcon = new ImageIcon("src/image/guess.png");
    ImageIcon bombIcon = new ImageIcon("src/image/bomb.jpg");
    ImageIcon failIcon = new ImageIcon("src/image/fail.jpg");
    ImageIcon winIcon = new ImageIcon("src/image/win.jpg");
    ImageIcon flagIcon = new ImageIcon("src/image/flag.jpg");




    JButton bannerBtn = new JButton(bannerIcon);

    //data structure
    int ROW = 20;
    int COL = 20;
    int[][] data = new int[ROW][COL];   //used to store the number inside of each button
    JButton[][] btns = new JButton[ROW][COL];
    int MINECOUNT = 10;  //the number of mine
    int MINECODE = -1;  //mine = -1
    int unopened = ROW * COL;
    int opened = 0;
    int second = 0; //duration of game time

    JLabel label1 = new JLabel("待开: " + unopened);
    JLabel label2 = new JLabel("已开: " + opened);
    JLabel label3 = new JLabel("用时: " + second + "s");
    Timer timer = new Timer();

//    //menu area
//    JButton restartBtn = new JButton("Restart");
//    JButton exitBtn = new JButton("Exit");



    public MineSweeperWindow(){
        frame.setSize(900, 650);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());


        runTime();
        setHeader();
        addMine();
        setButton();


        frame.setVisible(true);
    }

    /**
     * running the time while the game is on
     */
    public void runTime(){

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                second++;
                label3.setText("用时: " + second + "s");
            }
        };

        timer.schedule(task, 1,  1000);
    }

    /**
     * The header pic & the button to restart the game
     */
    public void setHeader(){
        JPanel panel = new JPanel(new GridBagLayout());

        GridBagConstraints c1 = new GridBagConstraints(0,0,3,1,1.0,1.0,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0,0,0,0), 0,0);
        panel.add(bannerBtn, c1);

        bannerBtn.addActionListener(this);



        label1.setOpaque(true); //设置成不透明
        label1.setBackground(Color.white);
        label1.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        label2.setOpaque(true);
        label2.setBackground(Color.white);
        label2.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        label3.setOpaque(true);
        label3.setBackground(Color.white);
        label3.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        bannerBtn.setOpaque(true);
        bannerBtn.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        bannerBtn.setBackground(Color.white);

        GridBagConstraints c2 = new GridBagConstraints(0,1,1,1,1.0,1.0,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0,0,0,0), 0,0);
        panel.add(label1, c2);
        GridBagConstraints c3 = new GridBagConstraints(1,1,1,1,1.0,1.0,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0,0,0,0), 0,0);
        panel.add(label2, c3);
        GridBagConstraints c4 = new GridBagConstraints(2,1,1,1,1.0,1.0,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0,0,0,0), 0,0);
        panel.add(label3, c4);

        frame.add(panel, BorderLayout.NORTH);
    }

    /**
     * Place mines on the grid
     */
    public void addMine(){
        Random rand = new Random();
        for(int i = 0; i < MINECOUNT;) {
            int r = rand.nextInt(ROW);  //generate a rand num between 0 to 20
            int c = rand.nextInt(COL);

            if(data[r][c] != MINECODE){
                data[r][c] = MINECODE;
                i++;    //avoid place the mine on the same position so no increment here
            }

        }

        //calculate the nearby # of mines

        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                int tempCount = 0;
                if(data[i][j] == MINECODE) continue;
                if(i>0 && j>0 && data[i-1][j-1]==MINECODE){tempCount++;}
                if(i>0 && data[i-1][j]==MINECODE){tempCount++;}
                if(i>0 && j<19 && data[i-1][j+1]==MINECODE){tempCount++;}
                if(j>0 && data[i][j-1]==MINECODE){tempCount++;}
                if(j<19 && data[i][j+1]==MINECODE){tempCount++;}
                if(i<19 && j>0 && data[i+1][j-1]==MINECODE){tempCount++;}
                if(i<19 && data[i+1][j]==MINECODE){tempCount++;}
                if(i<19 && j<19 && data[i+1][j+1]==MINECODE){tempCount++;}
                data[i][j] = tempCount;


            }

        }
    }


    public void setButton(){
        Container c = new Container();
        c.setLayout(new GridLayout(ROW, COL));

        for (int i = 0; i < ROW; i++){
            for(int j = 0; j < COL; j++){
                JButton btn = new JButton(guessIcon);
                btn.setOpaque(true);
                btn.setBackground(Color.ORANGE);

                btn.setMargin(new Insets(0,0,0,0));
                btn.addActionListener(this);
                c.add(btn);
                btns[i][j] = btn;
            }
        }
        frame.add(c, BorderLayout.CENTER);
    }



    public static void main(String[] args) {
        new MineSweeperWindow();
    }

    /**
     * When click the block
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton btn = (JButton) e.getSource();

        if(btn.equals(bannerBtn)){
            restart();
            return;
        }

        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                if(btn.equals(btns[i][j]))
                {
                    if(data[i][j] == MINECODE)
                    {
                        fail();
                    }
                    else
                    {
                        openBlock(i, j);
                        checkWin();
                    }
                    return;
                }
            }

        }
    }

    /**
     * restart the games
     * 1. clean the data
     * 2. recover the status of buttons
     * 3. reset the timer
     */
    private void restart(){

        //recover the data and buttons
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                data[i][j] = 0;
                btns[i][j].setBackground(Color.ORANGE);
                btns[i][j].setEnabled(true);
                btns[i][j].setText("");
                btns[i][j].setIcon(guessIcon);

            }
        }

        //recover the header
        unopened = ROW * COL;
        opened = 0;
        second = 0;
        label1.setText("待开: " + unopened);
        label2.setText("已开: " + opened);
        label3.setText("用时: " + second + "s");


        addMine();
        runTime();

    }

    /**
     * Check whether we win when opening the block each time
     */
    private void checkWin() {
        int unopenedCount = 0;
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                if (btns[i][j].isEnabled()) unopenedCount++;
            }
        }

        if (unopenedCount == MINECOUNT) {
            for (int i = 0; i < ROW; i++) {
                for (int j = 0; j < COL; j++) {
                    if (btns[i][j].isEnabled()) {
                        btns[i][j].setIcon(flagIcon);
                    }
                }
            }
            bannerBtn.setIcon(winIcon);
            timer.cancel();
            JOptionPane.showMessageDialog(frame, "You Win! \nClick the top banner to restart!", "WIN", JOptionPane.PLAIN_MESSAGE);
        }


    }

    private void openBlock(int i, int j){
        JButton btn = btns[i][j];
        if(!btn.isEnabled()) return;

        btn.setIcon(null);
        btn.setEnabled(false);
        btn.setOpaque(true);
        btn.setBackground(Color.pink);
        btn.setText(data[i][j] + "");
        updateNumOfBlock();

        if(data[i][j] == 0){
            if(i>0 && j>0 && data[i-1][j-1]==0){openBlock(i-1, j-1);}
            if(i>0 && data[i-1][j]==0){openBlock(i-1, j);}
            if(i>0 && j<19 && data[i-1][j+1]==0){openBlock(i-1, j+1);}
            if(j>0 && data[i][j-1]==0){openBlock(i, j-1);}
            if(j<19 && data[i][j+1]==0){openBlock(i, j+1);}
            if(i<19 && j>0 && data[i+1][j-1]==0){openBlock(i+1, j-1);}
            if(i<19 && data[i+1][j]==0){openBlock(i+1, j);}
            if(i<19 && j<19 && data[i+1][j+1]==0){openBlock(i+1, j+1);}
        }

    }

    /**
     * Calculate the rest of opened and unopened blocks
     */
    private void updateNumOfBlock(){
        opened++;
        unopened--;
        label1.setText("待开: " + unopened);
        label2.setText("已开: " + opened);
    }

    /**
     * lose the game
     */
    private void fail(){

        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                if(btns[i][j].isEnabled())
                {
                    JButton btn = btns[i][j];
                    if(data[i][j] == MINECODE)
                    {
                        btn.setEnabled(false);
                        btn.setIcon(bombIcon);
                        btn.setDisabledIcon(bombIcon);
                    }

                    else
                    {
                        btn.setIcon(null);
                        btn.setEnabled(false);
                        btn.setOpaque(true);
                        btn.setText(data[i][j] + "");
                    }
                }
            }
        }
        bannerBtn.setIcon(failIcon);
        timer.cancel();
        JOptionPane.showMessageDialog(frame, "Sorry you hit the mine! \nClick the top banner to restart!", "HIT MINE!", JOptionPane.PLAIN_MESSAGE);

    }


}
