package els.wyrwatracker;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;


public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("MenuTest.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 700, 400);
                /*SQLiteIO testManager = new SQLiteIO();
                testManager.ConnectToBase("jdbc:sqlite:src/main/resources/Wyrwa_Tracker_Data");*/
                stage.setTitle("Hello! ");
                stage.setScene(scene);
                stage.show();
            }
            catch(Exception e)
            {
                System.err.println(e.getCause().getCause().getClass());
            }



    }

    public static void main(String[] args) {
        launch();
    }
}