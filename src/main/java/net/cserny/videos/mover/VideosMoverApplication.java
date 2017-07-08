package net.cserny.videos.mover;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.PropertySource;

import java.io.IOException;

/**
 * Created by Leonardo Cserny on 16.10.2016.
 */
@SpringBootApplication
public class VideosMoverApplication
{
    public static void main(String[] args) throws IOException {
        SpringApplication.run(VideosMoverApplication.class, args);
        openBrowser("http://localhost:9201");
    }

    private static void openBrowser(String url) throws IOException {
        String os = System.getProperty("os.name").toLowerCase();
        Runtime runtime = Runtime.getRuntime();

        if (os.contains("nix") || os.contains("nux")) {
            runtime.exec("xdg-open " + url);
        } else if (os.contains("win")) {
            runtime.exec( "rundll32 url.dll,FileProtocolHandler " + url);
        } else if (os.contains("mac")) {
            runtime.exec( "open" + url);
        }
    }
}
