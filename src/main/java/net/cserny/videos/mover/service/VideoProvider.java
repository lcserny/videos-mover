package net.cserny.videos.mover.service;

import org.apache.tika.detect.DefaultDetector;
import org.apache.tika.detect.Detector;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MimeTypes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Leonardo Cserny on 16.10.2016.
 */
public class VideoProvider
{
    public static final int MIN_VIDEO_SIZE = 50 * 1024 * 1024;

    private String rootPath;
    private List<String> excludePaths = Collections.singletonList("Programming Stuff");
    private Detector detector = new DefaultDetector(MimeTypes.getDefaultMimeTypes());
    private List<String> allowedTypes = getDefaultAllowedTypes();

    public VideoProvider(String rootPath)
    {
        this.rootPath = rootPath;
    }

    public Detector getDetector()
    {
        return detector;
    }

    public void setDetector(Detector detector)
    {
        this.detector = detector;
    }

    public List<String> getExcludePaths()
    {
        return excludePaths;
    }

    public void setExcludePaths(List<String> excludePaths)
    {
        this.excludePaths = excludePaths;
    }

    public void addExcludePath(String path)
    {
        this.excludePaths.add(path);
    }

    private List<String> getDefaultAllowedTypes()
    {
        List<String> allowedTypes = new ArrayList<>();
        allowedTypes.add("video");
        allowedTypes.add("application/x-matroska");
        allowedTypes.add("audio/mp4");

        return allowedTypes;
    }

    public List<File> processVideoFiles() throws IOException
    {
        List<File> videoFiles = new ArrayList<>();

        File directory = new File(rootPath);
        addVideosToList(videoFiles, directory);

        return videoFiles;
    }

    private void addVideosToList(List<File> videoFiles, File directory) throws IOException
    {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (isPathAllowed(file)) {
                    if (!file.isDirectory()) {
                        processFile(videoFiles, file);
                    } else {
                        addVideosToList(videoFiles, file);
                    }
                }
            }
        }
    }

    private void processFile(List<File> videoFiles, File file) throws IOException
    {
        if (isVideoSizeAcceptable(file)) {
            try (TikaInputStream stream = TikaInputStream.get(Paths.get(file.getAbsolutePath()))) {
                Metadata metadata = new Metadata();
                String fileInfo = detector.detect(stream, metadata).toString();
                if (doesMimeTypeContainVideo(fileInfo)) {
                    videoFiles.add(file);
                }
            }
        }
    }

    private boolean doesMimeTypeContainVideo(String fileInfo)
    {
        for (String allowedType : allowedTypes) {
            if (fileInfo.contains(allowedType)) {
                return true;
            }
        }
        return false;
    }

    private boolean isVideoSizeAcceptable(File file)
    {
        return file.length() > MIN_VIDEO_SIZE;
    }

    private boolean isPathAllowed(File file)
    {
        boolean allowed = true;
        for (String exclude : excludePaths) {
            if (file.getName().contains(exclude)) {
                allowed = false;
                break;
            }
        }
        return allowed;
    }
}
