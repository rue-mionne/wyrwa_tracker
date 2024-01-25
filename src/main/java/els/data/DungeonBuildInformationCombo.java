package els.data;

import els.mediators.DungeonSQLMediator;
import els.sqliteIO.NoActiveConnectionException;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DungeonBuildInformationCombo {
    Postac Character;
    Dungeon Plansza;
    public StringProperty CharacterName;
    public StringProperty DungeonName;
    public StringProperty QuestData = new SimpleStringProperty("Work in progress");

    public DungeonBuildInformationCombo(Database data, int BuildID, int DungeonID) throws NoActiveConnectionException, SQLException {
        Connection con = data.sqlCon.getActiveConnection();
        Statement query = con.createStatement();
        ResultSet rs = query.executeQuery("Select Name from Dungeons where ID=" +DungeonID+ ";");
        Plansza = new Dungeon(rs.getString("Name"));
        rs=query.executeQuery("Select CharacterID from BuildSetsTable where BuildSetID=" +BuildID+";");
        Character = data.konto.PobierzPostac(rs.getInt("CharacterID"));
        CharacterName = new SimpleStringProperty(Character.getIGN());
        DungeonName = new SimpleStringProperty(Plansza.getName());
    }
}
