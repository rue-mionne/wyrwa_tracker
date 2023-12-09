package els.data;

import java.util.ArrayList;
import java.util.EnumSet;

public class Postac {
    public void setID(int ID) {
        this.ID = ID;
    }

    int ID;
    String IGN;
    String CharacterName;
    String ClassName;
    String ShortClassName;
    //placeholder for icon
    float DPSRating;
    BuildSet BuildSetData = new BuildSet();
    Inventory ekwipunek = new Inventory();

    public Postac() {
        ekwipunek.setOwner(this);
    }

    public int getID(){
        return ID;
    }

    public Postac(int ID, String IGN, String characterName, String className, String shortClassName, float DPSRating) {
        this.ID = ID;
        this.IGN = IGN;
        CharacterName = characterName;
        ClassName = className;
        ShortClassName = shortClassName;
        this.DPSRating = DPSRating;
        ekwipunek.setOwner(this);
    }

    public void DodajPrzedmiot(Item przedmiot){
        ekwipunek.dodajPrzedmiot(przedmiot);
    }
    public Item PobierzPrzedmiot(int ID){
        return ekwipunek.pobierzPrzedmiot(ID);
    }

    public Inventory PobierzListePrzedmiotow(){
        return ekwipunek;
    }

    public Build PobierzBuild(String build){
        return BuildSetData.getBuild(build);
    }

    public BuildSet PobierzDaneBuildow(){
        return BuildSetData;
    }
    public String getIGN() {
        return IGN;
    }

    public void setIGN(String IGN) {
        this.IGN = IGN;
    }

    public String getCharacterName() {
        return CharacterName;
    }

    public void setCharacterName(String characterName) {
        CharacterName = characterName;
    }

    public String getClassName() {
        return ClassName;
    }

    public void setClassName(String className) {
        ClassName = className;
    }

    public String getShortClassName() {
        return ShortClassName;
    }

    public void setShortClassName(String shortClassName) {
        ShortClassName = shortClassName;
    }

    public Float getDPSRating() {
        return DPSRating;
    }

    public void setDPSRating(Float DPSRating) {
        this.DPSRating = DPSRating;
    }

    public ArrayList<String> PobierzListeBuildow(){
        return BuildSetData.getSetNameList();
    }
/*
    public enum SaveFlags{IGN, ClassName, BuildSet, Eq;
        public static final EnumSet<SaveFlags> ALL_UPT = EnumSet.allOf(SaveFlags.class);
    }
*/
}
