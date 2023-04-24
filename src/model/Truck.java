package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Truck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String model;
    private int yearManufactured;
    private double odometer;
    private double tankCapacity;
    private double weight;
    @Enumerated
    private GearboxType gearbox;
    @Enumerated
    private TyreType tyreType;
    @OneToOne
    private Destination currentDestination;
    @ManyToOne
    private Driver owner;

    public Truck(String model, int yearManufactured, double odometer, double tankCapacity, double weight, GearboxType gearbox, TyreType tyreType) {
        this.model = model;
        this.yearManufactured = yearManufactured;
        this.odometer = odometer;
        this.tankCapacity = tankCapacity;
        this.weight = weight;
        this.gearbox = gearbox;
        this.tyreType = tyreType;
        this.currentDestination = null;
    }

    @Override
    public String toString() {
        return "("+model+") "+yearManufactured;
    }
}
