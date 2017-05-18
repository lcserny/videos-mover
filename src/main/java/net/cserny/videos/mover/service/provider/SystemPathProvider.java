package net.cserny.videos.mover.service.provider;

import org.springframework.stereotype.Service;

/**
 * Created by Leonardo Cserny on 18.10.2016.
 */
@Service
public class SystemPathProvider
{
    private String downloadsPath;
    private String moviePath;
    private String tvShowPath;

    public SystemPathProvider()
    {
        initDefaultPaths();
    }

    private void initDefaultPaths() {
        String prefix = "/mnt/Data/";
        String osName = System.getProperty("os.name");
        if (osName.contains("Windows")) {
            prefix = "D:/";
        }

        downloadsPath = prefix + "Downloads";
        moviePath = prefix + "Movies/Movies";
        tvShowPath = prefix + "Movies/TV";
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
}
