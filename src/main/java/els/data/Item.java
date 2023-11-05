package els.data;

public class Item implements Cloneable {
    int ID;
    String Name;
    int Amount;
    int SalePrice;
    boolean Sellable;
    boolean Shareable;

    public Item() {
    }

    //TEMPORARY CONSTRUCTOR FOR TESTING, NEED DATABASE INTERFACE
    public Item(int ID, String name, int amount, int salePrice, boolean sellable, boolean shareable) {
        this.ID = ID;
        Name = name;
        Amount = amount;
        SalePrice = salePrice;
        Sellable = sellable;
        Shareable = shareable;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getAmount() {
        return Amount;
    }

    public Item setAmount(int amount) {
        Amount = amount;
        return this;
    }

    public int getSalePrice() {
        return SalePrice;
    }

    public void setSalePrice(int salePrice) {
        SalePrice = salePrice;
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
