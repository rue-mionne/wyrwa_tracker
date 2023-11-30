package els.surgeons;

import els.data.Database;
import els.data.Postac;
import els.sqliteIO.NoActiveConnectionException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.EnumSet;

public class CharacterSurgeon implements IUpdateSurgeon, IInsertSurgeon {
    Database baza=null;
    ArrayList<String> insertSchedule=new ArrayList<>();
    ArrayList<String> updateSchedule=new ArrayList<>();
    public CharacterSurgeon(Database baza){this.baza=baza;}

    @Override
    public void proceed() throws NoActiveConnectionException, SQLException {
        Connection con = baza.sqlCon.getActiveConnection();
        Statement query = con.createStatement();
        for(String polecenie: insertSchedule){
            query.execute(polecenie);
        }
        for(String polecenie: updateSchedule){
            query.execute(polecenie);
        }

    }

    @Override
    public void scheduleUpdate(Object updated) throws NoActiveConnectionException, SQLException{
        Postac postac=(Postac) updated;
        int ID = postac.getID();
        Connection con = baza.sqlCon.getActiveConnection();
        Statement query = con.createStatement();
        ResultSet rs = query.executeQuery("Select Classes.ClassID from Classes where ClassName='"+postac.getClassName()+"';");
        int classID = rs.getInt("ClassID");
        String polecenie;
        if(classID!=0)
            polecenie = "Update Characters set Name=" + postac.getIGN() + ", ClassID= "+classID+";";
        else
            polecenie = "Update Characters set Name=" + postac.getIGN() + ";";
        //update Equipment
        //update Builds
    }

    @Override
    public void scheduleInsert(Object inserted) throws NoActiveConnectionException, SQLException {
        Postac postac = (Postac) inserted;
        String polecenie = "INSERT INTO \"Characters\" (\"Name\") VALUES ('"+postac.getIGN()+"');";
        insertSchedule.add(polecenie);
    }
}
