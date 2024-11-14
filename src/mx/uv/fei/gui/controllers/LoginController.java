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
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mx.uv.fei.gui.AlertPopUpGenerator;
import mx.uv.fei.logic.daos.LoginDAO;
import mx.uv.fei.logic.domain.Admin;
import mx.uv.fei.logic.domain.MaintenanceMan;
import mx.uv.fei.logic.exceptions.LoginException;

public class LoginController {
    @FXML
    private TextField idTextField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Label errorLabel;

    @FXML
    private void logIn(ActionEvent event) {
        if (!idTextField.getText().isBlank() && !passwordField.getText().isBlank()) {
            LoginDAO loginDAO = new LoginDAO();

            try {
                Admin admin = loginDAO.logInAdmin(idTextField.getText(), passwordField.getText());
                if (admin.getAdminId() > 0) {
                    openMainMenu(event);
                    return;
                }

                MaintenanceMan maintenanceMan = loginDAO.logInMaintenanceMan(idTextField.getText(),
                        passwordField.getText());
                if ((maintenanceMan.getStaffNumber() > 0)) {
                    openMainMenu(event);
                    return;
                }

                errorLabel.setText("Las credenciales ingresadas no coinciden con ningun usuario");
            } catch (LoginException exception) {
                new AlertPopUpGenerator().showCustomMessage(AlertType.ERROR, "Error",
                        "Hubo un error, inténtelo más tarde");
            }

        } else {
            errorLabel.setText("Faltan campos por llenar");
        }
    }

    private void openMainMenu(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/mx/uv/fei/gui/fxml/MainMenu.fxml"));
            Parent parent = loader.load();

            Stage stage = new Stage();
            Scene scene = new Scene(parent);
            stage.setTitle("SPGER");
            stage.setScene(scene);
            stage.show();

            Stage oldStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            oldStage.close();
        } catch (IOException exception) {
            new AlertPopUpGenerator().showCustomMessage(AlertType.ERROR, "Error", "Hubo un error, inténtelo más tarde");
        }
    }
}
