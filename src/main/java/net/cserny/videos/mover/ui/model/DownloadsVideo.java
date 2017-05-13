package net.cserny.videos.mover.ui.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.File;
import java.util.List;

/**
 * Created by Leonardo Cserny on 17.10.2016.
 */
public class DownloadsVideo
{
    private File file;
    private List<File> subtitles;
    private StringProperty fileName = new SimpleStringProperty();
    private BooleanProperty movie = new SimpleBooleanProperty();
    private BooleanProperty tvShow = new SimpleBooleanProperty();
    private StringProperty outputPath = new SimpleStringProperty();

    public File getFile()
    {
        return file;
    }

    public void setFile(File file)
    {
        this.file = file;
    }

    public List<File> getSubtitles() {
        return subtitles;
    }

    public void setSubtitles(List<File> subtitles) {
        this.subtitles = subtitles;
    }

    public String getFileName()
    {
        return fileName.get();
    }

    public StringProperty fileNameProperty()
    {
        return fileName;
    }

    public void setFileName(String fileName)
    {
        this.fileName.set(fileName);
    }

    public boolean isMovie()
    {
        return movie.get();
    }

    public BooleanProperty movieProperty()
    {
        return movie;
    }

    public void setMovie(boolean movie)
    {
        if (movie) {
            this.movie.set(true);
            this.tvShow.set(false);
        } else {
            this.movie.set(false);
        }
    }

    public boolean isTvShow()
    {
        return tvShow.get();
    }

    public BooleanProperty tvShowProperty()
    {
        return tvShow;
    }

    public void setTvShow(boolean tvShow)
    {
        if (tvShow) {
            this.tvShow.set(true);
            this.movie.set(false);
        } else {
            this.tvShow.set(false);
        }
    }

    public String getOutputPath() {
        return outputPath.get();
    }

    public StringProperty outputPathProperty() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath.set(outputPath);
    }

    @Override
    public String toString()
    {
        return file.getName();
    }
}
