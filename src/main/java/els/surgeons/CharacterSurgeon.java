package els.surgeons;

import els.data.Build;
import els.data.Database;
import els.data.Postac;
import els.sqliteIO.NoActiveConnectionException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.EnumSet;

public class CharacterSurgeon implements IUpdateSurgeon, IInsertSurgeon, IDeleteSurgeon {

    BuildSurgeon buildSurgeon;
    InventorySurgeon inventorySurgeon;
    Database baza=null;
    ArrayList<Integer> updateSchedule=new ArrayList<>();
    ArrayList<Postac> insertList=new ArrayList<>();
    ArrayList<String> insertSchedule = new ArrayList<>();


    ArrayList<String> deleteSchedule=new ArrayList<>();
    public CharacterSurgeon(Database baza){
        this.baza=baza;
        buildSurgeon=new BuildSurgeon(baza);
        inventorySurgeon = new InventorySurgeon(baza);
    }

    @Override
    public void proceed() throws NoActiveConnectionException, SQLException {
        Connection con = baza.sqlCon.getActiveConnection();
        Statement query = con.createStatement();
            for (String polecenie : insertSchedule) {
                query.execute(polecenie);
                Postac postac = insertList.get(insertSchedule.indexOf(polecenie));
                String dajID = "Select Characters.ID from Characters where Name='" + postac.getIGN() + "';";
                ResultSet rs = query.executeQuery(dajID);
                postac.setID(rs.getInt("ID"));
            }
            insertSchedule.clear();
            insertList.clear();

            for (Integer id : updateSchedule) {
                String polecenie = generateQuery(baza.konto.PobierzPostac(id-1));
                query.execute(polecenie);
                Postac postac= baza.konto.PobierzPostac(id-1);
                for(Build build:postac.PobierzDaneBuildow().getBuildSetList()){
                    buildSurgeon.scheduleUpdate(build);
                }
                if(postac.PobierzListePrzedmiotow().getEditModeList().contains(1))
                    inventorySurgeon.scheduleUpdate(postac.PobierzListePrzedmiotow());
                inventorySurgeon.proceed();
            }
            buildSurgeon.proceed();
            updateSchedule.clear();

    }

    @Override
    public void scheduleUpdate(Object updated) throws NoActiveConnectionException, SQLException{
        Postac postac=(Postac) updated;
        int ID = postac.getID();
        if(!updateSchedule.contains(ID)) {
            updateSchedule.add(ID);
        }
    }

    private String generateQuery(Postac postac)throws NoActiveConnectionException, SQLException{

        Connection con = baza.sqlCon.getActiveConnection();
        Statement query = con.createStatement();
        ResultSet rs = query.executeQuery("Select Classes.ClassID from Classes where ClassName='" + postac.getClassName() + "';");
        int classID = rs.getInt("ClassID");
        String polecenie;
        if (classID != 0)
            polecenie = "Update Characters set Name='" + postac.getIGN() + "', ClassID= " + classID + " where ID="+postac.getID()+";";
        else
            polecenie = "Update Characters set Name='" + postac.getIGN() + "' where ID="+postac.getID()+";";
        //update Equipment
        return polecenie;
    }

    @Override
    public void scheduleInsert(Object inserted){
        Postac postac = (Postac) inserted;
        String polecenie = "INSERT INTO \"Characters\" (\"Name\") VALUES ('"+postac.getIGN()+"');";
        insertSchedule.add(polecenie);
        insertList.add(postac);
    }

    @Override
    public void scheduleDelete(Object deleted) {
        Postac postac = (Postac) deleted;
        String polecenie = "Delete from Characters where ID="+postac.getID()+";";
        deleteSchedule.add(polecenie);
    }

    @Override
    public void reverseChanges(){
        deleteSchedule.clear();
    }

    public BuildSurgeon getBuildSurgeon(){return buildSurgeon;}
}
