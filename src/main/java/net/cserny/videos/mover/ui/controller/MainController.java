package net.cserny.videos.mover.ui.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import net.cserny.videos.mover.service.VideoMover;
import net.cserny.videos.mover.service.VideoOutputPathResolver;
import net.cserny.videos.mover.service.VideoScanner;
import net.cserny.videos.mover.service.VideoSubtitlesFinder;
import net.cserny.videos.mover.service.provider.SystemPathProvider;
import net.cserny.videos.mover.ui.configuration.ComponentConfigurer;
import net.cserny.videos.mover.ui.model.DownloadsVideo;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
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
    private TextField downloadsPathTextField;
    @FXML
    private TextField moviePathTextField;
    @FXML
    private TextField tvShowPathTextField;
    @FXML
    private TableView<DownloadsVideo> tableView;

    private ComponentConfigurer componentConfigurer;
    private SystemPathProvider pathProvider;
    private VideoScanner videoScanner;
    private VideoMover videoMover;
    private VideoSubtitlesFinder videoSubtitlesFinder;
    private VideoOutputPathResolver videoOutputPathResolver;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        componentConfigurer = new ComponentConfigurer();
        componentConfigurer.configure(tableView);

        pathProvider = new SystemPathProvider();
        videoScanner = new VideoScanner();
        videoMover = new VideoMover();
        videoSubtitlesFinder = new VideoSubtitlesFinder(pathProvider);
        videoOutputPathResolver = new VideoOutputPathResolver(pathProvider);

        initDefaults();
    }

    private void initDefaults() {
        downloadsPathTextField.setText(pathProvider.getDownloadsPath());
        moviePathTextField.setText(pathProvider.getMoviePath());
        tvShowPathTextField.setText(pathProvider.getTvShowPath());
    }

    public void loadTableView() {
        ObservableList<DownloadsVideo> videoItems = tableView.getItems();
        videoItems.clear();
        for (File videoFile : videoScanner.scan(pathProvider.getDownloadsPath())) {
            videoItems.add(convertFileToDownloadsVideo(videoFile));
        }
        videoItems.sort(Comparator.comparing(video -> video.getFileName().toLowerCase()));
    }

    public void moveVideos(ActionEvent actionEvent) {
        List<DownloadsVideo> toBeRemoved = new ArrayList<>();
        for (DownloadsVideo downloadsVideo : tableView.getItems()) {
            if (downloadsVideo.isMovie() || downloadsVideo.isTvShow()) {
                videoMover.move(downloadsVideo);
                toBeRemoved.add(downloadsVideo);
            }
        }
        tableView.getItems().removeAll(toBeRemoved);
    }

    public void setDownloadsPath(ActionEvent actionEvent) {
        File selectedDirectory = getSelectedDirectory("Choose Downlooads folder", pathProvider.getDownloadsPath());
        if (selectedDirectory != null) {
            String selectedPath = selectedDirectory.getAbsolutePath();
            pathProvider.setDownloadsPath(selectedPath);
            downloadsPathTextField.setText(selectedPath);
        }
    }

    public void setMoviePath(ActionEvent actionEvent) {
        File selectedDirectory = getSelectedDirectory("Choose Movie folder", pathProvider.getMoviePath());
        if (selectedDirectory != null) {
            String selectedPath = selectedDirectory.getAbsolutePath();
            pathProvider.setMoviePath(selectedPath);
            moviePathTextField.setText(selectedPath);
        }
    }

    public void setTvShowPath(ActionEvent actionEvent) {
        File selectedDirectory = getSelectedDirectory("Choose Tv Show folder", pathProvider.getTvShowPath());
        if (selectedDirectory != null) {
            String selectedPath = selectedDirectory.getAbsolutePath();
            pathProvider.setTvShowPath(selectedPath);
            tvShowPathTextField.setText(selectedPath);
        }
    }

    private DownloadsVideo convertFileToDownloadsVideo(File videoFile) {
        DownloadsVideo downloadsVideo = new DownloadsVideo();
        downloadsVideo.setFile(videoFile);
        downloadsVideo.setFileName(videoFile.getName());
        downloadsVideo.setMovie(false);
        downloadsVideo.setTvShow(false);

        downloadsVideo.movieProperty().addListener((obs, prevCheck, currentCheck) -> {
            downloadsVideo.setMovie(currentCheck);
            downloadsVideo.setOutputPath(currentCheck ? videoOutputPathResolver.resolve(downloadsVideo) : "");
        });

        downloadsVideo.tvShowProperty().addListener((obs, prevCheck, currentCheck) -> {
            downloadsVideo.setTvShow(currentCheck);
            downloadsVideo.setOutputPath(currentCheck ? videoOutputPathResolver.resolve(downloadsVideo) : "");
        });

        downloadsVideo.setSubtitles(videoSubtitlesFinder.find(downloadsVideo));

        return downloadsVideo;
    }

    private File getSelectedDirectory(String title, String path) {
        File directory = new File(path);
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle(title);
        if (directory.exists()) {
            chooser.setInitialDirectory(directory);
        }
        return chooser.showDialog(getStage());
    }

    private Stage getStage() {
        return (Stage) container.getScene().getWindow();
    }
}
