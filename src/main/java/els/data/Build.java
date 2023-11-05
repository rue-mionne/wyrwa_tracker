package els.data;

public class Build {
    public int ID;
    public String Name;
    public int EstCP;
    public int EXPMultip;
    public int EDMultip;
    public int IDMultip;

    public Build(String Name, int estCP, int EXPMultip, int EDMultip, int IDMultip) {
        this.Name = Name;
        EstCP = estCP;
        this.EXPMultip = EXPMultip;
        this.EDMultip = EDMultip;
        this.IDMultip = IDMultip;
    }
}
