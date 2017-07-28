package net.cserny.videos.mover.service.provider;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Created by leonardo on 13.05.2017.
 */
@Configuration
public class VideoExcludePathsProvider
{
    @Value("#{'${video.exclude.paths}'.split(',')}")
    private List<String> paths;

    public List<String> getPaths() {
        return paths;
    }

    public void setPaths(List<String> paths) {
        this.paths = paths;
    }
}
