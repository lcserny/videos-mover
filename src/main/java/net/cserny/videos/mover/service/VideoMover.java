package net.cserny.videos.mover.service;

import net.cserny.videos.mover.ui.model.DownloadsVideo;
import net.cserny.videos.mover.ui.model.DownloadsVideoName;
import org.apache.commons.io.FileUtils;
import org.simmetrics.StringMetric;
import org.simmetrics.metrics.StringMetrics;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Leonardo Cserny on 18.10.2016.
 */
public class VideoMover
{
    public static final double ACCEPTED_SEARCH_COEFFICIENT = 0.8;

    private DownloadsVideo downloadsVideo;
    private String moviePath;
    private String tvShowPath;
    private Pattern videoPattern = Pattern.compile("(.*)(\\d{4})");

    public VideoMover(DownloadsVideo downloadsVideo) {
        this.downloadsVideo = downloadsVideo;
    }

    public void setMoviePath(String moviePath) {
        this.moviePath = moviePath;
    }

    public void setTvShowPath(String tvShowPath) {
        this.tvShowPath = tvShowPath;
    }

    public void move() {
        createFolder(new File(downloadsVideo.getOutputPath()));

        File newVideoFile = new File(getNewVideoFileName(downloadsVideo.getOutputPath(), downloadsVideo));
        if (newVideoFile.exists()) {
            return;
        }

        try {
            FileUtils.moveFile(downloadsVideo.getFile(), newVideoFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public File getNewVideoLocation() {
        String selectedPath = null;
        if (downloadsVideo.getMovie()) {
            selectedPath = moviePath;
        } else if (downloadsVideo.getTvShow()) {
            selectedPath = tvShowPath;
        }
        return getNewFolderPath(downloadsVideo, selectedPath);
    }

    private File getNewFolderPath(DownloadsVideo downloadsVideo, String path) {
        DownloadsVideoName downloadsVideoName = getVideoName(downloadsVideo);
        return getNewVideoFolder(path, downloadsVideoName);
    }

    private String getNewVideoFileName(String videoFolder, DownloadsVideo downloadsVideo) {
        return videoFolder + "/" + downloadsVideo.getFileName();
    }

    private String toCamelCase(String text) {
        String[] parts = text.split(" ");
        String camelCaseString = "";
        for (String part : parts){
            camelCaseString += toProperCase(part) + " ";
        }
        return camelCaseString;
    }

    private String toProperCase(String text) {
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }

    private File getNewVideoFolder(String path, DownloadsVideoName videoName) {
        if (path == null) {
            return null;
        }

        File videoParent = new File(path);
        File[] folders = videoParent.listFiles();
        StringMetric stringMetric = StringMetrics.cosineSimilarity();

        if (folders != null) {
            for (int i = folders.length - 1; i >= 0; i--) {
                File folder = folders[i];
                float result = stringMetric.compare(videoName.getName(), folder.getName());
                if (result >= ACCEPTED_SEARCH_COEFFICIENT) {
                    return folder;
                }
            }
        }

        return new File(path + "/" + videoName.getFormattedName());
    }

    private void createFolder(File videoFolder) {
        if (!videoFolder.exists()) {
            videoFolder.mkdir();
        }
    }

    private DownloadsVideoName getVideoName(DownloadsVideo downloadsVideo) {
        String videoName = preVideoNameProcess(downloadsVideo);
        Integer year = null;

        Matcher matcher = videoPattern.matcher(videoName);
        if (matcher.find()) {
            videoName = matcher.group(1);
            String yearString = matcher.group(2);
            if (yearString != null) {
                year = Integer.valueOf(yearString);
            }
        }

        return getDownloadsVideoName(year, postVideoNameProcess(videoName));
    }

    private String preVideoNameProcess(DownloadsVideo downloadsVideo) {
        VideoNameTrimmer trimmer = new VideoNameTrimmer();
        String videoName = removeExtension(downloadsVideo.getFile().getName());

        return trimmer.trim(videoName);
    }

    private String postVideoNameProcess(String videoName) {
        return videoName.replaceAll("(\\.|_)", " ").trim();
    }

    private DownloadsVideoName getDownloadsVideoName(Integer year, String videoName) {
        DownloadsVideoName downloadsVideoName = new DownloadsVideoName();
        downloadsVideoName.setName(toCamelCase(videoName).trim());
        downloadsVideoName.setYear(year);

        return downloadsVideoName;
    }

    private String removeExtension(String test) {
        return test.substring(0, test.length() - 3);
    }
}
