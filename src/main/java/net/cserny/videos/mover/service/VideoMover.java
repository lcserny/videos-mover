package net.cserny.videos.mover.service;

import javafx.scene.control.Alert;
import net.cserny.videos.mover.ui.configuration.MainTableService;
import net.cserny.videos.mover.ui.model.DownloadsVideo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leonardo Cserny on 18.10.2016.
 */
@Service
public class VideoMover
{
    @Autowired
    private PopupService popupService;
    @Autowired
    private MainTableService mainTableService;

    public void moveAllSelected() {
        boolean errorOccurred = false;
        List<DownloadsVideo> toBeRemoved = new ArrayList<>();
        for (DownloadsVideo downloadsVideo : mainTableService.getTableItems()) {
            if (downloadsVideo.isMovie() || downloadsVideo.isTvShow()) {
                try {
                    move(downloadsVideo);
                    toBeRemoved.add(downloadsVideo);
                } catch (IOException e) {
                    errorOccurred = true;
                    popupService.showPopupMessage(Alert.AlertType.ERROR, String.format("Failed to move video file: %s, please check!", downloadsVideo.getFileName()), "Move Failed!");
                    break;
                }
            }
        }
        mainTableService.removeAllTableItems(toBeRemoved);

        if (!errorOccurred) {
            popupService.showMoveCompletionMessage(!toBeRemoved.isEmpty());
        }
    }

    private void move(DownloadsVideo downloadsVideo) throws IOException {
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
