package net.cserny.videos.mover.service;

/**
 * Created by Leonardo Cserny on 18.10.2016.
 */
public class DefaultPathProvider
{
    private String downloadsPath;
    private String moviePath;
    private String tvShowPath;

    public DefaultPathProvider()
    {
        String prefix = "/mnt/Data/";
        String osName = System.getProperty("os.name");
        if (osName.contains("Windows")) {
            prefix = "D:/";
        }

        downloadsPath = prefix + "Downloads";
        moviePath = prefix + "Movies/Movies";
        tvShowPath = prefix + "Movies/TV";
    }

    public String getDownloadsPath()
    {
        return downloadsPath;
    }

    public String getMoviePath()
    {
        return moviePath;
    }

    public String getTvShowPath()
    {
        return tvShowPath;
    }
}
