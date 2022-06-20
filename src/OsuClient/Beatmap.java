package OsuClient;

import java.io.File;
import java.util.ArrayList;

public class Beatmap
{
    public File file;

    public ArrayList<Circle> _hitobjects = new ArrayList<>();

    //public AudioClip song;

    public String artist, title, author;

    public Beatmap(String dir, String osuFile)
    {
        this.file = new File(OsuClient.getSongsPath() + dir + osuFile);
    }
}

