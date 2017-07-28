package net.cserny.videos.mover.service;

import net.cserny.videos.mover.ui.model.DownloadsVideo;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Leonardo Cserny on 18.10.2016.
 */
@Service
public class VideoMover
{
    public void move(DownloadsVideo downloadsVideo) throws IOException {
        createFolder(Paths.get(downloadsVideo.getOutputPath()));

        Path newVideoFile = Paths.get(downloadsVideo.getOutputPath() + "/" + downloadsVideo.getFileName());
        if (Files.exists(newVideoFile)) {
            return;
        }

        Files.move(downloadsVideo.getFile(), newVideoFile);
        for (Path subtitle : downloadsVideo.getSubtitles()) {
            Files.move(subtitle, Paths.get(newVideoFile.getParent() + "/" + subtitle.getFileName()));
        }
    }

    private void createFolder(Path videoFolder) throws IOException {
        if (!Files.exists(videoFolder)) {
            Files.createDirectory(videoFolder);
        }
    }
}
