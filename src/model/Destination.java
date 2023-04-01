package model;

import hib.UserHibernateController;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
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
    @ManyToOne
    private Driver driver;
    @ManyToMany(mappedBy = "myManagedDestination", cascade = CascadeType.ALL)
    private List<Manager> responsibleManagers;
    @OneToOne(mappedBy = "destination", cascade = CascadeType.ALL)
    private Cargo cargo;
    @OneToMany(mappedBy = "destination", cascade = CascadeType.ALL)
    private List<Checkpoint> checkpoints;
    @OneToOne(mappedBy = "currentDestination", cascade = CascadeType.ALL)
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
    }

    public Destination(String startCity, long startLn, long startLat, long endLat, long endLn, String endCity, List<Manager> responsibleManagers, List<Checkpoint> checkpoints) {
        this.startCity = startCity;
        this.startLn = startLn;
        this.startLat = startLat;
        this.endLat = endLat;
        this.endLn = endLn;
        this.endCity = endCity;
        this.responsibleManagers = responsibleManagers;
        this.checkpoints = checkpoints;
    }
    public void setProperty(String propertyName, String newValue) {
        switch (propertyName) {
            case "startCity" -> setStartCity(newValue);
            case "endCity" -> setEndCity(newValue);
            default -> throw new IllegalArgumentException("Invalid property name: " + propertyName);
        }
    }
}
