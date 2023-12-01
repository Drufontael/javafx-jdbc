package gui;

import application.Main;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Department;
import model.service.DepartmentService;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DepartmentListController implements Initializable {
    private DepartmentService service;
    @FXML
    private TableView<Department> tableViewDepartment;
    @FXML
    private TableColumn<Department,Integer> tableColumnId;
    @FXML
    private TableColumn<Department,String> tableColumnName;
    @FXML
    private Button btNew;
    private ObservableList<Department> obsList;

    @FXML
    public void onBtNewAction(ActionEvent event)
    {
        createDialogform("DepartmentForm.fxml", Utils.currentStage(event));

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();

    }
    public void setService(DepartmentService service){
        this.service=service;
    }

    private void initializeNodes() {
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));

        Stage stage=(Stage) Main.getMainScene().getWindow();
        tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());

    }

    public void updateTableView(){
        if(service==null){
            throw new IllegalStateException("Service was null");
        }
        List<Department> list=service.findAll();
        obsList = FXCollections.observableArrayList(list);
        tableViewDepartment.setItems(obsList);
    }

    public void createDialogform(String absoluteName,Stage stageParent){
        try{
            FXMLLoader loader=new FXMLLoader(getClass().getResource(absoluteName));
            Pane pane=loader.load();

            Stage dialogStage=new Stage();
            dialogStage.setTitle("Digite os dados do Departamento");
            dialogStage.setScene(new Scene(pane));
            dialogStage.setResizable(false);
            dialogStage.initOwner(stageParent);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.showAndWait();

        }catch (IOException e){
            Alerts.showAlerts("IOException","Error loading view",e.getMessage(), Alert.AlertType.ERROR);
        }
    }
}