package net.cserny.videos.mover.service;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.springframework.stereotype.Service;

@Service
public class LoadingImageService
{
    private ImageView loadingImage;

    public void setLoadingImage(ImageView loadingImage) {
        this.loadingImage = loadingImage;
    }

    public void restoreDefaultScanImage() {
        changeLoadingImage("scan-button.png");
    }

    public void showLoadingGif() {
        changeLoadingImage("loading.gif");
    }

    private void changeLoadingImage(String imageName) {
        loadingImage.setImage(new Image(getClass().getResourceAsStream("/images/" + imageName)));
    }
}
