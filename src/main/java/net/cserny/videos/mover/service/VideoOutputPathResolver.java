package net.cserny.videos.mover.service;

import net.cserny.videos.mover.model.VideoNameDTO;
import net.cserny.videos.mover.service.provider.SystemPathProvider;
import net.cserny.videos.mover.ui.model.DownloadsVideo;
import org.apache.commons.text.similarity.FuzzyScore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
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

        if (folders != null) {
            FuzzyScore fuzzyScore = new FuzzyScore(LocaleContextHolder.getLocale());
            int maxCoefficient = 0;
            File selectedFolder = null;

            for (File folder : folders) {
                if (!folder.isDirectory()) {
                    continue;
                }

                int currentCoefficient = fuzzyScore.fuzzyScore(videoName.getName(), folder.getName());
                if (currentCoefficient > maxCoefficient) {
                    maxCoefficient = currentCoefficient;
                    selectedFolder = folder;
                }
            }

            if (selectedFolder != null && maxCoefficient >= videoName.getName().length()) {
                return selectedFolder;
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
