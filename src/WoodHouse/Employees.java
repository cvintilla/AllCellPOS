package WoodHouse;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class Employees {

    //variables for employee object
    private SimpleStringProperty time;
    private SimpleStringProperty name;

    //employee constructor
    public Employees( String type, String hours) {
        this.time = new SimpleStringProperty(hours);
        this.name = new SimpleStringProperty (type);
    }

    //getter and setter for time
    public String getTime(){ return time.get();}
    public void setTime(String time){
        this.time.set(time);
    }

    //getter and setter for name
    public String getName() {
        return name.get();
    }
    public void setName(String name) {
        this.name.set(name);
    }

}