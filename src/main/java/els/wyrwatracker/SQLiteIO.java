package els.wyrwatracker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.jar.Attributes;

public class SQLiteIO {
                public static void ConnectToBase() {
                        Connection connection = null;
                        try {
                                connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/Wyrwa_Tracker_Data");
                                Statement statement = connection.createStatement();
                                statement.setQueryTimeout(30);
                        }
                        catch(SQLException e){
                            System.err.println(e.getMessage());
                        }
                        finally {

                        }
                }
}
