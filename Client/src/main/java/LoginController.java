import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.User;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


public class LoginController extends UnicastRemoteObject implements  Serializable  {
    private IService service;
    private ObservableList<User> model;


    @FXML
    TextField textFieldUsername;
    @FXML
    PasswordField passwordFieldParola;
    @FXML
    Button buttonLogin;

    public LoginController() throws RemoteException {
    }


    public void setService(IService inscriereService) {
        this.service = inscriereService;
    }
    public void initialize(){}

    public void handleLoginBotton(MouseEvent mouseEvent) {
        String userName = textFieldUsername.getText();
        String parola = passwordFieldParola.getText();
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/MainView.fxml"));
            BorderPane root=loader.load();
            MainController ctr = loader.getController();
            ctr.setService(service,new User(userName,parola));

            if(service.login(userName,parola,ctr)){
                Stage dialogStage = new Stage();
                dialogStage.setTitle("Main");
                Scene scene = new Scene(root);
                dialogStage.setScene(scene);
                dialogStage.setResizable(false);
                dialogStage.show();
                ((Node)(mouseEvent.getSource())).getScene().getWindow().hide();
            }
            else
                ShowMessage.showMessage(Alert.AlertType.ERROR,"Eroare","Username sau parola invalida!");
        } catch (ServiceException e) {
            ShowMessage.showMessage(Alert.AlertType.ERROR,"Eroare",e.getMessage());

        } catch (Exception e) {
            //ShowMessage.showMessage(Alert.AlertType.ERROR,"Eroare",e.getMessage());
            System.out.println(e.getMessage());

        }
    }
}