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

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;

public class MainController extends UnicastRemoteObject implements IObserver, Serializable {
    @FXML
    TableView<MasinaDTO> table;
    @FXML
    TableColumn<MasinaDTO, Integer> tableColumnId;
    @FXML
    TableColumn<MasinaDTO, String> tableColumnNume;
    @FXML
    TableColumn<MasinaDTO, Integer> tableColumnPunctControl;
    @FXML
    TableColumn<MasinaDTO, String> tableColumnOra;

    @FXML
    Label labelPunctControl;
    @FXML
    Button buttonLogout;
    @FXML
    Button buttonMarcare;
    @FXML
    TextField textFieldOra;
    @FXML
    TextField textFieldMinute;
    @FXML
    ComboBox<Masina> comboBoxMasini;
    private IService service;
    private ObservableList<MasinaDTO> model = FXCollections.observableArrayList();
    private ObservableList<Masina> modelCombo = FXCollections.observableArrayList();
    private User user;

    public MainController() throws RemoteException {
    }

    public void setService(IService service, User user) {
        this.user = user;
        this.service = service;

        model = FXCollections.observableArrayList(service.getMasini(user.getPunctControl()));
        modelCombo = FXCollections.observableArrayList(service.getMasiniNetrecute(user.getPunctControl()));
        table.setItems(model);
        comboBoxMasini.setItems(modelCombo);

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
        labelPunctControl.setText(user.getPunctControl().toString());

    }

    @FXML
    public void initialize() {
        initializeTableMasini();

    }

    private void initializeTableMasini() {

        tableColumnNume.setCellValueFactory(new PropertyValueFactory<>("nume"));
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnOra.setCellValueFactory(new PropertyValueFactory<>("ora"));
        tableColumnPunctControl.setCellValueFactory(new PropertyValueFactory<>("punctControl"));


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
    public void rezultatAdded() {
        Platform.runLater(() -> {

            model.setAll(service.getMasini(user.getPunctControl()));
            modelCombo.setAll(service.getMasiniNetrecute(user.getPunctControl()));

        });
    }

    public void handleMarcareButton(MouseEvent mouseEvent) {
        Integer ora = Integer.parseInt(textFieldOra.getText());
        Integer minute = Integer.parseInt(textFieldMinute.getText());
        service.add(comboBoxMasini.getSelectionModel().getSelectedItem().getId(), user.getPunctControl(), ora + ":" + minute);
        comboBoxMasini.getSelectionModel().clearSelection();
        textFieldMinute.clear();
        textFieldOra.clear();
    }
}