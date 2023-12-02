package els.wyrwatracker;

import els.sqliteIO.NoActiveConnectionException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;


public class HelloApplication extends Application {
    ControllerMain menuController;
    @Override
    public void start(Stage stage) throws IOException {

            try {
                stage.setTitle("Wyrwa Tracker 1.0");
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("MenuTest.fxml"));
                menuController = (ControllerMain) fxmlLoader.getController();
                menuController.giveParent(stage);
                Scene scena1 = new Scene(fxmlLoader.load(), 700, 400);
                stage.setScene(scena1);
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