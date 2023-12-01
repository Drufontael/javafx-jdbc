package gui;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;
import model.exceptions.ValidationException;
import model.service.DepartmentService;

import java.net.URL;
import java.util.*;

public class DepartmentFormController implements Initializable {
    private Department entity;
    private DepartmentService service;
    private List<DataChangeListener> dataChangeListeners=new ArrayList<>();
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtName;
    @FXML
    private Label labelErrorName;
    @FXML
    private Button btSave;
    @FXML
    private Button btCancel;

    public void setDepartment(Department entity){
        this.entity=entity;
    }
    public void setDepartmentService(DepartmentService service){
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

    private Department getFormData() {
        ValidationException exception=new ValidationException("Validation error");
        Integer id= Utils.tryParseInt(txtId.getText());
        if(txtName.getText()==null || txtName.getText().trim().equals(""))
            exception.addError("name","Field can't be empty");
        String name=txtName.getText();
        if (exception.getErrors().size()>0) throw exception;
        return new Department(id,name);
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
        Constraints.setTextFieldMaxLength(txtName,30);
    }

    public void updateFormData(){
        if(entity==null) throw new IllegalStateException("Entity was null");
        txtId.setText(String.valueOf(entity.getId()));
        txtName.setText(entity.getName());
    }
    private void setErrorMessage(Map<String,String> errors){
        Set<String> fields=errors.keySet();
        if(fields.contains("name")){
            labelErrorName.setText(errors.get("name"));
        }
    }
}
