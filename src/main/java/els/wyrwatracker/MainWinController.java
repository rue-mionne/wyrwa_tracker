//TODO: as much as I feared doing so... I REALLY need to split this class into smaller classes with clunky constructors, problems with navigating and making changes increase

package els.wyrwatracker;

import els.data.*;
import els.sqliteIO.NoActiveConnectionException;
import els.sqliteIO.SQLiteConnector;
import els.surgeons.*;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
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
import java.util.Map;
import java.util.Optional;

import els.data.*;
import els.mediators.*;
import javafx.util.Callback;
import javafx.util.converter.IntegerStringConverter;

public class MainWinController {

    SQLiteConnector sqlboss;


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
    private Database baza;
    private Account konto;

    ArrayList<ISurgeon> listaChirurgow = new ArrayList<>();
    CharacterSurgeon chirurgTestowy;
    QuestGenerator questGenerator;

    @FXML
    private ScrollPane MainClassWin;
    @FXML
    private ScrollPane QuestsForCharacter;
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

    public void OdbierzBaze(Database base){
        this.baza = base;
        this.konto = base.konto;
    }


    public void AddBuildConfig(ActionEvent actionEvent) {
        Postac postac = konto.PobierzPostac(CharacterList.getSelectionModel().getSelectedItem());
        Optional<String> wynik = new GiveNameDialog("konfiguracji").showAndWait();
        if(wynik.isPresent()) {
            String nazwaBuildu = wynik.get();
            Build build = new Build(nazwaBuildu, 0, 0, 0, 0, postac.getID());
            postac.PobierzDaneBuildow().AddNewBuildSet(build);
            olistBuilds.add(nazwaBuildu);
            ConfigChoice.setItems(olistBuilds);
            BuildSurgeon chirurg= new BuildSurgeon(baza);
            try {
                chirurg.scheduleInsert(build);
                chirurg.proceed();
            }
            catch(NoActiveConnectionException e){

            }
            catch(SQLException e){
                System.err.println(".....");
                System.err.println(e.getMessage());
            }
            ConfigChoice.getSelectionModel().selectLast();
        }
    }

    public void PostaciLoadUnload(Event event) throws NoActiveConnectionException, SQLException{
        if(konto!=null){
            if(PostaciTab.isSelected()&&!konto.pobierzListePostaci().isEmpty()){
                ObservableList<String> olistPostaci = FXCollections.observableArrayList(konto.pobierzListePostaci());
                CharacterList.setItems(olistPostaci);
                CharacterList.getSelectionModel().select(0);
                CharacterList.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent keyEvent) {
                        if(keyEvent.getCode()==KeyCode.DELETE&&!CharacterList.getSelectionModel().getSelectedItem().equals("Account")){
                            Postac postac=konto.PobierzPostac(CharacterList.getSelectionModel().getSelectedItem());
                            konto.pobierzListePostaci().remove(postac);
                            CharacterList.getItems().remove(CharacterList.getSelectionModel().getSelectedItem());
                            chirurgTestowy.scheduleDelete(postac);

                            CharacterList.getSelectionModel().select(0);
                            try {
                                ZaladujPostac(konto.PobierzPostac(0));
                            }
                            catch(SQLException e){

                            }
                            catch(NoActiveConnectionException e){

                            }
                        }
                    }
                });
                chirurgTestowy = new CharacterSurgeon(baza);
                listaChirurgow.add(chirurgTestowy);
                ZaladujPostac(konto.PobierzPostac(0));

