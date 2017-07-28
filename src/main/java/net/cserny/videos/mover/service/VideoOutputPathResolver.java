package net.cserny.videos.mover.service;

import me.xdrop.fuzzywuzzy.FuzzySearch;
import net.cserny.videos.mover.model.VideoNameDTO;
import net.cserny.videos.mover.service.provider.SystemPathProvider;
import net.cserny.videos.mover.ui.model.DownloadsVideo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by leonardo on 13.05.2017.
 */
@Service
public class VideoOutputPathResolver
{
    @Value("${similarity.percent}")
    private Integer similarityPercent;

    @Autowired
    private SystemPathProvider pathProvider;
    @Autowired
    private VideoNameTrimmer nameTrimmer;

    private Pattern videoPattern = Pattern.compile("(.*)(\\d{4})");

    public String resolve(DownloadsVideo downloadsVideo) {
        VideoNameDTO videoName = processVideoName(downloadsVideo);
        if (downloadsVideo.isMovie()) {
            return processOutputVideoFolder(pathProvider.getMoviePath(), videoName);
        } else if (downloadsVideo.isTvShow()) {
            return processOutputVideoFolder(pathProvider.getTvShowPath(), videoName);
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

    private String processOutputVideoFolder(String path, VideoNameDTO videoName) {
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(path), Files::isDirectory)) {
            int maxCoefficient = 0;
            Path selectedFolder = null;

            for (Path dirPath : directoryStream) {
                int currentCoefficient = FuzzySearch.ratio(videoName.getName(), dirPath.getFileName().toString());
                if (currentCoefficient > maxCoefficient) {
                    maxCoefficient = currentCoefficient;
                    selectedFolder = dirPath;
                }
            }

            if (selectedFolder != null && maxCoefficient >= similarityPercent) {
                return selectedFolder.toString();
            }
        } catch (IOException e) { e.printStackTrace(); }

        return path + "/" + videoName.getFormattedName();
    }

    private String retrieveTrimmedVideoName(DownloadsVideo downloadsVideo) {
        String videoName = removeExtension(downloadsVideo.getFile().getFileName().toString());
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
        StringBuilder camelCaseString = new StringBuilder();
        for (String part : text.split("\\s+")){
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
