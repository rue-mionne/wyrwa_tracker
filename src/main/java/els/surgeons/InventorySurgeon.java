package els.surgeons;

import els.data.Database;
import els.data.Inventory;
import els.data.Item;
import els.data.Postac;
import els.sqliteIO.NoActiveConnectionException;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

public class InventorySurgeon implements IUpdateSurgeon, IInsertSurgeon{
    ArrayList<String> insertSchedule = new ArrayList<>();
    boolean modified=false;
    Inventory eq=null;
    Database baza = null;

    public InventorySurgeon(Database baza){this.baza=baza;}
    @Override
    public void scheduleInsert(Object inserted) throws NoActiveConnectionException, SQLException {
        Inventory inventory = (Inventory) inserted;
        Postac owner = inventory.getOwner();
        String polecenie = "Insert into InventoryData (OwnerID,ItemID, Amount) values ("+owner.getID()+", "+inventory.getLastInserted()+", "+inventory.pobierzPrzedmiot(inventory.getLastInserted()).getAmount().getValue()+");";
        insertSchedule.add(polecenie);
    }

    @Override
    public void proceed() throws NoActiveConnectionException, SQLException {
        Connection con = baza.sqlCon.getActiveConnection();
        Statement query = con.createStatement();
        for(String polecenie: insertSchedule){
            query.execute(polecenie);
        }
        if(modified){
            int wsk=0;
            for(Integer itemID:eq.getEditModeList()){
                if(itemID==1){
                    Item modifiedItem = eq.getListaPrzedmiotow().get(wsk);
                    Postac owner = eq.getOwner();
                    String polecenie = "Update InventoryData set Amount=" +modifiedItem.getAmount().getValue()+" where OwnerID=" +owner.getID()+" and ItemID=" +modifiedItem.getID()+";";
                    query.execute(polecenie);
                    polecenie = "Update ItemData set SalePrice="+modifiedItem.getSalePrice().getValue()+" where ItemID="+modifiedItem.getID()+";";
                    query.execute(polecenie);
                    itemID=0;
                }
                wsk++;
            }
            modified=false;
        }
    }

    @Override
    public void scheduleUpdate(Object updated) throws NoActiveConnectionException, SQLException {
        if(modified==false)
            eq = (Inventory) updated;
        modified=true;
    }
}
