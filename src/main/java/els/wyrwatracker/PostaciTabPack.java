package els.wyrwatracker;

import els.data.Account;
import els.data.Database;
import els.data.Item;
import els.data.Quest;
import els.surgeons.CharacterSurgeon;
import els.surgeons.ISurgeon;
import els.surgeons.QuestGenerator;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

public class PostaciTabPack {
    private TableView<Item> Ekwipunek;
    private TableColumn<Item,String> Przedmiot;
    private TableColumn<Item,Integer> Ilosc;
    private TableColumn<Item,Integer> Wartosc;
    private ListView<String> CharacterList;
    private Label CharacterName;
    private TextField CharacterNameField;
    private ImageView ClassIcon;
    private Label ClassName;
    private ChoiceBox<String> ClassNameField;
    private ChoiceBox<String> ConfigChoice;
    private TextField EDMultipField;
    private TextField EXPMultipField;
    private Label EstCP;
    private Label EstCP1;
    private Label EstCP11;
    private Label EstCP111;
    private TextField EstCPField;
    private TextField IDMultipField;
    private Tab PrzegladTab;
    private Database baza;
    private Account konto;
    private ScrollPane MainClassWin;
    private ScrollPane QuestsForCharacter;

    private ChoiceBox<String> QuestTypeChoice;
    private ChoiceBox<String> RegionChoice;
    private TableView<Quest> ShowCharQuest;
    private TableColumn<Quest,String> QuestNameChar;
    private TableColumn<Quest,Integer> CompletionCountChar;

    private Button QuestForCharReturn;

   private Button AddConfigButton;
    private Button QuestInterfaceButton;
    private Button DodajPostac;

    public void setAddConfigButton(Button addConfigButton) {
        AddConfigButton = addConfigButton;
    }

    public void setQuestInterfaceButton(Button questInterfaceButton) {
        QuestInterfaceButton = questInterfaceButton;
    }

    public void setDodajPostac(Button dodajPostac) {
        DodajPostac = dodajPostac;
    }

    public Button getAddConfigButton() {
        return AddConfigButton;
    }

    public Button getQuestInterfaceButton() {
        return QuestInterfaceButton;
    }

    public Button getDodajPostac() {
        return DodajPostac;
    }

    public Button getQuestForCharReturn() {
        return QuestForCharReturn;
    }

    public Tab getPostaciTab() {
        return PostaciTab;
    }

    private Tab PostaciTab;

    public TableView<Item> getEkwipunek() {
        return Ekwipunek;
    }

    public TableColumn<Item, String> getPrzedmiot() {
        return Przedmiot;
    }

    public TableColumn<Item, Integer> getIlosc() {
        return Ilosc;
    }

    public TableColumn<Item, Integer> getWartosc() {
        return Wartosc;
    }

    public ListView<String> getCharacterList() {
        return CharacterList;
    }

    public Label getCharacterName() {
        return CharacterName;
    }

    public TextField getCharacterNameField() {
        return CharacterNameField;
    }

    public ImageView getClassIcon() {
        return ClassIcon;
    }

    public Label getClassName() {
        return ClassName;
    }

    public ChoiceBox<String> getClassNameField() {
        return ClassNameField;
    }

    public ChoiceBox<String> getConfigChoice() {
        return ConfigChoice;
    }

    public TextField getEDMultipField() {
        return EDMultipField;
    }

    public TextField getEXPMultipField() {
        return EXPMultipField;
    }

    public Label getEstCP() {
        return EstCP;
    }

    public Label getEstCP1() {
        return EstCP1;
    }

    public Label getEstCP11() {
        return EstCP11;
    }

    public Label getEstCP111() {
        return EstCP111;
    }

    public TextField getEstCPField() {
        return EstCPField;
    }

    public TextField getIDMultipField() {
        return IDMultipField;
    }

    public Tab getPrzegladTab() {
        return PrzegladTab;
    }

    public Database getBaza() {
        return baza;
    }

    public Account getKonto() {
        return konto;
    }

    public ScrollPane getMainClassWin() {
        return MainClassWin;
    }

    public ScrollPane getQuestsForCharacter() {
        return QuestsForCharacter;
    }

    public ChoiceBox<String> getQuestTypeChoice() {
        return QuestTypeChoice;
    }

    public ChoiceBox<String> getRegionChoice() {
        return RegionChoice;
    }

    public TableView<Quest> getShowCharQuest() {
        return ShowCharQuest;
    }

    public TableColumn<Quest, String> getQuestNameChar() {
        return QuestNameChar;
    }

    public TableColumn<Quest, Integer> getCompletionCountChar() {
        return CompletionCountChar;
    }

    public PostaciTabPack(TableView<Item> ekwipunek, TableColumn<Item, String> przedmiot, TableColumn<Item, Integer> ilosc, TableColumn<Item, Integer> wartosc, ListView<String> characterList, Label characterName, TextField characterNameField, ImageView classIcon, Label className, ChoiceBox<String> classNameField, ChoiceBox<String> configChoice, TextField EDMultipField, TextField EXPMultipField, Label estCP, Label estCP1, Label estCP11, Label estCP111, TextField estCPField, TextField IDMultipField, Tab przegladTab, Database baza, Account konto, ScrollPane mainClassWin, ScrollPane questsForCharacter, ChoiceBox<String> questTypeChoice, ChoiceBox<String> regionChoice, TableView<Quest> showCharQuest, TableColumn<Quest, String> questNameChar, TableColumn<Quest, Integer> completionCountChar, Tab postaciTab, Button QuestReturn) {
        Ekwipunek = ekwipunek;
        Przedmiot = przedmiot;
        Ilosc = ilosc;
        Wartosc = wartosc;
        CharacterList = characterList;
        CharacterName = characterName;
        CharacterNameField = characterNameField;
        ClassIcon = classIcon;
        ClassName = className;
        ClassNameField = classNameField;
        ConfigChoice = configChoice;
        this.EDMultipField = EDMultipField;
        this.EXPMultipField = EXPMultipField;
        EstCP = estCP;
        EstCP1 = estCP1;
        EstCP11 = estCP11;
        EstCP111 = estCP111;
        EstCPField = estCPField;
        this.IDMultipField = IDMultipField;
        PrzegladTab = przegladTab;
        this.baza = baza;
        this.konto = konto;
        MainClassWin = mainClassWin;
        QuestsForCharacter = questsForCharacter;
        QuestTypeChoice = questTypeChoice;
        RegionChoice = regionChoice;
        ShowCharQuest = showCharQuest;
        QuestNameChar = questNameChar;
        CompletionCountChar = completionCountChar;
        PostaciTab = postaciTab;
        QuestForCharReturn=QuestReturn;
    }
}
