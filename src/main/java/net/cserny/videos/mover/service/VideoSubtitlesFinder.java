package net.cserny.videos.mover.service;

import net.cserny.videos.mover.service.provider.SubtitleExtensionsProvider;
import net.cserny.videos.mover.service.provider.SystemPathProvider;
import net.cserny.videos.mover.ui.model.DownloadsVideo;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by leonardo on 13.05.2017.
 */
@Service
public class VideoSubtitlesFinder
{
    private SystemPathProvider pathProvider;
    private SubtitleExtensionsProvider subtitleExtensionsProvider;

    @Autowired
    public void setPathProvider(SystemPathProvider pathProvider) {
        this.pathProvider = pathProvider;
    }

    @Autowired
    public void setSubtitleExtensionsProvider(SubtitleExtensionsProvider subtitleExtensionsProvider) {
        this.subtitleExtensionsProvider = subtitleExtensionsProvider;
    }

    public List<File> find(File videoFile) {
        List<File> files = new ArrayList<>();
        if (videoFile.getParent() != pathProvider.getDownloadsPath()) {
            addSubtitleByExtension(files, videoFile.getParent(), Arrays.asList(subtitleExtensionsProvider.getExtensions()));
        }
        return files;
    }

    private void addSubtitleByExtension(List<File> files, String path, List<String> extensions) {
        File dir = new File(path);
        File[] dirFiles = dir.listFiles();
        if (dirFiles != null && dirFiles.length > 0) {
            for (File file : dirFiles) {
                if (!file.isDirectory()) {
                    if (extensions.contains(FilenameUtils.getExtension(file.getAbsolutePath()))) {
                        files.add(file);
                    }
                } else {
                    addSubtitleByExtension(files, file.getPath(), extensions);
                }
            }
        }
    }
}
