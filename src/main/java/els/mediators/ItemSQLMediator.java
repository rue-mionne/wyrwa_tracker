package els.mediators;

import els.data.*;
import els.sqliteIO.NoActiveConnectionException;
import els.sqliteIO.SQLiteConnector;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class ItemSQLMediator {
    public static Connection con=null;
    public static ItemDatabase database=null;
    public static void loadInventory(Postac postac, SQLiteConnector connect, ItemDatabase itemData) throws NoActiveConnectionException, SQLException {
        if(con==null)
            con=connect.getActiveConnection();
        if(database==null)
            database=itemData;
        Statement query = con.createStatement();
        ResultSet rs = query.executeQuery("Select * from InventoryData where OwnerID="+postac.getID()+";");
        while(rs.next()){
            Item przedmiot = database.PobierzPrzedmiot(rs.getInt("ItemID"));
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

    public static void fillRewardTable(Quest zadanie) throws SQLException{
        if(con==null||database==null){
            System.out.println("Program nie zostal wywolany poprawnie");
        }
        Statement query = con.createStatement();
        ResultSet rs = query.executeQuery("Select * from RewardData where QuestID="+zadanie.getID()+";");
        RewardTable tablicaNagrod = zadanie.getListaPrzedmiotow();
        Map<Integer,Float> kluczbaza = new HashMap<>();
        while(rs.next()){
            int ID =rs.getInt("ItemID");
            Float amount=rs.getFloat("Amount");
            kluczbaza.put(ID,amount);
        }
        tablicaNagrod.wczytajTablice(kluczbaza,database);
    }

}
