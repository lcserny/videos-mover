package net.cserny.videos.mover.ui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import net.cserny.videos.mover.service.LoadingImageService;
import net.cserny.videos.mover.service.VideoMover;
import net.cserny.videos.mover.service.provider.SystemPathProvider;
import net.cserny.videos.mover.ui.configuration.MainTableService;
import net.cserny.videos.mover.ui.model.DownloadsVideo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Leonardo Cserny on 16.10.2016.
 */
@Controller
public class MainController implements Initializable
{
    @FXML private BorderPane container;
    @FXML private ImageView loadingImage;
    @FXML private TextField downloadsPathTextField;
    @FXML private TextField moviePathTextField;
    @FXML private TextField tvShowPathTextField;
    @FXML private TableView<DownloadsVideo> tableView;
    @FXML private TableColumn<DownloadsVideo, String> nameCol;
    @FXML private TableColumn<DownloadsVideo, Boolean> movieCol;
    @FXML private TableColumn<DownloadsVideo, Boolean> tvshowCol;
    @FXML private TableColumn<DownloadsVideo, String> outputCol;

    @Autowired
    private LoadingImageService loadingImageService;
    @Autowired
    private MainTableService mainTableService;
    @Autowired
    private SystemPathProvider pathProvider;
    @Autowired
    private VideoMover videoMover;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mainTableService.configure(tableView, nameCol, movieCol, tvshowCol, outputCol);
        loadingImageService.setLoadingImage(loadingImage);
        pathProvider.setContainer(container);
        pathProvider.setDownloadsTextField(downloadsPathTextField);
        pathProvider.setMoviesTextField(moviePathTextField);
        pathProvider.setTvShowTextField(tvShowPathTextField);
    }

    public void loadTableView(ActionEvent actionEvent) {
        mainTableService.loadTable();
    }

    public void moveVideos(ActionEvent actionEvent) {
        videoMover.moveAllSelected();
    }

    public void setDownloadsPath(ActionEvent actionEvent) {
        pathProvider.populateDownloadsPath();
    }

    public void setMoviePath(ActionEvent actionEvent) {
        pathProvider.populateMoviePath();
    }

    public void setTvShowPath(ActionEvent actionEvent) {
        pathProvider.populateTvShowPath();
    }
}
