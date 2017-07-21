package net.cserny.videos.mover.service;

import net.cserny.videos.mover.model.VideoNameDTO;
import net.cserny.videos.mover.service.provider.SystemPathProvider;
import net.cserny.videos.mover.ui.model.DownloadsVideo;
import org.simmetrics.StringMetric;
import org.simmetrics.metrics.StringMetrics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by leonardo on 13.05.2017.
 */
@Service
public class VideoOutputPathResolver
{
    public static final double ACCEPTED_SEARCH_COEFFICIENT = 0.87;

    @Autowired
    private SystemPathProvider pathProvider;
    @Autowired
    private VideoNameTrimmer nameTrimmer;

    private Pattern videoPattern = Pattern.compile("(.*)(\\d{4})");

    public String resolve(DownloadsVideo downloadsVideo) {
        VideoNameDTO videoName = processVideoName(downloadsVideo);
        if (downloadsVideo.isMovie()) {
            return processOutputVideoFolder(pathProvider.getMoviePath(), videoName).getAbsolutePath();
        } else if (downloadsVideo.isTvShow()) {
            return processOutputVideoFolder(pathProvider.getTvShowPath(), videoName).getAbsolutePath();
        }
        return null;
    }

    private VideoNameDTO processVideoName(DownloadsVideo downloadsVideo) {
        String videoName = retrieveTrimmedVideoName(downloadsVideo);
        Integer year = null;

        Matcher matcher = videoPattern.matcher(videoName);
        if (matcher.find()) {
            videoName = matcher.group(1);
            String yearString = matcher.group(2);
            if (yearString != null && downloadsVideo.isMovie()) {
                year = Integer.valueOf(yearString);
            }
        }

        return buildVideoName(year, stripSpecialChars(videoName));
    }

    private File processOutputVideoFolder(String path, VideoNameDTO videoName) {
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

    private String retrieveTrimmedVideoName(DownloadsVideo downloadsVideo) {
        String videoName = removeExtension(downloadsVideo.getFile().getName());
        return nameTrimmer.trim(videoName);
    }

    private String stripSpecialChars(String videoName) {
        return videoName.replaceAll("(\\.|_)", " ").trim();
    }

    private VideoNameDTO buildVideoName(Integer year, String videoNameString) {
        VideoNameDTO videoName = new VideoNameDTO();
        videoName.setName(toCamelCase(videoNameString));
        videoName.setYear(year);
        return videoName;
    }

    private String toCamelCase(String text) {
        String[] parts = text.split("\\s+");
        StringBuilder camelCaseString = new StringBuilder();
        for (String part : parts){
            camelCaseString.append(toProperCase(part)).append(" ");
        }
        return camelCaseString.toString().trim();
    }

    private String toProperCase(String text) {
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }

    private String removeExtension(String test) {
        return test.substring(0, test.length() - 3);
    }
}
