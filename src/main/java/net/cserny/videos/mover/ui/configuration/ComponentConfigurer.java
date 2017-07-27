package net.cserny.videos.mover.ui.configuration;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import net.cserny.videos.mover.ui.model.DownloadsVideo;
import org.springframework.stereotype.Service;

/**
 * Created by Leonardo Cserny on 18.03.2017.
 */
@Service
public class ComponentConfigurer
{
    @SuppressWarnings("unchecked")
    public void configureTable(TableView tableView, TableColumn... columns) {
        tableView.setEditable(true);
        tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        for (TableColumn column : columns) {
            switch (column.getText()) {
                case "Name":
                    TableColumn<DownloadsVideo, String> nameCol = (TableColumn<DownloadsVideo, String>) column;
                    nameCol.setCellValueFactory(new PropertyValueFactory<>("fileName"));
                    nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
                    break;
                case "Movie":
                    TableColumn<DownloadsVideo, Boolean> movieCol = (TableColumn<DownloadsVideo, Boolean>) column;
                    movieCol.setCellValueFactory(new PropertyValueFactory<>("movie"));
                    movieCol.setCellFactory(param -> new CheckBoxTableCell<>());
                    break;
                case "TVShow":
                    TableColumn<DownloadsVideo, Boolean> tvShowCol = (TableColumn<DownloadsVideo, Boolean>) column;
                    tvShowCol.setCellValueFactory(new PropertyValueFactory<>("tvShow"));
                    tvShowCol.setCellFactory(param -> new CheckBoxTableCell<>());
                    break;
                case "Output":
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
