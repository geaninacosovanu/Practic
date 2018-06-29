import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.User;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class MainController extends UnicastRemoteObject implements IObserver, Serializable {
    @FXML
    Button buttonStart;
    @FXML
    Button buttonLogout;
    @FXML
    Button buttonGenerare;
    @FXML
    Label labelId;
    @FXML
    Label labelPozitii;
    @FXML
    Label labelMutare;
    boolean mutat = false;
    private Integer pozitieCurenta;
    private IService service;
    private Integer incercari;
    private User user;
    private String oponent;

    public MainController() throws RemoteException {
    }

    public void setService(IService service, User user) {
        this.user = user;
        this.service = service;

    }

    @FXML
    public void initialize() {
    }


    public void handleLogoutBotton(MouseEvent mouseEvent) {
        try {
            service.logout(user, this);
            showLoginWindow(initLoginView());
            ((Node) (mouseEvent.getSource())).getScene().getWindow().hide();
        } catch (ServiceException e) {
            ShowMessage.showMessage(Alert.AlertType.ERROR, "Eroare", e.getMessage());
        }

    }

    public BorderPane initLoginView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/LoginView.fxml"));
            BorderPane root = loader.load();
            LoginController ctr = loader.getController();
            ctr.setService(service);
            return root;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void showLoginWindow(BorderPane root) {
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Login");
        Scene scene = new Scene(root);
        dialogStage.setScene(scene);
        dialogStage.show();
    }


    @Override
    public void notificareMutare(Integer pozitie, String[] joc) throws ServiceException, RemoteException {
        Platform.runLater(() -> {
            labelMutare.setText(String.valueOf(pozitie));
            String s = "";
            for (int i = 0; i < 9; i++)
                if (joc[i] == null)
                    s += "_ ";
                else
                    s = s + joc[i] + " ";
            labelPozitii.setText(s);
        });
    }

    @Override
    public void notificareAsteptare(String msg) throws ServiceException, RemoteException {
        Platform.runLater(() -> {
            ShowMessage.showMessage(Alert.AlertType.ERROR, "Erroare", msg);
        });
    }

    @Override
    public void notificareStart(String id) throws ServiceException, RemoteException {
        Platform.runLater(() -> {
            incercari = 0;
            oponent=id;
            labelId.setText(id);
            labelPozitii.setText("_ _ _ _ _ _ _ _ _");
            pozitieCurenta = -1;
        });
    }

    public void handleStart(MouseEvent mouseEvent) {
        service.start(user, this);
    }

    public void handleGenerare(MouseEvent mouseEvent) {
        //if (service.canMutare(user,oponent)) {
            if (incercari < 3) {
                int randomNum = ThreadLocalRandom.current().nextInt(1, 4);
                pozitieCurenta = service.addPozitie(user, randomNum, pozitieCurenta);
            } else
                ShowMessage.showMessage(Alert.AlertType.INFORMATION, "Info", "Jocul s-a terminat!");
        //} else ShowMessage.showMessage(Alert.AlertType.ERROR, "Eroare", "Trebuie sa mute si celalalt!");
    }
}
