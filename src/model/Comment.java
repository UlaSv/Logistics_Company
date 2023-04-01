package model;

import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private LocalDate dateCreated;
    private LocalDate dateModified;
    @ManyToOne
    private Forum parentForum;
    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Comment> replies;
    @ManyToOne
    private User user;
    private String commentText;

    public Comment(Forum parentForum, User user, String commentText) {
        this.dateCreated = LocalDate.now();
        this.parentForum = parentForum;
        this.user = user;
        this.commentText = commentText;
        replies = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "\"" + commentText + "\" by" + user;
    }
}
