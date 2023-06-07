package mx.uv.fei.gui.controllers.refactions;

import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import mx.uv.fei.gui.AlertPopUpGenerator;
import mx.uv.fei.logic.daos.RefactionDAO;
import mx.uv.fei.logic.domain.Mark;
import mx.uv.fei.logic.domain.Refaction;
import mx.uv.fei.logic.domain.enums.RefactionType;
import mx.uv.fei.logic.exceptions.ConstraintViolationException;
import mx.uv.fei.logic.exceptions.DataRetrievalException;
import mx.uv.fei.logic.exceptions.DataWritingException;
import mx.uv.fei.logic.daos.MarkDAO;

public class GuiAddRefactionsController{
    
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
    public void initialize() throws DataRetrievalException{
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
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,Integer.MAX_VALUE,0);
        quantitySpinner.setValueFactory(valueFactory);
        
    }

    @FXML
    public void saveRegister(ActionEvent event) throws DataRetrievalException{
        if(codeTextField.getText().trim().isEmpty() ||
           nameTextArea.getText().trim().isEmpty() ||
           markChoiceBox.getValue() == null ||
           typeChoiceBox.getValue() == null ||
           priceTextField.getText().trim().isEmpty() ||
           quantitySpinner.getValue() == null){
               new AlertPopUpGenerator().showCustomMessage(AlertType.WARNING, "Error", "Faltan campos por llenar");
               return;
        }
                    
        if(!isValidCode()){
            new AlertPopUpGenerator().showCustomMessage(AlertType.WARNING, "No se puede registrar la refaccion", "El código es erróneo");
            return; 
        }

        if(!isValidName()){
            new AlertPopUpGenerator().showCustomMessage(AlertType.WARNING, "No se puede registrar la refaccion", "La descripción debe de tener 125 caracteres o menos");
            return; 
        }

        if(!isValidPrice()){
            new AlertPopUpGenerator().showCustomMessage(AlertType.WARNING, "No se puede registrar la refaccion", "El precio introducido es erróneo");
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
        
        for(int i = 0; i < marks.size(); i++){
            Mark x = marks.get(i);
            if(x.getName().equals(marca.getName())){
                marca.setIdMark(x.getIdMark());
            }
        }
        refaction.setBrand(marca);
        
        refaction.setPrice(Double.parseDouble(priceTextField.getText()));
        refaction.setQuantity((int) quantitySpinner.getValue());
        
        if(editing){
            try{
                refactionDAO.modifyRefactionToDatabase(refaction, originalRefaction);
            }catch(DataWritingException e){
                new AlertPopUpGenerator().showCustomMessage(AlertType.ERROR, "Error", "Hubo un error, inténtelo más tarde");
                return;
            }catch(ConstraintViolationException e) {
                new AlertPopUpGenerator().showCustomMessage(AlertType.ERROR, "Error al modificar refacción", "El código ya está usado");
                return;
            }
        }else{
            try{
                refactionDAO.addRefactionToDatabase(refaction);
            }catch(DataWritingException e){
                new AlertPopUpGenerator().showCustomMessage(AlertType.ERROR, "Error", "Hubo un error, inténtelo más tarde");
                return;
            }catch(ConstraintViolationException e) {
                new AlertPopUpGenerator().showCustomMessage(AlertType.ERROR, "Error al registrar refacción", "El código ya está usado");
                return;
            }
        }
        
        
        codeTextField.setText("");
        nameTextArea.setText("");
        markChoiceBox.setValue(null);
        typeChoiceBox.setValue(null);
        priceTextField.setText("");

        new AlertPopUpGenerator().showCustomMessage(AlertType.WARNING, "Éxito", "Registro exitoso");
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    public void cancelRegister(ActionEvent event){
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    public void editRefaction(){
        codeTextField.setText(originalRefaction.getCode());
        codeTextField.setDisable(true);
        
        nameTextArea.setText(originalRefaction.getName());
        markChoiceBox.setValue(originalRefaction.getBrand().getName());
        typeChoiceBox.setValue(originalRefaction.getType());
        priceTextField.setText(String.valueOf(originalRefaction.getPrice()));
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,Integer.MAX_VALUE,originalRefaction.getQuantity());
        quantitySpinner.setValueFactory(valueFactory);
        
    }
    
    public void setEditing(boolean x){
        editing = x;
    }
    public boolean getEditing(){
        return editing;
    }    

    public boolean isValidCode(){
        return codeTextField.getText().trim().matches("^[a-zA-Z0-9_-]+$");
    }

    public boolean isValidName(){
        return nameTextArea.getText().length() < 125;
    }

    public boolean isValidPrice(){
        return priceTextField.getText().trim().matches("^\\d+(\\.\\d+)?$");
    }
}