                try {
                    questGenerator = new QuestGenerator(baza);
                }
                catch(NoActiveConnectionException e){
                    System.err.println(e.getMessage());
                }
                catch(SQLException e){
                    System.err.println(e.getMessage());
                }
                ConfigChoice.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observableValue, String number, String t1) {
                        ZaładujBuild(ConfigChoice.getValue());
                    }
                }) ;
                InicjujHandleryPol();
                InitiateQuestForChar();
            }

        }

            if(!PostaciTab.isSelected()){
                chirurgTestowy.proceed();
            }
            else {
              /*  RegionChoice.getSelectionModel().select(0);
                RegionChoice.setValue(RegionChoice.getSelectionModel().getSelectedItem());
                QuestTypeChoice.getSelectionModel().select(0);
                QuestTypeChoice.setValue(QuestTypeChoice.getSelectionModel().getSelectedItem());*/
                RegionChoice.getSelectionModel().select("All");
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

    void ZaladujPostac(Postac postac) throws NoActiveConnectionException, SQLException{
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
            olistBuilds = FXCollections.observableArrayList(listaBuildow);
            EstCPField.setText("0");
            EDMultipField.setText("0");
            EXPMultipField.setText("0");
            IDMultipField.setText("0");
            ConfigChoice.getItems().clear();
        }
        chirurgTestowy.scheduleUpdate(postac);

    }

    public void WybierzPostac(MouseEvent mouseEvent) throws NoActiveConnectionException, SQLException{
        String nazwaPostaci = CharacterList.getSelectionModel().getSelectedItem();
        ZaladujPostac(konto.PobierzPostac(nazwaPostaci));
        FillCharQuestsTab();
    }

    public void InicjujHandleryPol() {

        CharacterNameField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String old, String newval) {
                if(old.equals(CharacterList.getSelectionModel().getSelectedItem())) {
                    String staraNazwaPostaci = CharacterList.getSelectionModel().getSelectedItem();
                    Postac postac = konto.PobierzPostac(staraNazwaPostaci);
                    postac.setIGN(newval);
                    CharacterList.getItems().set((postac.getID() - 1), newval);

                }
            }
        });

        /*ClassNameField.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                Postac postac = konto.PobierzPostac(CharacterList.getSelectionModel().getSelectedItem());
                postac.setClassName(t1);
            }
        });*/

        EstCPField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String old, String newval) {
                Postac postac = konto.PobierzPostac(CharacterList.getSelectionModel().getSelectedItem());
                Build build = postac.PobierzBuild(ConfigChoice.getValue());
                if(build!=null&&checkIfDigitAndCorrect(EstCPField))
                    build.EstCP= Integer.parseInt(newval);
            }
        });

        EDMultipField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String old, String newval) {
                Postac postac = konto.PobierzPostac(CharacterList.getSelectionModel().getSelectedItem());
                Build build = postac.PobierzBuild(ConfigChoice.getValue());
                if(build!=null&&checkIfDigitAndCorrect(EDMultipField))
                    build.EDMultip= Integer.parseInt(newval);
            }
        });

        IDMultipField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String old, String newval) {
                Postac postac = konto.PobierzPostac(CharacterList.getSelectionModel().getSelectedItem());
                Build build = postac.PobierzBuild(ConfigChoice.getValue());
                if(build!=null&&checkIfDigitAndCorrect(IDMultipField))
                    build.IDMultip= Integer.parseInt(newval);
            }
        });

        EDMultipField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String old, String newval) {
                Postac postac = konto.PobierzPostac(CharacterList.getSelectionModel().getSelectedItem());
                Build build = postac.PobierzBuild(ConfigChoice.getValue());
                if(build!=null&&checkIfDigitAndCorrect(EDMultipField))
                    build.EDMultip= Integer.parseInt(newval);
            }
        });
        EXPMultipField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String old, String newval) {
                Postac postac = konto.PobierzPostac(CharacterList.getSelectionModel().getSelectedItem());
                Build build = postac.PobierzBuild(ConfigChoice.getValue());
                if(build!=null&&checkIfDigitAndCorrect(EXPMultipField))
                    build.EXPMultip= Integer.parseInt(newval);
            }
        });

        ClassNameField.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
               @Override
               public void changed(ObservableValue<? extends String> observableValue, String old, String newval) {
                   Postac postac = konto.PobierzPostac(CharacterList.getSelectionModel().getSelectedItem());
                   if(newval!= postac.getClassName()&&newval!=null) {
                       postac.setClassName(ClassNameField.getValue());
                       try {
                          String polecenie ="Select NameTable.CharacterName from NameTable left join Classes on Classes.CharacterName=NameTable.ID WHERE Classes.ClassName like '"+ClassNameField.getValue()+"';";
                          Connection con = sqlboss.getActiveConnection();
                          Statement query = con.createStatement();
                          ResultSet rs = query.executeQuery(polecenie);
                           String charname = rs.getString("CharacterName");
                           postac.setCharacterName(charname);

                           if (old == null) {
                               ClassNameField.getItems().clear();
                               ClassNameField.getSelectionModel().select(newval);

                               questGenerator.scheduleInsert(postac);
                           }} catch(NoActiveConnectionException e){

                           } catch(SQLException e){
                               System.err.println(e.getMessage());

                           }

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

    public void AddCharacter(ActionEvent actionEvent) throws NoActiveConnectionException,SQLException{
        Optional<String> wynik = new GiveNameDialog("postaci:").showAndWait();
        if(wynik.isPresent()){
            Postac postac = konto.DodajPostac(new Postac());
            postac.setIGN(wynik.get());
            CharacterList.getItems().add(wynik.get());
            chirurgTestowy.scheduleInsert(postac);
            chirurgTestowy.proceed();
            questGenerator.scheduleInsert(postac);
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
            ItemSQLMediator.loadInventory(postac,sqlboss,baza.bazaPrzedmiotow);
        }
        Ekwipunek.setItems(null);
        if(!ekwipunek.getListaPrzedmiotow().isEmpty()){
            ObservableList<Item> sledzEq = FXCollections.observableArrayList(ekwipunek.getListaPrzedmiotow());
            Ekwipunek.setItems(sledzEq);

            Ilosc.setEditable(true);
            Ilosc.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

            Ilosc.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Item, Integer>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<Item, Integer> itemIntegerCellEditEvent) {
                    itemIntegerCellEditEvent.getRowValue().setAmount(itemIntegerCellEditEvent.getNewValue());
                    ekwipunek.getEditModeList().set(ekwipunek.getListaPrzedmiotow().indexOf(itemIntegerCellEditEvent.getRowValue()),1);
                }
            });
            Wartosc.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
            Wartosc.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Item, Integer>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<Item, Integer> itemIntegerCellEditEvent) {
                    itemIntegerCellEditEvent.getRowValue().setSalePrice(itemIntegerCellEditEvent.getNewValue());
                    ekwipunek.getEditModeList().set(ekwipunek.getListaPrzedmiotow().indexOf(itemIntegerCellEditEvent.getRowValue()),1);
                }
            });
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

        }


    }

    public void ShowQuestsForCharacter(ActionEvent actionEvent) {
        MainClassWin.setVisible(false);
        MainClassWin.setDisable(true);
        QuestsForCharacter.setVisible(true);
        QuestsForCharacter.setDisable(false);
    }

    public void ReturnToCharPrev(ActionEvent e){
        QuestsForCharacter.setDisable(true);
        QuestsForCharacter.setVisible(false);
        MainClassWin.setVisible(true);
        MainClassWin.setDisable(false);
    }
    @FXML
    ChoiceBox<String> QuestTypeChoice;
    @FXML
    ChoiceBox<String> RegionChoice;
    @FXML
    TableView<Quest> ShowCharQuest;
    @FXML
    TableColumn<Quest,String> QuestNameChar;
    @FXML
    TableColumn<Quest,Integer> CompletionCountChar;
    ObservableList<Quest> misjePostaci;
    boolean firstTime=true;
    public void InitiateQuestForChar() throws NoActiveConnectionException, SQLException{
        InitiateCharQuestsHandlers();
        InitiateQuestTypeChoice();
        InitiateRegionChoice();
    }

    public void InitiateQuestTypeChoice(){
        ArrayList<String> choiceList = new ArrayList<>();
        choiceList.add("All");
        choiceList.add("Drops");
        choiceList.add("Daily");
        choiceList.add("Weekly");
        choiceList.add("Epic");
        QuestTypeChoice.setItems(FXCollections.observableArrayList(choiceList));
        QuestTypeChoice.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(RegionChoice.getSelectionModel().getSelectedItem()!=null)
                    FillCharQuestsTab();
            }
        });
    }

    public void InitiateRegionChoice() throws NoActiveConnectionException, SQLException{
        Connection con = sqlboss.getActiveConnection();
        Statement query = con.createStatement();
        ResultSet rs = query.executeQuery("Select distinct Region from Dungeons");
        ArrayList<String> choiceList = new ArrayList<>();
        choiceList.add("All");
        while(rs.next()) {
            choiceList.add(rs.getString("Region"));
        }
        RegionChoice.getSelectionModel().select("All");
        RegionChoice.setItems(FXCollections.observableArrayList(choiceList));
        RegionChoice.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(RegionChoice.getSelectionModel().getSelectedItem()==null)
                    RegionChoice.getSelectionModel().select("All");
                FillCharQuestsTab();
            }
        });
    }

    public void InitiateCharQuestsHandlers(){
        ShowCharQuest.setEditable(true);
        CompletionCountChar.setEditable(true);
        ArrayList<Quest> tempMisje = new ArrayList<>();
        misjePostaci = FXCollections.observableArrayList(tempMisje);
        ShowCharQuest.setItems(misjePostaci);
        FillCharQuestsTab();
        QuestNameChar.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Quest, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Quest, String> questStringCellDataFeatures) {
                return new ReadOnlyObjectWrapper<>(questStringCellDataFeatures.getValue().getQuestName());
            }
        });

        CompletionCountChar.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Quest, Integer>, ObservableValue<Integer>>() {
            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Quest, Integer> questIntegerCellDataFeatures) {
                return new ReadOnlyObjectWrapper<>(questIntegerCellDataFeatures.getValue().getCompletionCount());
            }
        });

        CompletionCountChar.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        CompletionCountChar.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Quest, Integer>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Quest, Integer> questIntegerCellEditEvent) {
                Quest edytowane = questIntegerCellEditEvent.getRowValue();
                if(questIntegerCellEditEvent.getNewValue()> edytowane.getMaxCompletionCount()){
                    edytowane.setCompletionCount(edytowane.getMaxCompletionCount());
                }
                else{
                    edytowane.setCompletionCount(questIntegerCellEditEvent.getNewValue());
                }
                try{
                    Connection con = sqlboss.getActiveConnection();
                    Statement query = con.createStatement();
                    int charID = konto.PobierzPostac(CharacterList.getSelectionModel().getSelectedItem()).getID();
                    String polecenie;

                    //TODO:shared dungeon count?
                    //TODO: completion date if daily or weekly might need to write a separate function for all the cases at this rate

                    if(edytowane.getCompletionCount()== edytowane.getMaxCompletionCount())
                        polecenie = "Update QuestCompletion set Status = 'COMPLETED', CompletionCount="+edytowane.getCompletionCount()+" where QuestID ="+edytowane.getID()+" and CharacterID="+charID+";";
                    else
                        polecenie = "Update QuestCompletion set CompletionCount="+edytowane.getCompletionCount()+" where QuestID ="+edytowane.getID()+" and CharacterID="+charID+";";
                    System.err.println(polecenie);
                    query.execute(polecenie);
                }
                catch(NoActiveConnectionException|SQLException e){
                    e.getMessage();
                }
                CheckForNextQuest(edytowane.getID());
                FillCharQuestsTab();
            }
        });
    }

    public void CheckForNextQuest(int QuestID){
        try {
            Connection con = sqlboss.getActiveConnection();
            Statement query = con.createStatement();
            String polecenie = "Select NextQuest from QuestData where ID=" +QuestID+";";
            ResultSet rs= query.executeQuery(polecenie);
            int next = rs.getInt("NextQuest");
            if(next!=0){
                int charID = konto.PobierzPostac(CharacterList.getSelectionModel().getSelectedItem()).getID();
                polecenie = "Update QuestCompletion set Status = 'ACTIVE' where CharacterID=" +charID+" and QuestID=" +next+";";
                System.err.println(polecenie);
                query.execute(polecenie);
            }
        }
        catch(NoActiveConnectionException | SQLException e){
            e.getMessage();
        }
    }

    public void FillCharQuestsTab() {
        try {
            Connection con = sqlboss.getActiveConnection();
            Statement query = con.createStatement();
            if (firstTime) {
                firstTime=false;
                Postac postac = konto.PobierzPostac(CharacterList.getSelectionModel().getSelectedItem());
                String polecenie = "SELECT QuestData.QuestName, QuestCompletion.CompletionCount, QuestData.MaxCompletionCount, QuestData.ID FROM QuestCompletion\n" +
                        "JOIN QuestData on QuestData.ID=QuestCompletion.QuestID\n" +
                        "WHERE QuestCompletion.CharacterID="+postac.getID()+" and QuestCompletion.Status like 'ACTIVE';";
                ResultSet rs = query.executeQuery(polecenie);
                while (rs.next()) {
                    Quest temp = new Quest();
                    temp.setQuestName(rs.getString("QuestName"));
                    temp.setCompletionCount(rs.getInt("CompletionCount"));
                    temp.setID(rs.getInt("ID"));
                    if(rs.getInt("MaxCompletionCount")==0)
                        temp.setMaxCompletionCount(1);
                    else
                        temp.setMaxCompletionCount(rs.getInt("MaxCompletionCount"));
                    if(temp.getCompletionCount()!=temp.getMaxCompletionCount())
                        misjePostaci.add(temp);
                }
            }
            else{
                misjePostaci.clear();
                int ID = konto.PobierzPostac(CharacterList.getSelectionModel().getSelectedItem()).getID();
                String typeChoice = "";
                String regionChoice = "";
                if(!ConvertQuestTypeToEnum().equals("ALL"))
                    typeChoice =  "and QuestData.CompletionType='" + ConvertQuestTypeToEnum()+"' ";
                if(RegionChoice!=null){
                        if (!RegionChoice.getSelectionModel().getSelectedItem().equals("All"))
                            regionChoice = "and Dungeons.Region like '" + RegionChoice.getValue() + "' ";

                }
                String polecenie = "SELECT DISTINCT QuestData.QuestName, QuestCompletion.CompletionCount, QuestData.MaxCompletionCount, QuestData.ID FROM QuestCompletion\n" +
                            "JOIN QuestData on QuestData.ID=QuestCompletion.QuestID\n" +
                            "JOIN QuestDungeonData on QuestData.ID=QuestDungeonData.QuestID\n" +
                            "JOIN Dungeons on Dungeons.ID=QuestDungeonData.DungeonID\n" +
                            "WHERE QuestCompletion.CharacterID=" + ID + " and QuestCompletion.Status like 'ACTIVE' " + regionChoice+ typeChoice+";";

                ResultSet rs = query.executeQuery(polecenie);
                while (rs.next()) {
                    Quest temp = new Quest();
                    temp.setQuestName(rs.getString("QuestName"));
                    temp.setCompletionCount(rs.getInt("CompletionCount"));
                    temp.setID(rs.getInt("ID"));
                    if(rs.getInt("MaxCompletionCount")==0)
                        temp.setMaxCompletionCount(1);
                    else
                        temp.setMaxCompletionCount(rs.getInt("MaxCompletionCount"));
                    if(temp.getCompletionCount()!=temp.getMaxCompletionCount())
                        misjePostaci.add(temp);
                }
            }


        }
        catch (NoActiveConnectionException | SQLException e){
            System.err.println(e.getMessage());
        }
    }

    private String ConvertQuestTypeToEnum(){
        String choice = QuestTypeChoice.getSelectionModel().getSelectedItem();

        if(choice==null){
            return "ALL";
        }
        else if(choice.equals("Drops")){
            return "REPEAT";
        }
        else if(choice.equals("Daily")){
            return "DAILY";
        }
        else if(choice.equals("Weekly")){
            return "WEEKLY";
        }
        else if(choice.equals("Epic")){
            return "ONETIME";
        }
        else{
            return "ALL";
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
    private TableView ListaMisji;
    @FXML
    public TableColumn RodzajMisji;
    @FXML
    public TableColumn NazwaMisji;
    @FXML
    public TableView ListaPrzedmiotow;
    @FXML
    public TableColumn NazwaDropu;
    @FXML
    public TableColumn PrzecIlosc;

    @FXML
    private void InitiateDungeonView(){
        try {
            if(baza.drzewoPlansz.getListaRegionow().isEmpty())
                DungeonSQLMediator.loadDungeonList(baza.drzewoPlansz, baza);
            if(ListaPlansz.getRoot()==null) {
                InitiateTreeView();
                //import data of first dungeon

            }
        }
        catch(SQLException e){
            System.err.println("Literówka");
            System.err.println(e.getMessage());
        }
        catch(NoActiveConnectionException e){
            System.err.println("Boss umarł");
        }
    }

    private void InitiateTreeView(){
        TreeItem<String> root = new TreeItem<String>("Dungeons");
        root.setExpanded(true);
        for(Region region : baza.drzewoPlansz.getListaRegionow()){
            TreeItem<String> newRegion = new TreeItem<String>(region.getName());
            root.getChildren().add(newRegion);
            for(String plansza : region.getDungeonList()){
                newRegion.getChildren().add(new TreeItem<String>(plansza));
            }

        }
        ListaPlansz.setRoot(root);
        ListaPlansz.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<String>>() {
            @Override
            public void changed(ObservableValue<? extends TreeItem<String>> observableValue, TreeItem<String> stringTreeItem, TreeItem<String> t1) {
                if(t1.isLeaf()){
                    NazwaPlanszy.setText(t1.getValue());
                    Dungeon plansza = baza.drzewoPlansz.getRegion(t1.getParent().getValue()).getDungeon(t1.getValue());
                    BaseEXPValue.setText(String.valueOf(plansza.getBaseEXP()));
                    BaseEDValue.setText(String.valueOf(plansza.getBaseED()));
                    MinCP.setText(String.valueOf(plansza.getMinCP()));
                    if(plansza.getTablicaDropow().getDropy().isEmpty())
                        plansza.inicjujTabliceDropow();
                    InitiateQuestPrev(plansza);
                    InitiateItemPrev(plansza);
                }
            }
        });
    }

    private void InitiateQuestPrev(Dungeon dungeon){
        ListaMisji.setItems(FXCollections.observableArrayList(dungeon.getTablicaZadan().getQuestTable()));
        RodzajMisji.setCellValueFactory(new Callback<TableColumn.CellDataFeatures, ObservableValue>() {
            @Override
            public ObservableValue call(TableColumn.CellDataFeatures cellDataFeatures) {
                Quest zad = (Quest)cellDataFeatures.getValue();
                return new ReadOnlyObjectWrapper(zad.getCompletionType());
            }
        });

        NazwaMisji.setCellValueFactory(new Callback<TableColumn.CellDataFeatures, ObservableValue>() {
            @Override
            public ObservableValue call(TableColumn.CellDataFeatures cellDataFeatures) {
                Quest zad = (Quest)cellDataFeatures.getValue();
                return new ReadOnlyObjectWrapper(zad.getQuestName());
            }
        });

    }

    private void InitiateItemPrev(Dungeon dungeon){
        ListaPrzedmiotow.setItems(FXCollections.observableArrayList(dungeon.getTablicaDropow().getDropy()));
        NazwaDropu.setCellValueFactory(new Callback<TableColumn.CellDataFeatures, ObservableValue>() {
            @Override
            public ObservableValue call(TableColumn.CellDataFeatures cellDataFeatures) {
                Item drop = (Item)cellDataFeatures.getValue();
                return new ReadOnlyObjectWrapper(drop.getName().getValue());
            }
        });

        PrzecIlosc.setCellValueFactory(new Callback<TableColumn.CellDataFeatures, ObservableValue>() {
            @Override
            public ObservableValue call(TableColumn.CellDataFeatures cellDataFeatures) {
                Item drop = (Item) cellDataFeatures.getValue();
                return new ReadOnlyObjectWrapper(drop.getRate().getValue());
            }
        });
    }

    //
    //
    ////
    ////Zadania Tab
    ////
    //
    //

    @FXML
    TreeView<String> ZadaniaRegiony;
    @FXML
    ListView<String> ZadaniaNagrody;
    @FXML
    ListView<String> ZadaniaPostaciActive;

    public void InitiateZadaniaView(){
        try {
            if (baza.drzewoPlansz.getListaRegionow().isEmpty()) {
                DungeonSQLMediator.loadDungeonList(baza.drzewoPlansz, baza);
            }
            QuestSQLMediator.loadAllQuestData(baza.bazaZadan,sqlboss);
            if (ZadaniaRegiony.getRoot() == null){
                InitiateTreeViewZadania();

            }
        }
        catch(NoActiveConnectionException e){
            System.out.println("Zadania no con");
        }
        catch(SQLException e){
            System.out.println("Wywaliło SQL: zadania tab");
        }
    }

    public void InitiateTreeViewZadania() throws SQLException,NoActiveConnectionException{
        TreeItem<String> root = new TreeItem<String>("Regiony");
        root.setExpanded(true);
        for(Region region : baza.drzewoPlansz.getListaRegionow()){
            TreeItem<String> newRegion = new TreeItem<String>(region.getName());
            root.getChildren().add(newRegion);
            for(String plansza : region.getDungeonList()){
                TreeItem<String> newplansza = new TreeItem<String>(plansza);
                newRegion.getChildren().add(newplansza);
                Dungeon dung = region.getDungeon(plansza);
                if(dung.getTablicaZadan().getQuestTable().isEmpty()){
                    DungeonSQLMediator.getQuestData(dung,baza);
                }
                for(Quest zadanie:dung.getTablicaZadan().getQuestTable()){
                    newplansza.getChildren().add(new TreeItem<String>(zadanie.getQuestName()));
                }
            }

        }
        ZadaniaRegiony.setRoot(root);
        ZadaniaRegiony.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<String>>() {
            @Override
            public void changed(ObservableValue<? extends TreeItem<String>> observableValue, TreeItem<String> stringTreeItem, TreeItem<String> t1) {
                if(t1.isLeaf()){
                    RewardTable nagrody = baza.bazaZadan.getQuest(t1.getValue()).getListaPrzedmiotow();
                    InitiateZadaniaNagrody(nagrody);
                    InitiateZadaniaPostaciActive(baza.bazaZadan.getQuest(t1.getValue()).getID());
                }
            }
        });
    }

    public void InitiateZadaniaNagrody(RewardTable rewards){
        ZadaniaNagrody.setItems(FXCollections.observableArrayList(rewards.getRewardList()));
    }

    public void InitiateZadaniaPostaciActive(int QuestID){
        ArrayList<String> listaPostaci=new ArrayList<>();
        try{
            Connection con = sqlboss.getActiveConnection();
            Statement query = con.createStatement();
            String polecenie = "Select Characters.Name from Characters\n" +
                    "INNER JOIN QuestCompletion on Characters.ID=QuestCompletion.CharacterID\n" +
                    "where QuestCompletion.QuestID = "+QuestID+" and QuestCompletion.Status like 'ACTIVE';";
            ResultSet rs = query.executeQuery(polecenie);
            while(rs.next()){
                listaPostaci.add(rs.getString("Name"));
            }
            ZadaniaPostaciActive.setItems(FXCollections.observableArrayList(listaPostaci));
        }
        catch(NoActiveConnectionException|SQLException e){
            System.err.println(e.getMessage());
        }
    }

    //
    //
    ////
    ////Przedmioty Tab
    ////
    //
    //
    @FXML
    ComboBox<String> NazwaPrzedmiotu;
    @FXML
    ListView<String> ListaEkwipunkow;
    @FXML
    TextField WartoscPrzedmiotu;
    @FXML
    CheckBox CheckSellable;
    @FXML
    CheckBox CheckShareable;
    @FXML
    ComboBox<String> CharacterChoiceItemsCombo;
    @FXML
    Spinner<Integer> ItemAmountSpinner;
    @FXML
    Button InventoryItemAddButton;

    ObservableList<String> dropDown;
    ArrayList<String> listaPrzedmiotow;
    Item wybrany;
    Item lastWybrany;

    public void InitiatePrzedmiotyView(){
        try {
            listaPrzedmiotow = ItemSQLMediator.loadItemList(baza);
            InitiateNazwaPrzedmiotu();
            InitiateCharacterChoiceCombo();
            InitiateAmountSpinner();
            InitiateAddItemButton();
        }
        catch(NoActiveConnectionException|SQLException e){
            e.getMessage();
        }
    }

    public void InitiateNazwaPrzedmiotu(){
        String filter = NazwaPrzedmiotu.getEditor().getText();
        dropDown = FXCollections.observableArrayList((ArrayList<String>) listaPrzedmiotow.clone());
 //       FiltrujPrzedmioty(dropDown, filter);
        NazwaPrzedmiotu.setItems(dropDown);
 /*       NazwaPrzedmiotu.getEditor().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                String filter = NazwaPrzedmiotu.getEditor().getText();
                FiltrujPrzedmioty(dropDown, filter);
            }
        });*/
        NazwaPrzedmiotu.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                try {
                    int ID = 0;
                    if(t1!=null&&!t1.isEmpty())
                        ID = ItemSQLMediator.getItemID(t1, baza);
                    wybrany=new Item();
                    if(ID!=0)
                         wybrany = baza.bazaPrzedmiotow.PobierzPrzedmiot(ID);
                    if(wybrany.getID()!=0){
                        CheckSellable.setSelected(wybrany.isSellable());
                        CheckShareable.setSelected(wybrany.isShareable());
                        WartoscPrzedmiotu.setText(wybrany.getSalePrice().getValue().toString());
                        ArrayList<Postac> ownerList = ItemSQLMediator.getOwners(wybrany.getID(),baza);
                        ArrayList<String> nameList = new ArrayList<>();
                        for(Postac owner: ownerList){
                            nameList.add(owner.getIGN());
                        }
                        ListaEkwipunkow.setItems(FXCollections.observableArrayList(nameList));
                        lastWybrany=wybrany;
                    }
                }
                catch(NoActiveConnectionException|SQLException e){
                    e.getMessage();
                }
            }
        });

    }

    public void InitiateCharacterChoiceCombo(){
        ObservableList<String> listaPostaci = FXCollections.observableArrayList(konto.pobierzListePostaci());
        CharacterChoiceItemsCombo.setItems(listaPostaci);
    }

    public void InitiateAmountSpinner(){
        ItemAmountSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,99999));
    }

    public void InitiateAddItemButton(){
        InventoryItemAddButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Postac postac = konto.PobierzPostac(CharacterChoiceItemsCombo.getSelectionModel().getSelectedItem());
                Inventory eq = postac.PobierzListePrzedmiotow();
                if(eq.pobierzPrzedmiot(lastWybrany.getID())!=null){
                    eq.pobierzPrzedmiot(lastWybrany.getID()).setAmount(ItemAmountSpinner.getValue()+eq.pobierzPrzedmiot(eq.getLastInserted()).getAmount().getValue());
                    eq.getEditModeList().set(eq.getListaPrzedmiotow().indexOf(eq.pobierzPrzedmiot(lastWybrany.getID())),1);
                }
                else{
                    eq.dodajPrzedmiot(baza.bazaPrzedmiotow.PobierzPrzedmiot(lastWybrany.getID()));
                    eq.pobierzPrzedmiot(eq.getLastInserted()).setAmount(ItemAmountSpinner.getValue());
                    int index = eq.getListaPrzedmiotow().indexOf(eq.pobierzPrzedmiot(eq.getLastInserted()));
                    eq.getEditModeList().set(index,1);
                }
                InventorySurgeon inventorySurgeon = new InventorySurgeon(baza);
                try{
                    inventorySurgeon.scheduleUpdate(postac.PobierzListePrzedmiotow());
                    inventorySurgeon.proceed();
                }
                catch(NoActiveConnectionException|SQLException e){
                    e.getMessage();
                }
            }
        });
    }

    public void FiltrujPrzedmioty(ObservableList<String> listaPzedmiotow, String filter){
        listaPzedmiotow.removeIf((nazwa)->!nazwa.toLowerCase().contains(filter.toLowerCase()));

    }



    public ArrayList<ISurgeon> getListaChirurgow() {
        return listaChirurgow;
    }


}



