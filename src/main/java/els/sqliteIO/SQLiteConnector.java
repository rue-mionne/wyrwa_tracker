package els.sqliteIO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.jar.Attributes;

public class SQLiteConnector {
                Connection activeConnection;
                public void ConnectToBase(String databaseName) {
                        Connection connection = null;
                        try {
                                connection = DriverManager.getConnection(databaseName);
                                Statement statement = connection.createStatement();
                                statement.setQueryTimeout(30);
                                activeConnection=connection;
                        }
                        catch(SQLException e){
                            System.err.println(e.getMessage());
                        }
                        finally {

                        }
                }
                public Connection getActiveConnection() throws NoActiveConnectionException{
                    if(activeConnection==null){
                        throw new NoActiveConnectionException();
                    }
                    return activeConnection;
                }
}
