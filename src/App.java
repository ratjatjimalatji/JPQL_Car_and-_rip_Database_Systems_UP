import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
 
public class App extends Application {
    @Override
    public void start(Stage primaryStage) {

        
  
  
  Parent root;
  try {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("MainScene.fxml"));
root = loader.load();
MainSceneController controller = loader.getController();  // Get reference to initialized controller
    
    Scene scene = new Scene(root);
  
  primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();

  } catch (IOException e) {
    e.printStackTrace();
  }
  
    }
 
 public static void main(String[] args) {
        launch(args);

    }}

