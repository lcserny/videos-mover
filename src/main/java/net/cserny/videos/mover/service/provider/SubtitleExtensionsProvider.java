package net.cserny.videos.mover.service.provider;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Created by leonardo on 10.05.2017.
 */
@Configuration
public class SubtitleExtensionsProvider
{
    @Value("#{'${video.subtitle.extensions}'.split(',')}")
    private List<String> extensions;

    public List<String> getExtensions() {
        return extensions;
    }

    public void setExtensions(List<String> extensions) {
        this.extensions = extensions;
    }
}
