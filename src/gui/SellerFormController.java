package gui;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.service.SellerService;

import java.net.URL;
import java.time.LocalDate;
import java.util.*;

public class SellerFormController implements Initializable {
    private Seller entity;
    private SellerService service;
    private List<DataChangeListener> dataChangeListeners=new ArrayList<>();
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtEmail;
    @FXML
    private DatePicker dpBirthDate;
    @FXML
    private TextField txtBaseSalary;
    @FXML
    private Label labelErrorName;
    @FXML
    private Label labelErrorEmail;
    @FXML
    private Label labelErrorBirthDate;
    @FXML
    private Label labelErrorBaseSalary;
    @FXML
    private Button btSave;
    @FXML
    private Button btCancel;

    public void setSeller(Seller entity){
        this.entity=entity;
    }
    public void setSellerService(SellerService service){
        this.service=service;
    }
    public void subscribeDataChangeListener(DataChangeListener listener){
        dataChangeListeners.add(listener);
    }

    @FXML
    public void onBtSaveAction(ActionEvent event){
        if(entity==null || service==null) throw new IllegalStateException("Dependency not found");
        try{
            entity = getFormData();
            service.saveOrUpdate(entity);
            notifyDataChangeListeners();
            Utils.currentStage(event).close();
        }catch (DbException e){
            Alerts.showAlerts("Error saving object",null,e.getMessage(), Alert.AlertType.ERROR);
        }catch (ValidationException e){
            setErrorMessage(e.getErrors());
        }
    }

    private void notifyDataChangeListeners() {
        for(DataChangeListener listener:dataChangeListeners){
            listener.onDataChanged();
        }
    }

    private Seller getFormData() {

        ValidationException exception=new ValidationException("Validation error");
        Integer id= Utils.tryParseInt(txtId.getText());
        if(txtName.getText()==null || txtName.getText().trim().equals(""))
            exception.addError("name","Field can't be empty");
        String name=txtName.getText();
        String email=txtEmail.getText();
        LocalDate birthDate=dpBirthDate.getValue();
        Double baseSalary=Double.parseDouble(txtBaseSalary.getText());

        if (exception.getErrors().size()>0) throw exception;
        return new Seller(id,name,email,birthDate,baseSalary,null);
    }

    @FXML
    public void onBtCancelAction(ActionEvent event){
        Utils.currentStage(event).close();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();
    }
    private void initializeNodes(){
        Constraints.setTextFieldInteger(txtId);
        Constraints.setTextFieldMaxLength(txtName,60);
        Constraints.setTextFieldDouble(txtBaseSalary);
        Constraints.setTextFieldMaxLength(txtEmail,60);
        Utils.formatDatePicker(dpBirthDate,"dd/MM/yyyy");
    }

    public void updateFormData(){
        if(entity==null) throw new IllegalStateException("Entity was null");
        txtId.setText(String.valueOf(entity.getId()));
        txtName.setText(entity.getName());
        txtEmail.setText(entity.getEmail());
        Locale.setDefault(Locale.US);
        txtBaseSalary.setText(String.format("%.2f",entity.getBaseSalary()));
        dpBirthDate.setValue(entity.getBirthDate());
    }
    private void setErrorMessage(Map<String,String> errors){
        Set<String> fields=errors.keySet();
        if(fields.contains("name")){
            labelErrorName.setText(errors.get("name"));
        }
    }
}
