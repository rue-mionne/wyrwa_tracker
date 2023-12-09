package els.data;

import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;

public class Item implements Cloneable {
    int ID;
    StringProperty Name;
    IntegerProperty Amount;
    FloatProperty Rate;
    IntegerProperty SalePrice;
    boolean Sellable;
    boolean Shareable;

    public Item() {
    }

    public Item(int ID, String name, int amount, int salePrice, boolean sellable, boolean shareable) {
        this.ID = ID;
        Name = new SimpleStringProperty(name);
        Amount = new SimpleIntegerProperty(amount);
        SalePrice = new SimpleIntegerProperty(salePrice);
        Rate = new SimpleFloatProperty(amount);
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

    public void setRate(Float newRate){
        Rate.setValue(newRate);
    }
    public FloatProperty getRate(){
        return Rate;
    }

    public Item clone() throws CloneNotSupportedException{
        return (Item)super.clone();
    }

    public int getID(){return ID;}
//placeholder for icon
}
