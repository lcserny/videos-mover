package net.cserny.videos.mover;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

/**
 * Created by Leonardo Cserny on 16.10.2016.
 */
@SpringBootApplication
public class VideosMoverApplication extends Application
{
    private ConfigurableApplicationContext context;
    private Parent root;

    public static void main(String[] args) throws IOException {
        launch(VideosMoverApplication.class, args);
    }

    @Override
    public void init() throws Exception {
        context = SpringApplication.run(VideosMoverApplication.class);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        loader.setControllerFactory(context::getBean);
        root = loader.load();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Downloads Video Mover");
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/pomodoro.png")));
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        context.stop();
    }
}
