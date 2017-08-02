package net.cserny.videos.mover.service;

import javafx.scene.control.Alert;
import org.springframework.stereotype.Service;

@Service
public class PopupService
{
    public void showPopupMessage(Alert.AlertType type, String message, String title) {
        Alert alert = new Alert(type, message);
        alert.setHeaderText(null);
        alert.setTitle(title);
        alert.show();
    }

    public void showMoveCompletionMessage(boolean filesMoved) {
        if (filesMoved) {
            showPopupMessage(Alert.AlertType.INFORMATION, "Selected video files have been moved successfully!", "Move Successful!");
        } else {
            showPopupMessage(Alert.AlertType.INFORMATION, "No video files have been selected, nothing was moved...", "Nothing to Move");
        }
    }
}
