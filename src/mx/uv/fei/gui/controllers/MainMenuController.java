package mx.uv.fei.gui.controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import mx.uv.fei.gui.AlertPopUpGenerator;

public class MainMenuController {
    @FXML
    private Button partsButton;
    @FXML
    private Button computersButton;

    @FXML
    private void goToPartsManager(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mx/uv/fei/gui/fxml/refactions/GuiRefactions.fxml"));
        try {
            Parent parent = loader.load();
            Scene scene = new Scene(parent);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException exception) {
            new AlertPopUpGenerator().showCustomMessage(AlertType.ERROR, "Error", "Hubo un error, inténtelo más tarde");
        }
    }

    @FXML
    private void goToComputerManager(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mx/uv/fei/gui/fxml/computers/GuiComputers.fxml"));
        try {
            Parent parent = loader.load();
            Scene scene = new Scene(parent);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException exception) {
            new AlertPopUpGenerator().showCustomMessage(AlertType.ERROR, "Error", "Hubo un error, inténtelo más tarde");
        }
    }
}
