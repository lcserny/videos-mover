package net.cserny.videos.mover.service;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by Leonardo Cserny on 22.10.2016.
 */
public class VideoNameTrimmer
{
    private List<String> removeParts = new ArrayList<>();

    public VideoNameTrimmer()
    {
        loadRemoveParts();
    }

    private void loadRemoveParts()
    {
        String line;
        InputStream in = getClass().getResourceAsStream("/videoname.parts");
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
