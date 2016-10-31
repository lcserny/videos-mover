package net.cserny.videos.mover.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Leonardo Cserny on 22.10.2016.
 */
public class VideoNameTrimmer
{
    public static final String REMOVE_PART_KEY = "remove.part";

    private List<String> removeParts;

    public VideoNameTrimmer()
    {
        loadRemoveParts();
    }

    private void loadRemoveParts()
    {
        Properties properties = getProperties();
        removeParts = new ArrayList<>();

        String value;
        for (int i = 0; (value = properties.getProperty(REMOVE_PART_KEY + "." + i)) != null; i++) {
            removeParts.add(value);
        }
    }

    private Properties getProperties()
    {
        Properties properties = new Properties();
        try {
            InputStream input = getClass().getResourceAsStream("/videoname.properties");
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
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
