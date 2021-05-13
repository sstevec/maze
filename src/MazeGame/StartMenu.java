package MazeGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartMenu implements ActionListener {

    private GameInitializer gameInitializer;
    private int defaultSize = 10;

    private JFrame jFrame;
    private JLabel title;
    private JButton startButton;

    public StartMenu(){
        jFrame = new JFrame("MAZE GAME !!!");

        jFrame.setBounds(450, 200, 1000, 600);
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setLayout(null);

        title = new JLabel("Welcome To MAZE GAME !!!", JLabel.CENTER);
        startButton = new JButton("Start");

        startButton.addActionListener(this);

        title.setFont(new java.awt.Font("Dialog", Font.BOLD,50));
        startButton.setFont(new java.awt.Font("Dialog", Font.BOLD,20));

        title.setBounds(0,0,1000,300);
        startButton.setBounds(400,300,200,60);

        jFrame.getContentPane().add(title);
        jFrame.add(startButton);



    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object eventObject = e.getSource();
        if(eventObject == startButton){
            gameInitializer = new GameInitializer(defaultSize, this);
            jFrame.setVisible(false);
            gameInitializer.initGame();
        }
    }

    public void show(){
        jFrame.setVisible(true);
        title.setText("YOU DIED !!! TRY AGAIN?");
    }
}
