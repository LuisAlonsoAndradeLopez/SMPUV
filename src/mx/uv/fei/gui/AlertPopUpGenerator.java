package mx.uv.fei.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;

public class AlertPopUpGenerator {

    @FXML
    private DialogPane dialogPane;

    public void showCustomMessage(Alert.AlertType AlertType, String header, String content) {
        Alert customMessage = new Alert(AlertType);
        customMessage.setHeaderText(header);
        customMessage.setContentText(content);
        dialogPane = customMessage.getDialogPane();
        dialogPane.getStyleClass().add("dialog");
        customMessage.showAndWait();
    }
}
