package net.cserny.videos.mover.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Leonardo Cserny on 22.10.2016.
 */
public class VideoNameTrimmer
{
    public static final String RESOURCE_PATH = "/video.name.parts";

    private List<String> removeParts = new ArrayList<>();

    public VideoNameTrimmer()
    {
        loadRemoveParts();
    }

    private void loadRemoveParts()
    {
        String line;
        InputStream in = getClass().getResourceAsStream(RESOURCE_PATH);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        try {
            while ((line = reader.readLine()) != null) {
                removeParts.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String trim(String videoName)
    {
        for (String part : removeParts) {
            Pattern compile = Pattern.compile("(?i)(" + part + ")");
            Matcher matcher = compile.matcher(videoName);
            if (!part.isEmpty() && matcher.find()) {
                videoName = videoName.substring(0, matcher.start());
            }
        }
        return videoName;
    }
}
