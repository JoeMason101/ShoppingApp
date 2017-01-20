package sb.basket.model;

import java.text.DecimalFormat;
import java.util.ArrayList;

import sb.basket.MainApp;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Model class for a Basket items.
 */
public class Items {

    private final StringProperty name;
    private final IntegerProperty quantity;
    private final DoubleProperty price;
    private final ReadOnlyDoubleWrapper total;

      public Items() {
          this(null,0,0); 
          }


    public Items(String name, int quantity, double price) {
        this.name = new SimpleStringProperty(name);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.price = new SimpleDoubleProperty(price);
        this.total = new ReadOnlyDoubleWrapper();
        NumberBinding multiplication = Bindings.multiply(
        this.priceProperty(), this.quantityProperty());
        this.total.bind(multiplication);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public int getQuantity() {
        return quantity.get();
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }

    public IntegerProperty quantityProperty() {
        return quantity;
    }

    public double getPrice() {
        return price.get();
    }

    public void setPrice(double price) {
        this.price.set(price);
    }

    public DoubleProperty priceProperty() {
        return price;
    }

    @XmlTransient
    public double getTotal() {
        return total.get();
    }

    public ReadOnlyDoubleProperty totalProperty() {
        return total.getReadOnlyProperty();
    }

}