package els.sqliteIO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.jar.Attributes;

import els.filehandlers.SQLFileHandler;


public class SQLiteConnector {
                Connection activeConnection;
                public void ConnectToBase(String databaseName) {
                        Connection connection = null;
                        try {
                                connection = DriverManager.getConnection("jdbc:sqlite:db/"+ databaseName);
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

                public void InitiateDatabase(String template){
                        SQLFileHandler szablonImporter = new SQLFileHandler();
                        szablonImporter.Inicjuj(template);
                        LinkedList<String> listaPolecen = szablonImporter.getStatementList();
                        listaPolecen.forEach((polecenie)->EgzekwujPolecenie(polecenie));
                }

                private void EgzekwujPolecenie(String polecenie){
                    try{
                    Statement query = activeConnection.createStatement();
                        System.out.println(polecenie);
                    query.execute(polecenie);

                    }
                    catch(SQLException e){
                        System.err.println(e.getMessage());
                    }
                }
}
