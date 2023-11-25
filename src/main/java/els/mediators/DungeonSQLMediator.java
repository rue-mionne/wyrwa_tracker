package els.mediators;

import els.data.*;
import els.sqliteIO.NoActiveConnectionException;
import els.sqliteIO.SQLiteConnector;

import java.security.spec.EdDSAParameterSpec;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DungeonSQLMediator {

    public static void loadDungeonList(DungeonTree drzewoPlansz, Database baza) throws NoActiveConnectionException, SQLException {
        Connection con = baza.sqlCon.getActiveConnection();
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
            getQuestData(newDungeon,baza);


            //dodaj plansze
            //UWAGA BASE ED i EXP są zawarte w QUESTDATA w rekordzie z DUNGID+REPEAT type
        }
    }

    public static void getQuestData(Dungeon plansza, Database baza) throws SQLException, NoActiveConnectionException{

        Connection con = baza.sqlCon.getActiveConnection();
        Statement query = con.createStatement();
        ResultSet rs = query.executeQuery("Select * from QuestData, QuestDungeonData where QuestDungeonData.DungeonID="+plansza.getID()+" and QuestDungeonData.QuestID=QuestData.ID;");
        ArrayList<Integer> listaID = new ArrayList<>();
        while(rs.next()){
            listaID.add(rs.getInt("ID"));
        }
        plansza.getTablicaZadan().fillQuestTable(listaID,baza);
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
