package els.surgeons;

import els.sqliteIO.NoActiveConnectionException;

import java.sql.SQLException;

public interface IUpdateSurgeon extends ISurgeon{
    void scheduleUpdate(Object updated)throws NoActiveConnectionException, SQLException;
}
