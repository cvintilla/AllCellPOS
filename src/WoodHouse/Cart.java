package WoodHouse;

import javafx.beans.property.SimpleStringProperty;

import java.math.BigDecimal;

public class Cart {
    //private objects for Cart class
    private SimpleStringProperty itemPrice;
    private SimpleStringProperty itemDescription;

    private String itemName;
    private String phoneName;
    private String itemColor;
    //cart Constructor
    public Cart( String type, String price, String itemName, String pName, String color) {
        this.itemPrice = new SimpleStringProperty(price);
        this.itemDescription = new SimpleStringProperty (type);
        this.itemName = itemName;
        this.phoneName = pName;
        this.itemColor = color;
    }

    //getter and setter for ItemPrice
    public String getItemPrice() {
        return itemPrice.get();
    }
    public void setItemPrice(String itemPrice) {
        this.itemPrice.set(itemPrice);
    }

    //getter and setter for ItemDescription
    public String getItemDescription() {
        return itemDescription.get();
    }
    public void setItemDescription(String itemDescription) {
        this.itemDescription.set(itemDescription);
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getPhoneName() {
        return phoneName;
    }

    public void setPhoneName(String phoneName) {
        this.phoneName = phoneName;
    }

    public String getItemColor() {
        return itemColor;
    }

    public void setItemColor(String itemColor) {
        this.itemColor = itemColor;
    }

}


