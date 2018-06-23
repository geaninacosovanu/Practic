import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


import java.io.IOException;

public class StartClient extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            ApplicationContext factory = new ClassPathXmlApplicationContext("classpath:spring-client");
            IService service = (IService) factory.getBean("service");
            System.out.println("Am obtinut o referita la serverul remote");

            primaryStage.setTitle("Login");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("LoginView.fxml"));
            BorderPane root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            LoginController ctr = loader.getController();


            ctr.setService(service);
            Scene scene = new Scene(root);
            primaryStage.setResizable(false);
            primaryStage.setScene(scene);
            primaryStage.show();


        } catch (Exception e) {
            System.err.println("Service Initialization  exception:" + e);
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
