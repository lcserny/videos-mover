package net.cserny.videos.mover.service.provider;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;

/**
 * Created by Leonardo Cserny on 18.10.2016.
 */
@Service
public class SystemPathProvider
{
    private String downloadsPath;
    private String moviePath;
    private String tvShowPath;

    @PostConstruct
    private void initDefaultPaths() {
        String prefix = processPrefix();
        String downloadsDir = prefix + "Downloads";
        String movieDir = prefix + "Movies/Movies";
        String tvShowDir = prefix + "Movies/TV";

        if (new File(downloadsDir).exists()) {
            downloadsPath = downloadsDir;
        }

        if (new File(movieDir).exists()) {
            moviePath = movieDir;
        }

        if (new File(tvShowDir).exists()) {
            tvShowPath = tvShowDir;
        }
    }

    public String getDownloadsPath() {
        return downloadsPath;
    }

    public void setDownloadsPath(String downloadsPath) {
        this.downloadsPath = downloadsPath;
    }

    public String getMoviePath() {
        return moviePath;
    }

    public void setMoviePath(String moviePath) {
        this.moviePath = moviePath;
    }

    public String getTvShowPath() {
        return tvShowPath;
    }

    public void setTvShowPath(String tvShowPath) {
        this.tvShowPath = tvShowPath;
    }

    private String processPrefix() {
        String prefix = "/mnt/Data/";
        String osName = System.getProperty("os.name");
        if (osName.contains("Windows")) {
            prefix = "D:/";
        }

        return prefix;
    }
}
