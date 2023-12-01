package gui;

import db.DbException;
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
import model.service.DepartmentService;

import java.net.URL;
import java.util.ResourceBundle;

public class DepartmentFormController implements Initializable {
    private Department entity;
    private DepartmentService service;
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

    @FXML
    public void onBtSaveAction(ActionEvent event){
        if(entity==null || service==null) throw new IllegalStateException("Dependency not found");
        try{
            entity = getFormData();
            service.saveOrUpdate(entity);
            Utils.currentStage(event).close();
        }catch (DbException e){
            Alerts.showAlerts("Error saving object",null,e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private Department getFormData() {
        Integer id= Utils.tryParseInt(txtId.getText());
        String name=txtName.getText();
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
}
