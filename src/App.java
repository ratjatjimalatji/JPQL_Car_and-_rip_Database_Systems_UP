import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
  @Override
  public void start(Stage primaryStage) {

    Parent root;
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("MainScene.fxml"));
      root = loader.load();
      MainSceneController controller = loader.getController(); // Get reference to initialized controller

      Scene scene = new Scene(root);

      primaryStage.setTitle("U25267869's CAR CRUD JPQL");
      primaryStage.setScene(scene);
      primaryStage.show();

    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  public static void main(String[] args) {
    launch(args);

  }
}
