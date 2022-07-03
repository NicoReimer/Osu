package OsuClient;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;

import static OsuClient.OsuClient.getSongsPath;
import static OsuClient.OsuClient.path_skin;


//Ende Freezing
//Timing Hitanzeige
//Scoresystem != Combo
//Random Bugs
//Pause Menu
//Settings

//Präsentation
//Dokumentation


public class GameField extends JPanel {

    private Beatmap currentBeatmap;
    private double timeCounter;
    private int lastGameObject;
    private LinkedList drawObject;
    private Image  approachCircleImage;
    private Image  hitCircleOverlayImage;
    private Image  cursorImage;
    private Image  hitCircleImage;
    private Image  scoreboardImage;
    private Image  menuBackImage;
    private Point mousePosition;
    private boolean buttonOne;
    private boolean buttonTwo;
    private Image[] numberImages;
    private ArrayList<Map.Entry<Clip, Double>> soundClips;
    private Clip musicClip;
    private int combo;
    private int score;
    private int musicLength;
    private boolean drawScoreboard;
    private int threehundredCount;
    private int hundredCount;
    private int fiftyCount;
    private int missCount;
    private int maxCombo;
    private boolean isPaused;
    private boolean mousePressed;
    private boolean restart;

    private ScheduledExecutorService executor;



