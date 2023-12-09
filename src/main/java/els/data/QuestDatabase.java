package els.data;

import els.mediators.QuestSQLMediator;
import els.sqliteIO.NoActiveConnectionException;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class QuestDatabase {
    Map<Integer,Quest> bazaZadan = new HashMap<>();

    public Map<Integer,Quest> getBazeZadan(){return bazaZadan;}

    public Quest getQuest(int ID, Database base) throws NoActiveConnectionException, SQLException {
        if(!bazaZadan.containsKey(ID)){
            bazaZadan.put(ID, QuestSQLMediator.loadQuestData(ID, base.sqlCon));
        }
        return bazaZadan.get(ID);
    }

    public Quest getQuest(String name){
        for(Quest zadanie:bazaZadan.values()){
            if(zadanie.getQuestName().equals(name)){
                return zadanie;
            }
        }
        return null;
    }
}
