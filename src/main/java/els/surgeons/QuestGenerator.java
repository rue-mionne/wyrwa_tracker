package els.surgeons;

import els.data.Database;
import els.data.Postac;
import els.sqliteIO.NoActiveConnectionException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public class QuestGenerator implements  IInsertSurgeon{
    Database baza = null;

    public QuestGenerator(Database baza) throws NoActiveConnectionException,SQLException{
        this.baza=baza;
        Connection con = baza.sqlCon.getActiveConnection();
        Statement query = con.createStatement();
        String polecenie = "Select QuestCompletion.PairID from QuestCompletion;";
        ResultSet rs = query.executeQuery(polecenie);
        if(!rs.next())
            initiateAccountMissions();
    }
    @Override
    public void scheduleInsert(Object inserted) throws NoActiveConnectionException, SQLException {
        Postac postac = (Postac)inserted;
        Connection con = baza.sqlCon.getActiveConnection();
        Statement query = con.createStatement();
        String polecenie = "Select QuestCompletion.PairID from QuestCompletion where CharacterID = "+postac.getID()+";";
        ResultSet rs = query.executeQuery(polecenie);
        if(!rs.next())
            initiateClasslessMissions(postac);
        else if(!postac.getClassName().isEmpty()){
            initiateCharacterSpecificMissions(postac);
        }
    }

    @Override
    public void proceed() throws NoActiveConnectionException, SQLException {

    }

    private void initiateAccountMissions() throws NoActiveConnectionException, SQLException{
        Postac konto = baza.konto.PobierzPostac("Account");
        Connection con = baza.sqlCon.getActiveConnection();
        Statement query = con.createStatement();
        Statement query2 = con.createStatement();
        String polecenie = "Select ID from QuestData where CompletionRange = 'ACCOUNT' and CharacterType = 'ALL';";
        ResultSet rs= query.executeQuery(polecenie);
        while(rs.next()){
            int ID = rs.getInt("ID");
            polecenie = "Insert into QuestCompletion (CharacterID,QuestID,Status,Progress,CompletionCount) values (1, "+ID+",'ACTIVE',0,0);";
            query2.execute(polecenie);
            System.err.println(ID);
        }
    }

    private void initiateClasslessMissions(Postac postac)throws NoActiveConnectionException,SQLException{
        Connection con = baza.sqlCon.getActiveConnection();
        Statement query = con.createStatement();
        ResultSet rs = query.executeQuery("Select ID from QuestData where CompletionRange = 'CHARACTER' and CharacterType = 'ALL';");
        Statement query2 = con.createStatement();
        while (rs.next()){
            int ID = rs.getInt("ID");
            String polecenie =  "Insert into QuestCompletion (CharacterID,QuestID,Status,Progress,CompletionCount) values ("+postac.getID()+", "+ID+",'ACTIVE',0,0);";
            query2.execute(polecenie);
        }
    }

    private void initiateCharacterSpecificMissions(Postac postac) throws NoActiveConnectionException, SQLException{
            Connection con = baza.sqlCon.getActiveConnection();
            Statement query = con.createStatement();
            ResultSet rs = query.executeQuery("Select NameTable.QuestENUM from NameTable where CharacterName like '" +postac.getCharacterName() +"';");
            String questENUM = rs.getString("QuestENUM");
            rs = query.executeQuery("Select ID, PreviousQuest from QuestData where CharacterType like '%" + questENUM +"%';" );
            Statement query2 = con.createStatement();
            while(rs.next()){
                int ID = rs.getInt("ID");
                String polecenie;
                if(rs.getInt("PreviousQuest")==0){
                    polecenie = "Insert into QuestCompletion (CharacterID, QuestID, Status, Progress, CompletionCount) values ("+postac.getID()+", "+ID+", 'ACTIVE', 0, 0);";
                }
                else{
                    polecenie = "Insert into QuestCompletion (CharacterID, QuestID, Status, Progress, CompletionCount) values ("+postac.getID()+", "+ID+", 'INACTIVE', 0, 0);";
                }
                Statement insertQuery = con.createStatement();
                query2.execute(polecenie);
            }
    }
}
