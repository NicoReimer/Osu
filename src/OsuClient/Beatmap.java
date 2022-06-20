package OsuClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Beatmap
{
    public File file;
    public Circle[] _hitobjects;
    //public AudioClip song;
    public String artist, title, author;

    public Beatmap(String dir, String osuFile)
    {
        this.file = new File(dir + osuFile);
        this._hitobjects = GetGameObjects(this.file); //Get All Beatmaps
    }

    public Circle[] GetGameObjects(File playFile){
        try {

            Scanner scanner = new Scanner(playFile);

            int count = 0;

            while(scanner.hasNextLine()) {
                count++;
                scanner.nextLine();
            }

            scanner.close();

            int[][] zahlen = new int[count][4];

            scanner = new Scanner(playFile);

            scanner.useDelimiter(";");

            for(int i = 0; scanner.hasNextLine(); i++){
                String[] line = scanner.nextLine().split(";");
                for(int j = 0; j < 4; j++) {
                    zahlen[i][j] = Integer.parseInt(line[j]);
                }
            }

            scanner.close();

            Circle[] circles = new Circle[count];

            for(int i = 0; i < count; i++) {
                circles[i] = new Circle(zahlen[i][0], zahlen[i][1], zahlen[i][2], zahlen[i][3]);
            }

            return circles;

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}

