package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Forum {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private LocalDate dateCreated;
    private LocalDate dateModified;
    @OneToMany(mappedBy = "parentForum", cascade = CascadeType.ALL)
    private List<Comment> comments;
    @ManyToOne
    private User user;

    public Forum(String title, User user) {
        this.title = title;
        this.dateCreated = LocalDate.now();
        this.user = user;
        this.comments = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "\"" + title + "\" by " + user;
    }
}
