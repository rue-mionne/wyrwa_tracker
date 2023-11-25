package els.data;

public class Dungeon {
    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    Integer ID;
    String name;
    Integer BaseED;
    Integer BaseEXP;
    Integer MinCP;

    QuestTable tablicaZadan = new QuestTable();

    public DropTable getTablicaDropow() {
        return tablicaDropow;
    }

    public void inicjujTabliceDropow() {
        tablicaDropow.fillDropList(tablicaZadan);
    }

    DropTable tablicaDropow = new DropTable();

    public Dungeon(String name){
        this.name=name;
    }

    public Integer getBaseED() {
        return BaseED;
    }

    public void setBaseED(Integer baseED) {
        BaseED = baseED;
    }

    public Integer getBaseEXP() {
        return BaseEXP;
    }

    public void setBaseEXP(Integer baseEXP) {
        BaseEXP = baseEXP;
    }

    public Integer getMinCP() {
        return MinCP;
    }

    public void setMinCP(Integer minCP) {
        MinCP = minCP;
    }

    public String getName(){
        return name;
    }

    public QuestTable getTablicaZadan() {
        return tablicaZadan;
    }

    public void setTablicaZadan(QuestTable tablicaZadan) {
        this.tablicaZadan = tablicaZadan;
    }
}
