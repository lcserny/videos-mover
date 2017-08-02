package net.cserny.videos.mover.service;

import net.cserny.videos.mover.service.provider.SubtitleExtensionsProvider;
import net.cserny.videos.mover.service.provider.SystemPathProvider;
import net.cserny.videos.mover.ui.model.DownloadsVideo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by leonardo on 13.05.2017.
 */
@Service
public class VideoSubtitlesFinder
{
    @Autowired
    private SystemPathProvider pathProvider;
    @Autowired
    private SubtitleExtensionsProvider subtitleExtensionsProvider;

    public List<Path> find(DownloadsVideo downloadsVideo) {
        List<Path> subtitlePaths = new ArrayList<>();
        if (downloadsVideo.getFile().getParent().toString().equals(pathProvider.getDownloadsPath())) {
            addSubtitleByExtension(subtitlePaths, downloadsVideo.getFile().getParent());
        }
        return subtitlePaths;
    }

    private void addSubtitleByExtension(List<Path> subtitles, Path path) {
        try (DirectoryStream<Path> subtitleDirectory = Files.newDirectoryStream(path)) {
            for (Path subtitle : subtitleDirectory) {
                if (Files.isDirectory(subtitle)) addSubtitleByExtension(subtitles, subtitle);
                else if (isSubtitleExtensionAllowed(subtitle)) subtitles.add(subtitle);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isSubtitleExtensionAllowed(Path subtitle) {
        for (String extension : subtitleExtensionsProvider.getExtensions()) {
            if (subtitle.endsWith(extension)) {
                return true;
            }
        }
        return false;
    }
}
