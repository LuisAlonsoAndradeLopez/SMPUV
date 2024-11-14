package mx.uv.fei.gui.controllers.computers;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
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
import mx.uv.fei.logic.domain.enums.ComputerType;
import mx.uv.fei.logic.exceptions.ConstraintViolationException;
import mx.uv.fei.logic.exceptions.DataRetrievalException;
import mx.uv.fei.logic.exceptions.DataWritingException;

public class GuiRegisterComputerController {
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
    private Button registerButton;

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
        typeComboBox.getItems().add(ComputerType.DESKTOP.getValue());
        typeComboBox.getItems().add(ComputerType.LAPTOP.getValue());
    }

    @FXML
    private void registerButtonController(ActionEvent event) {
        if (!allFieldsContainsData()) {
            new AlertPopUpGenerator().showCustomMessage(AlertType.WARNING, "Error", "Faltan campos por llenar");
            return;
        }

        if (!isValidSerialNumber()) {
            new AlertPopUpGenerator().showCustomMessage(AlertType.WARNING, "No se puede registrar la computadora",
                    "El número de serie es erróneo");
            return;
        }

        if (isValidAdquisitionDate() > 0) {
            new AlertPopUpGenerator().showCustomMessage(AlertType.WARNING, "No se puede registrar la computadora",
                    "La fecha de adquisición no puede ser después de la fecha actual");
            return;
        }

        ComputerDAO computerDAO = new ComputerDAO();
        Computer computer = new Computer();
        computer.setAdquisitionDate(Date.valueOf(adquisitionDateDatePicker.getValue()));
        computer.setCpu(cpuComboBox.getValue());
        computer.setDisk(diskComboBox.getValue());
        computer.setGpu(gpuComboBox.getValue());
        computer.setMark(markComboBox.getValue());
        computer.setMotherBoard(motherBoardComboBox.getValue());
        computer.setPowerSource(powerSourceComboBox.getValue());
        computer.setRamMemory(ramMemoryComboBox.getValue());
        computer.setSerialNumber(serialNumberTextField.getText());
        computer.setStatus(statusComboBox.getValue());
        computer.setType(typeComboBox.getValue());

        try {
            if (computerDAO.theComputerIsAlreadyRegisted(computer)) {
                new AlertPopUpGenerator().showCustomMessage(AlertType.WARNING, "Error",
                        "La computadora ya está registrada en el sistema");
                return;
            }
        } catch (DataRetrievalException e) {
            new AlertPopUpGenerator().showCustomMessage(AlertType.ERROR, "Error", "Hubo un error, inténtelo más tarde");
        }

        try {
            computerDAO.addComputerToDatabase(computer);
            new AlertPopUpGenerator().showCustomMessage(AlertType.WARNING, "Éxito",
                    "Computadora registrada exitosamente");
            ArrayList<Computer> computers = computerDAO.getComputersFromDatabase();
            guiComputersController.computerButtonMaker(computers);
            Stage stage = (Stage) registerButton.getScene().getWindow();
            stage.close();
        } catch (DataWritingException e) {
            new AlertPopUpGenerator().showCustomMessage(AlertType.ERROR, "Error", "Hubo un error, inténtelo más tarde");
        } catch (DataRetrievalException e) {
            new AlertPopUpGenerator().showCustomMessage(AlertType.ERROR, "Error", "Hubo un error, inténtelo más tarde");
        } catch (ConstraintViolationException e) {
            new AlertPopUpGenerator().showCustomMessage(AlertType.ERROR, "Error al registrar computadora",
                    "El número de serie ya está usado");
        }
    }

    public GuiComputersController getGuiComputersController() {
        return guiComputersController;
    }

    public void setGuiComputersController(GuiComputersController guiComputersController) {
        this.guiComputersController = guiComputersController;
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
