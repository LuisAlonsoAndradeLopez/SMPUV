package mx.uv.fei.gui.controllers.computers;

import java.io.IOException;
import java.sql.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import mx.uv.fei.gui.AlertPopUpGenerator;
import mx.uv.fei.gui.controllers.services.GuiStartMaintenanceController;
import mx.uv.fei.logic.daos.ComputerDAO;
import mx.uv.fei.logic.domain.Computer;
import mx.uv.fei.logic.domain.Mark;
import mx.uv.fei.logic.exceptions.DataRetrievalException;

public class ComputerInformationController {
    private static final Logger LOGGER = Logger.getLogger(ComputerInformationController.class.getName());
    private GuiComputersController guiComputersController;

    @FXML
    private Label adquisitionDateLabel;

    @FXML
    private Label cpuLabel;

    @FXML
    private Label diskLabel;

    @FXML
    private Label gpuLabel;

    @FXML
    private Label markLabel;

    @FXML
    private Label motherBoardLabel;

    @FXML
    private Label powerSourceLabel;

    @FXML
    private Label ramMemoryLabel;

    @FXML
    private Label serialNumberLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private Label typeLabel;

    @FXML
    private void editButtonController(ActionEvent event) {
        guiComputersController.openModifyComputerPane(this);
    }

    @FXML
    private void startMaintenanceButton(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/mx/uv/fei/gui/fxml/services/GuiStartMaintenance.fxml"));
        try {
            Parent parent = loader.load();
            ComputerDAO computerDAO = new ComputerDAO();
            Computer computer = computerDAO.getComputerFromDatabase(getSerialNumber());
            GuiStartMaintenanceController guiStartMaintenanceController = loader.getController();
            guiStartMaintenanceController.setComputer(computer);
            Scene scene = new Scene(parent);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);

            stage.show();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Something went wrong", e);
            new AlertPopUpGenerator().showCustomMessage(AlertType.ERROR, "Error", "Hubo un error, inténtelo más tarde");
        } catch (DataRetrievalException e) {
            LOGGER.log(Level.SEVERE, "Something went wrong", e);
            new AlertPopUpGenerator().showCustomMessage(AlertType.ERROR, "Error", "Hubo un error, inténtelo más tarde");
        }
    }

    public GuiComputersController getGuiComputersController() {
        return guiComputersController;
    }

    public void setGuiComputersController(GuiComputersController guiComputersController) {
        this.guiComputersController = guiComputersController;
    }

    public Date getAdquisitionDate() {
        return Date.valueOf(adquisitionDateLabel.getText());
    }

    public void setAdquisitionDate(Date adquisitionDate) {
        this.adquisitionDateLabel.setText(adquisitionDate.toString());
    }

    public String getCpu() {
        return cpuLabel.getText();
    }

    public void setCpu(String cpu) {
        this.cpuLabel.setText(cpu);
    }

    public String getDisk() {
        return diskLabel.getText();
    }

    public void setDisk(String disk) {
        this.diskLabel.setText(disk);
    }

    public String getGpu() {
        return gpuLabel.getText();
    }

    public void setGpu(String gpu) {
        this.gpuLabel.setText(gpu);
    }

    public String getMark() {
        return markLabel.getText();
    }

    public void setMark(Mark mark) {
        this.markLabel.setText(mark.getName());
    }

    public String getMotherBoard() {
        return motherBoardLabel.getText();
    }

    public void setMotherBoard(String motherBoard) {
        this.motherBoardLabel.setText(motherBoard);
    }

    public String getPowerSource() {
        return powerSourceLabel.getText();
    }

    public void setPowerSource(String powerSource) {
        this.powerSourceLabel.setText(powerSource);
    }

    public String getRamMemory() {
        return ramMemoryLabel.getText();
    }

    public void setRamMemory(String ramMemory) {
        this.ramMemoryLabel.setText(ramMemory);
    }

    public String getSerialNumber() {
        return serialNumberLabel.getText();
    }

    public void setSerialNumber(String status) {
        this.serialNumberLabel.setText(status);
    }

    public String getStatus() {
        return statusLabel.getText();
    }

    public void setStatus(String status) {
        this.statusLabel.setText(status);
    }

    public String getType() {
        return typeLabel.getText();
    }

    public void setType(String type) {
        this.typeLabel.setText(type);
    }
}
