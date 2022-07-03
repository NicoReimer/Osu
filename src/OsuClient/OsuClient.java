package OsuClient;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class OsuClient extends JFrame {

    public  JPanel pnlMain;
    private JPanel pnlMenu;
    private JPanel pnlGameField;
    private JButton btnQuit;
    private JButton btnPlay;
    private JLabel lblOsu;
    private JPanel pnlBeatmaps;
    private JList ltBeatmaps;
    private Beatmap currentBeatmap;
    private GameField gameField = new GameField();
    private final CardLayout pnlLayout;
    private static final String path_songs = "res\\songs";
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

        pnlLayout = (CardLayout)pnlMain.getLayout();

        //Buttons
        btnPlay.addActionListener(e -> {
            pnlLayout.show(pnlMain,"cardBeatmaps");
            loadBeatmaps();
        });

        btnQuit.addActionListener(e -> System.exit(0));

        ltBeatmaps.addListSelectionListener(e -> {

            if(e.getValueIsAdjusting()) {

                //select Beatmap
                String selectedValue = (String) ltBeatmaps.getSelectedValue();

                //select GameField Layout
                pnlLayout.show(pnlMain, "cardPlayField");

                //create Beatmap
                currentBeatmap = new Beatmap(getSongsPath() + '/' + selectedValue, "/play.csv");

                //set background Color
                gameField.setBackground(Color.black);

                //add GameField to JFrame
                setContentPane(gameField);

                //remove Cursor
                removeCursor();

                //star the Game
                gameField.startGame(currentBeatmap, selectedValue);
            }
        });
    }

    public void removeCursor(){
        //Transparent 16 x 16 pixel cursor image.
        BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);

        //Create a new blank cursor.
        Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                cursorImg, new Point(0, 0), "blank cursor");

        //Set the blank cursor to the JFrame.
        getContentPane().setCursor(blankCursor);
    }

    public void loadBeatmaps(){
        DefaultListModel<String> model = new DefaultListModel<>();

        //Creating a File object for directory
        File directoryPath = new File(getSongsPath());
        //List of all files and directories
        String[] contents = directoryPath.list();

        if (contents != null) {
            for (String content : contents) {
                System.out.println(content);
                model.addElement(content);
            }
        }

        ltBeatmaps.setModel(model);
    }
    //plays sound files only wav
    public static void playSound(String musicLocation) {

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
}