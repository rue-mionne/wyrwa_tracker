package els.mediators;

import els.data.Dungeon;
import els.data.DungeonTree;
import els.data.Region;
import els.sqliteIO.NoActiveConnectionException;
import els.sqliteIO.SQLiteConnector;

import java.security.spec.EdDSAParameterSpec;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DungeonSQLMediator {

    public static void loadDungeonList(DungeonTree drzewoPlansz, SQLiteConnector sqlboss) throws NoActiveConnectionException, SQLException {
        Connection con = sqlboss.getActiveConnection();
        Statement query = con.createStatement();
        ResultSet rs = query.executeQuery("Select * from Dungeons");
        while(rs.next()){
            String regionName = rs.getString("Region");
            Region region;
            if(drzewoPlansz.RegionExists(regionName)){
                region = drzewoPlansz.getRegion(regionName);
            }
            else{
                drzewoPlansz.addRegion(new Region(regionName));
                region = drzewoPlansz.getRegion(regionName);
            }
            Dungeon newDungeon = new Dungeon(rs.getString("Name"));
            region.addDungeon(newDungeon);
            newDungeon.setMinCP(rs.getInt("MinCP"));
            newDungeon.setID(rs.getInt("ID"));
            getQuestData(newDungeon,con);


            //dodaj plansze
            //UWAGA BASE ED i EXP są zawarte w QUESTDATA w rekordzie z DUNGID+REPEAT type
        }
    }

    private static void getQuestData(Dungeon plansza, Connection con) throws SQLException{
        Statement query = con.createStatement();
        ResultSet rs = query.executeQuery("Select * from QuestData, QuestDungeonData where QuestDungeonData.DungeonID="+plansza.getID()+" and QuestDungeonData.QuestID=QuestData.ID;");
        while(rs.next()){
            //load quest table
        }
        //temporary for testing
        rs = query.executeQuery("Select QuestData.ED, QuestData.EXP from QuestData, QuestDungeonData where QuestDungeonData.DungeonID="+plansza.getID()+" and QuestDungeonData.QuestID=QuestData.ID and QuestData.CompletionType like 'REPEAT'");
        if(rs.next()) {
            int EDVal = rs.getInt("ED");
            plansza.setBaseED(EDVal);
            plansza.setBaseEXP(rs.getInt("EXP"));
        }
        else{
            plansza.setBaseED(0);
            plansza.setBaseEXP(0);
        }
    }
}
