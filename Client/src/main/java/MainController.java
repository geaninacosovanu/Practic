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

public class MainController extends UnicastRemoteObject implements IObserver, Serializable {
    @FXML
    Button buttonStart;
    @FXML
    Button buttonLogout;
    @FXML
    Label labelId;
    private IService service;
    //    private ObservableList<ProbaDTO> modelProba = FXCollections.observableArrayList();
    private User user;


    public MainController() throws RemoteException {
    }

    public void setService(IService service, User user) {
        this.user = user;
        this.service = service;
//        try {
//            modelProba = FXCollections.observableArrayList(service.getAllProba());
//        } catch (InscriereServiceException e) {
//            e.printStackTrace();
//        }
//        tableViewProba.setItems(modelProba);
//        tableViewProba.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
//            if (newSelection != null) {
//                try {
//                    tableViewParticipant.setItems(FXCollections.observableArrayList(service.getParticipanti(newSelection.getIdProba())));
//                } catch (InscriereServiceException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        List<Proba> probe = new ArrayList<>();
//        for (ProbaDTO p : modelProba)
//            probe.add(p.getProba());
//        listViewProbe.setItems(FXCollections.observableArrayList(probe));
//        listViewProbe.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    @FXML
    public void initialize() {
    }

    private void initializeTableParticipanti() {

//        tableColumnNume.setCellValueFactory(new PropertyValueFactory<>("participantNume"));
//        tableColumnVarsta.setCellValueFactory(new PropertyValueFactory<>("participantVarsta"));
//        tableColumnProbe.setCellValueFactory(new PropertyValueFactory<>("probe"));


    }

    private void initializeTableProba() {
//        tableColumnStil.setCellValueFactory(new PropertyValueFactory<>("numeProba"));
//        tableColumnDistanta.setCellValueFactory(new PropertyValueFactory<>("distantaProba"));
//
//        tableColumnNrParticipanti.setCellValueFactory(new PropertyValueFactory<>("nrParticipanti"));
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


    @Override
    public void notificare() {
//        Platform.runLater(() -> {
//            try {
//                modelProba.setAll(service.getAllProba());
//            } catch (InscriereServiceException e) {
//                e.printStackTrace();
//            }
//        });
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
            labelId.setText(id);
        });
    }

    public void handleStart(MouseEvent mouseEvent) {
        service.start(user, this);
    }
}
