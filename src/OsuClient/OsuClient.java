package OsuClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class OsuClient extends JFrame {
    private JPanel pnlMain;

    public OsuClient() {
        //ImageIcon icon = new ImageIcon("src/Chatogram/Client/img/ChatogramIcon.png");
        //this.setIconImage(icon.getImage());
        playSound("sounds/background.wav");
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

    //plays sound files only wav
    public void playSound(String musicLocation) {

        try{
            //Get File location
            File musicPath = new File(musicLocation);
            if(musicPath.exists())
            {
                //Get AudioInputStream and make a Clip out of it
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInput);
                //Start the Clip
                clip.start();
            }
            else{
                System.out.println("[ERROR] Can´t find file");
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
}