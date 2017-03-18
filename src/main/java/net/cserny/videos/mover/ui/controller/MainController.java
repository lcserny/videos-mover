package net.cserny.videos.mover.ui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import net.cserny.videos.mover.service.DefaultPathProvider;
import net.cserny.videos.mover.service.ComponentConfigurer;
import net.cserny.videos.mover.service.VideoMover;
import net.cserny.videos.mover.service.VideoProvider;
import net.cserny.videos.mover.ui.model.DownloadsVideo;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Leonardo Cserny on 16.10.2016.
 */
public class MainController implements Initializable
{
    @FXML
    private VBox container;
    @FXML
    private TextField chooseMoviePathTextField;
    @FXML
    private TextField chooseTextField;
    @FXML
    private TextField chooseTvShowPathTextField;
    @FXML
    private TableView<DownloadsVideo> tableView;

    private String downloadsPath;
    private String moviePath;
    private String tvShowPath;
    private ComponentConfigurer componentConfigurer = new ComponentConfigurer();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initDefaultPaths();
        componentConfigurer.configure(tableView);
    }

    public void loadTableView() {
        tableView.getItems().clear();
        for (File videoFile : getVideoFiles(downloadsPath)) {
            DownloadsVideo item = getDownloadsVideo(videoFile);
            tableView.getItems().add(item);
        }
    }

    public void setDownloadsPath(ActionEvent actionEvent) {
        File selectedDirectory = getSelectedDirectory("Choose Downlooads folder", downloadsPath);
        if (selectedDirectory != null) {
            setDownloadsPath(selectedDirectory.getAbsolutePath());
        }
    }

    public void setMoviePath(ActionEvent actionEvent) {
        File selectedDirectory = getSelectedDirectory("Choose Movie folder", moviePath);
        if (selectedDirectory != null) {
            setMoviePath(selectedDirectory.getAbsolutePath());
        }
    }

    public void setTvShowPath(ActionEvent actionEvent) {
        File selectedDirectory = getSelectedDirectory("Choose Tv Show folder", tvShowPath);
        if (selectedDirectory != null) {
            setTvShowPath(selectedDirectory.getAbsolutePath());
        }
    }

    public void moveVideos(ActionEvent actionEvent) {
        List<DownloadsVideo> toBeRemoved = new ArrayList<>();
        tableView.getItems()
                .stream()
                .filter(downloadsVideo -> downloadsVideo.getMovie() || downloadsVideo.getTvShow())
                .forEach(downloadsVideo -> {
                    getVideoMover(downloadsVideo).move();
                    toBeRemoved.add(downloadsVideo);
                });
        tableView.getItems().removeAll(toBeRemoved);
    }

    private void initDefaultPaths() {
        DefaultPathProvider pathProvider = new DefaultPathProvider();
        setDownloadsPath(pathProvider.getDownloadsPath());
        setMoviePath(pathProvider.getMoviePath());
        setTvShowPath(pathProvider.getTvShowPath());
    }

    private DownloadsVideo getDownloadsVideo(File video) {
        DownloadsVideo item = new DownloadsVideo();
        item.setFile(video);
        item.setFileName(video.getName());
        item.setMovie(false);
        item.setTvShow(false);

        item.movieProperty().addListener((obs, wasOn, isNowOn) -> {
            item.setMovie(isNowOn);
            processOutputPath(isNowOn, item);
        });

        item.tvShowProperty().addListener((obs, wasOn, isNowOn) -> {
            item.setTvShow(isNowOn);
            processOutputPath(isNowOn, item);
        });

        return item;
    }

    private void processOutputPath(boolean isNowOn, DownloadsVideo downloadsVideo) {
        String outputPath = "";
        if (isNowOn) {
            outputPath = getVideoMover(downloadsVideo).getNewVideoLocation().getAbsolutePath();
        }
        downloadsVideo.setOutputPath(outputPath);
    }

    private List<File> getVideoFiles(String path) {
        List<File> videoFiles = new ArrayList<>();
        VideoProvider videoProvider = new VideoProvider(path);
        try {
            videoFiles = videoProvider.processVideoFiles();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return videoFiles;
    }

    private void setDownloadsPath(String path) {
        downloadsPath = path;
        chooseTextField.setText(path);
    }

    private void setMoviePath(String path) {
        moviePath = path;
        chooseMoviePathTextField.setText(path);
    }

    private void setTvShowPath(String path) {
        tvShowPath = path;
        chooseTvShowPathTextField.setText(path);
    }

    private File getSelectedDirectory(String title, String path) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle(title);
        setDefaultDirectoryToChooser(chooser, path);

        return chooser.showDialog(getStage());
    }

    private void setDefaultDirectoryToChooser(DirectoryChooser chooser, String path) {
        File defaultDirectory = new File(path);
        if (defaultDirectory.exists()) {
            chooser.setInitialDirectory(defaultDirectory);
        }
    }

    private Stage getStage() {
        return (Stage) container.getScene().getWindow();
    }

    private VideoMover getVideoMover(DownloadsVideo downloadsVideo) {
        VideoMover videoMover = new VideoMover(downloadsVideo);
        videoMover.setMoviePath(moviePath);
        videoMover.setTvShowPath(tvShowPath);

        return videoMover;
    }
}
