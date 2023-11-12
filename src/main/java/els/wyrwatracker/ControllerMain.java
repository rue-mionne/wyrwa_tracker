package els.wyrwatracker;

import els.data.Account;
import els.filehandlers.FileNotFoundException;
import els.filehandlers.ServerListHandler;

import els.mediators.GeneralMediator;
import els.sqliteIO.NoActiveConnectionException;
import els.sqliteIO.SQLiteConnector;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class ControllerMain {
    ServerListHandler serverList= null;
    ServerListHandler serverTemplateList = null;

    static Stage parent;
    @FXML
    private ChoiceBox<String> DatabaseChoice;

    @FXML
    private TextField NewDatabaseName;

    @FXML
    private Button PrzyciskNowaBaza;

    @FXML
    private Button PrzyciskWczytaj;

    @FXML
    private Label TextPowitanie;

    @FXML
    private AnchorPane WelcomePanel;

    @FXML
    private ChoiceBox<String> wyborWersji;

    @FXML
    void Usun(ActionEvent event) {
        ArrayList<String> listaSerwerow = serverList.getServerList();
        listaSerwerow.remove(DatabaseChoice.getValue());
        serverList.SaveServerList();
        DatabaseChoice.getItems().remove(DatabaseChoice.getValue());

            File usuwacz = new File("db/" + DatabaseChoice.getValue());
            usuwacz.delete();

    }

    @FXML
    void UtworzNowaBaze(ActionEvent event) {
        String nowaBaza = NewDatabaseName.getText();
        if(serverList!=null){
            ArrayList<String> list = serverList.getServerList();
            if (!list.contains(nowaBaza)&&wyborWersji.getValue()!=null) {
                //utworz nowa baze

                    SQLiteConnector sqLiteConnector = new SQLiteConnector();
                    sqLiteConnector.ConnectToBase(nowaBaza);
                    sqLiteConnector.InitiateDatabase("db/"+wyborWersji.getValue());

                DatabaseChoice.getItems().add(nowaBaza);
                list.add(nowaBaza);
                serverList.SaveServerList();
                inicjujProgram(sqLiteConnector);

            } else if(list.contains(nowaBaza)) {
                TextPowitanie.setText("Baza o tej nazwie już istnieje!");
            }
            else {
                TextPowitanie.setText("Nie wybrano wersji!");
            }
        }
    }

    @FXML
    void Wczytaj(ActionEvent event) {
        String nazwaBazy = DatabaseChoice.getValue();
        SQLiteConnector slqHandler = new SQLiteConnector();
        slqHandler.ConnectToBase(nazwaBazy);
        inicjujProgram(slqHandler);
    }
   @FXML
    public void initialize(){

        try {
            serverList = new ServerListHandler("/home/aru/IdeaProjects/WyrwaTracker/src/main/resources/els/testowaBazaServerow");
            ArrayList<String> list = serverList.getServerList();
            list.forEach((server) ->DatabaseChoice.getItems().add(server));
            serverTemplateList = new ServerListHandler("/home/aru/IdeaProjects/WyrwaTracker/src/main/resources/els/testowaListaSzablonow");
            ArrayList<String> listaTemplates = serverTemplateList.getServerList();
            listaTemplates.forEach((template) -> wyborWersji.getItems().add(template));
        }
        catch(FileNotFoundException e){
            System.err.println(e.getMessage());
        }
    }
    public static void giveParent(Stage scena){parent = scena;}

    void inicjujProgram(SQLiteConnector sqlHandler){
        try{
            GeneralMediator mediator = new GeneralMediator(sqlHandler);
            Account konto = new Account();
            mediator.initialize(konto);
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("MainWin.fxml"));
            Scene scena2 = new Scene(fxmlLoader.load(), 700,400);
            parent.setScene(scena2);
            MainWinController dzieciak = (MainWinController) fxmlLoader.getController();
            dzieciak.OdbierzSQLHandler(sqlHandler);
            dzieciak.OdbierzKonto(konto);
            dzieciak.PostaciLoadUnload(new Event(new EventType<>()));
        }
        catch(IOException e){
            System.err.println("Hurrrr");
        }
        catch(NoActiveConnectionException e){
            System.err.println("Połączenie yeeeet");
        }
        catch(SQLException e){
            System.err.println("Umiesz w SQL?");
        }
    }
}
