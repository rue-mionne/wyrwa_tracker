package els.data;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.*;

public class Inventory  {
    ArrayList<Item> listaPrzedmiotow = new ArrayList<Item>();

    public void dodajPrzedmiot(Item przedmiot){
        try {
            listaPrzedmiotow.add(przedmiot.clone());
        }
        catch(CloneNotSupportedException e){
            System.err.println("Klon się zemścił: " + e.getMessage());
        }
    }

    public Item pobierzPrzedmiot(int ID){
        return new Item();
    }

    public ArrayList<Item> getListaPrzedmiotow(){return listaPrzedmiotow;}


}
