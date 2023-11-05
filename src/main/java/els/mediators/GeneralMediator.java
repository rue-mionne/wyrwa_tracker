package els.mediators;

import els.data.Account;
import els.data.Postac;
import els.sqliteIO.NoActiveConnectionException;
import els.sqliteIO.SQLiteConnector;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GeneralMediator {
    SQLiteConnector connector;
    public GeneralMediator(SQLiteConnector connector){
        this.connector=connector;
    }

    public void initialize(Account konto) throws NoActiveConnectionException, SQLException {
        Connection pol = connector.getActiveConnection();
        Statement query = pol.createStatement();
        ResultSet rs = query.executeQuery("Select Count(Characters.ID) AS Count FROM Characters");
        int count = rs.getInt("Count");
        for(int i=0;i<count;i++){
            konto.DodajPostac(CharacterSQLMediator.ReadCharacter(i+1, connector));
            Postac postacTestowa = konto.PobierzPostac(i);
            CharacterSQLMediator.ReadBuildSet(postacTestowa,connector);
        }

    }
}
