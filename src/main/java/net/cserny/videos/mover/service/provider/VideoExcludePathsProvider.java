package net.cserny.videos.mover.service.provider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by leonardo on 13.05.2017.
 */
public class VideoExcludePathsProvider
{
    public static final String RESOURCE_PATH = "/video.exclude.paths";

    private List<String> excludePaths = new ArrayList<>();

    public VideoExcludePathsProvider() {
        loadExcludePaths();
    }

    public List<String> getExcludePaths() {
        return excludePaths;
    }

    public void setExcludePaths(List<String> excludePaths) {
        this.excludePaths = excludePaths;
    }

    private void loadExcludePaths() {
        String line;
        InputStream in = getClass().getResourceAsStream(RESOURCE_PATH);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        try {
            while ((line = reader.readLine()) != null) {
                excludePaths.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
