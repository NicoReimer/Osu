package OsuClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.util.ArrayList;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class OsuClient extends JFrame {
    private JPanel pnlMain;
    private JPanel pnlMenu;
    private JButton btnQuit;
    private JButton btnPlay;
    private JLabel lblOsu;
    private JPanel pnlBeatmaps;
    private JList ltBeatmaps;
    private JPanel pnlGameField;
    private static String path_songs = "res\\songs";

    static String path_skin = "res\\skin";

    public OsuClient() {
        //ImageIcon icon = new ImageIcon("src/Chatogram/Client/img/ChatogramIcon.png");
        //this.setIconImage(icon.getImage());
        //playSound("sounds/background.wav");

        //Init Menu
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

        CardLayout pnlLayout = (CardLayout) pnlMain.getLayout();

        //Set Cursor
        setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
                new ImageIcon(path_skin + "/cursor.png").getImage(),
                new Point(0,0),"custom cursor"));

        //Todo: rework cursor function

        //Buttons
        btnPlay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pnlLayout.show(pnlMain,"cardBeatmaps");
                loadBeatmaps();
            }
        });
        btnQuit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        ltBeatmaps.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {

                //Todo: make this only trigger one time


                //select Beatmap
                String selectedValue = (String) ltBeatmaps.getSelectedValue();

                System.out.println(getSongsPath() + selectedValue);

                pnlLayout.show(pnlMain,"cardPlayField");

                //create Beatmap
                Beatmap playBeatmap = new Beatmap(getSongsPath()+ '/' +  selectedValue,"/play.csv");

                //Test Start Song
                //playSound(getSongsPath()+ '/' +  selectedValue + "/song.wav");

                //Todo: create Beatmap Objekt from this
                //playBeatmap.Start();


            }
        });
    }

    public void loadBeatmaps(){
        DefaultListModel<String> model = new DefaultListModel<>();

        //Creating a File object for directory
        File directoryPath = new File(getSongsPath());
        //List of all files and directories
        String[] contents = directoryPath.list();

        for (String content : contents) {
            System.out.println(content);
            model.addElement(content);
        }

        ltBeatmaps.setModel(model);
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

    public static String getSongsPath()
    {
        return path_songs;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}