package gui;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.service.DepartmentService;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {
    @FXML
    private MenuItem menuItemSeller;
    @FXML
    private MenuItem menuItemDepartment;
    @FXML
    private MenuItem menuItemAbout;

    @FXML
    public void onMenuItemSellerAction(){
        System.out.println("onMenuItemSellerAction()");
    }
    @FXML
    public void onMenuItemDepartmentAction(){
        loadView2("DepartmentList.fxml");
    }



    @FXML
    public void onMenuItemAboutAction(){
        loadView("About.fxml");
    }


    @Override
    public void initialize(URL uri, ResourceBundle rb) {

    }

    private synchronized void loadView(String absoluteName){
        FXMLLoader loader=new FXMLLoader(getClass().getResource(absoluteName));
        try {
            VBox vBox=loader.load();
            Scene mainScene= Main.getMainScene();
            VBox vBoxMain=(VBox) ((ScrollPane) mainScene.getRoot()).getContent();
            Node mainMenu=vBoxMain.getChildren().get(0);
            vBoxMain.getChildren().clear();
            vBoxMain.getChildren().add(mainMenu);
            vBoxMain.getChildren().addAll(vBox.getChildren());
        } catch (IOException e) {
            Alerts.showAlerts("IO Exception","Error load view",e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }

    }
    private void loadView2(String absoluteName) {
        FXMLLoader loader=new FXMLLoader(getClass().getResource(absoluteName));
        try {
            VBox vBox=loader.load();
            Scene mainScene= Main.getMainScene();
            VBox vBoxMain=(VBox) ((ScrollPane) mainScene.getRoot()).getContent();
            Node mainMenu=vBoxMain.getChildren().get(0);
            vBoxMain.getChildren().clear();
            vBoxMain.getChildren().add(mainMenu);
            vBoxMain.getChildren().addAll(vBox.getChildren());

            DepartmentListController departmentListController=loader.getController();
            departmentListController.setService(new DepartmentService());
            departmentListController.updateTableView();


        } catch (IOException e) {
            Alerts.showAlerts("IO Exception","Error load view",e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }


    }
}
