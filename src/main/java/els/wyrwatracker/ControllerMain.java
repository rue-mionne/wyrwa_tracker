package els.wyrwatracker;

import els.filehandlers.FileNotFoundException;
import els.filehandlers.ServerListHandler;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;

public class ControllerMain {
    ServerListHandler serverList= null;
    ServerListHandler serverTemplateList = null;

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
    }

    @FXML
    void UtworzNowaBaze(ActionEvent event) {
        String nowaBaza = NewDatabaseName.getText();
        if(serverList!=null){
            ArrayList<String> list = serverList.getServerList();
            if (!list.contains(nowaBaza)&&wyborWersji.getValue()!=null) {
                //utworz nowa baze
                DatabaseChoice.getItems().add(nowaBaza);
                list.add(nowaBaza);
                serverList.SaveServerList();
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
        TextPowitanie.setText("TestWczytaj");
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

}
