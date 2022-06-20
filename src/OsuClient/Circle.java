package OsuClient;

import javax.swing.*;
import java.awt.*;

public class Circle {

    //variables
    private double x;
    private double y;
    private boolean hit;
    private Image image;
    private double timing;

    Circle(double x, double y, double timing){

        this.x = x;
        this.y = y;
        this.hit = false;
        this.timing = timing;

        //get and set Image
        var ii = new ImageIcon("skin/hitcircle.png");
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
