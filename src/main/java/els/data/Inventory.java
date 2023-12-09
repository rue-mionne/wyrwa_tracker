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
    ArrayList<Integer> editMode = new ArrayList<>();

    Postac owner;
    Integer lastInserted;

    public Integer getLastInserted(){return lastInserted;}
    void setLastInserted(int insertedID){lastInserted=insertedID;}
    public ArrayList<Integer> getEditModeList(){return editMode;}

    public void dodajPrzedmiot(Item przedmiot){
        try {
            listaPrzedmiotow.add(przedmiot.clone());
            editMode.add(0);
            lastInserted= przedmiot.ID;
        }
        catch(CloneNotSupportedException e){
            System.err.println("Klon się zemścił: " + e.getMessage());
        }
    }

    public Item pobierzPrzedmiot(int ID){
        return new Item();
    }

    public ArrayList<Item> getListaPrzedmiotow(){return listaPrzedmiotow;}

    public Postac getOwner() {
        return owner;
    }

    void setOwner(Postac owner) {
        this.owner = owner;
    }
}
