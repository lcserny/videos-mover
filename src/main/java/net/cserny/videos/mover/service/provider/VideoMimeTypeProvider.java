package net.cserny.videos.mover.service.provider;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Created by leonardo on 13.05.2017.
 */
@Configuration
public class VideoMimeTypeProvider
{
    @Value("${video.mime.types}")
    private String[] types;

    public String[] getTypes() {
        return types;
    }

    public void setTypes(String[] types) {
        this.types = types;
    }
}