    public void startGame(Beatmap beatmap, String mapName){

        //Load Images from resource Folder
        try {
            this.approachCircleImage = ImageIO.read(new File(path_skin + "/approachcircle.png"));
            this.hitCircleOverlayImage = ImageIO.read(new File(path_skin + "/hitcircleoverlay.png"));
            this.cursorImage = ImageIO.read(new File(path_skin + "/cursor.png"));
            this.hitCircleImage = ImageIO.read(new File(path_skin + "/hitcircle.png"));
            this.numberImages = new Image[11];
            this.scoreboardImage = ImageIO.read(new File(path_skin + "/scoreboard.png"));
            this.menuBackImage = ImageIO.read(new File(path_skin + "/menu-back.png"));

            for (int i = 0; i < 11; i++){
                this.numberImages[i] = ImageIO.read(new File(path_skin + "/numbers-" + i + ".png"));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //Adding Key binds
        addKeyBinding(this, KeyEvent.VK_A, "1Button", (evt)-> buttonOne = true);

        addKeyBinding(this, KeyEvent.VK_D, "2Button", (evt)-> buttonTwo = true);

        addKeyBinding(this, KeyEvent.VK_ESCAPE, "3Button", (evt)-> pauseGame());

        //Adding Mouse Listner
        addMouseListener(new MouseAdapter() {
            private Color background;

            @Override
            public void mousePressed(MouseEvent e) {

                mousePressed = true;
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                mousePressed = false;
            }
        });

        //Add game Variables
        drawScoreboard = false;
        isPaused = false;
        restart = true;
        combo = 0;
        score = 0;
        threehundredCount = 0;
        hundredCount = 0;
        fiftyCount = 0;
        missCount = 0;
        maxCombo = 0;
        drawObject = new LinkedList();
        soundClips = new ArrayList<>();
        lastGameObject = 0;
        currentBeatmap = beatmap;

        //Start a timer
        long start = System.currentTimeMillis();

        //Start Song
        playMusic(getSongsPath()+ '/' +  mapName + "/song.wav");

        //End a timer
        long end = System.currentTimeMillis();

        //Calculate the time to load the Song and use it as offset
        timeCounter = end - start;

        //Start Game Cycle
        startGameTick();
    }

    public void pauseGame(){

        if(!isPaused) {
            isPaused = true;
            musicClip.stop();
        }
        else{
            isPaused = false;
            musicClip.start();
        }
    }

    //Game Cycle
    private void doGameCycle() {

        if(!isPaused)
            timeCounter++;

        manageSounds();
        manageCircles();
        repaint();

        buttonOne = false;
        buttonTwo = false;
    }

    public void startGameTick(){
        Runnable gameTick = () -> doGameCycle();

        executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(gameTick, 0, 1, TimeUnit.MILLISECONDS);
    }

    //RePaint Components
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }

    public void endGame(){

        if(!restart) return;

        restart = false;

        musicClip.stop();

        JFrame parent = (JFrame) this.getTopLevelAncestor();
        parent.dispose();

        new OsuClient();
    }

    public void manageSounds(){

        if(soundClips.size() == 0)
            return;

        for(int i = 0;  i < soundClips.size(); i++){

            if(soundClips.get(i).getValue() <= timeCounter) {

                soundClips.get(i).getKey().flush();
                soundClips.remove(i);
            }
        }
    }

    public int modifier(Circle circle){

        //Make 200ms hit window
        int difference = (int) (circle.getTiming() - timeCounter) + 200;

        if(difference > -32 && difference < 32) {

            threehundredCount++;
            return 300;
        }
        else if(difference > -76 && difference < 76){

            hundredCount++;
            return 100;
        }
        else if(difference > -120 && difference < 120){

            fiftyCount++;
            return 50;
        }

        return 0;
    }

    public void drawElements(Graphics g, String string, int x, int y, boolean rightAlign,boolean drawX){

        char[] numberChars = string.toCharArray();
        int relativeX = x;

        if (rightAlign) {
            for (int i = numberChars.length - 1; i >= 0; i--) {

                g.drawImage(numberImages[numberChars[i] - '0'], relativeX, y, this);
                relativeX -= 40;
            }
        } else {
            for (int i = 0; i < numberChars.length; i++) {

                g.drawImage(numberImages[numberChars[i] - '0'], relativeX, y, this);
                relativeX += 40;
            }

            if(drawX)
                g.drawImage(numberImages[10], relativeX, y, this);
        }
    }

    public void drawEndscore(Graphics g){

        //draw Scoreboard Frame
        g.drawImage(scoreboardImage, 0, 0, this);

        //draw Score
        drawElements(g,String.valueOf(score),350,60,false,false);
        drawElements(g,String.valueOf(threehundredCount),260,260,false,true);
        drawElements(g,String.valueOf(hundredCount),260,452,false,true);
        drawElements(g,String.valueOf(fiftyCount),260,645,false,true);
        drawElements(g,String.valueOf(missCount),850,645,false,true);
        drawElements(g,String.valueOf(maxCombo),260,855,false,true);

        //draw back Button
        g.drawImage(menuBackImage, 1400,820, this);

        //check if back Button is pressed
        Rectangle imageBounds = new Rectangle(1400,820,404, 212);
        if (imageBounds.contains(mousePosition)){
            if(mousePressed)
                endGame();
        }
    }

    public void manageCircles() {

        mousePosition = MouseInfo.getPointerInfo().getLocation();

        if(isPaused)
            return;

        //Reset Game
        if (currentBeatmap._hitobjects.length - 1 < lastGameObject && timeCounter > musicLength) {
            drawScoreboard = true;
        }

        //Adding HitCircles
        if (currentBeatmap._hitobjects.length - 1 >= lastGameObject){
            if (currentBeatmap._hitobjects[lastGameObject].getTiming() - 300 <= timeCounter) {
                drawObject.add(currentBeatmap._hitobjects[lastGameObject]);
                lastGameObject++;
            }
        }

        //HitSound and Set Circle Hit
        for (int i = 0; i < drawObject.size(); i++) {

            Circle circle = (Circle)drawObject.get(i);

            if (circle.getHit())
                return;

            //Check if we hit the Ballon and reset Input
            if ((int) circle.getPosX() + 240 > mousePosition.x && (int) circle.getPosX() < mousePosition.x && (int) circle.getPosY() + 240 > mousePosition.y && (int) circle.getPosY() < mousePosition.y) {

                if (buttonOne || buttonTwo) {

                    circle.setHit(true);

                    //Play Sound
                    switch (circle.getHitsound()) {
                        case 0 -> playSound(path_skin + "/drum-hitnormal.wav");
                        case 1 -> playSound(path_skin + "/drum-hitwhistle.wav");
                        case 2 -> playSound(path_skin + "/drum-hitfinish.wav");
                        case 3 -> playSound(path_skin + "/drum-hitclap.wav");
                        default -> playSound(path_skin + "/drum-hitnormal.wav");
                    }

                    buttonOne = false;
                    buttonTwo = false;
                }
            }
        }

        //Remove, add Combo/Score and Miss Circle
        for(int i = 0; i < drawObject.size(); i++){

            Circle circle = (Circle)drawObject.get(i);

            if(circle.getHit()) {
                drawObject.remove(i);

                int getModifier = modifier(circle);

                if(getModifier == 0) {
                     processMiss();
                     return;
                }

                combo++;

                if(combo > maxCombo)
                    maxCombo = combo;

                score += combo * getModifier;
            }
            else if(timeCounter - circle.getTiming() > 300)
            {
                drawObject.remove(i);

                processMiss();
            }
        }
    }

    public void processMiss(){
        if(combo > 5)
            playSound(path_skin + "/combobreak.wav");

        missCount++;
        combo = 0;
    }

    public void drawCircles(Graphics g){

        //draw circle and approachRate
        if (drawObject.size() > 0){
            for (int i = 0; i < drawObject.size(); i++) {

                Circle circle = (Circle) drawObject.get(i);

                int apporachCircleSize = (int) ((circle.getTiming() - timeCounter) * 0.8 + 150 + 240);

                if (apporachCircleSize < 240)
                    apporachCircleSize = 240;

                g.drawImage(approachCircleImage, (int) circle.getPosX() - (apporachCircleSize / 2) + 120, (int) circle.getPosY() - (apporachCircleSize / 2) + 120, apporachCircleSize, apporachCircleSize, this);

                g.drawImage(hitCircleOverlayImage, (int) circle.getPosX() - 10, (int) circle.getPosY() - 10, this);

                g.drawImage(hitCircleImage, (int) circle.getPosX(), (int) circle.getPosY(), this);
            }
        }

        //Draw Combo
        drawElements(g,String.valueOf(combo),120,975,false,true);

        //Draw Score
        drawElements(g,String.valueOf(score),1860,5,true,false);
    }

    private void doDrawing(Graphics g) {


        if(drawScoreboard) {
            drawEndscore(g);
        }
        else {
            drawCircles(g);
        }

        if(mousePosition == null) return;

        g.drawImage(cursorImage, mousePosition.x - 128, mousePosition.y- 128,this);
    }

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

                soundClips.add(soundClips.size(), Map.entry(clip,timeCounter + getAudioLength(audioInput,musicPath)));
            }
            else{
                System.out.println("[ERROR] Can´t find file");
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public int getAudioLength( AudioInputStream audioInput, File file){
        AudioFormat format = audioInput.getFormat();
        long audioFileLength = file.length();
        int frameSize = format.getFrameSize();
        float frameRate = format.getFrameRate();
        float durationInSeconds = (audioFileLength / (frameSize * frameRate));

         return (int) (durationInSeconds * 1000);
    }

    public void playMusic(String musicLocation) {

        //Make Sure that only one Clip is Running
        if(musicClip != null)
         if(musicClip.isRunning())
                return;

        try{
            //Get File location
            File musicPath = new File(musicLocation);
            if(musicPath.exists())
            {
                //Get AudioInputStream and make a Clip out of it
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);

                musicLength = getAudioLength(audioInput,musicPath);

                musicClip = AudioSystem.getClip();
                musicClip.open(audioInput);
                //Start the Clip
                musicClip.start();
            }
            else{
                System.out.println("[ERROR] Can´t find file");
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public static void addKeyBinding(JComponent comp, int keyCode, String id, ActionListener actionListener){

        InputMap im = comp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap ap = comp.getActionMap();

        im.put(KeyStroke.getKeyStroke(keyCode, 0, false),
                id);

        ap.put(id, new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent e) {
                actionListener.actionPerformed(e);
            }

        });

    }
}
