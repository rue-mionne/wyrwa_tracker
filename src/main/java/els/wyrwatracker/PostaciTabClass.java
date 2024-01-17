//TODO: as much as I feared doing so... I REALLY need to split this class into smaller classes with clunky constructors, problems with navigating and making changes increase

package els.wyrwatracker;

import els.data.*;
import els.mediators.DungeonSQLMediator;
import els.mediators.ItemSQLMediator;
import els.mediators.QuestSQLMediator;
import els.sqliteIO.NoActiveConnectionException;
import els.sqliteIO.SQLiteConnector;
import els.surgeons.*;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import javafx.util.converter.IntegerStringConverter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;

public class PostaciTabClass {
    Database baza;
    Account konto;
    SQLiteConnector sqlboss;
    PostaciTabPack ctrls;
    ListView<String> CharacterList;
    CharacterSurgeon CharacterAutoSaveSurgeon;
    QuestGenerator questGenerator;
    ChoiceBox<String> ClassNameField;
    ChoiceBox<String> RegionChoice;
    ScrollPane QuestsForCharacter;
    ScrollPane MainClassWin;
    PostaciTabClass(Database baza, PostaciTabPack kontrols){
        this.baza=baza;
        this.konto= baza.konto;
        this.sqlboss= baza.sqlCon;
        ctrls = kontrols;
        CharacterList = ctrls.getCharacterList();
        ClassNameField = ctrls.getClassNameField();
        RegionChoice = ctrls.getRegionChoice();
        QuestsForCharacter = ctrls.getQuestsForCharacter();
        MainClassWin = ctrls.getMainClassWin();
        ctrls.getQuestForCharReturn().setOnAction(this::ReturnToCharPrev);
        ctrls.getPostaciTab().setOnSelectionChanged(this::PostaciLoadUnload);
        CharacterList.setOnMouseClicked(this::WybierzPostac);
        ctrls.getAddConfigButton().setOnAction(this::AddBuildConfig);
        ctrls.getQuestInterfaceButton().setOnAction(this::ShowQuestsForCharacter);
        ctrls.getDodajPostac().setOnAction(this::AddCharacter);
    }

    ObservableList<String> olistBuilds;
    public void AddBuildConfig(ActionEvent actionEvent) {
        Postac postac = konto.PobierzPostac(ctrls.getCharacterList().getSelectionModel().getSelectedItem());
        Optional<String> wynik = new GiveNameDialog("konfiguracji").showAndWait();
        if(wynik.isPresent()) {
            String nazwaBuildu = wynik.get();
            Build build = new Build(0,nazwaBuildu, 0, 0, 0, 0, postac.getID());
            postac.PobierzDaneBuildow().AddNewBuildSet(build);
            olistBuilds.add(nazwaBuildu);
            ctrls.getConfigChoice().setItems(olistBuilds);
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
            ctrls.getConfigChoice().getSelectionModel().selectLast();
        }
    }

