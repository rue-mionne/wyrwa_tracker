package els.data;

import java.util.ArrayList;

public class Region {
    String name;
    ArrayList<Dungeon> listaPlansz = new ArrayList<Dungeon>();

    public Region(){}
    public Region(String name){this.name=name;}

    public ArrayList<String> getDungeonList(){
        ArrayList<String> listaNazwPlansz = new ArrayList<String>();
        listaPlansz.forEach(dungeon-> listaNazwPlansz.add(dungeon.getName()));
        return listaNazwPlansz;
    }

    public String getName() {
        return name;
    }

    public void addDungeon(Dungeon plansza){
        listaPlansz.add(plansza);
    }

    public Dungeon getDungeon(String dungeonName){
        for(Dungeon plansza: listaPlansz){
            if(plansza.getName().equals(dungeonName)){
                return plansza;
            }
        }
        return null;
    }
}
