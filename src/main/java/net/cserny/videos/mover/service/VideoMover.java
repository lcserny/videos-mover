package net.cserny.videos.mover.service;

import net.cserny.videos.mover.ui.model.DownloadsVideo;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

/**
 * Created by Leonardo Cserny on 18.10.2016.
 */
@Service
public class VideoMover
{
    public void move(DownloadsVideo downloadsVideo) throws IOException {
        createFolder(new File(downloadsVideo.getOutputPath()));

        File newVideoFile = new File(downloadsVideo.getOutputPath() + "/" + downloadsVideo.getFileName());
        if (newVideoFile.exists()) {
            return;
        }

        FileUtils.moveFile(downloadsVideo.getFile(), newVideoFile);
        for (File subtitle : downloadsVideo.getSubtitles()) {
            FileUtils.moveFile(subtitle, new File(newVideoFile.getParent() + "/" + subtitle.getName()));
        }
    }

    private void createFolder(File videoFolder) {
        if (!videoFolder.exists()) {
            videoFolder.mkdir();
        }
    }
}
