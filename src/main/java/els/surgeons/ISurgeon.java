package els.surgeons;

import els.sqliteIO.NoActiveConnectionException;

import java.sql.SQLException;

public interface ISurgeon {
    void proceed()throws NoActiveConnectionException, SQLException;
}
