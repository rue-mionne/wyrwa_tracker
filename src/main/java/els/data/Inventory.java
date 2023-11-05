package els.data;

import java.util.ArrayList;

public class Inventory {
    ArrayList<Item> listaPrzedmiotow = new ArrayList<Item>();

    public void dodajPrzedmiot(Item przedmiot){
        try {
            listaPrzedmiotow.add(przedmiot.clone().setAmount(0));
        }
        catch(CloneNotSupportedException e){
            System.err.println("Klon się zemścił: " + e.getMessage());
        }
    }

    public Item pobierzPrzedmiot(int ID){
        return new Item();
    }
}
