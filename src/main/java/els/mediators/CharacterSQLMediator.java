package els.mediators;

import els.sqliteIO.*;
import els.data.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CharacterSQLMediator {
    public static Postac ReadCharacter(int CharacterID, SQLiteConnector connector) throws NoActiveConnectionException, SQLException {
        Connection polaczenie = connector.getActiveConnection();
        Statement query = polaczenie.createStatement();
        ResultSet rs = query.executeQuery("Select Characters.ID, Characters.Name, NameTable.CharacterName, Classes.ClassName, Classes.ShortClassName, Classes.DPSRating from Characters left join Classes on Characters.ClassID=Classes.ClassID left join NameTable on Classes.CharacterName=NameTable.ID WHERE Characters.ID=" + CharacterID +";");
        Postac postac = new Postac(rs.getInt("ID"),rs.getString("Name"), rs.getString("CharacterName"), rs.getString("ClassName"), rs.getString("ShortClassName"), rs.getFloat("DPSRating"));
        return postac;
    }

    public static void ReadBuildSet(Postac postac, SQLiteConnector connector) throws NoActiveConnectionException, SQLException{
        Connection polaczenie = connector.getActiveConnection();
        Statement query = polaczenie.createStatement();
        ResultSet rs = query.executeQuery("Select BuildSetsTable.BuildName AS BuildName, BuildSetsTable.EDMultip AS EDMultip, BuildSetsTable.EXPMultip AS EXPMultip, BuildSetsTable.EstCP AS ESTCp, BuildSetsTable.IDMultip AS IDMultip from BuildSetsTable INNER JOIN Characters ON BuildSetsTable.CharacterID=Characters.ID WHERE ID="+postac.getID()+";");
        while(rs.next()){
            postac.PobierzDaneBuildow().AddNewBuildSet(ReadBuild(postac,rs));
        }
    }

    public static Build ReadBuild(Postac postac, ResultSet rs) throws SQLException{

        Build build = new Build(rs.getString("BuildName"),rs.getInt("EstCP"),rs.getInt("EXPMultip"),rs.getInt("EDMultip"),rs.getInt("IDMultip"), postac.getID());
        return build;
    }


    //UWAGA Item jest częścią należącą do ekwipunku, ale konieczną do zapełnienia innych modułów; przeanalizuj, jaki mediator go weźmie
    //UWAGA2 planując, patrz na powiązania - jeżeli element jest kluczem zewnętrznym, prawdopodobnie tabela potrzebuje mediatora pod ten klucz
}


