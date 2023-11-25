package els.mediators;

import els.data.Quest;
import els.data.QuestDatabase;
import els.sqliteIO.NoActiveConnectionException;
import els.sqliteIO.SQLiteConnector;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.EnumSet;

public class QuestSQLMediator {
    public static void loadAllQuestData(QuestDatabase quests, SQLiteConnector sqlboss)throws NoActiveConnectionException, SQLException {
        Connection con= sqlboss.getActiveConnection();
        Statement query= con.createStatement();
        ResultSet rs= query.executeQuery("Select * from QuestData");
        while(rs.next()){
            Quest.CompletionType complType = Quest.CompletionType.DAILY;
            Quest.CompletionRange complRange = Quest.CompletionRange.ACCOUNT;
            Quest newQuest = new Quest(rs.getInt("ID"),rs.getString("QuestName"), Quest.CompletionType.valueOf(complType.getDeclaringClass(), rs.getString("CompletionType")), Quest.CompletionRange.valueOf(rs.getString("CompletionRange")),rs.getInt("ED"), rs.getInt("EXP"), rs.getInt("ED"));
            quests.getBazeZadan().put(rs.getInt("ID"),newQuest);
            ItemSQLMediator.fillRewardTable(newQuest);
        }
    }

    public static Quest loadQuestData(int ID, SQLiteConnector sqlboss) throws NoActiveConnectionException, SQLException{
        Connection con = sqlboss.getActiveConnection();
        Statement query = con.createStatement();
        ResultSet rs = query.executeQuery("Select * from QuestData where ID="+ID+";");
        Quest.CompletionType complType = Quest.CompletionType.DAILY;
        Quest.CompletionRange complRange = Quest.CompletionRange.ACCOUNT;
        Quest newQuest = new Quest(rs.getInt("ID"),rs.getString("QuestName"), Quest.CompletionType.valueOf(complType.getDeclaringClass(), rs.getString("CompletionType")), Quest.CompletionRange.valueOf(rs.getString("CompletionRange")),rs.getInt("ED"), rs.getInt("EXP"), rs.getInt("ED"));
        ItemSQLMediator.fillRewardTable(newQuest);
        return newQuest;
    }
}
