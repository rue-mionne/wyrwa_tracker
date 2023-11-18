package els.wyrwatracker;

import els.data.Postac;
import els.sqliteIO.NoActiveConnectionException;
import els.sqliteIO.SQLiteConnector;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Optional;

import els.data.*;
import els.mediators.*;
import javafx.util.Callback;
import javafx.util.converter.IntegerStringConverter;

public class MainWinController {
    SQLiteConnector sqlboss;

    EnumSet<Postac.SaveFlags> flagiZapisu;
    ObservableList<String> olistBuilds;
    @FXML
    private Tab PostaciTab;
    @FXML
    private Tab CeleTab;

    @FXML
    private TableView<Item> Ekwipunek;
    @FXML
    private TableColumn<Item,String> Przedmiot;
    @FXML
    private TableColumn<Item,Integer> Ilosc;
    @FXML
    private TableColumn<Item,Integer> Wartosc;


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
        flagiZapisu = EnumSet.of(Postac.SaveFlags.IGN);
        ConfigChoice.getSelectionModel().clearSelection();
        CharacterNameField.setText(postac.getIGN());
        ClassNameField.getSelectionModel().select(postac.getClassName());
        String selected = ClassNameField.getValue();
        ClassNameField.getItems().clear();
        ClassNameField.getSelectionModel().select(selected);
        try{
            InitiateClassChoice();
            InicjujEkwipunek(postac);
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
        ZaladujPostac(konto.PobierzPostac(nazwaPostaci));
    }

