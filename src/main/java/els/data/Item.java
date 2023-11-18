package els.data;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;

public class Item implements Cloneable {
    int ID;
    StringProperty Name;
    IntegerProperty Amount;
    IntegerProperty SalePrice;
    boolean Sellable;
    boolean Shareable;

    public Item() {
    }

    //TEMPORARY CONSTRUCTOR FOR TESTING, NEED DATABASE INTERFACE
    public Item(int ID, String name, int amount, int salePrice, boolean sellable, boolean shareable) {
        this.ID = ID;
        Name = new SimpleStringProperty(name);
        Amount = new SimpleIntegerProperty(amount);
        SalePrice = new SimpleIntegerProperty(salePrice);
        Sellable = sellable;
        Shareable = shareable;
    }

    public StringProperty getName() {
        return Name;
    }

    public void setName(String name) {
        Name.setValue(name);
    }

    public IntegerProperty getAmount() {
        return Amount;
    }

    public Item setAmount(int amount) {
        Amount.setValue(amount);
        return this;
    }

    public IntegerProperty getSalePrice() {
        return SalePrice;
    }

    public void setSalePrice(int salePrice) {
        SalePrice.setValue(salePrice);
    }

    public boolean isSellable() {
        return Sellable;
    }

    public void setSellable(boolean sellable) {
        Sellable = sellable;
    }

    public boolean isShareable() {
        return Shareable;
    }

    public void setShareable(boolean shareable) {
        Shareable = shareable;
    }

    public Item clone() throws CloneNotSupportedException{
        return (Item)super.clone();
    }
//placeholder for icon
}
