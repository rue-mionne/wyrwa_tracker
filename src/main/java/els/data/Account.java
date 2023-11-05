package els.data;

import javafx.collections.ObservableList;

import java.util.ArrayList;

public class Account {
    ArrayList<Postac> listaPostaci = new ArrayList<Postac>();

    public void DodajPostac(Postac postac){
        listaPostaci.add(postac);
    }

    public Postac PobierzPostac(int ID){
        return listaPostaci.get(ID);
    }

    public ArrayList<String> pobierzListePostaci(){
        ArrayList<String> listaNazwPostaci = new ArrayList<String>();
        listaPostaci.forEach(postac->listaNazwPostaci.add(postac.getIGN()));
        return listaNazwPostaci;
    }

    public Postac PobierzPostac(String nazwa){
        for(Postac postac: listaPostaci){
            if(postac.getIGN()==nazwa){
                return postac;
            }
        }
        return null;
    }
}
