package OsuClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;


public class OsuClient extends JFrame {

    public int panelType;
    public  JPanel cards;
    private Beatmap currentBeatmap;
    private final BeatmapMenu beatmapMenu = new BeatmapMenu();
    private final GameField gameField = new GameField();
    private final MainMenu mainMenu = new MainMenu();
    private static final String path_songs = "res\\songs";
    static String path_skin = "res\\skin";
    final static String MAINMENU = "Card with JButtons";
    final static String BEATMAPMENU = "Card with JTextField";
    final static String GAMEFIELD = "Card with GameField";


    public OsuClient() {
        //Set Icon
        ImageIcon icon = new ImageIcon("res\\osuicon.png");
        this.setIconImage(icon.getImage());

        //Init Menu
        cards = new JPanel(new CardLayout());

        this.setContentPane(this.cards);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setTitle("osu!");
        this.setResizable(false);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.dispose();
        this.setUndecorated(true);
        this.setVisible(true);

        panelType = 0;
        cards.add(mainMenu, MAINMENU);
        cards.add(beatmapMenu, BEATMAPMENU);
        cards.add(gameField, GAMEFIELD);

        CardLayout cl = (CardLayout)(cards.getLayout());
        cl.show(cards, MAINMENU);

        mainMenu.LoadMenu();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {

                if (panelType == 0) {

                    Rectangle imageBounds = new Rectangle(770, 347, 368, 127);

                    if (imageBounds.contains(MouseInfo.getPointerInfo().getLocation())) {
                        cl.show(cards, BEATMAPMENU);
                        beatmapMenu.LoadMenu();
                        panelType = 1;
                    }

                    Rectangle imageBounds2 = new Rectangle(793, 561, 339, 106);

                    if (imageBounds2.contains(MouseInfo.getPointerInfo().getLocation())) {
                        System.exit(0);

                    }
                }
                else if(panelType == 1){

                    Rectangle imageBounds = new Rectangle(1101, 190, 762, 168);

                    if (imageBounds.contains(MouseInfo.getPointerInfo().getLocation())) {

                        currentBeatmap = new Beatmap(getSongsPath() + '/' + "140662 cYsmix feat. Emmy - Tear Rain", "/play.csv");

                        //set background Color
                        gameField.setBackground(Color.black);

                        //remove Cursor
                        removeCursor();

                        cl.show(cards, GAMEFIELD);

                        //star the Game
                        gameField.startGame(currentBeatmap, "140662 cYsmix feat. Emmy - Tear Rain");

                        panelType = 2;
                    }

                    Rectangle imageBounds2 = new Rectangle(1112, 413, 751, 161);

                    if (imageBounds2.contains(MouseInfo.getPointerInfo().getLocation())) {

                        currentBeatmap = new Beatmap(getSongsPath() + '/' + "89912 Stan SB - Let This Go", "/play.csv");

                        //set background Color
                        gameField.setBackground(Color.black);

                        //remove Cursor
                        removeCursor();

                        cl.show(cards, GAMEFIELD);

                        panelType = 2;

                        //star the Game
                        gameField.startGame(currentBeatmap, "89912 Stan SB - Let This Go");

                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
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

    public static String getSongsPath()
    {
        return path_songs;
    }
}