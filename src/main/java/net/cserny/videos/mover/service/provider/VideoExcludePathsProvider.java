package net.cserny.videos.mover.service.provider;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Created by leonardo on 13.05.2017.
 */
@Configuration
public class VideoExcludePathsProvider
{
    @Value("${video.exclude.paths}")
    private String[] paths;

    public String[] getPaths() {
        return paths;
    }

    public void setPaths(String[] paths) {
        this.paths = paths;
    }
}
