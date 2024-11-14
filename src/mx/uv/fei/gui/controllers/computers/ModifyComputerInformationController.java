package mx.uv.fei.gui.controllers.computers;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import mx.uv.fei.gui.AlertPopUpGenerator;
import mx.uv.fei.logic.daos.ComputerDAO;
import mx.uv.fei.logic.daos.MarkDAO;
import mx.uv.fei.logic.domain.Computer;
import mx.uv.fei.logic.domain.Mark;
import mx.uv.fei.logic.domain.enums.ComputerStatus;
import mx.uv.fei.logic.exceptions.ConstraintViolationException;
import mx.uv.fei.logic.exceptions.DataRetrievalException;
import mx.uv.fei.logic.exceptions.DataWritingException;

public class ModifyComputerInformationController {
    private ComputerInformationController computerInformationController;
    private GuiComputersController guiComputersController;

    @FXML
    private DatePicker adquisitionDateDatePicker;

    @FXML
    private ComboBox<String> cpuComboBox;

    @FXML
    private ComboBox<String> diskComboBox;

    @FXML
    private ComboBox<String> gpuComboBox;

    @FXML
    private ComboBox<Mark> markComboBox;

    @FXML
    private ComboBox<String> motherBoardComboBox;

    @FXML
    private ComboBox<String> powerSourceComboBox;

    @FXML
    private ComboBox<String> ramMemoryComboBox;

    @FXML
    private TextField serialNumberTextField;

    @FXML
    private ComboBox<String> statusComboBox;

    @FXML
    private ComboBox<String> typeComboBox;

    @FXML
    private void initialize() {
        MarkDAO markDAO = new MarkDAO();
        try {
            markComboBox.getItems().addAll(markDAO.getMarksFromDatabase());
        } catch (DataRetrievalException e) {
            new AlertPopUpGenerator().showCustomMessage(AlertType.ERROR, "Error", "Hubo un error, inténtelo más tarde");
        }

        markComboBox.setConverter(new StringConverter<Mark>() {

            @Override
            public Mark fromString(String arg0) {
                return null;
            }

            @Override
            public String toString(Mark arg0) {
                if (arg0 != null) {
                    return arg0.getName();
                }

                return null;
            }

        });

        cpuComboBox.getItems().add("Ryzen 5 1600 AF");
        cpuComboBox.getItems().add("Core i5-10600KF");
        cpuComboBox.getItems().add("Ryzen 5 5600G");
        cpuComboBox.getItems().add("Ryzen 5 5600X");
        diskComboBox.getItems().add("Seagate Barracuda 2 TB");
        diskComboBox.getItems().add("SEAGATE IronWolf 125");
        diskComboBox.getItems().add("WD Blue");
        gpuComboBox.getItems().add("NVIDIA RTX A3000");
        gpuComboBox.getItems().add("GeForce RTX 3070 Ti");
        motherBoardComboBox.getItems().add("Micro-ATX");
        motherBoardComboBox.getItems().add("Mini-ITX");
        motherBoardComboBox.getItems().add("Nano-ITX");
        powerSourceComboBox.getItems().add("ATX");
        powerSourceComboBox.getItems().add("SFX");
        powerSourceComboBox.getItems().add("TFX");
        ramMemoryComboBox.getItems().add("DDR3");
        ramMemoryComboBox.getItems().add("DDR4");
        ramMemoryComboBox.getItems().add("DDR5");
        statusComboBox.getItems().add(ComputerStatus.ACTIVE.getValue());
        statusComboBox.getItems().add(ComputerStatus.ON_MAINTENANCE.getValue());
        statusComboBox.getItems().add(ComputerStatus.OUT_OF_SERVICE.getValue());
        typeComboBox.getItems().add("Escritorio");
        typeComboBox.getItems().add("Laptop");
    }

    @FXML
    private void exitButtonController(ActionEvent event) {
        returnToGuiComputers(event);
    }

    @FXML
    private void modifyButtonController(ActionEvent event) {

        try {
            if (!allFieldsContainsData()) {
                new AlertPopUpGenerator().showCustomMessage(AlertType.WARNING, "Error", "Faltan campos por llenar");
                return;
            }

            if (!isValidSerialNumber()) {
                new AlertPopUpGenerator().showCustomMessage(AlertType.WARNING, "No se puede modificar la computadora",
                        "El número de serie es erróneo");
                return;
            }

            if (isValidAdquisitionDate() > 0) {
                new AlertPopUpGenerator().showCustomMessage(AlertType.WARNING, "No se puede registrar la computadora",
                        "La fecha de adquisición no puede ser después de la fecha actual");
                return;
            }

            ComputerDAO computerDAO = new ComputerDAO();
            Computer newComputerData = new Computer();
            Computer oldComputerData = computerDAO
                    .getComputerFromDatabase(computerInformationController.getSerialNumber());
            newComputerData.setAdquisitionDate(Date.valueOf(adquisitionDateDatePicker.getValue()));
            newComputerData.setCpu(cpuComboBox.getValue());
            newComputerData.setDisk(diskComboBox.getValue());
            newComputerData.setGpu(gpuComboBox.getValue());
            newComputerData.setMark(markComboBox.getValue());
            newComputerData.setMotherBoard(motherBoardComboBox.getValue());
            newComputerData.setPowerSource(powerSourceComboBox.getValue());
            newComputerData.setRamMemory(ramMemoryComboBox.getValue());
            newComputerData.setSerialNumber(serialNumberTextField.getText());
            newComputerData.setStatus(statusComboBox.getValue());
            newComputerData.setType(typeComboBox.getValue());
            if (computerDAO.theComputerIsAlreadyRegisted(newComputerData)) {
                new AlertPopUpGenerator().showCustomMessage(AlertType.WARNING, "Error",
                        "La computadora ya está registrada en el sistema");
                return;
            }
            computerDAO.modifyComputerDataFromDatabase(newComputerData, oldComputerData);
            new AlertPopUpGenerator().showCustomMessage(AlertType.WARNING, "Éxito",
                    "Computadora modificada exitosamente");
            returnToGuiComputers(event);
        } catch (DataRetrievalException e) {
            new AlertPopUpGenerator().showCustomMessage(AlertType.ERROR, "Error", "Hubo un error, inténtelo más tarde");
        } catch (DataWritingException e) {
            new AlertPopUpGenerator().showCustomMessage(AlertType.ERROR, "Error", "Hubo un error, inténtelo más tarde");
        } catch (ConstraintViolationException e) {
            new AlertPopUpGenerator().showCustomMessage(AlertType.ERROR, "Error al modificar computadora",
                    "El número de serie ya está ocupado");
        }
    }

