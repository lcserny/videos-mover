package net.cserny.videos.mover.service.provider;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Created by leonardo on 13.05.2017.
 */
@Configuration
public class VideoMimeTypeProvider
{
    @Value("#{'${video.mime.types}'.split(',')}")
    private List<String> types;

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }
}
