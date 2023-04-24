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
public class Manager extends User implements Serializable {
    private String email;
    private LocalDate employmentDate;
    @ManyToMany
    private List<Destination> myManagedDestination;
    private Boolean isAdmin;

    public Manager(String login, String password, String name, String surname, LocalDate birthDate, String phoneNumber, String email, LocalDate employmentDate, boolean isAdmin) {
        super(login, password, name, surname, birthDate, phoneNumber);
        this.email = email;
        this.employmentDate = employmentDate;
        this.isAdmin = isAdmin;
        myManagedDestination = new ArrayList<>();
    }

    public Manager(String login, String password, String name, String surname, LocalDate birthDate, String phoneNumber, String email) {
        super(login, password, name, surname, birthDate, phoneNumber);
        this.email = email;
        this.employmentDate = LocalDate.now();
        this.isAdmin = false;
        myManagedDestination = new ArrayList<>();
    }

    @Override
    public String toString() {
        return name +" "+ surname;
    }
}