    public ComputerInformationController getComputerInformationController() {
        return computerInformationController;
    }

    public void setComputerInformationController(ComputerInformationController computerInformationController) {
        this.computerInformationController = computerInformationController;
    }

    public GuiComputersController getGuiComputersController() {
        return guiComputersController;
    }

    public void setGuiComputersController(GuiComputersController guiComputersController) {
        this.guiComputersController = guiComputersController;
    }

    public Date getAdquisitionDate() {
        return Date.valueOf(adquisitionDateDatePicker.getValue());
    }

    public void setAdquisitionDate(Date adquisitionDate) {
        this.adquisitionDateDatePicker.setValue(adquisitionDate.toLocalDate());
    }

    public String getCpu() {
        return cpuComboBox.getValue();
    }

    public void setCpu(String cpuComboBox) {
        this.cpuComboBox.setValue(cpuComboBox);
    }

    public String getDisk() {
        return diskComboBox.getValue();
    }

    public void setDisk(String diskComboBox) {
        this.diskComboBox.setValue(diskComboBox);
    }

    public String getGpu() {
        return gpuComboBox.getValue();
    }

    public void setGpu(String gpuComboBox) {
        this.gpuComboBox.setValue(gpuComboBox);
    }

    public Mark getMark() {
        return markComboBox.getValue();
    }

    public void setMark(Mark markComboBox) {
        this.markComboBox.setValue(markComboBox);
    }

    public String getMotherBoard() {
        return motherBoardComboBox.getValue();
    }

    public void setMotherBoard(String MotherBoardComboBox) {
        this.motherBoardComboBox.setValue(MotherBoardComboBox);
    }

    public String getPowerSource() {
        return powerSourceComboBox.getValue();
    }

    public void setPowerSource(String powerSourceComboBox) {
        this.powerSourceComboBox.setValue(powerSourceComboBox);
    }

    public String getRamMemory() {
        return ramMemoryComboBox.getValue();
    }

    public void setRamMemory(String ramMemoryComboBox) {
        this.ramMemoryComboBox.setValue(ramMemoryComboBox);
    }

    public String getSerialNumber() {
        return serialNumberTextField.getText();
    }

    public void setSerialNumber(String serialNumberTextField) {
        this.serialNumberTextField.setText(serialNumberTextField);
    }

    public String getStatus() {
        return statusComboBox.getValue();
    }

    public void setStatus(String status) {
        this.statusComboBox.setValue(status);
    }

    public String getType() {
        return typeComboBox.getValue();
    }

    public void setType(String type) {
        this.typeComboBox.setValue(type);
    }

    private void returnToGuiComputers(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/mx/uv/fei/gui/fxml/computers/GuiComputers.fxml"));
            Parent parent = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(parent);
            stage.setTitle("SMPUV");
            stage.setScene(scene);
            stage.show();
        } catch (IllegalStateException | IOException exception) {
            new AlertPopUpGenerator().showCustomMessage(AlertType.ERROR, "Error", "Hubo un error, inténtelo más tarde");
        }
    }

    private boolean allFieldsContainsData() {
        return adquisitionDateDatePicker.getValue() != null &&
                cpuComboBox.getValue() != null &&
                diskComboBox.getValue() != null &&
                gpuComboBox.getValue() != null &&
                markComboBox.getValue() != null &&
                motherBoardComboBox.getValue() != null &&
                powerSourceComboBox.getValue() != null &&
                ramMemoryComboBox.getValue() != null &&
                !serialNumberTextField.getText().trim().isEmpty() &&
                statusComboBox.getValue() != null &&
                typeComboBox.getValue() != null;
    }

    private boolean isValidSerialNumber() {
        return serialNumberTextField.getText().trim().matches("^[a-zA-Z0-9_-]+$");
    }

    private int isValidAdquisitionDate() {
        return Date.valueOf(adquisitionDateDatePicker.getValue()).compareTo(Date.valueOf(LocalDate.now()));
    }
}
