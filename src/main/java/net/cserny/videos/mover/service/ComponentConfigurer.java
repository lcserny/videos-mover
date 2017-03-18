package net.cserny.videos.mover.service;


import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import net.cserny.videos.mover.ui.model.DownloadsVideo;

import java.io.File;

/**
 * Created by Leonardo Cserny on 18.03.2017.
 */
public class ComponentConfigurer
{
    @SuppressWarnings("unchecked")
    public void configure(TableView tableView) {
        tableView.setEditable(true);
        tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        tableView.setMinWidth(780);
        tableView.setMinHeight(350);

        TableColumn<DownloadsVideo, String> nameCol = new TableColumn<>("Name");
        nameCol.setMinWidth(335);
        nameCol.setCellValueFactory(new PropertyValueFactory<>("fileName"));
        nameCol.setCellFactory(TextFieldTableCell.forTableColumn());

        TableColumn<DownloadsVideo, Boolean> movieCol = new TableColumn<>("Is Movie");
        movieCol.setMinWidth(80);
        movieCol.setCellValueFactory(new PropertyValueFactory<>("movie"));
        movieCol.setCellFactory(param -> new CheckBoxTableCell<>());

        TableColumn<DownloadsVideo, Boolean> tvCol = new TableColumn<>("Is TvShow");
        tvCol.setMinWidth(80);
        tvCol.setCellValueFactory(new PropertyValueFactory<>("tvShow"));
        tvCol.setCellFactory(param -> new CheckBoxTableCell<>());

        TableColumn<DownloadsVideo, String> outputCol = new TableColumn<>("Output");
        outputCol.setMinWidth(265);
        outputCol.setCellValueFactory(new PropertyValueFactory<>("outputPath"));
        outputCol.setCellFactory(TextFieldTableCell.forTableColumn());

        outputCol.setOnEditCommit(event -> event.getTableView()
                .getItems()
                .get(event.getTablePosition().getRow())
                .setOutputPath(event.getNewValue()));

        tableView.getColumns().addAll(nameCol, movieCol, tvCol, outputCol);
    }
}
