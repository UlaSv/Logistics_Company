package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Destination {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String startCity;
    private String endCity;
    private long startLn;
    private long startLat;
    private long endLat;
    private long endLn;
    @Enumerated
    private StatusType status;
    private Date departureDate;
    private Date arrivalDate;
    @ManyToOne
    private Driver driver;
    @ManyToMany(mappedBy = "myManagedDestination", cascade = {CascadeType.DETACH, CascadeType.REMOVE})
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Manager> responsibleManagers;
    @OneToOne(mappedBy = "destination", cascade = CascadeType.MERGE)
    private Cargo cargo;
    @OneToMany(mappedBy = "destination", cascade = CascadeType.MERGE, orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Checkpoint> checkpoints;
    @OneToOne(mappedBy = "currentDestination", cascade = {CascadeType.MERGE})
    private Truck truck;

    public Destination(String startCity, long startLn, long startLat, long endLat, long endLn, String endCity, Driver driver, List<Manager> responsibleManagers, Cargo cargo, List<Checkpoint> checkpoints, Truck truck) {
        this.startCity = startCity;
        this.startLn = startLn;
        this.startLat = startLat;
        this.endLat = endLat;
        this.endLn = endLn;
        this.endCity = endCity;
        this.driver = driver;
        this.responsibleManagers = responsibleManagers;
        this.cargo = cargo;
        this.checkpoints = checkpoints;
        this.truck = truck;
        status = StatusType.READY;
    }

    public Destination(String startCity, String endCity, long startLn, long startLat, long endLat, long endLn, Date departureDate, Date arrivalDate, Driver driver, List<Manager> responsibleManagers, Cargo cargo, List<Checkpoint> checkpoints) {
        this.startCity = startCity;
        this.endCity = endCity;
        this.startLn = startLn;
        this.startLat = startLat;
        this.endLat = endLat;
        this.endLn = endLn;
        this.departureDate = departureDate;
        this.arrivalDate = arrivalDate;
        this.driver = driver;
        this.responsibleManagers = responsibleManagers;
        this.cargo = cargo;
        this.checkpoints = checkpoints;
        status = StatusType.WAITING_FOR_INFO;
    }


    public void setProperty(String propertyName, String newValue) {
        switch (propertyName) {
            case "startCity" -> setStartCity(newValue);
            case "endCity" -> setEndCity(newValue);
            default -> throw new IllegalArgumentException("Invalid property name: " + propertyName);
        }
    }
}
