package net.cserny.videos.mover.service;

import net.cserny.videos.mover.dao.Video;
import net.cserny.videos.mover.ui.model.DownloadsVideo;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

/**
 * Created by Leonardo Cserny on 18.10.2016.
 */
@Service
public class VideoMover
{
    @Autowired
    private VideoSubtitlesFinder videoSubtitlesFinder;

    public void moveTvShow(Video video) {
        move(video.getTvOutput(), video.getName(), video.getFilePath());
    }

    public void moveMovie(Video video) {
        move(video.getMovieOutput(), video.getName(), video.getFilePath());
    }

    private void move(String outputPath, String fileName, String filePath) {
        createFolder(new File(outputPath));

        File newVideoFile = new File(outputPath + "/" + fileName);
        if (newVideoFile.exists()) {
            return;
        }

        File videoFile = new File(filePath);
//        try {
//            FileUtils.moveFile(videoFile, newVideoFile);
            System.out.println("Moving from: " + videoFile.getPath() + ", to: " + newVideoFile.getPath());
            for (File subtitle : videoSubtitlesFinder.find(videoFile)) {
//                FileUtils.moveFile(subtitle, new File(newVideoFile.getParent() + "/" + subtitle.getName()));
                System.out.println("Moving subtitle: " + subtitle.getPath());
            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    private void createFolder(File videoFolder) {
        if (!videoFolder.exists()) {
            videoFolder.mkdir();
        }
    }
}
