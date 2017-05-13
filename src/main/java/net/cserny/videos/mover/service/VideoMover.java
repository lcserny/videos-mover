package net.cserny.videos.mover.service;

import net.cserny.videos.mover.ui.model.DownloadsVideo;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by Leonardo Cserny on 18.10.2016.
 */
public class VideoMover
{
    public void move(DownloadsVideo downloadsVideo) {
        createFolder(new File(downloadsVideo.getOutputPath()));

        File newVideoFile = new File(downloadsVideo.getOutputPath() + "/" + downloadsVideo.getFileName());
        if (newVideoFile.exists()) {
            return;
        }

        try {
            FileUtils.moveFile(downloadsVideo.getFile(), newVideoFile);
            for (File subtitle : downloadsVideo.getSubtitles()) {
                FileUtils.moveFile(subtitle, new File(newVideoFile.getParent() + "/" + subtitle.getName()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createFolder(File videoFolder) {
        if (!videoFolder.exists()) {
            videoFolder.mkdir();
        }
    }
}
