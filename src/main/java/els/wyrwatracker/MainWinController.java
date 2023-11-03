package els.wyrwatracker;

import els.filehandlers.SQLFileHandler;
import els.sqliteIO.NoActiveConnectionException;
import els.sqliteIO.SQLiteConnector;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MainWinController {
    SQLiteConnector sqlboss;
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
    private TextField ClassNameField;

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

    @FXML
    void EnableCharNameEdit(MouseEvent event) {
        CharacterNameField.setEditable(true);
    }

    @FXML
    void EnableClassNameEdit(MouseEvent event) {
        //ClassNameField.setEditable(true);
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
        try {
            Connection pol = sqlboss.getActiveConnection();
            Statement query = pol.createStatement();
            ResultSet rs = query.executeQuery("Select * from Characters");
            System.out.println(rs.getString("Name"));
        }
        catch(NoActiveConnectionException e){
            System.err.println("fuuuuu");
        }
        catch(SQLException f){
            System.err.println("aaaaaa");
        }
    }

}

