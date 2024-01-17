package els.wyrwatracker;

import els.genAlgImpl.CharDungPair;
import els.genAlgImpl.DungeonOrder;
import els.sqliteIO.NoActiveConnectionException;
import els.sqliteIO.SQLiteConnector;
import gen_alg.GenerationCompleteEvent;
import gen_alg.Incubator;
import gen_alg.IncubatorBase;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.Flow;

public class KowalskiAnalysis {
    AnalizaFields polaAnalizy;
    SQLiteConnector sqlBoss;
    ArrayList<DungeonOrder> specimenLista;

    Flow.Subscriber<GenerationCompleteEvent> listener = new Flow.Subscriber<GenerationCompleteEvent>() {
        @Override
        public void onSubscribe(Flow.Subscription subscription) {
            System.out.println("Registered");
        }

        @Override
        public void onNext(GenerationCompleteEvent generationCompleteEvent) {
            specimenLista= (ArrayList<DungeonOrder>) inkubator.getSpecimen();
            System.out.println("Event received");
            refreshTables();
        }

        @Override
        public void onError(Throwable throwable) {
            throwable.getMessage();
        }

        @Override
        public void onComplete() {

        }
    };

    Incubator inkubator;

    KowalskiAnalysis(SQLiteConnector sqlboss,AnalizaFields analiza) throws SQLException, NoActiveConnectionException {
        polaAnalizy=analiza;
        sqlBoss=sqlboss;
        setStartStopHandler();
        CharDungPair templcdp = new CharDungPair(0,0);
        templcdp.initiateIDBases(sqlBoss.getActiveConnection());
        DungeonOrder templ = new DungeonOrder(templcdp);
        templ.setAlleleTemplate(templcdp);
        inkubator= new IncubatorBase(templ);
        inkubator.addGenerationPeeker(listener);
        setDelaySpinnerHandler();
        setMaxIlPokSpinner();
        setMutProbSpinner();
        setPopulacjaSpinner();
        setGeneralOverviewTab();

    }

    void setStartStopHandler(){
        final boolean[] started = {false};
        polaAnalizy.StartStopButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(!started[0]){
                    started[0] =true;
                    Thread thread = new Thread(inkubator);
                    inkubator.run();
                    polaAnalizy.DelaySpinner.setDisable(true);
                    polaAnalizy.MaxIlPokSpinner.setDisable(true);
                    polaAnalizy.MutProbSpinner.setDisable(true);
                    polaAnalizy.PopulacjaSpinner.setDisable(true);
                }
                else{
                    if(inkubator.isPaused()){
                        inkubator.restart();
                    }
                    else
                        inkubator.pause();
                }
            }
        });
    }

    void setDelaySpinnerHandler(){
        polaAnalizy.DelaySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,100000,1));
        polaAnalizy.DelaySpinner.getValueFactory().setValue(inkubator.getDelayInMs());
        polaAnalizy.DelaySpinner.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observableValue, Integer integer, Integer t1) {
                inkubator.setDelayInMs(observableValue.getValue());
            }
        });
    }
    void setMaxIlPokSpinner(){
        polaAnalizy.MaxIlPokSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,Integer.MAX_VALUE,1));
        polaAnalizy.MaxIlPokSpinner.getValueFactory().setValue(inkubator.getMaxGenerationCount());
        polaAnalizy.MaxIlPokSpinner.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observableValue, Integer integer, Integer t1) {
                inkubator.setMaxGenerationCount(observableValue.getValue());
            }
        });
    }
    void setMutProbSpinner(){
        polaAnalizy.MutProbSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0,1.0,0.01));

        polaAnalizy.MutProbSpinner.valueProperty().addListener(new ChangeListener<Double>() {
            @Override
            public void changed(ObservableValue<? extends Double> observableValue, Double aDouble, Double t1) {
                    inkubator.setMutationProbability(observableValue.getValue());
            }
        });
        polaAnalizy.MutProbSpinner.getValueFactory().setValue(0.1);
    }
    void setPopulacjaSpinner(){
        polaAnalizy.PopulacjaSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,30,1));
        polaAnalizy.PopulacjaSpinner.getValueFactory().setValue(5);
        inkubator.setPopulation(5);
        polaAnalizy.PopulacjaSpinner.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observableValue, Integer integer, Integer t1) {
                inkubator.setPopulation(observableValue.getValue());
            }
        });
    }
    void setGeneralOverviewTab(){
        String tekst = "Najedź lub kliknij, żeby poznać szczegóły";
        polaAnalizy.MainOverview.setEditable(false);
        polaAnalizy.MainOverview.setItems(FXCollections.observableArrayList(specimenLista));
        polaAnalizy.PathCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<DungeonOrder, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<DungeonOrder, String> dungeonOrderStringCellDataFeatures) {
                return new ReadOnlyObjectWrapper<String>(tekst);
            }
        });
        polaAnalizy.ScoreCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<DungeonOrder, Double>, ObservableValue<Double>>() {
            @Override
            public ObservableValue<Double> call(TableColumn.CellDataFeatures<DungeonOrder, Double> dungeonOrderDoubleCellDataFeatures) {
                Double score = dungeonOrderDoubleCellDataFeatures.getValue().score;
                return new ReadOnlyObjectWrapper<>(score);
            }
        });
    }
    void refreshTables(){
        polaAnalizy.MainOverview.setItems(FXCollections.observableArrayList(specimenLista));

    }
}
