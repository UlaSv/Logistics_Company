package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Cargo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    @Enumerated
    private CargoType cargoType;
    private LocalDate dateRegistered;
    private double weight;
    private boolean fragile;
    @OneToOne
    private Destination destination;

    public Cargo(String title, CargoType cargoType, LocalDate dateRegistered, double weight, boolean fragile) {
        this.title = title;
        this.cargoType = cargoType;
        this.dateRegistered = dateRegistered;
        this.weight = weight;
        this.fragile = fragile;
        this.destination = null;
    }

    public Cargo(String title, CargoType cargoType, double weight, boolean fragile) {
        this.title = title;
        this.cargoType = cargoType;
        this.weight = weight;
        this.dateRegistered = LocalDate.now();
        this.fragile = fragile;
        this.destination = null;
    }

    @Override
    public String toString() {
        return "\"" + title + "\" " + dateRegistered;
    }
}
