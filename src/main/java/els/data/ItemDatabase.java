package els.data;

import java.util.ArrayList;
import els.mediators.ItemSQLMediator;

public class ItemDatabase {
    ArrayList<Item> database=new ArrayList<Item>();

    public void ImportujDanePrzedmiotu(Item przedmiot){
        int ID = przedmiot.ID;
        database.add(ID, przedmiot);
    }

    public Item PobierzPrzedmiot(int ID){
        if(database.get(ID)!=null){
            return database.get(ID);
        }
        else{
            ImportujDanePrzedmiotu(ItemSQLMediator.loadItem(ID));
            return database.get(ID);
        }
    }
}
