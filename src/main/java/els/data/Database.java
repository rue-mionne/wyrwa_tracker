package els.data;

import els.sqliteIO.SQLiteConnector;

public class Database {
    public Account konto;
    public DungeonTree drzewoPlansz;
    public ItemDatabase bazaPrzedmiotow=new ItemDatabase();

    public QuestDatabase bazaZadan=new QuestDatabase();

    public SQLiteConnector sqlCon;
}
