package fxmlTableParameters;

import javafx.beans.property.*;

import java.time.LocalDate;
import java.text.SimpleDateFormat;

public class ManagerTableParameters {
    private SimpleIntegerProperty managerId = new SimpleIntegerProperty();
    private SimpleStringProperty email = new SimpleStringProperty();
    private ObjectProperty<LocalDate> employmentDate = new SimpleObjectProperty<>();
    private SimpleBooleanProperty isAdmin = new SimpleBooleanProperty();

    public ManagerTableParameters(SimpleIntegerProperty managerId, SimpleStringProperty email, ObjectProperty<LocalDate> employmentDate, SimpleBooleanProperty isAdmin) {
        this.managerId = managerId;
        this.email = email;
        this.employmentDate = employmentDate;
        this.isAdmin = isAdmin;
    }

    public ManagerTableParameters() {
    }

    public int getManagerId() {
        return managerId.get();
    }

    public SimpleIntegerProperty managerIdProperty() {
        return managerId;
    }

    public void setManagerId(int managerId) {
        this.managerId.set(managerId);
    }

    public String getEmail() {
        return email.get();
    }

    public SimpleStringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public LocalDate getEmploymentDate() {
        return employmentDate.get();
    }

    public ObjectProperty<LocalDate> employmentDateProperty() {
        return employmentDate;
    }

    public void setEmploymentDate(LocalDate employmentDate) {
        this.employmentDate.set(employmentDate);
    }

    public boolean isIsAdmin() {
        return isAdmin.get();
    }

    public SimpleBooleanProperty isAdminProperty() {
        return isAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin.set(isAdmin);
    }
}
