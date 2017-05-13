package net.cserny.videos.mover.service.provider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by leonardo on 10.05.2017.
 */
public class SubtitleExtensionsProvider
{
    public static final String RESOURCE_PATH = "/subtitle.extensions";

    private List<String> extensions = new ArrayList<>();

    public SubtitleExtensionsProvider() {
        loadSubtitleExtensions();
    }

    public List<String> getExtensions() {
        return extensions;
    }

    public void setExtensions(List<String> extensions) {
        this.extensions = extensions;
    }

    private void loadSubtitleExtensions() {
        String line;
        InputStream in = getClass().getResourceAsStream(RESOURCE_PATH);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        try {
            while ((line = reader.readLine()) != null) {
                extensions.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
