package mx.uv.fei.gui.controllers.refactions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mx.uv.fei.gui.AlertPopUpGenerator;
import mx.uv.fei.logic.daos.RefactionDAO;
import mx.uv.fei.logic.domain.Refaction;
import mx.uv.fei.logic.exceptions.DataRetrievalException;
import mx.uv.fei.logic.exceptions.DataWritingException;

public class GuiRefactionsController {
    private static final Logger LOGGER = Logger.getLogger(GuiRefactionsController.class.getName());

    @FXML
    private TableColumn<Refaction, String> codeColumn;

    @FXML
    private Button deleteButton;

    @FXML
    private Button editButton;

    @FXML
    private TableColumn<Refaction, String> markColumn;

    @FXML
    private TableColumn<Refaction, String> nameColumn;

    @FXML
    private TableColumn<Refaction, Double> priceColumn;

    @FXML
    private TableColumn<Refaction, Integer> quantityColumn;

    @FXML
    private TableView<Refaction> refactionsTable;

    @FXML
    private Button registerButton;

    @FXML
    private TextField searchText;

    @FXML
    private TableColumn<Refaction, String> typeColumn;

    @FXML
    public void initialize() {
        try {
            RefactionDAO refactionDAO = new RefactionDAO();
            ArrayList<Refaction> refactions = refactionDAO.getRefactionsFromDatabase();
            ObservableList<Refaction> refactionsList = FXCollections.observableArrayList(refactions);
            refactionsTable.setItems(refactionsList);
        } catch (DataRetrievalException e) {
            LOGGER.log(Level.SEVERE, "Something went wrong", e);

            new AlertPopUpGenerator().showCustomMessage(AlertType.ERROR, "Error", "Hubo un error, inténtelo más tarde");
        }

        deleteButton.disableProperty().bind(refactionsTable.getSelectionModel().selectedItemProperty().isNull());
        editButton.disableProperty().bind(refactionsTable.getSelectionModel().selectedItemProperty().isNull());
        codeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCode()));
        typeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getType()));
        nameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        priceColumn.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getPrice()).asObject());
        quantityColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getQuantity()).asObject());
        markColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBrand().getName()));

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
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Something went wrong", e);
            new AlertPopUpGenerator().showCustomMessage(AlertType.ERROR, "Error", "Hubo un error, inténtelo más tarde");
        }
    }

    @FXML
    public void searchOnRealTime(KeyEvent event) {
        String searchTermine = searchText.getText();

        try {
            RefactionDAO refactionDAO = new RefactionDAO();
            ArrayList<Refaction> refacciones = refactionDAO.getSpecifiedRefactionFromDatabase(searchTermine);
            ObservableList<Refaction> refactionsList = FXCollections.observableArrayList(refacciones);
            refactionsTable.setItems(refactionsList);
        } catch (DataRetrievalException e) {
            LOGGER.log(Level.SEVERE, "Something went wrong", e);

            new AlertPopUpGenerator().showCustomMessage(AlertType.ERROR, "Error", "Hubo un error, inténtelo más tarde");
        }

        codeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCode()));
        typeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getType()));
        nameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        priceColumn.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getPrice()).asObject());
        quantityColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getQuantity()).asObject());
        markColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBrand().getName()));
    }

    @FXML
    private void clearSelection(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            if (refactionsTable.getSelectionModel().getSelectedItem() != null) {
                refactionsTable.getSelectionModel().clearSelection();
            }
        }
    }

    @FXML
    public void openGuiAddRefaction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/mx/uv/fei/gui/fxml/refactions/GuiAddRefactions.fxml"));
            Parent guiAddRefactions = loader.load();

            Scene scene = new Scene(guiAddRefactions);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle(("Registrar refacción"));

            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
            stage.setResizable(false);
            stage.showAndWait();

            try {
                RefactionDAO refactionDAO = new RefactionDAO();
                ArrayList<Refaction> refactions = refactionDAO.getRefactionsFromDatabase();
                ObservableList<Refaction> refactionsList = FXCollections.observableArrayList(refactions);
                refactionsTable.setItems(refactionsList);
            } catch (DataRetrievalException e) {
                LOGGER.log(Level.SEVERE, "Something went wrong", e);
                new AlertPopUpGenerator().showCustomMessage(AlertType.ERROR, "Error",
                        "Hubo un error, inténtelo más tarde");
            }

            codeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCode()));
            typeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getType()));
            nameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
            priceColumn.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getPrice()).asObject());
            quantityColumn
                    .setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getQuantity()).asObject());
            markColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBrand().getName()));

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Something went wrong", e);

            new AlertPopUpGenerator().showCustomMessage(AlertType.ERROR, "Error", "Hubo un error, inténtelo más tarde");
        }
    }

    @FXML
    public void openGuiEdit(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/mx/uv/fei/gui/fxml/refactions/GuiAddRefactions.fxml"));
            Parent guiAddRefactions = loader.load();
            GuiAddRefactionsController guiAddRefactionsController = loader.getController();

            Refaction refaccion = refactionsTable.getSelectionModel().getSelectedItem();

            guiAddRefactionsController.originalRefaction = refaccion;
            guiAddRefactionsController.titleLabel.setText("Modificar refacción");
            guiAddRefactionsController.saveRegisterButton.setText("Modificar");
            guiAddRefactionsController.setEditing(true);
            guiAddRefactionsController.editRefaction();

            Scene scene = new Scene(guiAddRefactions);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle(("Modificar refacción"));

            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
            stage.setResizable(false);
            stage.showAndWait();

            try {
                RefactionDAO refactionDAO = new RefactionDAO();
                ArrayList<Refaction> refactions = refactionDAO.getRefactionsFromDatabase();
                ObservableList<Refaction> refactionsList = FXCollections.observableArrayList(refactions);
                refactionsTable.setItems(refactionsList);
            } catch (DataRetrievalException e) {
                LOGGER.log(Level.SEVERE, "Something went wrong", e);
                new AlertPopUpGenerator().showCustomMessage(AlertType.ERROR, "Error",
                        "Hubo un error, inténtelo más tarde");
            }

            codeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCode()));
            typeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getType()));
            nameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
            priceColumn.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getPrice()).asObject());
            quantityColumn
                    .setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getQuantity()).asObject());
            markColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBrand().getName()));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Something went wrong", e);
            new AlertPopUpGenerator().showCustomMessage(AlertType.ERROR, "Error", "Hubo un error, inténtelo más tarde");
        }
    }

    @FXML
    public void deleteSelection() throws DataWritingException {
        Refaction refactionSel = refactionsTable.getSelectionModel().getSelectedItem();
        String id = refactionSel.getCode();

        try {
            RefactionDAO refactionDAO = new RefactionDAO();
            refactionDAO.deleteRefactionsToDatabase(id);
            ArrayList<Refaction> refactions = refactionDAO.getRefactionsFromDatabase();
            ObservableList<Refaction> refactionsList = FXCollections.observableArrayList(refactions);
            refactionsTable.setItems(refactionsList);
        } catch (DataRetrievalException e) {
            LOGGER.log(Level.SEVERE, "Something went wrong", e);

            new AlertPopUpGenerator().showCustomMessage(AlertType.ERROR, "Error", "Hubo un error, inténtelo más tarde");
        }

        deleteButton.disableProperty().bind(refactionsTable.getSelectionModel().selectedItemProperty().isNull());
        editButton.disableProperty().bind(refactionsTable.getSelectionModel().selectedItemProperty().isNull());

        codeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCode()));
        typeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getType()));
        nameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        priceColumn.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getPrice()).asObject());
        quantityColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getQuantity()).asObject());
        markColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBrand().getName()));

    }
}
