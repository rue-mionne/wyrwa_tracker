package els.genAlgImpl;

import java.sql.Connection;

public class PackageRangeSQLConnectionHolder {
    static Connection con;
    static public void setConnection(Connection connection){
        con=connection;
    }
}
