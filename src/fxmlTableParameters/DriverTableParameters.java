package fxmlTableParameters;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import java.time.LocalDate;

public class DriverTableParameters {
    private SimpleIntegerProperty driverId = new SimpleIntegerProperty();
    private SimpleStringProperty driversLicenceNumber = new SimpleStringProperty();
    private SimpleStringProperty emergencyContactPhoneNumber = new SimpleStringProperty();
    private SimpleStringProperty medicalInsurance = new SimpleStringProperty();
    private ObjectProperty<LocalDate> medicalCheckDate = new SimpleObjectProperty<>();

    public DriverTableParameters(SimpleIntegerProperty driverId, SimpleStringProperty driversLicenceNumber, SimpleStringProperty emergencyContactPhoneNumber, SimpleStringProperty medicalInsurance, ObjectProperty<LocalDate> medicalCheckDate) {
        this.driverId = driverId;
        this.driversLicenceNumber = driversLicenceNumber;
        this.emergencyContactPhoneNumber = emergencyContactPhoneNumber;
        this.medicalInsurance = medicalInsurance;
        this.medicalCheckDate = medicalCheckDate;
    }

    public DriverTableParameters() {
    }

    public int getDriverId() {
        return driverId.get();
    }

    public SimpleIntegerProperty driverIdProperty() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId.set(driverId);
    }

    public String getDriversLicenceNumber() {
        return driversLicenceNumber.get();
    }

    public SimpleStringProperty driversLicenceNumberProperty() {
        return driversLicenceNumber;
    }

    public void setDriversLicenceNumber(String driversLicenceNumber) {
        this.driversLicenceNumber.set(driversLicenceNumber);
    }

    public String getEmergencyContactPhoneNumber() {
        return emergencyContactPhoneNumber.get();
    }

    public SimpleStringProperty emergencyContactPhoneNumberProperty() {
        return emergencyContactPhoneNumber;
    }

    public void setEmergencyContactPhoneNumber(String emergencyContactPhoneNumber) {
        this.emergencyContactPhoneNumber.set(emergencyContactPhoneNumber);
    }

    public String getMedicalInsurance() {
        return medicalInsurance.get();
    }

    public SimpleStringProperty medicalInsuranceProperty() {
        return medicalInsurance;
    }

    public void setMedicalInsurance(String medicalInsurance) {
        this.medicalInsurance.set(medicalInsurance);
    }

    public LocalDate getMedicalCheckDate() {
        return medicalCheckDate.get();
    }

    public ObjectProperty<LocalDate> medicalCheckDateProperty() {
        return medicalCheckDate;
    }

    public void setMedicalCheckDate(LocalDate medicalCheckDate) {
        this.medicalCheckDate.set(medicalCheckDate);
    }
}