    public void InicjujHandleryPol(){

        CharacterNameField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String old, String newval) {
                if(old.equals(CharacterList.getSelectionModel().getSelectedItem())) {
                    String staraNazwaPostaci = CharacterList.getSelectionModel().getSelectedItem();
                    Postac postac = konto.PobierzPostac(staraNazwaPostaci);
                    postac.setIGN(newval);
                    CharacterList.getItems().set((postac.getID() - 1), newval);
                    if(newval!=null)
                        flagiZapisu.add(Postac.SaveFlags.IGN);
                }
            }
        });

        ClassNameField.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                Postac postac = konto.PobierzPostac(CharacterList.getSelectionModel().getSelectedItem());
                postac.setClassName(t1);
                if(t1!=null)
                    flagiZapisu.add(Postac.SaveFlags.ClassName);
            }
        });

        EstCPField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String old, String newval) {
                Postac postac = konto.PobierzPostac(CharacterList.getSelectionModel().getSelectedItem());
                Build build = postac.PobierzBuild(ConfigChoice.getValue());
                if(build!=null&&checkIfDigitAndCorrect(EstCPField))
                    build.EstCP= Integer.parseInt(newval);
                if(newval!=null)
                    flagiZapisu.add(Postac.SaveFlags.BuildSet);
            }
        });

        EDMultipField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String old, String newval) {
                Postac postac = konto.PobierzPostac(CharacterList.getSelectionModel().getSelectedItem());
                Build build = postac.PobierzBuild(ConfigChoice.getValue());
                if(build!=null&&checkIfDigitAndCorrect(EDMultipField))
                    build.EDMultip= Integer.parseInt(newval);
                if(newval!=null)
                    flagiZapisu.add(Postac.SaveFlags.BuildSet);
            }
        });

        IDMultipField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String old, String newval) {
                Postac postac = konto.PobierzPostac(CharacterList.getSelectionModel().getSelectedItem());
                Build build = postac.PobierzBuild(ConfigChoice.getValue());
                if(build!=null&&checkIfDigitAndCorrect(IDMultipField))
                    build.IDMultip= Integer.parseInt(newval);
                if(newval!=null)
                    flagiZapisu.add(Postac.SaveFlags.BuildSet);
            }
        });

        EDMultipField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String old, String newval) {
                Postac postac = konto.PobierzPostac(CharacterList.getSelectionModel().getSelectedItem());
                Build build = postac.PobierzBuild(ConfigChoice.getValue());
                if(build!=null&&checkIfDigitAndCorrect(EDMultipField))
                    build.EDMultip= Integer.parseInt(newval);
                if(newval!=null)
                        flagiZapisu.add(Postac.SaveFlags.BuildSet);
            }
        });
        EXPMultipField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String old, String newval) {
                Postac postac = konto.PobierzPostac(CharacterList.getSelectionModel().getSelectedItem());
                Build build = postac.PobierzBuild(ConfigChoice.getValue());
                if(build!=null&&checkIfDigitAndCorrect(EXPMultipField))
                    build.EXPMultip= Integer.parseInt(newval);
                if(newval!=null)
                        flagiZapisu.add(Postac.SaveFlags.BuildSet);
            }
        });

        ClassNameField.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
               @Override
               public void changed(ObservableValue<? extends String> observableValue, String old, String newval) {
                   Postac postac = konto.PobierzPostac(CharacterList.getSelectionModel().getSelectedItem());
                   if(newval!= postac.getClassName()&&newval!=null){
                       postac.setClassName(ClassNameField.getValue());
                   }
                   if(newval!=null)
                       flagiZapisu.add(Postac.SaveFlags.ClassName);
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

    private void InicjujEkwipunek(Postac postac) throws NoActiveConnectionException, SQLException{
        Inventory ekwipunek = postac.PobierzListePrzedmiotow();
        //zapełnij ekwipunek/odśwież ekwipunek
        if(ekwipunek.getListaPrzedmiotow().isEmpty()){
            ItemSQLMediator.loadInventory(postac,sqlboss);
        }
        Ekwipunek.setItems(null);
        if(!ekwipunek.getListaPrzedmiotow().isEmpty()){
            ObservableList<Item> sledzEq = FXCollections.observableArrayList(ekwipunek.getListaPrzedmiotow());
            Ekwipunek.setItems(sledzEq);
            Przedmiot.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Item, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<Item, String> cellDataFeatures) {
                    return cellDataFeatures.getValue().getName();
                }
            });

            Ilosc.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Item, Integer>, ObservableValue<Integer>>() {
                @Override
                public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Item, Integer> itemIntegerCellDataFeatures) {
                    return itemIntegerCellDataFeatures.getValue().getAmount().asObject();
                }
            });

            Wartosc.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Item, Integer>, ObservableValue<Integer>>() {
                @Override
                public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Item, Integer> itemIntegerCellDataFeatures) {
                    return itemIntegerCellDataFeatures.getValue().getSalePrice().asObject();
                }
            });
            /*Ekwipunek.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent keyEvent) {
                    if(keyEvent.getCode()== KeyCode.ENTER){
                        return;
                    }
                    if(Ekwipunek.getEditingCell()==null){
                        if(keyEvent.getCode().isLetterKey()||keyEvent.getCode().isDigitKey()){
                            TablePosition focusedCellPosiotion = Ekwipunek.getFocusModel().getFocusedCell();
                            Ekwipunek.edit(focusedCellPosiotion.getRow(),focusedCellPosiotion.getTableColumn());
                        }
                    }
                }
            });*/
            Ilosc.setEditable(true);
            Ilosc.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

            Ilosc.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Item, Integer>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<Item, Integer> itemIntegerCellEditEvent) {
                    itemIntegerCellEditEvent.getRowValue().setAmount(itemIntegerCellEditEvent.getNewValue());
                }
            });
            Wartosc.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
            Wartosc.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Item, Integer>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<Item, Integer> itemIntegerCellEditEvent) {
                    itemIntegerCellEditEvent.getRowValue().setAmount(itemIntegerCellEditEvent.getNewValue());
                }
            });
        }
    }
    //
    //
    //
    //
    //
    /////
    /////Dungeon Tab
    /////
    //
    //
    //
    //

    @FXML
    private Label NazwaPlanszy;
    @FXML
    private Label BaseEXPValue;
    @FXML
    private Label BaseEDValue;
    @FXML
    private Label MinCP;
    @FXML
    private TreeView<String> ListaPlansz;

    @FXML
    private void InitiateDungeonView(){

    }
}



