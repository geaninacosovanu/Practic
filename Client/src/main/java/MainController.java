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
import model.User;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class MainController extends UnicastRemoteObject implements IObserver, Serializable {
    @FXML
    Button buttonLogout;
    @FXML
    Label labelStatus;
    @FXML
    Label label11;
    @FXML
    Label label12;
    @FXML
    Label label13;
    @FXML
    Label label21;
    @FXML
    Label label22;
    @FXML
    Label label23;
    @FXML
    Label label31;
    @FXML
    Label label32;
    @FXML
    Label label33;

    Integer semn;


    Label[][] labels=new Label[3][3];
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
    @FXML
    TextField textFieldLinie;
    @FXML
    TextField textFieldColoana;
    @FXML
    Button buttonAdd;

//    @FXML
//    CheckBox checkBoxParticipantExistent;
    private IService service;
    //    private ObservableList<ProbaDTO> modelProba = FXCollections.observableArrayList();
    private User user;

    public MainController() throws RemoteException {
    }

    public void setService(IService service, User user) {
        this.user = user;
        this.service = service;
//        for(int i=0;i<3;i++)
//        for(int j=0;j<3;j++)
//            labels[i][j]=new Label();

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
        labelStatus.setVisible(false);
        labels[0][0]=label11;
        labels[0][1]=label12;
        labels[0][2]=label13;
        labels[1][0]=label21;
        labels[1][1]=label22;
        labels[1][2]=label23;
        labels[2][0]=label31;
        labels[2][1]=label32;
        labels[2][2]=label33;
//
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
    public void notificareAsteptare(String msg) {
        Platform.runLater(() -> {
            ShowMessage.showMessage(Alert.AlertType.INFORMATION, "Information", msg);
        });
    }

    public void handleStartButton(MouseEvent mouseEvent) {
        service.start(user, this);
    }
    public void handleButton(MouseEvent mouseEvent){
        service.muta(user,Integer.parseInt(textFieldLinie.getText()),Integer.parseInt(textFieldColoana.getText()),semn);
    }

    @Override
    public void notificareStart(String msg,Integer semn) {
        Platform.runLater(() -> {
            labelStatus.setVisible(true);
            labelStatus.setText(msg);
            this.semn=semn;
        });

    }

    @Override
    public void notificareMutare(Integer i, Integer j, Integer semn) throws ServiceException, RemoteException {
        Platform.runLater(() -> {
            if(semn==1)
           labels[i][j].setText("X");
            else
                labels[i][j].setText("O");
        });
    }
}
