package mx.uv.fei.gui.controllers.computers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class ComputerButtonController {
    private GuiComputersController guiComputersController;

    @FXML
    private Label serialNumberLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private Pane computerPane;

    @FXML
    private void computerButtonController(ActionEvent event) {
        guiComputersController.openPaneWithComputerInformation(getSerialNumber());
    }

    public GuiComputersController getGuiComputersController() {
        return guiComputersController;
    }

    public void setGuiComputersController(GuiComputersController guiComputersController) {
        this.guiComputersController = guiComputersController;
    }

    public String getSerialNumber() {
        return serialNumberLabel.getText();
    }

    public void setSerialNumber(String serialNumberLabel) {
        this.serialNumberLabel.setText(serialNumberLabel);
    }

    public String getStatus() {
        return statusLabel.getText();
    }

    public void setStatus(String statusLabel) {
        this.statusLabel.setText(statusLabel);
    }

    public Pane getComputerPane() {
        return computerPane;
    }

    public void setComputerPane(Pane computerPane) {
        this.computerPane = computerPane;
    }

}
