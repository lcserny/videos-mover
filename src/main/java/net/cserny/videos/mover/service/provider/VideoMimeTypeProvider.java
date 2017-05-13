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
public class VideoMimeTypeProvider
{
    public static final String RESOURCE_PATH = "/video.mime.types";

    private List<String> mimeTypes = new ArrayList<>();

    public VideoMimeTypeProvider() {
        loadMimeTypes();
    }

    public List<String> getMimeTypes() {
        return mimeTypes;
    }

    public void setMimeTypes(List<String> mimeTypes) {
        this.mimeTypes = mimeTypes;
    }

    private void loadMimeTypes() {
        String line;
        InputStream in = getClass().getResourceAsStream(RESOURCE_PATH);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        try {
            while ((line = reader.readLine()) != null) {
                mimeTypes.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
