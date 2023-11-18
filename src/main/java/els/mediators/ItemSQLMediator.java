package els.mediators;

import els.data.*;
import els.sqliteIO.NoActiveConnectionException;
import els.sqliteIO.SQLiteConnector;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ItemSQLMediator {
    public static Connection con=null;
    public static void loadInventory(Postac postac, SQLiteConnector connect) throws NoActiveConnectionException, SQLException {
        con=connect.getActiveConnection();
        Statement query = con.createStatement();
        ResultSet rs = query.executeQuery("Select * from InventoryData where OwnerID="+postac.getID()+";");
        while(rs.next()){
            Item przedmiot = loadItem(rs.getInt("ItemID"));
            przedmiot.setAmount(rs.getInt("Amount"));
            postac.PobierzListePrzedmiotow().dodajPrzedmiot(przedmiot);
        }
    }

    public static Item loadItem(int ID){
        if(con==null){
            System.err.println("Connection not initiated;");
            return null;
        }
        else{
            try{
                Statement query = con.createStatement();
                ResultSet rs = query.executeQuery("Select * from ItemData where ItemID="+ID+";");
                return new Item(rs.getInt("ItemID"), rs.getString("ItemName"),0,rs.getInt("SalePrice"), Boolean.valueOf(String.valueOf(rs.getInt("Sellable"))), Boolean.valueOf(String.valueOf(rs.getInt("Shareable"))));
            }
            catch(SQLException e){
                System.err.println("SuckQL in ItemMed");
                return null;
            }
        }
    }
}
