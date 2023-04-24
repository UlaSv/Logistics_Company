package fxcontrollers;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import model.Driver;
import model.StatusType;
import model.Truck;

public class DestinationTableParameters {
    private SimpleIntegerProperty destinationId = new SimpleIntegerProperty();
    private SimpleStringProperty startCity = new SimpleStringProperty();
    private SimpleStringProperty endCity = new SimpleStringProperty();
    private ObjectProperty<Driver> driver = new SimpleObjectProperty<>();
    private ObjectProperty<Truck> truck = new SimpleObjectProperty<>();
    private SimpleObjectProperty<StatusType> orderStatus = new SimpleObjectProperty<>();

    public DestinationTableParameters(SimpleIntegerProperty destinationId, SimpleStringProperty startCity, SimpleStringProperty endCity, ObjectProperty<Driver> driver, ObjectProperty<Truck> truck, SimpleObjectProperty<StatusType> orderStatus) {
        this.destinationId = destinationId;
        this.startCity = startCity;
        this.endCity = endCity;
        this.driver = driver;
        this.truck = truck;
        this.orderStatus = orderStatus;
    }

    public DestinationTableParameters() {
    }

    public int getDestinationId() {
        return destinationId.get();
    }

    public SimpleIntegerProperty destinationIdProperty() {
        return destinationId;
    }

    public void setDestinationId(int destinationId) {
        this.destinationId.set(destinationId);
    }

    public String getStartCity() {
        return startCity.get();
    }

    public SimpleStringProperty startCityProperty() {
        return startCity;
    }

    public void setStartCity(String startCity) {
        this.startCity.set(startCity);
    }

    public String getEndCity() {
        return endCity.get();
    }

    public SimpleStringProperty endCityProperty() {
        return endCity;
    }

    public void setEndCity(String endCity) {
        this.endCity.set(endCity);
    }

    public Driver getDriver() {
        return driver.get();
    }

    public ObjectProperty<Driver> driverProperty() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver.set(driver);
    }

    public Truck getTruck() {
        return truck.get();
    }

    public ObjectProperty<Truck> truckProperty() {
        return truck;
    }

    public void setTruck(Truck truck) {
        this.truck.set(truck);
    }

    public StatusType getOrderStatus() {
        return orderStatus.get();
    }

    public SimpleObjectProperty<StatusType> orderStatusProperty() {
        return orderStatus;
    }

    public void setOrderStatus(StatusType orderStatus) {
        this.orderStatus.set(orderStatus);
    }
    public void setProperty(String propertyName, String newValue) {
        switch (propertyName) {
            case "startCity" -> setStartCity(newValue);
            case "endCity" -> setEndCity(newValue);
            default -> throw new IllegalArgumentException("Invalid property name: " + propertyName);
        }
    }
}