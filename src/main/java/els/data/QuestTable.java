package els.data;

import els.sqliteIO.NoActiveConnectionException;

import java.sql.SQLException;
import java.util.ArrayList;

public class QuestTable {
    ArrayList<Quest> listaZadan = new ArrayList<>();

    public void fillQuestTable(ArrayList<Integer> listaID, Database baza) throws SQLException, NoActiveConnectionException {
        for(Integer id: listaID){
            listaZadan.add(baza.bazaZadan.getQuest(id,baza));
        }
    }

    public ArrayList<Quest> getQuestTable(){return listaZadan;}

}
