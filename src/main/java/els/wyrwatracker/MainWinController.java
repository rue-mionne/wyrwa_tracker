package els.wyrwatracker;

import els.data.Postac;
import els.sqliteIO.NoActiveConnectionException;
import els.sqliteIO.SQLiteConnector;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;

import els.data.*;
import els.mediators.*;

public class MainWinController {
    SQLiteConnector sqlboss;
    ObservableList<String> olistBuilds;
    @FXML
    private Tab PostaciTab;
    @FXML
    private Tab CeleTab;

    @FXML
    private ListView<String> CharacterList;

    @FXML
    private Label CharacterName;

    @FXML
    private TextField CharacterNameField;

    @FXML
    private ImageView ClassIcon;

    @FXML
    private Label ClassName;

    @FXML
    private ChoiceBox<String> ClassNameField;

    @FXML
    private ChoiceBox<String> ConfigChoice;

    @FXML
    private TextField EDMultipField;

    @FXML
    private TextField EXPMultipField;

    @FXML
    private Label EstCP;

    @FXML
    private Label EstCP1;

    @FXML
    private Label EstCP11;

    @FXML
    private Label EstCP111;

    @FXML
    private TextField EstCPField;

    @FXML
    private TextField IDMultipField;

    @FXML
    private Tab PrzegladTab;
    private Account konto;

    @FXML
    void EnableCharNameEdit(MouseEvent event) {
        CharacterNameField.setEditable(true);
    }

    @FXML
    void EnableEDMultipEdit(MouseEvent event) {
        EDMultipField.setEditable(true);
    }

    @FXML
    void EnableEXPMultipEdit(MouseEvent event) {
        EXPMultipField.setEditable(true);
    }

    @FXML
    void EnableEstCPEdit(MouseEvent event) {
        EstCPField.setEditable(true);
    }

    @FXML
    void EnableIDMultipEdit(MouseEvent event) {
            IDMultipField.setEditable(true);
    }


    @FXML
    public void initialize(){



    }

    public void OdbierzSQLHandler(SQLiteConnector SQLConnector){
        sqlboss = SQLConnector;
    }

    public void OdbierzKonto(Account konto){
        this.konto = konto;
    }


    public void AddBuildConfig(ActionEvent actionEvent) {
        Postac postac = konto.PobierzPostac(CharacterList.getSelectionModel().getSelectedItem());
        Optional<String> wynik = new GiveNameDialog("konfiguracji").showAndWait();
        if(wynik.isPresent()) {
            String nazwaBuildu = wynik.get();
            postac.PobierzDaneBuildow().AddNewBuildSet(new Build(nazwaBuildu, 0, 0, 0, 0));
            olistBuilds.add(nazwaBuildu);
        }
    }

