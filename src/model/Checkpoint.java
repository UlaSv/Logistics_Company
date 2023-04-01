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
public class Checkpoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String place;
    private LocalDate arrivalDate;
    private boolean longStop;
    @ManyToOne
    private Destination destination;

    public Checkpoint(String place, LocalDate arrivalDate, boolean longStop, Destination destination) {
        this.place = place;
        this.arrivalDate = arrivalDate;
        this.longStop = longStop;
        this.destination = destination;
    }

    public Checkpoint(String place, boolean longStop) {
        this.place = place;
        this.longStop = longStop;
        this.destination = null;
    }

    public Checkpoint(String place, LocalDate arrivalDate, boolean longStop) {
        this.place = place;
        this.arrivalDate = arrivalDate;
        this.longStop = longStop;
    }

    @Override
    public String toString() {
        return place;
    }
}
