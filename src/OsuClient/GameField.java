package OsuClient;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static OsuClient.OsuClient.getSongsPath;
import static OsuClient.OsuClient.path_skin;

public class GameField extends JPanel {
    private Beatmap currentBeatmap;
    private double timeCounter;
    private int lastGameObject;
    private ArrayList drawObject;
    private Image  approachCircleImage;
    private Image  hitCircleOverlayImage;
    private Image  cursorImage;
    private Image  hitCircleImage;
    private Point mousePosition;
    private boolean buttonOne;
    private boolean buttonTwo;
    private Image[] numberImages;

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

        combo = 0;
        timeCounter = 180;
        drawObject = new ArrayList();
        lastGameObject = 0;
        currentBeatmap = beatmap;

        //start test song
        OsuClient.playSound(getSongsPath()+ '/' +  mapName + "/song.wav");

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

    public void manageCircles(){

        mousePosition = MouseInfo.getPointerInfo().getLocation();

        if(currentBeatmap._hitobjects[lastGameObject].getTiming() - 300 <= timeCounter)
        {
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
                        case 0 -> OsuClient.playSound(path_skin + "/drum-hitnormal.wav");
                        case 1 -> OsuClient.playSound(path_skin + "/drum-hitwhistle.wav");
                        case 2 -> OsuClient.playSound(path_skin + "/drum-hitfinish.wav");
                        case 3 -> OsuClient.playSound(path_skin + "/drum-hitclap.wav");
                        default -> OsuClient.playSound(path_skin + "/drum-hitnormal.wav");
                    }

                    buttonOne = false;
                    buttonTwo = false;


                }
            }
        }
    }

    //Draw Circles
    private void doDrawing(Graphics g) {


        for (int i = 0; i < drawObject.size(); i++) {

            Circle circle = (Circle)drawObject.get(i);

            int apporachCircleSize = (int) ((circle.getTiming() - timeCounter) * 0.8 + 150 + 240);

            if (apporachCircleSize < 240)
                apporachCircleSize = 240;

            g.drawImage(approachCircleImage, (int) circle.getPosX() - (apporachCircleSize / 2) + 120, (int) circle.getPosY() - (apporachCircleSize / 2) + 120, apporachCircleSize, apporachCircleSize, this);

            g.drawImage(hitCircleOverlayImage, (int) circle.getPosX() - 10, (int) circle.getPosY() - 10, this);

            g.drawImage(hitCircleImage, (int) circle.getPosX(), (int) circle.getPosY(), this);
        }

        for(int i = 0; i < drawObject.size(); i++){

            Circle circle = (Circle)drawObject.get(i);

            if(circle.getHit()) {
                drawObject.remove(i);
                combo++;
            }
            else if(timeCounter - circle.getTiming() > 300)
            {
                if(combo > 5)
                 OsuClient.playSound(path_skin + "/combobreak.wav");

                combo = 0;
                drawObject.remove(i);
            }
        }

        if(mousePosition == null) return;


        //Todo: simplify Combo Draw
        if(combo < 10) {
            g.drawImage(numberImages[combo], 120, 975, this);
            g.drawImage(numberImages[10], 160, 975, this);
        }
        else if(combo > 9 && combo < 99){
            g.drawImage(numberImages[combo / 10], 120, 975, this);
            g.drawImage(numberImages[combo % 10], 160, 975, this);
            g.drawImage(numberImages[10], 200, 975, this);
        }
        else if (combo == 100){
            g.drawImage(numberImages[1], 120, 975, this);
            g.drawImage(numberImages[0], 160, 975, this);
            g.drawImage(numberImages[0], 200, 975, this);
            g.drawImage(numberImages[10], 240, 975, this);
        }
        else if (combo > 100 && combo < 999){
            g.drawImage(numberImages[combo / 100], 120, 975, this);
            g.drawImage(numberImages[(combo / 10) % 10], 160, 975, this);
            g.drawImage(numberImages[combo % 10], 200, 975, this);
            g.drawImage(numberImages[10], 240, 975, this);
        }


        g.drawImage(cursorImage, mousePosition.x - 128, mousePosition.y- 128,this);
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
