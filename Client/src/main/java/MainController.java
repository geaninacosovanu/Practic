import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.User;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Set;

public class MainController extends UnicastRemoteObject  implements IObserver,Serializable {
    private IService service;
//    private ObservableList<ProbaDTO> modelProba = FXCollections.observableArrayList();
    private User user;
    @FXML
    private Button buttonStart;
//    @FXML
//    TableView<ProbaDTO> tableViewProba;
//    @FXML
//    TableColumn<Proba, String> tableColumnStil;
//    @FXML
//    TableColumn<Proba, Float> tableColumnDistanta;
//    @FXML
//    TableColumn<Proba, Integer> tableColumnNrParticipanti;
//
//    @FXML
//    TableView<ParticipantProbeDTO> tableViewParticipant;
//    @FXML
//    TableColumn<model.Participant, String> tableColumnNume;
//    @FXML
//    TableColumn<model.Participant, Integer> tableColumnVarsta;
//    @FXML
//    TableColumn<model.Participant, String> tableColumnProbe;
//    @FXML
//    ListView<Proba> listViewProbe;
//
//    @FXML
//    TextField textFieldNume;
//    @FXML
//    TextField textFieldVarsta;
//    @FXML
//    Button buttonInscriere;
//
//    @FXML
//    CheckBox checkBoxParticipantExistent;

    @FXML
    Button buttonLogout;

    @FXML
    ListView<String> listViewParticipanti;

    @FXML
    Label labelNumere;

    @FXML
    TextField textFieldNumar;

    @FXML
    Button buttonAdd;

    public MainController() throws RemoteException {
    }

    public void setService(IService service,User user) {
        this.user=user;
        this.service = service;
    }

    @FXML
    public void initialize() {


    }


    public void handleLogoutBotton(MouseEvent mouseEvent) {
        try {
            service.logout(user,this);
            showLoginWindow(initLoginView());
            ((Node) (mouseEvent.getSource())).getScene().getWindow().hide();
        } catch (ServiceException e) {
            ShowMessage.showMessage(Alert.AlertType.ERROR,"Eroare",e.getMessage());
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

//    public void handleButtonInscriere(MouseEvent mouseEvent) {
//        String nume = textFieldNume.getText();
//        Integer varsta = Integer.parseInt(textFieldVarsta.getText());
//        List<Proba> probe = new ArrayList<>(listViewProbe.getSelectionModel().getSelectedItems());
//        try {
//            if (probe.size() == 0)
//                ShowMessage.showMessage(Alert.AlertType.WARNING, "Warning", "Nu ati selectat nicio proba!");
//            else if (checkBoxParticipantExistent.isSelected() == false) {
//                service.saveInscriere(nume, varsta, probe, false);
//            } else {
//                service.saveInscriere(nume, varsta, probe, true);
//            }
//            String msg = "Participantul " + nume + " a fost inscris la probele:\n";
//            for (Proba p : probe)
//                msg += p.toString() + "\n";
//            ShowMessage.showMessage(Alert.AlertType.CONFIRMATION, "Confirmation", msg);
//            textFieldNume.clear();
//            textFieldVarsta.clear();
//            listViewProbe.getSelectionModel().clearSelection();
//            checkBoxParticipantExistent.setSelected(false);
//        } catch (InscriereServiceException e) {
//            ShowMessage.showMessage(Alert.AlertType.ERROR, "Eroare", e.getMessage());
//
//        }
//    }



    public void handlebuttonStart(MouseEvent mouseEvent) {
        try {
            service.start();
        } catch (Exception e) {
            ShowMessage.showMessage(Alert.AlertType.ERROR,"Eroare",e.getMessage());
        }
    }

    @Override
    public void notificareParticipanti(Set<String> participanti) throws ServiceException, RemoteException {
        Platform.runLater(() -> {
            listViewParticipanti.setItems(FXCollections.observableArrayList(participanti));
        });
    }

    @Override
    public void notificareNumereProprii(List<Integer> nr) throws ServiceException, RemoteException {
            Platform.runLater(() -> {
                String numere = "";
                for (Integer i:nr) numere=numere+i+" ";
                labelNumere.setText(numere);

        });
    }

    public void handleAdd(MouseEvent mouseEvent) {
        try {
            service.addNumar(user.getId(),Integer.parseInt(textFieldNumar.getText()));
        } catch (Exception e) {
            ShowMessage.showMessage(Alert.AlertType.ERROR,"Eroare",e.getMessage());
        }
        textFieldNumar.clear();
    }
}
