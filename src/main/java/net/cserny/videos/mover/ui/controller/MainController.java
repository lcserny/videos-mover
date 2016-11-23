package net.cserny.videos.mover.ui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import net.cserny.videos.mover.service.DefaultPathProvider;
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
    private Button chooseButton;
    @FXML
    private Button chooseTvShowPathButton;
    @FXML
    private Button scanButton;
    @FXML
    private Button moveButton;
    @FXML
    private Button chooseMoviePathButton;
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

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        initDefaultPaths();
        initTableView();
    }

    private void initDefaultPaths()
    {
        DefaultPathProvider pathProvider = new DefaultPathProvider();
        setDownloadsPath(pathProvider.getDownloadsPath());
        setMoviePath(pathProvider.getMoviePath());
        setTvShowPath(pathProvider.getTvShowPath());
    }

    public void loadTableView()
    {
        populateTableView(tableView, getVideos(downloadsPath));
    }

    @SuppressWarnings("unchecked")
    public void initTableView()
    {
        tableView.setEditable(true);
        tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        tableView.setMinWidth(595);

        TableColumn<DownloadsVideo, String> nameCol = new TableColumn<>("Name");
        nameCol.setMinWidth(350);
        nameCol.setCellValueFactory(new PropertyValueFactory<>("fileName"));
        nameCol.setCellFactory(param -> new TableCell<DownloadsVideo, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                setGraphic(new Label(item));
            }
        });

        TableColumn<DownloadsVideo, Boolean> movieCol = new TableColumn<>("Is Movie");
        movieCol.setMinWidth(100);
        movieCol.setCellValueFactory(new PropertyValueFactory<>("movie"));
        movieCol.setCellFactory(param -> new CheckBoxTableCell<>());

        TableColumn<DownloadsVideo, Boolean> tvCol = new TableColumn<>("Is TvShow");
        tvCol.setMinWidth(100);
        tvCol.setCellValueFactory(new PropertyValueFactory<>("tvShow"));
        tvCol.setCellFactory(param -> new CheckBoxTableCell<>());

        tableView.getColumns().addAll(nameCol, movieCol, tvCol);
    }

    private void populateTableView(TableView<DownloadsVideo> tableView, List<File> videos)
    {
        tableView.getItems().clear();
        for (File video : videos) {
            DownloadsVideo item = new DownloadsVideo();
            item.setFile(video);
            item.setFileName(video.getName());
            item.setMovie(false);
            item.setTvShow(false);

            item.movieProperty().addListener((obs, wasOn, isNowOn) -> {
                item.setMovie(isNowOn);
            });

            item.tvShowProperty().addListener((obs, wasOn, isNowOn) -> {
                item.setTvShow(isNowOn);
            });

            tableView.getItems().add(item);
        }
    }

    private List<File> getVideos(String path)
    {
        List<File> videoFiles = new ArrayList<>();
        VideoProvider videoProvider = new VideoProvider(path);
        try {
            videoFiles = videoProvider.processVideoFiles();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return videoFiles;
    }

    private void setDownloadsPath(String path)
    {
        downloadsPath = path;
        chooseTextField.setText(path);
    }

    private void setMoviePath(String path)
    {
        moviePath = path;
        chooseMoviePathTextField.setText(path);
    }

    private void setTvShowPath(String path)
    {
        tvShowPath = path;
        chooseTvShowPathTextField.setText(path);
    }

    private File getSelectedDirectory(String title, String path)
    {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle(title);
        setDefaultDirectoryToChooser(chooser, path);

        return chooser.showDialog(getStage());
    }

    public void setDownloadsPath(ActionEvent actionEvent)
    {
        File selectedDirectory = getSelectedDirectory("Choose Downlooads folder", downloadsPath);
        if (selectedDirectory != null) {
            setDownloadsPath(selectedDirectory.getAbsolutePath());
        }
    }

    public void setMoviePath(ActionEvent actionEvent)
    {
        File selectedDirectory = getSelectedDirectory("Choose Movie folder", moviePath);
        if (selectedDirectory != null) {
            setMoviePath(selectedDirectory.getAbsolutePath());
        }
    }

    public void setTvShowPath(ActionEvent actionEvent)
    {
        File selectedDirectory = getSelectedDirectory("Choose Tv Show folder", tvShowPath);
        if (selectedDirectory != null) {
            setTvShowPath(selectedDirectory.getAbsolutePath());
        }
    }

    private void setDefaultDirectoryToChooser(DirectoryChooser chooser, String path)
    {
        File defaultDirectory = new File(path);
        if (defaultDirectory.exists()) {
            chooser.setInitialDirectory(defaultDirectory);
        }
    }

    private Stage getStage()
    {
        return (Stage) container.getScene().getWindow();
    }

    public void moveVideos(ActionEvent actionEvent)
    {
        List<DownloadsVideo> toBeRemoved = new ArrayList<>();
        tableView.getItems()
            .stream()
            .filter(downloadsVideo -> downloadsVideo.getMovie() || downloadsVideo.getTvShow())
            .forEach(downloadsVideo -> {
                processMoveVideo(downloadsVideo);
                toBeRemoved.add(downloadsVideo);
            });
        tableView.getItems().removeAll(toBeRemoved);
    }

    private void processMoveVideo(DownloadsVideo downloadsVideo)
    {
        VideoMover videoMover = new VideoMover(downloadsVideo);
        videoMover.setMoviePath(moviePath);
        videoMover.setTvShowPath(tvShowPath);

        videoMover.move();
    }
}
