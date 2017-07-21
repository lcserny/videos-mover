package net.cserny.videos.mover.ui.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import net.cserny.videos.mover.service.VideoMover;
import net.cserny.videos.mover.service.VideoOutputPathResolver;
import net.cserny.videos.mover.service.VideoScanner;
import net.cserny.videos.mover.service.VideoSubtitlesFinder;
import net.cserny.videos.mover.service.provider.SystemPathProvider;
import net.cserny.videos.mover.ui.configuration.ComponentConfigurer;
import net.cserny.videos.mover.ui.model.DownloadsVideo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Leonardo Cserny on 16.10.2016.
 */
@Controller
public class MainController implements Initializable
{
    @FXML
    private BorderPane container;
    @FXML
    private ImageView loadingImage;
    @FXML
    private TextField downloadsPathTextField;
    @FXML
    private TextField moviePathTextField;
    @FXML
    private TextField tvShowPathTextField;
    @FXML
    private TableView<DownloadsVideo> tableView;
    @FXML
    private TableColumn<DownloadsVideo, String> nameCol;
    @FXML
    private TableColumn<DownloadsVideo, Boolean> movieCol;
    @FXML
    private TableColumn<DownloadsVideo, Boolean> tvshowCol;
    @FXML
    private TableColumn<DownloadsVideo, String> outputCol;

    private ComponentConfigurer componentConfigurer;
    private SystemPathProvider pathProvider;
    private VideoScanner videoScanner;
    private VideoMover videoMover;
    private VideoSubtitlesFinder videoSubtitlesFinder;
    private VideoOutputPathResolver videoOutputPathResolver;

    @Autowired
    public void setComponentConfigurer(ComponentConfigurer componentConfigurer) {
        this.componentConfigurer = componentConfigurer;
    }

    @Autowired
    public void setVideoMover(VideoMover videoMover) {
        this.videoMover = videoMover;
    }

    @Autowired
    public void setVideoOutputPathResolver(VideoOutputPathResolver videoOutputPathResolver) {
        this.videoOutputPathResolver = videoOutputPathResolver;
    }

    @Autowired
    public void setPathProvider(SystemPathProvider pathProvider) {
        this.pathProvider = pathProvider;
    }

    @Autowired
    public void setVideoScanner(VideoScanner videoScanner) {
        this.videoScanner = videoScanner;
    }

    @Autowired
    public void setVideoSubtitlesFinder(VideoSubtitlesFinder videoSubtitlesFinder) {
        this.videoSubtitlesFinder = videoSubtitlesFinder;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        componentConfigurer.configureTable(tableView, nameCol, movieCol, tvshowCol, outputCol);
        initDefaults();
    }

    private void initDefaults() {
        downloadsPathTextField.setText(pathProvider.getDownloadsPath());
        moviePathTextField.setText(pathProvider.getMoviePath());
        tvShowPathTextField.setText(pathProvider.getTvShowPath());
    }

    public synchronized void loadTableView() {
        loadingImage.setImage(new Image(getClass().getResourceAsStream("/images/loading.gif")));

        Runnable expensiveTask = () -> {
            ObservableList<DownloadsVideo> items = FXCollections.observableArrayList();
            for (File videoFile : videoScanner.scan(pathProvider.getDownloadsPath())) {
                items.add(convertFileToDownloadsVideo(videoFile));
            }
            items.sort(Comparator.comparing(video -> video.getFileName().toLowerCase()));
            tableView.setItems(items);
            loadingImage.setImage(new Image(getClass().getResourceAsStream("/images/scan-button.png")));
        };

        new Thread(expensiveTask).start();
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
