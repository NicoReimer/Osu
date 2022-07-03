package OsuClient;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static OsuClient.OsuClient.getSongsPath;
import static OsuClient.OsuClient.path_skin;

public class GameField extends JPanel {

    private Beatmap currentBeatmap;
    private double timeCounter;
    private int lastGameObject;
    private LinkedList drawObject;
    private Image  approachCircleImage;
    private Image  hitCircleOverlayImage;
    private Image  cursorImage;
    private Image  hitCircleImage;
    private Point mousePosition;
    private boolean buttonOne;
    private boolean buttonTwo;
    private Image[] numberImages;
    private Clip musicClip;

    private int combo;

    public void startGame(Beatmap beatmap, String mapName){

        try {
            this.approachCircleImage = ImageIO.read(new File(path_skin + "/approachcircle.png"));
            this.hitCircleOverlayImage = ImageIO.read(new File(path_skin + "/hitcircleoverlay.png"));
            this.cursorImage = ImageIO.read(new File(path_skin + "/cursor.png"));
            this.hitCircleImage = ImageIO.read(new File(path_skin + "/hitcircle.png"));
            this.numberImages = new Image[11];

            for (int i = 0; i < 11; i++){
                this.numberImages[i] = ImageIO.read(new File(path_skin + "/numbers-" + i + ".png"));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        addKeyBinding(this, KeyEvent.VK_A, "1Button", (evt)-> buttonOne = true);

        addKeyBinding(this, KeyEvent.VK_D, "2Button", (evt)-> buttonTwo = true);

        addKeyBinding(this, KeyEvent.VK_ESCAPE, "3Button", (evt)-> endGame());

        combo = 0;
        timeCounter = 180;
        drawObject = new LinkedList();
        lastGameObject = 0;
        currentBeatmap = beatmap;

        //start test song
        playMusic(getSongsPath()+ '/' +  mapName + "/song.wav");

        startGameTick();
    }

    //Game Cycle
    private void doGameCycle() {
        timeCounter++;
        manageCircles();
        repaint();

        buttonOne = false;
        buttonTwo = false;
    }

    public void startGameTick(){
        Runnable gameTick = () -> doGameCycle();

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(gameTick, 0, 1, TimeUnit.MILLISECONDS);
    }

    //RePaint Components
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }

    public void endGame(){

        musicClip.stop();

        JFrame parent = (JFrame) this.getTopLevelAncestor();
        parent.dispose();

        new OsuClient();
    }

    public void manageCircles(){

        mousePosition = MouseInfo.getPointerInfo().getLocation();

        //Todo add reset function and score list
        if(currentBeatmap._hitobjects.length <= lastGameObject) {
            endGame();
        }

        if (currentBeatmap._hitobjects[lastGameObject].getTiming() - 300 <= timeCounter) {
            drawObject.add(currentBeatmap._hitobjects[lastGameObject]);
            lastGameObject++;
        }

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

        for(int i = drawObject.size() - 1; i >= 0; i--){

            Circle circle = (Circle)drawObject.get(i);

            if(circle.getHit()) {
                drawObject.remove(i);
                combo++;
            }
            else if(timeCounter - circle.getTiming() > 300)
            {
                if(combo > 5)
                    playSound(path_skin + "/combobreak.wav");

                combo = 0;
                drawObject.remove(i);
            }
        }
    }

    //Draw Circles
    private void doDrawing(Graphics g) {

        //draw circle and approachRate
        for (int i = 0; i < drawObject.size(); i++) {

            Circle circle = (Circle)drawObject.get(i);

            int apporachCircleSize = (int) ((circle.getTiming() - timeCounter) * 0.8 + 150 + 240);

            if (apporachCircleSize < 240)
                apporachCircleSize = 240;

            g.drawImage(approachCircleImage, (int) circle.getPosX() - (apporachCircleSize / 2) + 120, (int) circle.getPosY() - (apporachCircleSize / 2) + 120, apporachCircleSize, apporachCircleSize, this);

            g.drawImage(hitCircleOverlayImage, (int) circle.getPosX() - 10, (int) circle.getPosY() - 10, this);

            g.drawImage(hitCircleImage, (int) circle.getPosX(), (int) circle.getPosY(), this);
        }

        //Todo: simplify Combo Draw
        if(combo < 10) {
            g.drawImage(numberImages[combo], 120, 975, this); //Zahl 1
            g.drawImage(numberImages[10], 160, 975, this); // X
        }
        else if(combo > 9 && combo < 100){
            g.drawImage(numberImages[combo / 10], 120, 975, this); //Zahl 1
            g.drawImage(numberImages[combo % 10], 160, 975, this); //Zahl 2
            g.drawImage(numberImages[10], 200, 975, this); // X
        }
        else if (combo > 99 && combo < 999){
            g.drawImage(numberImages[combo / 100], 120, 975, this);
            g.drawImage(numberImages[(combo / 10) % 10], 160, 975, this);
            g.drawImage(numberImages[combo % 10], 200, 975, this);
            g.drawImage(numberImages[10], 240, 975, this);
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
            }
            else{
                System.out.println("[ERROR] Can´t find file");
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public void playMusic(String musicLocation) {

        try{
            //Get File location
            File musicPath = new File(musicLocation);
            if(musicPath.exists())
            {
                //Get AudioInputStream and make a Clip out of it
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
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
