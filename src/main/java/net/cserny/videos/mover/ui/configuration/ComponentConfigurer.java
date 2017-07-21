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
    public void configure(TableView tableView) {
        tableView.setEditable(true);
        tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        TableColumn<DownloadsVideo, String> nameCol = new TableColumn<>("Name");
        nameCol.setPrefWidth(250);
        nameCol.setCellValueFactory(new PropertyValueFactory<>("fileName"));
        nameCol.setCellFactory(TextFieldTableCell.forTableColumn());

        TableColumn<DownloadsVideo, Boolean> movieCol = new TableColumn<>("Movie");
        movieCol.setPrefWidth(70);
        movieCol.setCellValueFactory(new PropertyValueFactory<>("movie"));
        movieCol.setCellFactory(param -> new CheckBoxTableCell<>());

        TableColumn<DownloadsVideo, Boolean> tvCol = new TableColumn<>("TvShow");
        tvCol.setPrefWidth(70);
        tvCol.setCellValueFactory(new PropertyValueFactory<>("tvShow"));
        tvCol.setCellFactory(param -> new CheckBoxTableCell<>());

        TableColumn<DownloadsVideo, String> outputCol = new TableColumn<>("Output");
        outputCol.setPrefWidth(490);
        outputCol.setCellValueFactory(new PropertyValueFactory<>("outputPath"));
        outputCol.setCellFactory(TextFieldTableCell.forTableColumn());

        outputCol.setOnEditCommit(event -> event.getTableView().getItems()
                .get(event.getTablePosition().getRow()).setOutputPath(event.getNewValue()));

        tableView.getColumns().addAll(nameCol, movieCol, tvCol, outputCol);
    }
}
