import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MenuGraphics implements ChangeListener, ActionListener {

    Game myGame;
    MyFonts mf = new MyFonts();

    int x,y;

    JFrame window = new JFrame("Snake Game");

    JLabel text = new JLabel();
    JLabel title = new JLabel();
    JLabel speed = new JLabel();
    JLabel gameOverText = new JLabel();
    JLabel winText = new JLabel();

    JButton button_exit = new JButton();
    JButton button_restart = new JButton();
    JButton button_ia = new JButton();

    JCheckBox check1 = new JCheckBox();
    JCheckBox check3 = new JCheckBox();
    JCheckBox checkPath = new JCheckBox();

    JSlider mySpeed = new JSlider(0,15,0);

    public MenuGraphics(Game mg, MyFonts mf, int x, int y){
        this.myGame = mg; this.mf = mf; this.x = x; this.y = y;
    }

    public void Load(){

        window.addKeyListener(myGame);
        window.setFocusable(true);
        window.setResizable(true);
        window.setUndecorated(true);
        window.setVisible(true);
        window.pack();
        window.setSize(new Dimension(x+120, y));
        window.setLocationRelativeTo(null);

        mySpeed.setBounds(x+45,35,70,20);
        mySpeed.addChangeListener(myGame);
        mySpeed.setMaximum(13);
        
        button_exit.addActionListener(myGame);
        button_exit.setBounds(x+98, y-18, 20, 18);
        button_exit.setFont(mf.font_slkscr.deriveFont(14F));
        button_exit.setFocusable(false);
        button_exit.setText("X");
        button_exit.setForeground(Color.white);
        button_exit.setBackground(Color.gray);
        button_exit.setBorder(BorderFactory.createEtchedBorder());

        button_restart.addActionListener(myGame);
        button_restart.setEnabled(false);
        button_restart.setBounds(x+2, y-18, 45, 18);
        button_restart.setFont(mf.font_slkscr.deriveFont(12F));
        button_restart.setFocusable(false);
        button_restart.setText("Restart");
        button_restart.setForeground(Color.black);
        button_restart.setBackground(Color.gray);
        button_restart.setBorder(BorderFactory.createEtchedBorder());

        button_ia.addActionListener(myGame);
        button_ia.setBounds(x+50, y-18, 45, 18);
        button_ia.setFont(mf.font_slkscr.deriveFont(12F));
        button_ia.setFocusable(false);
        button_ia.setText("IA");
        button_ia.setForeground(Color.black);
        button_ia.setBackground(Color.gray);
        button_ia.setBorder(BorderFactory.createEtchedBorder());

        check1.setForeground(Color.black);
        check1.setText("Limits");
        check1.setFocusable(false);
        check1.setFont(mf.font_slkscr.deriveFont(13F));
        check1.setBounds(x+46, 55, 55, 15);

        check3.setText("Mute");
        check3.setFocusable(false);
        check3.setFont(mf.font_slkscr.deriveFont(13F));
        check3.setBounds(x, 55, 50, 15);

        checkPath.setText("IA Paths");
        checkPath.setFocusable(false);
        checkPath.setFont(mf.font_slkscr.deriveFont(13F));
        checkPath.setBounds(x, 70, 70, 15);

        text.setText("Points");
        text.setFont(mf.font_slkscr.deriveFont(14F));
        text.setForeground(Color.black);
        text.setBounds(x+5, 20, 150, 15);

        gameOverText.setText("Game Over");
        gameOverText.setVisible(false);
        gameOverText.setFont(mf.font_slkscr.deriveFont(16F));
        gameOverText.setForeground(Color.black);
        gameOverText.setBounds(x+25, (y/2)+22, 72, 15);

        winText.setText("Win");
        winText.setVisible(false);
        winText.setFont(mf.font_slkscr.deriveFont(16F));
        winText.setForeground(Color.black);
        winText.setBounds(x+48, (y/2)+25, 25, 15);

        title.setText("Snake Game");
        title.setFont(mf.font_title.deriveFont(14F));
        title.setForeground(Color.black);
        title.setBounds(x+30, 0, 65, 15);

        speed.setText("Speed");
        speed.setFont(mf.font_slkscr.deriveFont(14F));
        speed.setForeground(Color.black);
        speed.setBounds(y+5, 35, 100, 15);
        
        window.add(mySpeed);
        window.add(winText);
        window.add(button_ia);
        window.add(gameOverText);
        window.add(speed); 
        window.add(text);
        window.add(title);
        window.add(check1);
        window.add(check3);
        window.add(checkPath);
        window.add(button_restart);
        window.add(button_exit);
        window.add(myGame);

    }


    @Override
    public void actionPerformed(ActionEvent e) {}

    @Override
    public void stateChanged(ChangeEvent e) {}
    
}
