package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Generated;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public abstract class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;
    @Column(unique = true)
    protected String login;
    protected String password;
    protected String name;
    protected String surname;
    protected LocalDate birthDate;
    protected String phoneNumber;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Forum> myForums;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Comment> myComments;

    public User(String login, String password, String name, String surname, LocalDate birthDate, String phoneNumber) {
        this.login = login;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
    }

    public void setProperty(String propertyName, String newValue) {
        switch (propertyName) {
            case "login" -> setLogin(newValue);
            case "password" -> setPassword(newValue);
            case "name" -> setName(newValue);
            case "surname" -> setSurname(newValue);
            case "birthday" -> setBirthDate(LocalDate.parse(newValue));
            case "phoneNumber" -> setPhoneNumber(newValue);
            default -> throw new IllegalArgumentException("Invalid property name: " + propertyName);
        }
    }
}
