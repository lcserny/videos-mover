package net.cserny.videos.mover.ui.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    @Autowired
    private ComponentConfigurer componentConfigurer;
    @Autowired
    private SystemPathProvider pathProvider;
    @Autowired
    private VideoScanner videoScanner;
    @Autowired
    private VideoMover videoMover;
    @Autowired
    private VideoSubtitlesFinder videoSubtitlesFinder;
    @Autowired
    private VideoOutputPathResolver videoOutputPathResolver;

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
        showLoadingGif();
        Runnable expensiveTask = () -> {
            if (pathProvider.getDownloadsPath() != null) {
                ObservableList<DownloadsVideo> items = FXCollections.observableArrayList();
                for (Path videoPath : videoScanner.scan(pathProvider.getDownloadsPath())) {
                    items.add(convertFileToDownloadsVideo(videoPath));
                }
                items.sort(Comparator.comparing(video -> video.getFileName().toLowerCase()));
                tableView.setItems(items);
            }
            restoreDefaultScanImage();
        };
        new Thread(expensiveTask).start();

        checkDownloadsFolderAvailability();
    }

    private boolean checkDownloadsFolderAvailability() {
        if (pathProvider.getDownloadsPath() == null) {
            showPopupMessage(AlertType.ERROR, "Downloads folder doesn't exist, please set correct path and try again.", "Downloads Error");
            return false;
        }
        return true;
    }

    private boolean checkMoviesFolderAvilability() {
        if (pathProvider.getMoviePath() == null) {
            showPopupMessage(AlertType.ERROR, "Movies folder doesn't exist, please set correct path and try again.", "Movies Error");
            return false;
        }
        return true;
    }

    private boolean checkTvShowsFolderAvailability() {
        if (pathProvider.getTvShowPath() == null) {
            showPopupMessage(AlertType.ERROR, "TVShows folder doesn't exist, please set correct path and try again.", "TVShows Error");
            return false;
        }
        return true;
    }

    private void showPopupMessage(AlertType type, String message, String title) {
        Alert alert = new Alert(type, message);
        alert.setHeaderText(null);
        alert.setTitle(title);
        alert.show();
    }

    private void changeLoadingImage(String imageName) {
        loadingImage.setImage(new Image(getClass().getResourceAsStream("/images/" + imageName)));
    }

    private void restoreDefaultScanImage() {
        changeLoadingImage("scan-button.png");
    }

    private void showLoadingGif() {
        changeLoadingImage("loading.gif");
    }

    public void moveVideos(ActionEvent actionEvent) {
        boolean errorOccurred = false;
        List<DownloadsVideo> toBeRemoved = new ArrayList<>();
        for (DownloadsVideo downloadsVideo : tableView.getItems()) {
            if (downloadsVideo.isMovie() || downloadsVideo.isTvShow()) {
                try {
                    videoMover.move(downloadsVideo);
                    toBeRemoved.add(downloadsVideo);
                } catch (IOException e) {
                    errorOccurred = true;
                    showPopupMessage(AlertType.ERROR, String.format("Failed to move video file: %s, please check!", downloadsVideo.getFileName()), "Move Failed!");
                    break;
                }
            }
        }
        tableView.getItems().removeAll(toBeRemoved);

        if (!errorOccurred) {
            showMoveCompletionMessage(!toBeRemoved.isEmpty());
        }
    }

    private void showMoveCompletionMessage(boolean filesMoved) {
        if (filesMoved) {
            showPopupMessage(AlertType.INFORMATION, "Selected video files have been moved successfully!", "Move Successful!");
        } else {
            showPopupMessage(AlertType.INFORMATION, "No video files have been selected, nothing was moved...", "Nothing to Move");
        }
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

    private DownloadsVideo convertFileToDownloadsVideo(Path videoPath) {
        DownloadsVideo downloadsVideo = new DownloadsVideo();
        downloadsVideo.setFile(videoPath);
        downloadsVideo.setFileName(videoPath.getFileName().toString());
        downloadsVideo.setMovie(false);
        downloadsVideo.setTvShow(false);

        downloadsVideo.movieProperty().addListener((obs, prevCheck, currentCheck) -> {
            if (checkMoviesFolderAvilability()) {
                downloadsVideo.setMovie(currentCheck);
                downloadsVideo.setOutputPath(currentCheck ? videoOutputPathResolver.resolve(downloadsVideo) : "");
            }
        });

        downloadsVideo.tvShowProperty().addListener((obs, prevCheck, currentCheck) -> {
            if (checkTvShowsFolderAvailability()) {
                downloadsVideo.setTvShow(currentCheck);
                downloadsVideo.setOutputPath(currentCheck ? videoOutputPathResolver.resolve(downloadsVideo) : "");
            }
        });

        downloadsVideo.setSubtitles(videoSubtitlesFinder.find(downloadsVideo));

        return downloadsVideo;
    }

    private File getSelectedDirectory(String title, String path) {
        File directory = path != null ? new File(path) : null;
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle(title);
        if (directory != null && directory.exists()) {
            chooser.setInitialDirectory(directory);
        }
        return chooser.showDialog(getStage());
    }

    private Stage getStage() {
        return (Stage) container.getScene().getWindow();
    }
}
