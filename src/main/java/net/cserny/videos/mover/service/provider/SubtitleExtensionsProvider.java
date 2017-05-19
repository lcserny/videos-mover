package net.cserny.videos.mover.service.provider;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Created by leonardo on 10.05.2017.
 */
@Configuration
public class SubtitleExtensionsProvider
{
    @Value("${video.subtitle.extensions}")
    private String[] extensions;

    public String[] getExtensions() {
        return extensions;
    }

    public void setExtensions(String[] extensions) {
        this.extensions = extensions;
    }
}
