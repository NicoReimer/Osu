package OsuClient;

public class Circle {

    //variables
    private int x, y, hitsound;
    private double timing;
    private boolean hit;

    private float xOffset;
    private float yOffset;
    private float xMultiplier;
    private float yMultiplier;

    //constructor
    Circle(int pX, int pY, double pTiming, int pHitsound){

        setOffsets();

        this.x = (int) (pX * xMultiplier + xOffset);
        this.y = (int) (pY * yMultiplier + yOffset);
        this.timing = pTiming;
        this.hitsound = pHitsound;

        this.hit = false;
    }

    public void setOffsets(){
        int swidth = 1920;
        int sheight = 1080;
        swidth = sheight * 4 / 3;
        xMultiplier = swidth / 640f;
        yMultiplier = sheight / 480f;
        xOffset = (int) (1920 - 512 * xMultiplier) / 2;
        yOffset = (int) (1080 - 384 * yMultiplier) / 2;
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

    public void setPosX(int p_x){
        this.x = p_x;
    }

    public double getPosY(){
        return this.y;
    }

    public void setPosY(int p_y){
        this.y = p_y;
    }

    public boolean getHit(){
        return this.hit;
    }

    public void setHit(boolean p_hit){
        this.hit = p_hit;
    }

    public int getHitsound() { return this.hitsound;}

    public void setHitsound (int p_hitsound){ this.hitsound = p_hitsound;}

}
