package net.cserny.videos.mover.ui.configuration;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import net.cserny.videos.mover.service.DownloadsVideoConverter;
import net.cserny.videos.mover.service.LoadingImageService;
import net.cserny.videos.mover.service.VideoScanner;
import net.cserny.videos.mover.service.provider.SystemPathProvider;
import net.cserny.videos.mover.ui.model.DownloadsVideo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Leonardo Cserny on 18.03.2017.
 */
@Service
public class MainTableService
{
    @Autowired
    private LoadingImageService loadingImageService;
    @Autowired
    private SystemPathProvider pathProvider;
    @Autowired
    private VideoScanner videoScanner;
    @Autowired
    private DownloadsVideoConverter downloadsVideoConverter;

    private TableView<DownloadsVideo> tableView;
    private List<TableColumn<DownloadsVideo, ?>> tableColumns;

    @SafeVarargs
    public final void configure(TableView<DownloadsVideo> tableView, TableColumn<DownloadsVideo, ?>... tableColumn) {
        this.tableView = tableView;
        this.tableColumns = Arrays.asList(tableColumn);

        configureTable();
        configureColumns();
    }

    public ObservableList<DownloadsVideo> getTableItems() {
        return tableView.getItems();
    }

    public void setTableItems(ObservableList<DownloadsVideo> items) {
        tableView.setItems(items);
    }

    public void removeAllTableItems(List<DownloadsVideo> toBeRemoved) {
        tableView.getItems().removeAll(toBeRemoved);
    }

    public synchronized void loadTable() {
        loadingImageService.showLoadingGif();
        Runnable expensiveTask = () -> {
            if (pathProvider.getDownloadsPath() != null) {
                ObservableList<DownloadsVideo> items = FXCollections.observableArrayList();
                for (Path videoPath : videoScanner.scan(pathProvider.getDownloadsPath())) {
                    items.add(downloadsVideoConverter.convertFileToDownloadsVideo(videoPath));
                }
                items.sort(Comparator.comparing(video -> video.getFileName().toLowerCase()));
                setTableItems(items);
            }
            loadingImageService.restoreDefaultScanImage();
        };
        new Thread(expensiveTask).start();

        pathProvider.checkDownloadsFolderAvailability();
    }

    private void configureTable() {
        tableView.setEditable(true);
        tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
    }

    @SuppressWarnings("unchecked")
    private void configureColumns() {
        for (TableColumn column : tableColumns) {
            switch (column.getId()) {
                case "nameCol":
                    TableColumn<DownloadsVideo, String> nameCol = (TableColumn<DownloadsVideo, String>) column;
                    nameCol.setCellValueFactory(new PropertyValueFactory<>("fileName"));
                    nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
                    break;
                case "movieCol":
                    TableColumn<DownloadsVideo, Boolean> movieCol = (TableColumn<DownloadsVideo, Boolean>) column;
                    movieCol.setCellValueFactory(new PropertyValueFactory<>("movie"));
                    movieCol.setCellFactory(param -> new CheckBoxTableCell<>());
                    break;
                case "tvshowCol":
                    TableColumn<DownloadsVideo, Boolean> tvShowCol = (TableColumn<DownloadsVideo, Boolean>) column;
                    tvShowCol.setCellValueFactory(new PropertyValueFactory<>("tvShow"));
                    tvShowCol.setCellFactory(param -> new CheckBoxTableCell<>());
                    break;
                case "outputCol":
                    TableColumn<DownloadsVideo, String> outputCol = (TableColumn<DownloadsVideo, String>) column;
                    outputCol.setCellValueFactory(new PropertyValueFactory<>("outputPath"));
                    outputCol.setCellFactory(TextFieldTableCell.forTableColumn());
                    outputCol.setOnEditCommit(event -> event.getTableView().getItems()
                            .get(event.getTablePosition().getRow()).setOutputPath(event.getNewValue()));
                    break;
            }
        }
    }
}
