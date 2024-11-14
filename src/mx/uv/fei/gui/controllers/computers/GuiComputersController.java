package mx.uv.fei.gui.controllers.computers;

import java.io.IOException;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mx.uv.fei.gui.AlertPopUpGenerator;
import mx.uv.fei.logic.daos.ComputerDAO;
import mx.uv.fei.logic.domain.Computer;
import mx.uv.fei.logic.exceptions.DataRetrievalException;

public class GuiComputersController {

    @FXML
    private Pane backgroundPane;

    @FXML
    private ScrollPane computerInformationScrollPane;

    @FXML
    private VBox computersVBox;

    @FXML
    private TextField searchByNameTextField;

    @FXML
    private void initialize() {
        ComputerDAO computerDAO = new ComputerDAO();
        ArrayList<Computer> computers;
        try {
            computers = computerDAO.getComputersFromDatabase();
            computerButtonMaker(computers);
        } catch (DataRetrievalException e) {
            new AlertPopUpGenerator().showCustomMessage(AlertType.ERROR, "Error", "Hubo un error, inténtelo más tarde");
        }
    }

    @FXML
    private void exitToMainMenu(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mx/uv/fei/gui/fxml/MainMenu.fxml"));

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
    private void registerComputerButtonController(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/mx/uv/fei/gui/fxml/computers/GuiRegisterComputer.fxml"));
            Parent guiRegisterComputer = loader.load();
            GuiRegisterComputerController guiRegisterComputerController = loader.getController();
            guiRegisterComputerController.setGuiComputersController(this);
            Scene scene = new Scene(guiRegisterComputer);
            Stage stage = new Stage();
            stage.setTitle("Registrar Compu");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            new AlertPopUpGenerator().showCustomMessage(AlertType.ERROR, "Error", "Hubo un error, inténtelo más tarde");
        }
    }

    @FXML
    private void searchByNameButtonController(ActionEvent event) {
        computersVBox.getChildren().clear();
        ;
        ComputerDAO computerDAO = new ComputerDAO();
        try {
            ArrayList<Computer> computers = computerDAO
                    .getSpecifiedComputersFromDatabase(searchByNameTextField.getText());
            computerButtonMaker(computers);
        } catch (DataRetrievalException e) {
            new AlertPopUpGenerator().showCustomMessage(AlertType.ERROR, "Error", "Hubo un error, inténtelo más tarde");
        }
    }

    public void openModifyComputerPane(ComputerInformationController computerInformationController) {
        ComputerDAO computerDAO = new ComputerDAO();
        try {
            Computer computer = computerDAO.getComputerFromDatabase(computerInformationController.getSerialNumber());
            FXMLLoader modifyComputerInformationControllerLoader = new FXMLLoader(
                    getClass().getResource("/mx/uv/fei/gui/fxml/computers/ModifyComputerInformation.fxml"));

            VBox modifyComputerInformationVBox = modifyComputerInformationControllerLoader.load();
            ModifyComputerInformationController modifyComputerInformationController = modifyComputerInformationControllerLoader
                    .getController();
            modifyComputerInformationController.setCpu(computer.getCpu());
            modifyComputerInformationController.setRamMemory(computer.getRamMemory());
            modifyComputerInformationController.setDisk(computer.getDisk());
            modifyComputerInformationController.setGpu(computer.getGpu());
            modifyComputerInformationController.setMark(computer.getMark());
            modifyComputerInformationController.setMotherBoard(computer.getMotherBoard());
            modifyComputerInformationController.setPowerSource(computer.getPowerSource());
            modifyComputerInformationController.setType(computer.getType());
            modifyComputerInformationController.setSerialNumber(computer.getSerialNumber());
            modifyComputerInformationController.setAdquisitionDate(computer.getAdquisitionDate());
            modifyComputerInformationController.setStatus(computer.getStatus());
            modifyComputerInformationController.setGuiComputersController(this);
            modifyComputerInformationController.setComputerInformationController(computerInformationController);
            computerInformationScrollPane.setContent(modifyComputerInformationVBox);

        } catch (IOException e) {
            new AlertPopUpGenerator().showCustomMessage(AlertType.ERROR, "Error", "Hubo un error, inténtelo más tarde");
        } catch (DataRetrievalException e) {
            new AlertPopUpGenerator().showCustomMessage(AlertType.ERROR, "Error", "Hubo un error, inténtelo más tarde");
        }
    }

    public void openPaneWithComputerInformation(String serialNumber) {
        ComputerDAO computerDAO = new ComputerDAO();
        try {
            Computer computer = computerDAO.getComputerFromDatabase(serialNumber);
            FXMLLoader computerInformationControllerLoader = new FXMLLoader(
                    getClass().getResource("/mx/uv/fei/gui/fxml/computers/ComputerInformation.fxml"));
            VBox computerInformationVBox = computerInformationControllerLoader.load();
            ComputerInformationController computerInformationController = computerInformationControllerLoader
                    .getController();
            computerInformationController.setCpu(computer.getCpu());
            computerInformationController.setRamMemory(computer.getRamMemory());
            computerInformationController.setDisk(computer.getDisk());
            computerInformationController.setGpu(computer.getGpu());
            computerInformationController.setMark(computer.getMark());
            computerInformationController.setMotherBoard(computer.getMotherBoard());
            computerInformationController.setPowerSource(computer.getPowerSource());
            computerInformationController.setType(computer.getType());
            computerInformationController.setSerialNumber(computer.getSerialNumber());
            computerInformationController.setAdquisitionDate(computer.getAdquisitionDate());
            computerInformationController.setStatus(computer.getStatus());
            computerInformationController.setGuiComputersController(this);
            computerInformationScrollPane.setContent(computerInformationVBox);

        } catch (IOException e) {
            new AlertPopUpGenerator().showCustomMessage(AlertType.ERROR, "Error", "Hubo un error, inténtelo más tarde");
        } catch (DataRetrievalException e) {
            new AlertPopUpGenerator().showCustomMessage(AlertType.ERROR, "Error", "Hubo un error, inténtelo más tarde");
        }
    }

    public void computerButtonMaker(ArrayList<Computer> computers) {
        computersVBox.getChildren().clear();
        try {
            for (Computer computer : computers) {
                FXMLLoader computerItemControllerLoader = new FXMLLoader(
                        getClass().getResource("/mx/uv/fei/gui/fxml/computers/ComputerButton.fxml"));
                Pane computerItemPane = computerItemControllerLoader.load();
                ComputerButtonController computerController = computerItemControllerLoader.getController();
                computerController.setSerialNumber(computer.getSerialNumber());
                computerController.setStatus(computer.getStatus());
                computerController.setGuiComputersController(this);
                computersVBox.getChildren().add(computerItemPane);
            }
        } catch (IOException e) {
            new AlertPopUpGenerator().showCustomMessage(AlertType.ERROR, "Error", "Hubo un error, inténtelo más tarde");
        }
    }

}
