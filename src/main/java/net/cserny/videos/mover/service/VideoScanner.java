package net.cserny.videos.mover.service;

import net.cserny.videos.mover.service.provider.VideoExcludePathsProvider;
import net.cserny.videos.mover.service.provider.VideoMimeTypeProvider;
import org.apache.tika.detect.DefaultDetector;
import org.apache.tika.detect.Detector;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.mime.MimeTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
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
    @Value("${min.video.size}")
    private Integer minVideoSize;

    @Autowired
    private VideoMimeTypeProvider mimeTypeProvider;
    @Autowired
    private VideoExcludePathsProvider excludePathsProvider;

    private Detector detector = new DefaultDetector(MimeTypes.getDefaultMimeTypes());

    public List<Path> scan(String path) {
        List<Path> videoFiles = new ArrayList<>();
        addVideosToList(videoFiles, Paths.get(path));
        return videoFiles;
    }

    private void addVideosToList(List<Path> videoFiles, Path directory) {
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(directory)) {
            for (Path path : directoryStream) {
                if (Files.isDirectory(path)) addVideosToList(videoFiles, path);
                else processFile(videoFiles, path);
            }
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void processFile(List<Path> videoFiles, Path filePath) {
        try (TikaInputStream stream = TikaInputStream.get(filePath)) {
            if (isVideoSizeAcceptable(filePath) && isPathAllowed(filePath)) {
                if (doesMimeTypeContainVideo(detector.detect(stream, new Metadata()))) {
                    videoFiles.add(filePath);
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
    }

    private boolean doesMimeTypeContainVideo(MediaType mediaType) {
        for (String allowedType : getDefaultAllowedTypes()) {
            if (mediaType.toString().contains(allowedType)) {
                return true;
            }
        }
        return false;
    }

    private boolean isVideoSizeAcceptable(Path filePath) throws IOException {
        return Files.size(filePath) > minVideoSize;
    }

    private boolean isPathAllowed(Path filePath) {
        for (String exclude : getDefaultExcludePaths()) {
            if (filePath.getParent().toString().contains(exclude)) {
                return false;
            }
        }
        return true;
    }

    private List<String> getDefaultAllowedTypes() {
        return mimeTypeProvider.getTypes();
    }

    private List<String> getDefaultExcludePaths() {
        return excludePathsProvider.getPaths();
    }
}
