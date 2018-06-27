import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.Cuvant;
import model.Joc;
import model.User;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class MainController extends UnicastRemoteObject implements IObserver, Serializable {
    @FXML
    Button buttonLogout;
    @FXML
    Button buttonIncepere;
    @FXML
    Label labelC;
    @FXML
    Label labelCuvant;
    @FXML
    Label labelL;
    @FXML
    Label labelListaLitere;
    @FXML
    Label labelGresit;
    @FXML
    TextField textFieldLitera;
    @FXML
    Button buttonAdd;
    private IService service;
    private Cuvant cuvant = null;
    private String listaLitere = "";
    private String listaLitereProprie = "";
    private Joc joc;
    //    private ObservableList<ProbaDTO> modelProba = FXCollections.observableArrayList();
    private User user;

    public MainController() throws RemoteException {
    }

    public void setService(IService service, User user) {
        this.user = user;
        this.service = service;
    }

    @FXML
    public void initialize() {
        labelC.setVisible(false);
        labelCuvant.setVisible(false);
        labelL.setVisible(false);
        labelListaLitere.setVisible(false);
        labelGresit.setVisible(false);

    }


    public void handleLogoutBotton(MouseEvent mouseEvent) {
        try {
            service.logout(user, this);
            service.addJocJucator(user.getId(),joc.getId(),listaLitereProprie);
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
    public void notificareStart(Cuvant c) {
        Platform.runLater(() -> {
            cuvant = c;
            listaLitere="";
            labelC.setVisible(true);
            labelCuvant.setVisible(true);
            labelL.setVisible(true);
            labelListaLitere.setVisible(true);
            labelListaLitere.setText(listaLitere);
            labelCuvant.setText(cuvant.getCuvant());
            listaLitereProprie="";
        });
    }

    @Override
    public void notificareMutare(Cuvant c, Character litera) throws ServiceException, RemoteException {
        Platform.runLater(() -> {
            labelGresit.setVisible(false);
            cuvant = c;
            labelL.setVisible(true);
            listaLitere += litera;
            labelListaLitere.setText(listaLitere);
            labelCuvant.setText(cuvant.getCuvant());
        });
    }

    @Override
    public void notificaGresit(Character c) throws RemoteException {
        Platform.runLater(() -> {
            listaLitere += c;
            labelListaLitere.setText(listaLitere);
            labelGresit.setVisible(true);
            labelGresit.setText("Gresit");
        });
    }

    @Override
    public void notificaCastigator(User user) throws RemoteException {
        Platform.runLater(() -> {
            labelGresit.setVisible(true);
            labelGresit.setText("Castigator: " + user.getId());
            service.addJocJucator(user.getId(),joc.getId(),listaLitereProprie);
        });
    }

    @Override
    public void notificaPierdere() throws RemoteException {
        Platform.runLater(() -> {
            labelGresit.setVisible(true);
            labelGresit.setText("Joc pierdut!");
            service.addJocJucator(user.getId(),joc.getId(),listaLitereProprie);

        });
    }

    public void handleButtonIncepere(MouseEvent mouseEvent) {
        if (service.start())
            ShowMessage.showMessage(Alert.AlertType.ERROR, "Eroare", "Un joc a fost inceput deja!");
        else
            joc=service.addJoc();
    }

    public void handleAddButton(MouseEvent mouseEvent) {
        String s = textFieldLitera.getText();
        textFieldLitera.clear();
        listaLitereProprie+=s;
        service.addLitera(user, cuvant, s.charAt(0));
    }
}