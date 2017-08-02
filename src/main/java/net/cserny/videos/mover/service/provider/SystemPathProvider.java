package net.cserny.videos.mover.service.provider;

import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import net.cserny.videos.mover.service.PopupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by Leonardo Cserny on 18.10.2016.
 */
@Service
public class SystemPathProvider
{
    @Autowired
    private PopupService popupService;

    private String downloadsPath;
    private String moviePath;
    private String tvShowPath;
    private TextField downloadsTextField;
    private TextField moviesTextField;
    private TextField tvShowTextField;
    private BorderPane container;

    @PostConstruct
    private void initDefaultPaths() {
        String prefix = processPrefix();
        String downloadsDir = prefix + "Downloads";
        String movieDir = prefix + "Movies/Movies";
        String tvShowDir = prefix + "Movies/TV";

        if (Files.exists(Paths.get(downloadsDir))) {
            downloadsPath = downloadsDir;
        }

        if (Files.exists(Paths.get(movieDir))) {
            moviePath = movieDir;
        }

        if (Files.exists(Paths.get(tvShowDir))) {
            tvShowPath = tvShowDir;
        }
    }

    public String getDownloadsPath() {
        return downloadsPath;
    }

    public String getMoviePath() {
        return moviePath;
    }

    public String getTvShowPath() {
        return tvShowPath;
    }

    public boolean checkDownloadsFolderAvailability() {
        if (downloadsPath == null) {
            popupService.showPopupMessage(Alert.AlertType.ERROR, "Downloads folder doesn't exist, please set correct path and try again.", "Downloads Error");
            return false;
        }
        return true;
    }

    public boolean checkMoviesFolderAvilability() {
        if (moviePath == null) {
            popupService.showPopupMessage(Alert.AlertType.ERROR, "Movies folder doesn't exist, please set correct path and try again.", "Movies Error");
            return false;
        }
        return true;
    }

    public boolean checkTvShowsFolderAvailability() {
        if (tvShowPath == null) {
            popupService.showPopupMessage(Alert.AlertType.ERROR, "TVShows folder doesn't exist, please set correct path and try again.", "TVShows Error");
            return false;
        }
        return true;
    }

    private String processPrefix() {
        String prefix = "/mnt/Data/";
        String osName = System.getProperty("os.name");
        if (osName.contains("Windows")) {
            prefix = "D:/";
        }
        return prefix;
    }

    public void setDownloadsTextField(TextField downloadsTextField) {
        this.downloadsTextField = downloadsTextField;
        this.downloadsTextField.setText(downloadsPath);
    }

    public void setMoviesTextField(TextField moviesTextField) {
        this.moviesTextField = moviesTextField;
        this.moviesTextField.setText(moviePath);
    }

    public void setTvShowTextField(TextField tvShowTextField) {
        this.tvShowTextField = tvShowTextField;
        this.tvShowTextField.setText(tvShowPath);
    }

    public void populateDownloadsPath() {
        File selectedDirectory = getSelectedDirectory("Choose Downlooads folder", downloadsPath);
        if (selectedDirectory != null) {
            String selectedPath = selectedDirectory.getAbsolutePath();
            downloadsPath = selectedPath;
            downloadsTextField.setText(selectedPath);
        }
    }

    public void populateMoviePath() {
        File selectedDirectory = getSelectedDirectory("Choose Movie folder", moviePath);
        if (selectedDirectory != null) {
            String selectedPath = selectedDirectory.getAbsolutePath();
            moviePath = selectedPath;
            moviesTextField.setText(selectedPath);
        }
    }

    public void populateTvShowPath() {
        File selectedDirectory = getSelectedDirectory("Choose Tv Show folder", tvShowPath);
        if (selectedDirectory != null) {
            String selectedPath = selectedDirectory.getAbsolutePath();
            tvShowPath = selectedPath;
            tvShowTextField.setText(selectedPath);
        }
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

    public void setContainer(BorderPane container) {
        this.container = container;
    }

    private Stage getStage() {
        return (Stage) container.getScene().getWindow();
    }
}
