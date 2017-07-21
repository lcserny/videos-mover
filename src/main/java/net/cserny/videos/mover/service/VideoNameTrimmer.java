package net.cserny.videos.mover.service;

import net.cserny.videos.mover.service.provider.VideoNameProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Leonardo Cserny on 22.10.2016.
 */
@Service
public class VideoNameTrimmer
{
    @Autowired
    private VideoNameProvider videoNameProvider;

    public String trim(String videoName) {
        for (String part : videoNameProvider.getNames()) {
            Pattern compile = Pattern.compile("(?i)(" + part + ")");
            Matcher matcher = compile.matcher(videoName);
            if (!part.isEmpty() && matcher.find()) {
                videoName = videoName.substring(0, matcher.start());
            }
        }
        return videoName;
    }
}
