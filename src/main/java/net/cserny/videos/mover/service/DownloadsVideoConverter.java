package net.cserny.videos.mover.service;

import net.cserny.videos.mover.service.provider.SystemPathProvider;
import net.cserny.videos.mover.ui.model.DownloadsVideo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

@Service
public class DownloadsVideoConverter
{
    @Autowired
    private SystemPathProvider pathProvider;
    @Autowired
    private VideoSubtitlesFinder videoSubtitlesFinder;
    @Autowired
    private VideoOutputPathResolver videoOutputPathResolver;

    public DownloadsVideo convertFileToDownloadsVideo(Path videoPath) {
        DownloadsVideo downloadsVideo = new DownloadsVideo();
        downloadsVideo.setFile(videoPath);
        downloadsVideo.setFileName(videoPath.getFileName().toString());
        downloadsVideo.setMovie(false);
        downloadsVideo.setTvShow(false);

        downloadsVideo.movieProperty().addListener((obs, prevCheck, currentCheck) -> {
            if (pathProvider.checkMoviesFolderAvilability()) {
                downloadsVideo.setMovie(currentCheck);
                downloadsVideo.setOutputPath(currentCheck ? videoOutputPathResolver.resolve(downloadsVideo) : "");
            }
        });

        downloadsVideo.tvShowProperty().addListener((obs, prevCheck, currentCheck) -> {
            if (pathProvider.checkTvShowsFolderAvailability()) {
                downloadsVideo.setTvShow(currentCheck);
                downloadsVideo.setOutputPath(currentCheck ? videoOutputPathResolver.resolve(downloadsVideo) : "");
            }
        });

        downloadsVideo.setSubtitles(videoSubtitlesFinder.find(downloadsVideo));

        return downloadsVideo;
    }
}
