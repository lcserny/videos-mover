package net.cserny.videos.mover.service.provider;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Created by leonardo on 10.05.2017.
 */
@Configuration
public class VideoNameProvider
{
    @Value("#{'${video.name.parts}'.split(';')}")
    private String[] names;

    public String[] getNames() {
        return names;
    }

    public void setNames(String[] names) {
        this.names = names;
    }
}
