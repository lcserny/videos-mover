package net.cserny.videos.mover.service;

import net.cserny.videos.mover.service.provider.VideoExcludePathsProvider;
import net.cserny.videos.mover.service.provider.VideoMimeTypeProvider;
import org.apache.tika.detect.DefaultDetector;
import org.apache.tika.detect.Detector;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MimeTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Leonardo Cserny on 16.10.2016.
 */
@Service
public class VideoScanner
{
    public static final int MIN_VIDEO_SIZE = 50 * 1024 * 1024;

    private Detector detector = new DefaultDetector(MimeTypes.getDefaultMimeTypes());
    @Autowired
    private VideoMimeTypeProvider mimeTypeProvider;
    @Autowired
    private VideoExcludePathsProvider excludePathsProvider;

    public List<File> scan(String path) {
        List<File> videoFiles = new ArrayList<>();
        File directory = new File(path);
        addVideosToList(videoFiles, directory);
        return videoFiles;
    }

    private void addVideosToList(List<File> videoFiles, File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (!file.isDirectory()) {
                    processFile(videoFiles, file);
                } else {
                    addVideosToList(videoFiles, file);
                }
            }
        }
    }

    private void processFile(List<File> videoFiles, File file) {
        if (isVideoSizeAcceptable(file) && isPathAllowed(file)) {
            try (TikaInputStream stream = TikaInputStream.get(Paths.get(file.getAbsolutePath()))) {
                Metadata metadata = new Metadata();
                String fileInfo = detector.detect(stream, metadata).toString();
                if (doesMimeTypeContainVideo(fileInfo)) {
                    videoFiles.add(file);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean doesMimeTypeContainVideo(String fileInfo) {
        for (String allowedType : getDefaultAllowedTypes()) {
            if (fileInfo.contains(allowedType)) {
                return true;
            }
        }
        return false;
    }

    private boolean isVideoSizeAcceptable(File file) {
        return file.length() > MIN_VIDEO_SIZE;
    }

    private boolean isPathAllowed(File file) {
        for (String exclude : getDefaultExcludePaths()) {
            if (file.getParent().contains(exclude)) {
                return false;
            }
        }
        return true;
    }

    private List<String> getDefaultAllowedTypes() {
        return Arrays.asList(mimeTypeProvider.getTypes());
    }

    private List<String> getDefaultExcludePaths() {
        return Arrays.asList(excludePathsProvider.getPaths());
    }
}
