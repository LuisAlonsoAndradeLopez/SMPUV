package mx.uv.fei.gui.controllers.refactions;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import mx.uv.fei.gui.AlertPopUpGenerator;
import mx.uv.fei.logic.daos.MarkDAO;
import mx.uv.fei.logic.daos.RefactionDAO;
import mx.uv.fei.logic.domain.Mark;
import mx.uv.fei.logic.domain.Refaction;
import mx.uv.fei.logic.domain.enums.RefactionType;
import mx.uv.fei.logic.exceptions.ConstraintViolationException;
import mx.uv.fei.logic.exceptions.DataRetrievalException;
import mx.uv.fei.logic.exceptions.DataWritingException;

public class GuiAddRefactionsController {
    private static final Logger LOGGER = Logger.getLogger(GuiAddRefactionsController.class.getName());

    private boolean editing;

    public Refaction originalRefaction;

    @FXML
    private TextField codeTextField;

    @FXML
    private TextArea nameTextArea;

    @FXML
    private ChoiceBox<String> markChoiceBox;

    @FXML
    private ChoiceBox<String> typeChoiceBox;

    @FXML
    private TextField priceTextField;

    @FXML
    private Spinner<Integer> quantitySpinner;

    @FXML
    Button saveRegisterButton;

    @FXML
    Label titleLabel;

    @FXML
    public void initialize() throws DataRetrievalException {
        nameTextArea.setWrapText(true);

        RefactionDAO refactionDAO = new RefactionDAO();
        ArrayList<String> marksList = refactionDAO.getBrandsFromDatabase();

        typeChoiceBox.getItems().add(RefactionType.CPU.getValue());
        typeChoiceBox.getItems().add(RefactionType.RAM.getValue());
        typeChoiceBox.getItems().add(RefactionType.HDD.getValue());
        typeChoiceBox.getItems().add(RefactionType.GPU.getValue());
        typeChoiceBox.getItems().add(RefactionType.MOTHERBOARD.getValue());
        typeChoiceBox.getItems().add(RefactionType.POWERSOURCE.getValue());
        markChoiceBox.getItems().addAll(marksList);

        quantitySpinner.setEditable(true);
        TextFormatter<Integer> textFormatter = new TextFormatter<>(new IntegerStringConverter());
        quantitySpinner.getEditor().setTextFormatter(textFormatter);
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,
                Integer.MAX_VALUE, 0);
        quantitySpinner.setValueFactory(valueFactory);
    }

    @FXML
    public void saveRegister(ActionEvent event) throws DataRetrievalException {
        if (codeTextField.getText().trim().isEmpty() ||
                nameTextArea.getText().trim().isEmpty() ||
                markChoiceBox.getValue() == null ||
                typeChoiceBox.getValue() == null ||
                priceTextField.getText().trim().isEmpty() ||
                quantitySpinner.getValue() == null) {
            new AlertPopUpGenerator().showCustomMessage(AlertType.WARNING, "Error", "Faltan campos por llenar");
            return;
        }

        if (codeTextField.getText().length() > 50) {
            new AlertPopUpGenerator().showCustomMessage(AlertType.WARNING, "No se puede registrar la computadora",
                    "El código debe tener de 1 a 50 caracteres");
            return;
        }
        
        if (!isValidCode()) {
            new AlertPopUpGenerator().showCustomMessage(AlertType.WARNING, "No se puede registrar la refaccion",
            "El código es erróneo");
            return;
        }
        
        if (nameTextArea.getText().length() > 125) {
            new AlertPopUpGenerator().showCustomMessage(AlertType.WARNING, "No se puede registrar la computadora",
            "El nombre debe tener de 1 a 125 caracteres");
            return;
        }

        if (!isValidPrice()) {
            new AlertPopUpGenerator().showCustomMessage(AlertType.WARNING, "No se puede registrar la computadora",
                    "El precio debe tener de 1 a 16 caracteres antes del punto, y maximo 2 números después del punto");
            return;
        }

        if (quantitySpinner.getEditor().getText().length() > 10) {
            new AlertPopUpGenerator().showCustomMessage(AlertType.WARNING, "No se puede registrar la computadora",
                    "La cantidad debe tener de 1 a 10 caracteres");
            return;
        }

        RefactionDAO refactionDAO = new RefactionDAO();
        Refaction refaction = new Refaction();
        refaction.setCode(codeTextField.getText());
        refaction.setName(nameTextArea.getText());
        refaction.setType((String) typeChoiceBox.getValue());

        Mark marca = new Mark();
        marca.setName((String) markChoiceBox.getValue());
        marca.setIdMark(0);

        MarkDAO markDAO = new MarkDAO();
        ArrayList<Mark> marks = new ArrayList<>(markDAO.getMarksFromDatabase());

        for (int i = 0; i < marks.size(); i++) {
            Mark x = marks.get(i);
            if (x.getName().equals(marca.getName())) {
                marca.setIdMark(x.getIdMark());
            }
        }
        refaction.setBrand(marca);

        refaction.setPrice(Double.parseDouble(priceTextField.getText()));
        refaction.setQuantity((int) quantitySpinner.getValue());

        if (editing) {
            try {
                refactionDAO.modifyRefactionToDatabase(refaction, originalRefaction);
            } catch (DataWritingException e) {
                LOGGER.log(Level.SEVERE, "Something went wrong", e);
                new AlertPopUpGenerator().showCustomMessage(AlertType.ERROR, "Error",
                        "Hubo un error, inténtelo más tarde");
                return;
            } catch (ConstraintViolationException e) {
                LOGGER.log(Level.SEVERE, "Something went wrong", e);
                new AlertPopUpGenerator().showCustomMessage(AlertType.ERROR, "Error al modificar refacción",
                        "El código ya está usado");
                return;
            }
        } else {
            try {
                refactionDAO.addRefactionToDatabase(refaction);
            } catch (DataWritingException e) {
                LOGGER.log(Level.SEVERE, "Something went wrong", e);
                new AlertPopUpGenerator().showCustomMessage(AlertType.ERROR, "Error",
                        "Hubo un error, inténtelo más tarde");
                return;
            } catch (ConstraintViolationException e) {
                LOGGER.log(Level.SEVERE, "Something went wrong", e);
                new AlertPopUpGenerator().showCustomMessage(AlertType.ERROR, "Error al registrar refacción",
                        "El código ya está usado");
                return;
            }
        }

        codeTextField.setText("");
        nameTextArea.setText("");
        markChoiceBox.setValue(null);
        typeChoiceBox.setValue(null);
        priceTextField.setText("");

        new AlertPopUpGenerator().showCustomMessage(AlertType.INFORMATION, "Éxito", "Registro exitoso");
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void cancelRegister(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    public void editRefaction() {
        codeTextField.setText(originalRefaction.getCode());
        codeTextField.setDisable(true);

        nameTextArea.setText(originalRefaction.getName());
        markChoiceBox.setValue(originalRefaction.getBrand().getName());
        typeChoiceBox.setValue(originalRefaction.getType());
        priceTextField.setText(String.valueOf(originalRefaction.getPrice()));
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,
                Integer.MAX_VALUE, originalRefaction.getQuantity());
        quantitySpinner.setValueFactory(valueFactory);

    }

    public void setEditing(boolean x) {
        editing = x;
    }

    public boolean getEditing() {
        return editing;
    }

    public boolean isValidCode() {
        return codeTextField.getText().trim().matches("^[a-zA-Z0-9_-]+$");
    }

    public boolean isValidPrice() {
        return priceTextField.getText().trim().matches("^\\d{1,16}(\\.\\d{0,2})?$");
    }
}
