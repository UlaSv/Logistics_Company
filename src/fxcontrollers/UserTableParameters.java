package fxcontrollers;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;

public class UserTableParameters {
    private SimpleIntegerProperty userId = new SimpleIntegerProperty();
    private SimpleStringProperty login = new SimpleStringProperty();
    private SimpleStringProperty password = new SimpleStringProperty();
    private SimpleStringProperty name = new SimpleStringProperty();
    private SimpleStringProperty surname = new SimpleStringProperty();
    private SimpleStringProperty phoneNumber = new SimpleStringProperty();

    public UserTableParameters(SimpleIntegerProperty userId, SimpleStringProperty login, SimpleStringProperty password, SimpleStringProperty name, SimpleStringProperty surname, SimpleStringProperty phoneNumber) {
        this.userId = userId;
        this.login = login;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
    }

    public UserTableParameters() {
    }

    public int getUserId() {
        return userId.get();
    }

    public SimpleIntegerProperty userIdProperty() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId.set(userId);
    }

    public String getLogin() {
        return login.get();
    }

    public SimpleStringProperty loginProperty() {
        return login;
    }

    public void setLogin(String login) {
        this.login.set(login);
    }

    public String getPassword() {
        return password.get();
    }

    public SimpleStringProperty passwordProperty() {
        return password;
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getSurname() {
        return surname.get();
    }

    public SimpleStringProperty surnameProperty() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname.set(surname);
    }

    public String getPhoneNumber() { return phoneNumber.get(); }

    public SimpleStringProperty phoneNumberProperty() { return phoneNumber; }

    public void setPhoneNumber(String phoneNr) { this.phoneNumber.set(phoneNr); }

    public void setProperty(String propertyName, String newValue) {
        switch (propertyName) {
            case "login" -> setLogin(newValue);
            case "password" -> setPassword(newValue);
            case "name" -> setName(newValue);
            case "surname" -> setSurname(newValue);
            case "phoneNumber" -> setPhoneNumber(newValue);
            default -> throw new IllegalArgumentException("Invalid property name: " + propertyName);
        }
    }
}
