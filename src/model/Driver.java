package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Driver extends User implements Serializable {
    private String driversLicenceNumber;
    private String emergencyContactPhoneNumber;
    private String medicalInsurance;
    private LocalDate medicalCheckDate;
    @OneToMany(mappedBy = "owner", cascade = {CascadeType.DETACH, CascadeType.REMOVE})
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Truck> myOwnedTrucks;
    @OneToMany(mappedBy = "driver", cascade = {CascadeType.DETACH, CascadeType.REMOVE})
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Destination> myDestinations;

    public Driver(String login, String password, String name, String surname, LocalDate birthDate, String phoneNumber, String driversLicenceNumber, String emergencyContactPhoneNumber, String medicalInsurance, LocalDate medicalCheckDate) {
        super(login, password, name, surname, birthDate, phoneNumber);
        this.driversLicenceNumber = driversLicenceNumber;
        this.emergencyContactPhoneNumber = emergencyContactPhoneNumber;
        this.medicalInsurance = medicalInsurance;
        this.medicalCheckDate = medicalCheckDate;
        myDestinations = new ArrayList<>();
        myOwnedTrucks = new ArrayList<>();
    }

    @Override
    public String toString() {
        return  name;
    }
}
