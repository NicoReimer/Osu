package OsuClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OsuClient extends JFrame {
    private JPanel pnlMain;

    public OsuClient() {
        //ImageIcon icon = new ImageIcon("src/Chatogram/Client/img/ChatogramIcon.png");
        //this.setIconImage(icon.getImage());
        this.setContentPane(this.pnlMain);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setTitle("osu!");
        this.setResizable(false);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.dispose();
        this.setUndecorated(true);
        this.setVisible(true);
    }
}