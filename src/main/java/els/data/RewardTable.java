package els.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RewardTable {
    ArrayList<Item> rewardTable = new ArrayList<>();

    public void wczytajTablice(Map<Integer,Float> itemMap, ItemDatabase bazaPrzedmiotow){
        for(Map.Entry<Integer, Float> item : itemMap.entrySet()){
            Item newItem = bazaPrzedmiotow.PobierzPrzedmiot(item.getKey());
            newItem.setRate(item.getValue());
            rewardTable.add(newItem);
        }
    }

    public ArrayList<Item> getRewardTable(){
        return rewardTable;
    }

    public ArrayList<String> getRewardList(){
        ArrayList<String> listaNagrod = new ArrayList<>();
        for(Item item:rewardTable){
            listaNagrod.add(item.getName().getValue());
        }
        return listaNagrod;
    }
}
