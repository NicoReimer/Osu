package OsuClient;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

import static OsuClient.OsuClient.*;

public class MainMenu extends JPanel {

    Image menuImage;
    Image menuPlayImage;
    Image menuQuitImage;

    public void LoadMenu(){
        try {
            this.menuImage = ImageIO.read(new File(path_skin + "/main-menu.png"));
            this.menuPlayImage = ImageIO.read(new File(path_skin + "/main-menu-play.png"));
            this.menuQuitImage = ImageIO.read(new File(path_skin + "/main-menu-quit.png"));

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
        g.drawImage(menuPlayImage,0,0,this);
        g.drawImage(menuQuitImage,0,0,this);
    }




}
