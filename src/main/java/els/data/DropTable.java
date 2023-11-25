package els.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DropTable {
    ArrayList<Item> dropy =  new ArrayList<>();

    public void fillDropList(QuestTable listaZadan){
        for(Quest zadanie : listaZadan.getQuestTable()){
            if(zadanie.getCompletionType()== Quest.CompletionType.REPEAT){
                dropy=zadanie.getListaPrzedmiotow().getRewardTable();
                return;
            }
        }
    }

    public ArrayList<Item> getDropy() {
        return dropy;
    }
}