    public void PostaciLoadUnload(Event event) {
        if(konto!=null){
            if(PostaciTab.isSelected()&&!konto.pobierzListePostaci().isEmpty()){
                ObservableList<String> olistPostaci = FXCollections.observableArrayList(konto.pobierzListePostaci());
                CharacterList.setItems(olistPostaci);
                CharacterList.getSelectionModel().select(0);
                ZaladujPostac(konto.PobierzPostac(0));
                ConfigChoice.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observableValue, String number, String t1) {
                        ZaładujBuild(ConfigChoice.getValue());
                    }
                }) ;
                InicjujHandleryPol();
            }

        }
    }

    void ZaładujBuild(String nazwaBuildu){
        Postac postac = konto.PobierzPostac(CharacterList.getSelectionModel().getSelectedItem());
        Build build = postac.PobierzDaneBuildow().getBuild(nazwaBuildu);
        if(build!=null) {
            EstCPField.setText(String.valueOf(build.EstCP));
            EDMultipField.setText(String.valueOf(build.EDMultip));
            EXPMultipField.setText(String.valueOf(build.EXPMultip));
            IDMultipField.setText(String.valueOf(build.IDMultip));
        }
    }

    void ZaladujPostac(Postac postac){
        ConfigChoice.getSelectionModel().clearSelection();
        CharacterNameField.setText(postac.getIGN());
        ClassNameField.getSelectionModel().select(postac.getClassName());
        String selected = ClassNameField.getValue();
        ClassNameField.getItems().clear();
        ClassNameField.getSelectionModel().select(selected);
        try{
        InitiateClassChoice();
        }
        catch(NoActiveConnectionException e){
            System.err.println("Połączenie wyparowało");
        }
        catch(SQLException e){
            System.err.println("Aru nie umie w SQL");
            System.err.println(e.getMessage());
        }
        ArrayList<String> listaBuildow =postac.PobierzListeBuildow();
        if(!listaBuildow.isEmpty()){
            olistBuilds = FXCollections.observableArrayList(listaBuildow);
            ConfigChoice.setItems(olistBuilds);
            ConfigChoice.getSelectionModel().select(0);
            if(EstCPField.getText().isEmpty()){
                ZaładujBuild(ConfigChoice.getSelectionModel().getSelectedItem());
            }
        }
        else{
            EstCPField.setText("0");
            EDMultipField.setText("0");
            EXPMultipField.setText("0");
            IDMultipField.setText("0");
            ConfigChoice.getItems().clear();
        }

    }

    public void WybierzPostac(MouseEvent mouseEvent) {
        String nazwaPostaci = CharacterList.getSelectionModel().getSelectedItem();
        ZaładujPostac(konto.PobierzPostac(nazwaPostaci));
    }

    public void InicjujHandleryPol(){
        /*
        CharacterNameField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String old, String newval) {
                if(old.equals(CharacterList.getSelectionModel().getSelectedItem())) {
                    String staraNazwaPostaci = CharacterList.getSelectionModel().getSelectedItem();
                    System.out.println(newval);
                    Postac postac = konto.PobierzPostac(staraNazwaPostaci);
                    postac.setIGN(newval);
                    CharacterList.getItems().set((postac.getID() - 1), newval);
                    //schedule database update
                }
            }
        });*/
        EstCPField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String old, String newval) {
                Postac postac = konto.PobierzPostac(CharacterList.getSelectionModel().getSelectedItem());
                Build build = postac.PobierzBuild(ConfigChoice.getValue());
                if(build!=null&&checkIfDigitAndCorrect(EstCPField))
                    build.EstCP= Integer.parseInt(newval);
                //schedule CPUpdate
            }
        });

        EDMultipField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String old, String newval) {
                Postac postac = konto.PobierzPostac(CharacterList.getSelectionModel().getSelectedItem());
                Build build = postac.PobierzBuild(ConfigChoice.getValue());
                if(build!=null&&checkIfDigitAndCorrect(EDMultipField))
                    build.EDMultip= Integer.parseInt(newval);
                //schedule EDUpdate
            }
        });

        IDMultipField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String old, String newval) {
                Postac postac = konto.PobierzPostac(CharacterList.getSelectionModel().getSelectedItem());
                Build build = postac.PobierzBuild(ConfigChoice.getValue());
                if(build!=null&&checkIfDigitAndCorrect(IDMultipField))
                    build.IDMultip= Integer.parseInt(newval);
                //schedule IDUpdate
            }
        });

        EDMultipField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String old, String newval) {
                Postac postac = konto.PobierzPostac(CharacterList.getSelectionModel().getSelectedItem());
                Build build = postac.PobierzBuild(ConfigChoice.getValue());
                if(build!=null&&checkIfDigitAndCorrect(EDMultipField))
                    build.EDMultip= Integer.parseInt(newval);
                //schedule CPUpdate
            }
        });
        EXPMultipField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String old, String newval) {
                Postac postac = konto.PobierzPostac(CharacterList.getSelectionModel().getSelectedItem());
                Build build = postac.PobierzBuild(ConfigChoice.getValue());
                if(build!=null&&checkIfDigitAndCorrect(EXPMultipField))
                    build.EXPMultip= Integer.parseInt(newval);
                //schedule CPUpdate
            }
        });

        ClassNameField.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
               @Override
               public void changed(ObservableValue<? extends String> observableValue, String old, String newval) {
                   Postac postac = konto.PobierzPostac(CharacterList.getSelectionModel().getSelectedItem());
                   if(newval!= postac.getClassName()&&newval!=null){
                       postac.setClassName(ClassNameField.getValue());
                   }
               }
            }
        );

    }

    boolean checkIfDigitAndCorrect(TextField pole){
        try{
            Integer.parseInt(pole.getText());
            return true;
        }
        catch(NumberFormatException e){
            Platform.runLater(()-> {pole.setText("0");});

            return false;
        }
    }

    public void AddCharacter(ActionEvent actionEvent) {
        Optional<String> wynik = new GiveNameDialog("postaci:").showAndWait();
        if(wynik.isPresent()){
            Postac postac = konto.DodajPostac(new Postac());
            postac.setIGN(wynik.get());
            CharacterList.getItems().add(wynik.get());
        }
    }
    private void InitiateClassChoice() throws NoActiveConnectionException, SQLException{
        if(ClassNameField.getSelectionModel().getSelectedItem()!=null){
            String klasa = ClassNameField.getSelectionModel().getSelectedItem();
            Connection con = sqlboss.getActiveConnection();
            Statement query = con.createStatement();
            ResultSet rs = query.executeQuery("Select Classes.CharacterName from Classes where ClassName=\""+klasa+"\"");
            String postac = rs.getString("CharacterName");
            rs = query.executeQuery("Select Classes.ClassName from Classes where CharacterName=\""+postac+"\"");
            while(rs.next()){
                ClassNameField.getItems().add(rs.getString("ClassName"));
            }
        }
        else{
            Connection con = sqlboss.getActiveConnection();
            Statement query = con.createStatement();
            ResultSet rs = query.executeQuery("Select Classes.ClassName from Classes");
            while(rs.next()){{
                ClassNameField.getItems().add(rs.getString("ClassName"));
            }}
        }
    }
}

