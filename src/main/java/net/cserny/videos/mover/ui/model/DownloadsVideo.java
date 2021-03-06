package net.cserny.videos.mover.ui.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.nio.file.Path;
import java.util.List;

/**
 * Created by Leonardo Cserny on 17.10.2016.
 */
public class DownloadsVideo
{
    private Path file;
    private List<Path> subtitles;
    private StringProperty fileName = new SimpleStringProperty();
    private BooleanProperty movie = new SimpleBooleanProperty();
    private BooleanProperty tvShow = new SimpleBooleanProperty();
    private StringProperty outputPath = new SimpleStringProperty();

    public Path getFile() {
        return file;
    }

    public void setFile(Path file) {
        this.file = file;
    }

    public List<Path> getSubtitles() {
        return subtitles;
    }

    public void setSubtitles(List<Path> subtitles) {
        this.subtitles = subtitles;
    }

    public String getFileName() {
        return fileName.get();
    }

    public void setFileName(String fileName) {
        this.fileName.set(fileName);
    }

    public StringProperty fileNameProperty() {
        return fileName;
    }

    public boolean isMovie() {
        return movie.get();
    }

    public void setMovie(boolean movie) {
        if (movie) {
            this.movie.set(true);
            this.tvShow.set(false);
        } else {
            this.movie.set(false);
        }
    }

    public BooleanProperty movieProperty() {
        return movie;
    }

    public boolean isTvShow() {
        return tvShow.get();
    }

    public void setTvShow(boolean tvShow) {
        if (tvShow) {
            this.tvShow.set(true);
            this.movie.set(false);
        } else {
            this.tvShow.set(false);
        }
    }

    public BooleanProperty tvShowProperty() {
        return tvShow;
    }

    public String getOutputPath() {
        return outputPath.get();
    }

    public void setOutputPath(String outputPath) {
        this.outputPath.set(outputPath);
    }

    public StringProperty outputPathProperty() {
        return outputPath;
    }

    @Override
    public String toString() {
        return file.getFileName().toString();
    }
}
