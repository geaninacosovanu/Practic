import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.Participant;
import model.User;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class MainController extends UnicastRemoteObject implements IObserver, Serializable {
    @FXML
    TableView<ParticipantDTO> tableViewParticipanti;
    @FXML
    TableColumn<Participant, Integer> tableColumnId;
    @FXML
    TableColumn<Participant, String> tableColumnNume;
    @FXML
    TableColumn<Participant, Status> tableColumnStatus;
    @FXML
    TextField textFieldNota;
    @FXML
    Button buttonAdd;
    @FXML
    ComboBox<Participant> comboBoxParticipant;
    @FXML
    Button buttonLogout;
    @FXML
    Label labelAspect;
    private IService service;
    private ObservableList<ParticipantDTO> modelAllParticipanti = FXCollections.observableArrayList();
    private ObservableList<Participant> modelParticipantCombo = FXCollections.observableArrayList();
    private User user;

    public MainController() throws RemoteException {
    }

    public void setService(IService service, User user) {
        this.user = user;
        this.service = service;
        modelAllParticipanti = FXCollections.observableArrayList(service.getAllParticipanti());
        modelParticipantCombo = FXCollections.observableArrayList(service.getParticipantiUndone());
        tableViewParticipanti.setItems(modelAllParticipanti);
        comboBoxParticipant.setItems(FXCollections.observableArrayList(modelParticipantCombo));

    }

    @FXML
    public void initialize() {
        initializeTable();
        labelAspect.setText(user.getAspect());
    }

    private void initializeTable() {
        tableColumnNume.setCellValueFactory(new PropertyValueFactory<>("nume"));
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnStatus.setCellValueFactory(new PropertyValueFactory<>("status"));


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
    public void rezultatAdded() {
        Platform.runLater(() -> {

            modelAllParticipanti.setAll(service.getAllParticipanti());
            modelParticipantCombo.setAll(service.getParticipantiUndone());

        });
    }

    public void handleAddButtonClick(MouseEvent mouseEvent) {
        Float nota = Float.parseFloat(textFieldNota.getText());
        Participant p = comboBoxParticipant.getSelectionModel().getSelectedItem();
        try {
            service.saveNota(p.getId(), user.getId(), nota);
            String msg = "Nota a fost adaugate ";
            ShowMessage.showMessage(Alert.AlertType.CONFIRMATION, "Confirmation", msg);
            textFieldNota.clear();
            comboBoxParticipant.getSelectionModel().clearSelection();
        } catch (ServiceException e) {
            ShowMessage.showMessage(Alert.AlertType.ERROR, "Eroare", e.getMessage());

        }
    }
}