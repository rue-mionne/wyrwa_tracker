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

    public Button QuestForCharReturn;
    public Button AddConfigButton;
    public Button QuestInterfaceButton;
    public Button DodajPostac;
    SQLiteConnector sqlboss;

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
    ChoiceBox<String> QuestTypeChoice;
    @FXML
    ChoiceBox<String> RegionChoice;
    @FXML
    TableView<Quest> ShowCharQuest;
    @FXML
    TableColumn<Quest,String> QuestNameChar;
    @FXML
    TableColumn<Quest,Integer> CompletionCountChar;

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
        PostaciTabPack postaciTabPack = new PostaciTabPack(Ekwipunek,Przedmiot,Ilosc,Wartosc,CharacterList, CharacterName, CharacterNameField, ClassIcon, ClassName, ClassNameField, ConfigChoice, EDMultipField, EXPMultipField, EstCP, EstCP1, EstCP11, EstCP111, EstCPField, IDMultipField, PrzegladTab, baza, konto, MainClassWin, QuestsForCharacter,QuestTypeChoice,RegionChoice,ShowCharQuest,QuestNameChar,CompletionCountChar,PostaciTab, QuestForCharReturn);
        postaciTabPack.setDodajPostac(DodajPostac);
        postaciTabPack.setAddConfigButton(AddConfigButton);
        postaciTabPack.setQuestInterfaceButton(QuestInterfaceButton);
        PostaciTabClass postaciTab = new PostaciTabClass(baza, postaciTabPack);
        postaciTab.PostaciLoadUnload(new Event(new EventType<>()));
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



