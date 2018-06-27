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
import model.Copil;
import model.CopilDTO;
import model.User;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class MainController extends UnicastRemoteObject implements IObserver, Serializable {
    @FXML
    Button buttonLogout;
    @FXML
    Label labelPunctControl;
    @FXML
    TableView<CopilDTO> tableView;
    @FXML
    TableColumn<CopilDTO, String> tableColumnNume;
    @FXML
    TableColumn<CopilDTO, String> tableColumnOra;
    @FXML
    TableColumn<CopilDTO, Integer> tableColumnId;
    @FXML
    TableColumn<CopilDTO, Integer> tableColumnPunctControl;
    @FXML
    ComboBox<Copil> comboCopil;
    @FXML
    Button buttonAdd;
    @FXML
    TextField textFieldOra;
    @FXML
    TextField textFieldMinute;

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
    private IService service;
    private ObservableList<CopilDTO> model = FXCollections.observableArrayList();
    private ObservableList<Copil> modelCombo = FXCollections.observableArrayList();
    private User user;

    public MainController() throws RemoteException {
    }

    public void setService(IService service, User user) {
        this.user = user;
        this.service = service;
        labelPunctControl.setText("Punct control:" + user.getPunctControl());
        model = FXCollections.observableArrayList(service.getAllCopii(user.getPunctControl()));
        modelCombo = FXCollections.observableArrayList(service.getAllCopil(user.getPunctControl()));
        tableView.setItems(model);
        comboCopil.setItems(modelCombo);

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
        initializeTableCopil();


    }


    private void initializeTableCopil() {
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnNume.setCellValueFactory(new PropertyValueFactory<>("nume"));
        tableColumnPunctControl.setCellValueFactory(new PropertyValueFactory<>("punctControl"));
        tableColumnOra.setCellValueFactory(new PropertyValueFactory<>("ora"));
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
        Platform.runLater(() -> {
            model.setAll(service.getAllCopii(user.getPunctControl()));
            modelCombo.setAll(service.getAllCopil(user.getPunctControl()));

        });
    }

    public void handleAddButton(MouseEvent mouseEvent) {
        String min=textFieldMinute.getText();
        String ora=textFieldOra.getText();
        Copil copil=comboCopil.getSelectionModel().getSelectedItem();
        try{
            service.add(copil.getId(),user.getPunctControl(),ora+":"+min);
        }catch(ServiceException e){
            ShowMessage.showMessage(Alert.AlertType.ERROR,"Eroare",e.getMessage());
        }
    }
}