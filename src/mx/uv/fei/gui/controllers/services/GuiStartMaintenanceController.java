package mx.uv.fei.gui.controllers.services;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mx.uv.fei.gui.AlertPopUpGenerator;
import mx.uv.fei.logic.daos.ServiceDAO;
import mx.uv.fei.logic.domain.Computer;
import mx.uv.fei.logic.domain.Service;
import mx.uv.fei.logic.domain.enums.ServiceStatus;
import mx.uv.fei.logic.exceptions.DataInsertionException;

public class GuiStartMaintenanceController {
    private static final Logger LOGGER = Logger.getLogger(GuiStartMaintenanceController.class.getName());
    private ArrayList<String> serviceTypes;
    private Computer computer;

    @FXML
    private Label cpuLabel;
    @FXML
    private Label ramLabel;
    @FXML
    private Label vramLabel;
    @FXML
    private Label brandLabel;
    @FXML
    private Label diskLabel;
    @FXML
    private Label powerSourceLabel;
    @FXML
    private Label typeLabel;
    @FXML
    private Label serialNumberLabel;
    @FXML
    private Label acquisitionDateLabel;
    @FXML
    private Label motherBoardLabel;
    @FXML
    private ComboBox<String> serviceTypeComboBox;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private TextArea diagnosisTextArea;
    @FXML
    private TextField costTextField;

    @FXML
    private void initialize() {
        serviceTypes = new ArrayList<>();
        serviceTypes.add("Mantenimiento preventivo");
        serviceTypes.add("Reparación");

        for (String serviceType : serviceTypes) {
            serviceTypeComboBox.getItems().add(serviceType);
        }
    }

    @FXML
    private void returnToComputerManager(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mx/uv/fei/gui/fxml/computers/GuiComputers.fxml"));

        try {
            Parent parent = loader.load();
            Scene scene = new Scene(parent);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);

            stage.show();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Something went wrong", e);
            new AlertPopUpGenerator().showCustomMessage(AlertType.ERROR, "Error", "Hubo un error, inténtelo más tarde");
        }
    }

    @FXML
    private void startMaintenance(ActionEvent event) {
        if (serviceTypeComboBox.getValue() != null) {
            if (startDatePicker.getValue() != null && endDatePicker.getValue() != null) {
                if (isValidPrice()) {
                    if (diagnosisTextArea.getText().length() <= 500) {
                        ServiceDAO serviceDAO = new ServiceDAO();
                        Service service = new Service();
                        service.setType(serviceTypeComboBox.getValue());
                        service.setStartDate(Date.valueOf(startDatePicker.getValue()));
                        service.setEndDate(Date.valueOf(endDatePicker.getValue()));
                        service.setDiagnosis(diagnosisTextArea.getText());
                        service.setStatus(ServiceStatus.ACTIVE.getValue());

                        BigDecimal price = new BigDecimal(costTextField.getText().trim());
                        service.setPrice(price);
                        service.setComputer(computer);

                        if (serviceDAO.isValidDate(service)) {
                            try {
                                if (serviceDAO.addService(service) > 0) {
                                    new AlertPopUpGenerator().showCustomMessage(Alert.AlertType.INFORMATION,
                                            "Mensaje de éxito", "Mantenimiento programado exitosamente");

                                    returnToComputerManager(event);
                                }
                            } catch (DataInsertionException e) {
                                LOGGER.log(Level.SEVERE, "Something went wrong", e);
                                new AlertPopUpGenerator().showCustomMessage(AlertType.ERROR, "Error",
                                        "Hubo un error, inténtelo más tarde");
                            }
                        } else {
                            new AlertPopUpGenerator().showCustomMessage(Alert.AlertType.WARNING,
                                    "No se puede iniciar mantenimiento", "Favor de seleccionar una fecha válida");
                        }
                    } else {
                        new AlertPopUpGenerator().showCustomMessage(Alert.AlertType.WARNING,
                                "No se puede iniciar mantenimiento",
                                "Las observaciones deben tener 500 caracteres o menos");
                    }
                } else {
                    new AlertPopUpGenerator().showCustomMessage(Alert.AlertType.WARNING,
                            "No se puede iniciar mantenimiento",
                            "El precio debe tener de 1 a 16 caracteres antes del punto, y maximo 2 números después del punto");
                }
            } else {
                new AlertPopUpGenerator().showCustomMessage(Alert.AlertType.WARNING,
                        "No se puede iniciar mantenimiento", "Favor de seleccionar una fecha");
            }
        } else {
            new AlertPopUpGenerator().showCustomMessage(Alert.AlertType.WARNING, "No se puede iniciar mantenimiento",
                    "Favor de seleccionar un tipo de servicio");
        }
    }

    public boolean isValidPrice() {
        return costTextField.getText().trim().matches("\\d{1,16}(\\.\\d{0,2})?");
    }

    public void setComputer(Computer computer) {
        this.computer = computer;

        cpuLabel.setText(computer.getCpu());
        ramLabel.setText(computer.getRamMemory());
        vramLabel.setText(computer.getGpu());
        brandLabel.setText(computer.getMark().getName());
        powerSourceLabel.setText(computer.getPowerSource());
        diskLabel.setText(computer.getDisk());
        typeLabel.setText(computer.getType());
        serialNumberLabel.setText(computer.getSerialNumber());
        motherBoardLabel.setText(computer.getMotherBoard());
        acquisitionDateLabel.setText(computer.getAdquisitionDate().toString());
    }
}