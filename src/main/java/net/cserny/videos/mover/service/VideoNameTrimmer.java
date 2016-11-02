package net.cserny.videos.mover.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
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
        try {
            Path videoNameParts = Paths.get(getClass().getResource("/videoname.parts").getPath());
            removeParts.addAll(Files.readAllLines(videoNameParts).stream().collect(Collectors.toList()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String trim(String videoName)
    {
        for (String part : removeParts) {
            Pattern compile = Pattern.compile("(?i)(" + part + ")");
            Matcher matcher = compile.matcher(videoName);
            if (matcher.find()) {
                videoName = videoName.substring(0, matcher.start());
            }
        }
        return videoName;
    }
}
