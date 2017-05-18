package net.cserny.videos.mover.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

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
@Service
public class VideoNameTrimmer
{
    @Value("#{'${video.name.parts}'.split(';')}")
    private String[] names;

    public String[] getNames() {
        return names;
    }

    public void setNames(String[] names) {
        this.names = names;
    }

    public String trim(String videoName) {
        for (String part : names) {
            Pattern compile = Pattern.compile("(?i)(" + part + ")");
            Matcher matcher = compile.matcher(videoName);
            if (!part.isEmpty() && matcher.find()) {
                videoName = videoName.substring(0, matcher.start());
            }
        }
        return videoName;
    }
}
