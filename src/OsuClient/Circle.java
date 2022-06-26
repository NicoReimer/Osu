package OsuClient;

import javax.swing.*;
import java.awt.*;

import static OsuClient.OsuClient.path_skin;

public class Circle {

    //variables
    private double x, y, timing, hitsound;
    private boolean hit;
    private Image image;

    //constructor
    Circle(double pX, double pY, double pTiming, double pHitsound){

        this.x = pX;
        this.y = pY;
        this.timing = pTiming;
        this.hitsound = pHitsound;

        this.hit = false;

        //get and set Image
        var ii = new ImageIcon(path_skin + "/hitcircle.png");
        this.image = ii.getImage();
    }

    //methods
    public double getTiming(){
        return this.timing;
    }

    public void setTiming(float p_timing){
        this.timing = p_timing;
    }

    public double getPosX(){
        return this.x;
    }

    public void setPosX(float p_x){
        this.x = p_x;
    }

    public double getPosY(){
        return this.y;
    }

    public void setPosY(float p_y){
        this.y = p_y;
    }

    public boolean getHit(){
        return this.hit;
    }

    public void setHit(boolean p_hit){
        this.hit = p_hit;
    }

    public Image getImage() {
        return this.image;
    }
}
