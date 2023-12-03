package gui;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;
import model.entities.Department;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.service.DepartmentService;
import model.service.SellerService;

import java.net.URL;
import java.time.LocalDate;
import java.util.*;

public class SellerFormController implements Initializable {
    private Seller entity;
    private SellerService service;
    private DepartmentService dependency;
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
    private ComboBox<Department> comboBoxDepartment;
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
    private ObservableList<Department> obsList;

    public void setSeller(Seller entity){
        this.entity=entity;
    }
    public void setServices(SellerService service,DepartmentService dependency){
        this.service=service;
        this.dependency=dependency;
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
        if(txtEmail.getText()==null || txtEmail.getText().trim().equals(""))
            exception.addError("email","Field can't be empty");
        if(txtBaseSalary.getText()==null || txtBaseSalary.getText().trim().equals(""))
            exception.addError("baseSalary","Field can't be empty");
        if(dpBirthDate.getValue()==null)
            exception.addError("birthDate","Field can't be empty");
        String name=txtName.getText();
        String email=txtEmail.getText();
        LocalDate birthDate=dpBirthDate.getValue();
        Double baseSalary=Utils.tryParseDouble(txtBaseSalary.getText());
        Department department=comboBoxDepartment.getValue();

        if (exception.getErrors().size()>0) throw exception;
        return new Seller(id,name,email,birthDate,baseSalary,department);
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
        initializeComboBoxDepartment();
    }

    public void updateFormData(){
        if(entity==null) throw new IllegalStateException("Entity was null");
        txtId.setText(String.valueOf(entity.getId()));
        txtName.setText(entity.getName());
        txtEmail.setText(entity.getEmail());
        Locale.setDefault(Locale.US);
        txtBaseSalary.setText(String.format("%.2f",entity.getBaseSalary()));
        dpBirthDate.setValue(entity.getBirthDate());
        if(entity.getDepartment()==null){
            comboBoxDepartment.getSelectionModel().selectFirst();
        }else {
            comboBoxDepartment.setValue(entity.getDepartment());
        }
    }

    public void loadAssociatedObjects(){
        obsList= FXCollections.observableArrayList(dependency.findAll());
        comboBoxDepartment.setItems(obsList);
    }

    private void setErrorMessage(Map<String,String> errors){
        Set<String> fields=errors.keySet();

        labelErrorName.setText(fields.contains("name")?errors.get("name"):"");
        labelErrorEmail.setText(fields.contains("email")?errors.get("email"):"");
        labelErrorBirthDate.setText(fields.contains("birthDate")?errors.get("birthDate"):"");
        labelErrorBaseSalary.setText(fields.contains("baseSalary")?errors.get("baseSalary"):"");
        errors.clear();

    }

    private void initializeComboBoxDepartment() {
        Callback<ListView<Department>, ListCell<Department>> factory = lv -> new ListCell<Department>() {
            @Override
            protected void updateItem(Department item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getName());
            }
        };
        comboBoxDepartment.setCellFactory(factory);
        comboBoxDepartment.setButtonCell(factory.call(null));
    }
}
