package els.surgeons;

import els.data.Build;
import els.data.Database;
import els.sqliteIO.NoActiveConnectionException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class BuildSurgeon implements IUpdateSurgeon, IInsertSurgeon{
        ArrayList<Build> insertSchedule = new ArrayList<>();
        ArrayList<Build> updateSchedule = new ArrayList<>();
        Database baza=null;

    public BuildSurgeon(Database baza){this.baza=baza;}
    @Override
    public void scheduleInsert(Object inserted) throws NoActiveConnectionException, SQLException {
        Build build = (Build) inserted;
        insertSchedule.add(build);
    }

    @Override
    public void proceed() throws NoActiveConnectionException, SQLException {
        Connection con = baza.sqlCon.getActiveConnection();
        Statement query = con.createStatement();
        for(Build build: insertSchedule){
            String polecenie = "Insert into BuildSetsTable (CharacterID,BuildName) values ("+ build.getOwnerID() +",'"+build.Name +"')";
            query.execute(polecenie);
            ResultSet rs = query.executeQuery("Select BuildSetsTable.BuildSetID from BuildSetsTable order by BuildSetID desc limit 1;");
            build.ID=rs.getInt("BuildSetID");}
        insertSchedule.clear();
        for(Build build: updateSchedule){
            String polecenie = "Update BuildSetsTable set EstCP=" + build.EstCP + ", EXPMultip=" + build.EXPMultip + ", EDMultip=" + build.EDMultip + ", IDMultip=" + build.IDMultip + " where BuildSetID=" + build.ID + ";";
            query.execute(polecenie);
            System.out.println(polecenie);
        }
        updateSchedule.clear();
    }

    @Override
    public void scheduleUpdate(Object updated) throws NoActiveConnectionException, SQLException {
        Build build = (Build) updated;
        if(!updateSchedule.contains(build))
            updateSchedule.add(build);
    }
}
