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

    public VideoMover(DownloadsVideo downloadsVideo)
    {
        this.downloadsVideo = downloadsVideo;
    }

    public void setMoviePath(String moviePath)
    {
        this.moviePath = moviePath;
    }

    public void setTvShowPath(String tvShowPath)
    {
        this.tvShowPath = tvShowPath;
    }

    public void move()
    {
        if (downloadsVideo.getMovie()) {
            moveToMovies(downloadsVideo);
        } else if (downloadsVideo.getTvShow()) {
            moveToTvShows(downloadsVideo);
        }
    }

    private void moveToMovies(DownloadsVideo downloadsVideo)
    {
        processMove(downloadsVideo, moviePath);
    }

    private void moveToTvShows(DownloadsVideo downloadsVideo)
    {
        processMove(downloadsVideo, tvShowPath);
    }

    private void processMove(DownloadsVideo downloadsVideo, String path)
    {
        DownloadsVideoName downloadsVideoName = getVideoName(downloadsVideo);
        String videoFolderName = createFolder(path, downloadsVideoName);
        String newVideoFileName = videoFolderName + "/" + downloadsVideo.getFileName();

        File newVideoFile = new File(newVideoFileName);
        if (newVideoFile.exists()) {
            return;
        }

        try {
            FileUtils.moveFile(downloadsVideo.getFile(), newVideoFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String toCamelCase(String text)
    {
        String[] parts = text.split(" ");
        String camelCaseString = "";
        for (String part : parts){
            camelCaseString += toProperCase(part) + " ";
        }
        return camelCaseString;
    }

    private String toProperCase(String text)
    {
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }

    private String createFolder(String path, DownloadsVideoName videoName)
    {
        File videoParent = new File(path);
        File[] folders = videoParent.listFiles();
        StringMetric stringMetric = StringMetrics.cosineSimilarity();

        if (folders != null) {
            for (int i = folders.length - 1; i >= 0; i--) {
                File folder = folders[i];
                float result = stringMetric.compare(videoName.getName(), folder.getName());
                if (result >= ACCEPTED_SEARCH_COEFFICIENT) {
                    return folder.getAbsolutePath();
                }
            }
        }

        File videoFolder = new File(path + "/" + videoName.getFormattedName());
        if (!videoFolder.exists()) {
            videoFolder.mkdir();
        }

        return videoFolder.getAbsolutePath();
    }

    private DownloadsVideoName getVideoName(DownloadsVideo downloadsVideo)
    {
        Integer year = null;
        String videoName = removeExtension(downloadsVideo.getFile().getName());
        VideoNameTrimmer trimmer = new VideoNameTrimmer();
        videoName = trimmer.trim(videoName);
        Matcher matcher = videoPattern.matcher(videoName);

        if (matcher.find()) {
            videoName = matcher.group(1);
            String yearString = matcher.group(2);
            if (yearString != null) {
                year = Integer.valueOf(yearString);
            }
        }

        videoName = videoName.replaceAll("(\\.|_)", " ");
        videoName = videoName.trim();

        DownloadsVideoName downloadsVideoName = new DownloadsVideoName();
        downloadsVideoName.setName(toCamelCase(videoName).trim());
        downloadsVideoName.setYear(year);

        return downloadsVideoName;
    }

    private String removeExtension(String test)
    {
        return test.substring(0, test.length() - 3);
    }
}
