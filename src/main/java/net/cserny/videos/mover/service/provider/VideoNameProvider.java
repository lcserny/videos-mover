package net.cserny.videos.mover.service.provider;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Created by leonardo on 10.05.2017.
 */
@Configuration
public class VideoNameProvider
{
    @Value("#{'${video.name.parts}'.split(';')}")
    private List<String> names;

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }
}