    public void PostaciLoadUnload(Event event) {
        try {
            if (konto != null) {
                if (ctrls.getPostaciTab().isSelected() && !konto.pobierzListePostaci().isEmpty()) {
                    ObservableList<String> olistPostaci = FXCollections.observableArrayList(konto.pobierzListePostaci());
                    CharacterList.setItems(olistPostaci);
                    CharacterList.getSelectionModel().select(0);
                    CharacterList.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
                        @Override
                        public void handle(KeyEvent keyEvent) {
                            if (keyEvent.getCode() == KeyCode.DELETE && !CharacterList.getSelectionModel().getSelectedItem().equals("Account")) {
                                Postac postac = konto.PobierzPostac(CharacterList.getSelectionModel().getSelectedItem());
                                konto.pobierzListePostaci().remove(postac);
                                CharacterList.getItems().remove(CharacterList.getSelectionModel().getSelectedItem());
                                CharacterAutoSaveSurgeon.scheduleDelete(postac);

                                CharacterList.getSelectionModel().select(0);
                                try {
                                    ZaladujPostac(konto.PobierzPostac(0));
                                } catch (SQLException e) {

                                } catch (NoActiveConnectionException e) {

                                }
                            }
                        }
                    });
                    CharacterAutoSaveSurgeon = new CharacterSurgeon(baza);
                    ZaladujPostac(konto.PobierzPostac(0));
                    try {
                        questGenerator = new QuestGenerator(baza);
                    } catch (NoActiveConnectionException e) {
                        System.err.println(e.getMessage());
                    } catch (SQLException e) {
                        System.err.println(e.getMessage());
                    }
                    ctrls.getConfigChoice().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
                        @Override
                        public void changed(ObservableValue<? extends String> observableValue, String number, String t1) {
                            ZaładujBuild(ctrls.getConfigChoice().getValue());
                        }
                    });
                    InicjujHandleryPol();
                    InitiateQuestForChar();
                }

            }

            if (!ctrls.getPostaciTab().isSelected()) {
                CharacterAutoSaveSurgeon.proceed();
            } else {
              /*  RegionChoice.getSelectionModel().select(0);
                RegionChoice.setValue(RegionChoice.getSelectionModel().getSelectedItem());
                QuestTypeChoice.getSelectionModel().select(0);
                QuestTypeChoice.setValue(QuestTypeChoice.getSelectionModel().getSelectedItem());*/
                ctrls.getRegionChoice().getSelectionModel().select("All");
            }
        }
        catch(NoActiveConnectionException|SQLException e){
            System.err.println(e.getMessage());
        }
    }

    void ZaładujBuild(String nazwaBuildu){
        Postac postac = konto.PobierzPostac(CharacterList.getSelectionModel().getSelectedItem());
        Build build = postac.PobierzDaneBuildow().getBuild(nazwaBuildu);
        if(build!=null) {
            ctrls.getEstCPField().setText(String.valueOf(build.EstCP));
            ctrls.getEDMultipField().setText(String.valueOf(build.EDMultip));
            ctrls.getEXPMultipField().setText(String.valueOf(build.EXPMultip));
            ctrls.getIDMultipField().setText(String.valueOf(build.IDMultip));
        }
    }

    void ZaladujPostac(Postac postac) throws NoActiveConnectionException, SQLException{
        ctrls.getConfigChoice().getSelectionModel().clearSelection();
        ctrls.getCharacterNameField().setText(postac.getIGN());
        ctrls.getClassNameField().getSelectionModel().select(postac.getClassName());
        String selected = ctrls.getClassNameField().getValue();
        ctrls.getClassNameField().getItems().clear();
        ctrls.getClassNameField().getSelectionModel().select(selected);
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
            ctrls.getConfigChoice().setItems(olistBuilds);
            ctrls.getConfigChoice().getSelectionModel().select(0);
            if(ctrls.getEstCPField().getText().isEmpty()){
                ZaładujBuild(ctrls.getConfigChoice().getSelectionModel().getSelectedItem());
            }
        }
        else{
            olistBuilds = FXCollections.observableArrayList(listaBuildow);
            ctrls.getEstCPField().setText("0");
            ctrls.getEDMultipField().setText("0");
            ctrls.getEXPMultipField().setText("0");
            ctrls.getIDMultipField().setText("0");
            ctrls.getConfigChoice().getItems().clear();
        }
        CharacterAutoSaveSurgeon.scheduleUpdate(postac);

    }

    public void WybierzPostac(MouseEvent mouseEvent){
        try{
        String nazwaPostaci = CharacterList.getSelectionModel().getSelectedItem();
        ZaladujPostac(konto.PobierzPostac(nazwaPostaci));
        FillCharQuestsTab();
        }
        catch(SQLException|NoActiveConnectionException e){
            System.err.println(e.getMessage());
        }

    }

    public void InicjujHandleryPol() {

        ctrls.getCharacterNameField().textProperty().addListener(new ChangeListener<String>() {
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

        ctrls.getEstCPField().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String old, String newval) {
                Postac postac = konto.PobierzPostac(CharacterList.getSelectionModel().getSelectedItem());
                Build build = postac.PobierzBuild(ctrls.getConfigChoice().getValue());
                if(build!=null&&checkIfDigitAndCorrect(ctrls.getEstCPField())) {
                    build.EstCP = Integer.parseInt(newval);
                    try {
                        CharacterAutoSaveSurgeon.getBuildSurgeon().scheduleUpdate(build);
                    } catch (NoActiveConnectionException|SQLException e){
                        System.err.println(e.getMessage());
                    }
                }
            }
        });

        ctrls.getEDMultipField().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String old, String newval) {
                Postac postac = konto.PobierzPostac(CharacterList.getSelectionModel().getSelectedItem());
                Build build = postac.PobierzBuild(ctrls.getConfigChoice().getValue());
                if(build!=null&&checkIfDigitAndCorrect(ctrls.getEDMultipField())){
                    build.EDMultip= Integer.parseInt(newval);
                try {
                    CharacterAutoSaveSurgeon.getBuildSurgeon().scheduleUpdate(build);
                } catch (NoActiveConnectionException|SQLException e){
                    System.err.println(e.getMessage());
                }
                }
            }
        });

        ctrls.getIDMultipField().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String old, String newval) {
                Postac postac = konto.PobierzPostac(CharacterList.getSelectionModel().getSelectedItem());
                Build build = postac.PobierzBuild(ctrls.getConfigChoice().getValue());
                if(build!=null&&checkIfDigitAndCorrect(ctrls.getIDMultipField()))
                    {
                        build.IDMultip = Integer.parseInt(newval);
                        try {
                            CharacterAutoSaveSurgeon.getBuildSurgeon().scheduleUpdate(build);
                        } catch (NoActiveConnectionException|SQLException e){
                            System.err.println(e.getMessage());
                        }
                    }
            }
        });

        ctrls.getEDMultipField().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String old, String newval) {
                Postac postac = konto.PobierzPostac(CharacterList.getSelectionModel().getSelectedItem());
                Build build = postac.PobierzBuild(ctrls.getConfigChoice().getValue());
                if(build!=null&&checkIfDigitAndCorrect(ctrls.getEDMultipField())) {
                    build.EDMultip = Integer.parseInt(newval);
                    try {
                        CharacterAutoSaveSurgeon.getBuildSurgeon().scheduleUpdate(build);
                    } catch (NoActiveConnectionException|SQLException e){
                        System.err.println(e.getMessage());
                    }
                }
            }
        });
        ctrls.getEXPMultipField().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String old, String newval) {
                Postac postac = konto.PobierzPostac(CharacterList.getSelectionModel().getSelectedItem());
                Build build = postac.PobierzBuild(ctrls.getConfigChoice().getValue());
                if(build!=null&&checkIfDigitAndCorrect(ctrls.getEXPMultipField())) {
                    build.EXPMultip = Integer.parseInt(newval);
                    try {
                        CharacterAutoSaveSurgeon.getBuildSurgeon().scheduleUpdate(build);
                    } catch (NoActiveConnectionException|SQLException e){
                        System.err.println(e.getMessage());
                    }
                }
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

    public void AddCharacter(ActionEvent actionEvent){
        Optional<String> wynik = new GiveNameDialog("postaci:").showAndWait();
        if(wynik.isPresent()){
            Postac postac = konto.DodajPostac(new Postac());
            postac.setIGN(wynik.get());
            CharacterList.getItems().add(wynik.get());
            CharacterAutoSaveSurgeon.scheduleInsert(postac);
            try {
                CharacterAutoSaveSurgeon.proceed();
                questGenerator.scheduleInsert(postac);
            }
            catch(NoActiveConnectionException|SQLException e){
                System.err.println(e.getMessage());
            }
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
        ctrls.getEkwipunek().setItems(null);
        if(!ekwipunek.getListaPrzedmiotow().isEmpty()){
            ObservableList<Item> sledzEq = FXCollections.observableArrayList(ekwipunek.getListaPrzedmiotow());
            ctrls.getEkwipunek().setItems(sledzEq);

            ctrls.getIlosc().setEditable(true);
            ctrls.getIlosc().setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

            ctrls.getIlosc().setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Item, Integer>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<Item, Integer> itemIntegerCellEditEvent) {
                    itemIntegerCellEditEvent.getRowValue().setAmount(itemIntegerCellEditEvent.getNewValue());
                    ekwipunek.getEditModeList().set(ekwipunek.getListaPrzedmiotow().indexOf(itemIntegerCellEditEvent.getRowValue()),1);
                }
            });
            ctrls.getWartosc().setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
            ctrls.getWartosc().setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Item, Integer>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<Item, Integer> itemIntegerCellEditEvent) {
                    itemIntegerCellEditEvent.getRowValue().setSalePrice(itemIntegerCellEditEvent.getNewValue());
                    ekwipunek.getEditModeList().set(ekwipunek.getListaPrzedmiotow().indexOf(itemIntegerCellEditEvent.getRowValue()),1);
                }
            });
            ctrls.getPrzedmiot().setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Item, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<Item, String> cellDataFeatures) {
                    return cellDataFeatures.getValue().getName();
                }
            });

            ctrls.getIlosc().setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Item, Integer>, ObservableValue<Integer>>() {
                @Override
                public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Item, Integer> itemIntegerCellDataFeatures) {
                    return itemIntegerCellDataFeatures.getValue().getAmount().asObject();
                }
            });

            ctrls.getWartosc().setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Item, Integer>, ObservableValue<Integer>>() {
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
        ctrls.getQuestTypeChoice().setItems(FXCollections.observableArrayList(choiceList));
        ctrls.getQuestTypeChoice().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
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
        ctrls.getShowCharQuest().setEditable(true);
        ctrls.getCompletionCountChar().setEditable(true);
        ArrayList<Quest> tempMisje = new ArrayList<>();
        misjePostaci = FXCollections.observableArrayList(tempMisje);
        ctrls.getShowCharQuest().setItems(misjePostaci);
        FillCharQuestsTab();
        ctrls.getQuestNameChar().setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Quest, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Quest, String> questStringCellDataFeatures) {
                return new ReadOnlyObjectWrapper<>(questStringCellDataFeatures.getValue().getQuestName());
            }
        });

        ctrls.getCompletionCountChar().setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Quest, Integer>, ObservableValue<Integer>>() {
            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Quest, Integer> questIntegerCellDataFeatures) {
                return new ReadOnlyObjectWrapper<>(questIntegerCellDataFeatures.getValue().getCompletionCount());
            }
        });

        ctrls.getCompletionCountChar().setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        ctrls.getCompletionCountChar().setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Quest, Integer>>() {
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
        String choice = ctrls.getQuestTypeChoice().getSelectionModel().getSelectedItem();

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
}



