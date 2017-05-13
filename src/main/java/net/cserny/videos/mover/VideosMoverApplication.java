package net.cserny.videos.mover;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Leonardo Cserny on 16.10.2016.
 */
// TODO: add Spring to the project for easy properties loading and such
public class VideosMoverApplication extends Application
{
    public static void main(String[] args) throws IOException
    {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"));
        Scene scene = new Scene(root, 800, 600);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Downloads Video Mover");
        primaryStage.centerOnScreen();
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/pomodoro.png")));
        primaryStage.show();
    }
}
