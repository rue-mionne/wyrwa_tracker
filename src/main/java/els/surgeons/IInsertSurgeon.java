package els.surgeons;

import els.sqliteIO.NoActiveConnectionException;

import java.sql.SQLException;

public interface IInsertSurgeon extends ISurgeon{
    void scheduleInsert(Object inserted) throws NoActiveConnectionException, SQLException;
}
