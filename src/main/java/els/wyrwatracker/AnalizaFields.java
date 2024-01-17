package els.wyrwatracker;

import els.genAlgImpl.CharDungPair;
import els.genAlgImpl.DungeonOrder;
import javafx.scene.control.*;

public class AnalizaFields {
    //Analiza fields
    public Label GenerationCounter;
    public TableColumn<DungeonOrder,String> PathCol;
    public TableColumn<DungeonOrder,Double> ScoreCol;
    public Button StartStopButton;
    public TableView<DungeonOrder> MainOverview;
    public TableView<CharDungPair> DetailedOverview;
    public TableColumn<CharDungPair,String> CharacterPathData;
    public TableColumn<CharDungPair,String> DungeonPathData;
    public TableColumn<CharDungPair,String> QuestPathData;
    public Button SzablonPrzeplotowSpinner;
    public Spinner<Integer> PopulacjaSpinner;
    public Spinner<Double> MutProbSpinner;
    public Spinner<Integer> MaxIlPokSpinner;
    public Spinner<Integer> DelaySpinner;

    public AnalizaFields(Label generationCounter, TableColumn<DungeonOrder,String> pathCol, TableColumn<DungeonOrder,Double> scoreCol, Button startStopButton, TableView<DungeonOrder> mainOverview, TableView<CharDungPair> detailedOverview, TableColumn<CharDungPair,String> characterPathData, TableColumn<CharDungPair,String> dungeonPathData, TableColumn<CharDungPair,String> questPathData, Button szablonPrzeplotowSpinner, Spinner<Integer> populacjaSpinner, Spinner<Double> mutProbSpinner, Spinner<Integer> maxIlPokSpinner, Spinner<Integer> delaySpinner) {
        GenerationCounter = generationCounter;
        PathCol = pathCol;
        ScoreCol = scoreCol;
        StartStopButton = startStopButton;
        MainOverview = mainOverview;
        DetailedOverview = detailedOverview;
        CharacterPathData = characterPathData;
        DungeonPathData = dungeonPathData;
        QuestPathData = questPathData;
        SzablonPrzeplotowSpinner = szablonPrzeplotowSpinner;
        PopulacjaSpinner = populacjaSpinner;
        MutProbSpinner = mutProbSpinner;
        MaxIlPokSpinner = maxIlPokSpinner;
        DelaySpinner = delaySpinner;
    }
}