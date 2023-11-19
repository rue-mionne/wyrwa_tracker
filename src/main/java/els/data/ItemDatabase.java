package els.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import els.mediators.ItemSQLMediator;

public class ItemDatabase {
    Map<Integer,Item> database=new HashMap<>();

    public void ImportujDanePrzedmiotu(Item przedmiot){
        int ID = przedmiot.ID;
        database.put(ID, przedmiot);
    }

    public Item PobierzPrzedmiot(int ID){
        if(database.containsKey(ID)){
            return database.get(ID);
        }
        else{
            ImportujDanePrzedmiotu(ItemSQLMediator.loadItem(ID));
            return database.get(ID);
        }
    }
}
