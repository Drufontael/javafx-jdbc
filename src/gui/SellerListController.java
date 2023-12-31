package gui;

import application.Main;
import db.DbIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Department;
import model.entities.Seller;
import model.service.DepartmentService;
import model.service.SellerService;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class SellerListController implements Initializable, DataChangeListener {
    private SellerService service;
    @FXML
    private TableView<Seller> tableViewSeller;
    @FXML
    private TableColumn<Seller,Integer> tableColumnId;
    @FXML
    private TableColumn<Seller,String> tableColumnName;
    @FXML
    private TableColumn<Seller,String> tableColumnEmail;
    @FXML
    private TableColumn<Seller, LocalDate> tableColumnBirthDate;
    @FXML
    private TableColumn<Seller,Double> tableColumnBaseSalary;
    @FXML
    private TableColumn<Seller, Department> tableColumnDepartment;
    @FXML
    private TableColumn<Seller,Seller> tableColumnEDIT;
    @FXML
    TableColumn<Seller, Seller> tableColumnREMOVE;
    @FXML
    private Button btNew;
    private ObservableList<Seller> obsList;

    @FXML
    public void onBtNewAction(ActionEvent event)
    {
        Seller obj=new Seller();
        createDialogForm(obj,"SellerForm.fxml", Utils.currentStage(event));

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();

    }
    public void setService(SellerService service){
        this.service=service;
    }

    private void initializeNodes() {
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tableColumnBirthDate.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
        Utils.formatTableColumnDate(tableColumnBirthDate,"dd/MM/yyyy");
        tableColumnBaseSalary.setCellValueFactory(new PropertyValueFactory<>("baseSalary"));
        Utils.formatTableColumnDouble(tableColumnBaseSalary,2);
        tableColumnDepartment.setCellValueFactory(new PropertyValueFactory<>("department"));
        Utils.formatTableColumnDepartment(tableColumnDepartment);






        Stage stage=(Stage) Main.getMainScene().getWindow();
        tableViewSeller.prefHeightProperty().bind(stage.heightProperty());

    }

    public void updateTableView(){
        if(service==null){
            throw new IllegalStateException("Service was null");
        }
        List<Seller> list=service.findAll();
        obsList = FXCollections.observableArrayList(list);
        tableViewSeller.setItems(obsList);
        initEditButtons();
        initRemoveButtons();
    }

    public void createDialogForm(Seller obj, String absoluteName, Stage stageParent){
        try{
            FXMLLoader loader=new FXMLLoader(getClass().getResource(absoluteName));
            Pane pane=loader.load();

            SellerFormController controller=loader.getController();
            controller.setSeller(obj);
            controller.setServices(new SellerService(),new DepartmentService());
            controller.loadAssociatedObjects();
            controller.subscribeDataChangeListener(this);
            controller.updateFormData();

            Stage dialogStage=new Stage();
            dialogStage.setTitle("Digite os dados do Vendedor");
            dialogStage.setScene(new Scene(pane));
            dialogStage.setResizable(false);
            dialogStage.initOwner(stageParent);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.showAndWait();

        }catch (IOException e){
            Alerts.showAlerts("IOException","Error loading view",e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @Override
    public void onDataChanged() {
        updateTableView();
    }

    private void initEditButtons() {
        tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnEDIT.setCellFactory(param -> new TableCell<Seller, Seller>() {
            private final Button button = new Button("edit");
            @Override
            protected void updateItem(Seller obj, boolean empty) {
                super.updateItem(obj, empty);
                if (obj == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(button);
                button.setOnAction(
                        event -> createDialogForm(
                                obj, "/gui/SellerForm.fxml",Utils.currentStage(event)));
            }
        });
    }
    private void initRemoveButtons() {
        tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnREMOVE.setCellFactory(param -> new TableCell<Seller, Seller>() {
            private final Button button = new Button("remove");
            @Override
            protected void updateItem(Seller obj, boolean empty) {
                super.updateItem(obj, empty);
                if (obj == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(button);
                button.setOnAction(event -> removeEntity(obj));
            }
        });
    }

    private void removeEntity(Seller obj) {
        Optional<ButtonType> result=Alerts.showConfirmation("Confirmação","Tem certeza que quer deletar?");
        if(result.get()==ButtonType.OK){
            if(service==null) throw new IllegalStateException("Service is null");
            try {
                service.remove(obj);
                updateTableView();
            }catch (DbIntegrityException e){
                Alerts.showAlerts("Error removing object",null,e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

}
