package els.wyrwatracker;

import els.data.Database;
import els.data.DungeonBuildInformationCombo;
import els.genAlgImpl.CharDungPair;
import els.genAlgImpl.DungeonOrder;
import els.sqliteIO.NoActiveConnectionException;
import els.sqliteIO.SQLiteConnector;
import gen_alg.GenerationCompleteEvent;
import gen_alg.Incubator;
import gen_alg.IncubatorBase;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.Flow;

public class KowalskiAnalysis {
    AnalizaFields polaAnalizy;
    Database data;
    SQLiteConnector sqlBoss;
    ObservableList<DungeonOrder> oList;
    ArrayList<DungeonOrder> specimenLista=new ArrayList<>();
    Object lock;
    Flow.Subscription thisSubscription;

    Flow.Subscriber<GenerationCompleteEvent> listener = new Flow.Subscriber<GenerationCompleteEvent>() {
        @Override
        public void onSubscribe(Flow.Subscription subscription) {
            thisSubscription=subscription;
            System.out.println("Registered");
            subscription.request(1);
        }

        @Override
        public void onNext(GenerationCompleteEvent generationCompleteEvent) {
            synchronized (lock){
                oList.clear();
                oList.addAll((ArrayList<DungeonOrder>)inkubator.getSpecimen());
                polaAnalizy.MainOverview.getSelectionModel().select(0);
            }
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    polaAnalizy.GenerationCounter.setText("Pokolenia: "+inkubator.getGenerationCount());
                    System.out.println("Event received");
                    thisSubscription.request(1);
                }
            });

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

    KowalskiAnalysis(Database database,AnalizaFields analiza) throws SQLException, NoActiveConnectionException {
        polaAnalizy=analiza;
        data=database;
        sqlBoss=data.sqlCon;
        setStartStopHandler();
        CharDungPair templcdp = new CharDungPair(0,0);
        templcdp.initiateIDBases(sqlBoss.getActiveConnection());
        DungeonOrder templ = new DungeonOrder(templcdp);
        templ.setAlleleTemplate(templcdp);
        inkubator= new IncubatorBase(templ);
        lock=inkubator.getLock();
        inkubator.addGenerationPeeker(listener);
        setDelaySpinnerHandler();
        setMaxIlPokSpinner();
        setMutProbSpinner();
        setPopulacjaSpinner();
        setGeneralOverviewTab();
        setDetailedOverviewTab();
    }

    void setStartStopHandler(){
        final boolean[] started = {false};
        polaAnalizy.StartStopButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(!started[0]){
                    started[0] =true;
                    Thread thread = new Thread(inkubator);
                    thread.start();
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
        polaAnalizy.MutProbSpinner.setEditable(true);
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

    ObservableList<DungeonBuildInformationCombo> detailedOList = FXCollections.observableArrayList(new ArrayList<DungeonBuildInformationCombo>());
    void setGeneralOverviewTab(){
        String tekst = "Najedź lub kliknij, żeby poznać szczegóły";
        polaAnalizy.MainOverview.setEditable(false);
        oList=FXCollections.observableArrayList(specimenLista);
        polaAnalizy.MainOverview.setItems(oList);
        polaAnalizy.MainOverview.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<DungeonOrder>() {
            @Override
            public void changed(ObservableValue<? extends DungeonOrder> observableValue, DungeonOrder dungeonOrder, DungeonOrder t1) {
                DungeonOrder selectedSpecimen = observableValue.getValue();
                if (selectedSpecimen.getOrder() != null) {
                    ArrayList<CharDungPair> selectedOrder = (ArrayList<CharDungPair>) selectedSpecimen.getOrder();
                    detailedOList.clear();
                    System.out.println("AAAAAAAA");
                    for (CharDungPair pair : selectedOrder) {
                        try {
                            detailedOList.add(new DungeonBuildInformationCombo(data, pair.getChosenBuildID(), pair.getChosenDungeonID()));
                        } catch (NoActiveConnectionException e) {
                            throw new RuntimeException(e);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        });

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

    void setDetailedOverviewTab(){
        polaAnalizy.DetailedOverview.setEditable(false);
        polaAnalizy.DetailedOverview.setItems(detailedOList);
        polaAnalizy.CharacterPathData.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<DungeonBuildInformationCombo, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<DungeonBuildInformationCombo, String> dungeonBuildInformationComboStringCellDataFeatures) {
                return dungeonBuildInformationComboStringCellDataFeatures.getValue().CharacterName;
            }
        });

        polaAnalizy.DungeonPathData.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<DungeonBuildInformationCombo, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<DungeonBuildInformationCombo, String> dungeonBuildInformationComboStringCellDataFeatures) {
                return dungeonBuildInformationComboStringCellDataFeatures.getValue().DungeonName;
            }
        });

        polaAnalizy.QuestPathData.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<DungeonBuildInformationCombo, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<DungeonBuildInformationCombo, String> dungeonBuildInformationComboStringCellDataFeatures) {
                return dungeonBuildInformationComboStringCellDataFeatures.getValue().QuestData;
            }
        });
    }
}
