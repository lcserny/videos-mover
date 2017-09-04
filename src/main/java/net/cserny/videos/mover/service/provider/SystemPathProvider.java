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
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Leonardo Cserny on 18.10.2016.
 */
@Service
public class SystemPathProvider
{
    @Autowired
    private PopupService popupService;

    private Path downloadsPath;
    private Path moviePath;
    private Path tvShowPath;
    private TextField downloadsTextField;
    private TextField moviesTextField;
    private TextField tvShowTextField;
    private BorderPane container;

    @PostConstruct
    private void initDefaultPaths() {
        if (isWindowsOs()) {
            initPaths("D:/");
        } else {
            if (isLaptopWithHostname("ubulap")) {
                initPaths("/home/sabyx/");
            } else {
                initPaths("/mnt/Data/");
            }
        }
    }

    private boolean isLaptopWithHostname(String hostname) {
        try {
            return hostname.equals(InetAddress.getLocalHost().getHostName());
        } catch (UnknownHostException ignored) { }
        return false;
    }

    private void initPaths(String osPrefix) {
        Path downloadsPath = Paths.get(osPrefix + "Downloads");
        if (Files.exists(downloadsPath)) {
            this.downloadsPath = downloadsPath;
        }

        Path moviesPath = Paths.get(osPrefix + "Movies/Movies");
        Path alternateMoviesPath = Paths.get(osPrefix + "Videos/Movies");
        if (Files.exists(moviesPath)) {
            this.moviePath = moviesPath;
        } else if (Files.exists(alternateMoviesPath)) {
            this.moviePath = alternateMoviesPath;
        }

        Path tvShowsPath = Paths.get(osPrefix + "Movies/TV");
        Path alternateTvShowsPath = Paths.get(osPrefix + "Videos/TV");
        if (Files.exists(tvShowsPath)) {
            this.tvShowPath = tvShowsPath;
        } else if (Files.exists(alternateTvShowsPath)) {
            this.tvShowPath = alternateTvShowsPath;
        }
    }

    private boolean isWindowsOs() {
        String osName = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
        return osName.contains("win");
    }

    public Path getDownloadsPath() {
        return downloadsPath;
    }

    public Path getMoviePath() {
        return moviePath;
    }

    public Path getTvShowPath() {
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

    public void setDownloadsTextField(TextField downloadsTextField) {
        if (downloadsPath != null) {
            this.downloadsTextField = downloadsTextField;
            this.downloadsTextField.setText(downloadsPath.toString());
        }
    }

    public void setMoviesTextField(TextField moviesTextField) {
        if (moviePath != null) {
            this.moviesTextField = moviesTextField;
            this.moviesTextField.setText(moviePath.toString());
        }
    }

    public void setTvShowTextField(TextField tvShowTextField) {
        if (tvShowPath != null) {
            this.tvShowTextField = tvShowTextField;
            this.tvShowTextField.setText(tvShowPath.toString());
        }
    }

    public void populateDownloadsPath() {
        File selectedDirectory = getSelectedDirectory("Choose Downlooads folder", downloadsPath.toString());
        if (selectedDirectory != null) {
            String selectedPath = selectedDirectory.getAbsolutePath();
            downloadsPath = Paths.get(selectedPath);
            downloadsTextField.setText(selectedPath);
        }
    }

    public void populateMoviePath() {
        File selectedDirectory = getSelectedDirectory("Choose Movie folder", moviePath.toString());
        if (selectedDirectory != null) {
            String selectedPath = selectedDirectory.getAbsolutePath();
            moviePath = Paths.get(selectedPath);
            moviesTextField.setText(selectedPath);
        }
    }

    public void populateTvShowPath() {
        File selectedDirectory = getSelectedDirectory("Choose Tv Show folder", tvShowPath.toString());
        if (selectedDirectory != null) {
            String selectedPath = selectedDirectory.getAbsolutePath();
            tvShowPath = Paths.get(selectedPath);
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
