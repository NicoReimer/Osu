package OsuClient;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

import static OsuClient.OsuClient.path_skin;

public class BeatmapMenu extends JPanel {

    Image menuImage;
    Image menuSongOneImage;
    Image menuSongTwoImage;

    public void LoadMenu(){
        try {
            this.menuImage = ImageIO.read(new File(path_skin + "/map-screen.png"));
            this.menuSongOneImage = ImageIO.read(new File(path_skin + "/song-1.png"));
            this.menuSongTwoImage = ImageIO.read(new File(path_skin + "/song-2.png"));

            repaint();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //RePaint Components
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(menuImage,0,0,this);
        g.drawImage(menuSongOneImage,0,0,this);
        g.drawImage(menuSongTwoImage,0,0,this);
    }


}
