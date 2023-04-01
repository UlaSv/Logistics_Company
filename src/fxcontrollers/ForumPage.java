package fxcontrollers;

import hib.UserHibernateController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.*;
import utils.FxUtils;

import javax.persistence.EntityManagerFactory;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class ForumPage {
    @FXML
    public TreeView<Comment> commentTreeView;
    @FXML
    public TextArea commentField;
    @FXML
    public ListView<Forum> forumListView;
    @FXML
    public TextField forumTitleField;

    private User user;
    private Forum selectedForum;
    private EntityManagerFactory entityManagerFactory;
    private UserHibernateController userHibernateController;
    private TreeItem<Comment> selectedItem;

    public void setInfo(User user, EntityManagerFactory entityManagerFactory) {
        this.user = user;
        this.entityManagerFactory = entityManagerFactory;
        userHibernateController = new UserHibernateController(this.entityManagerFactory);
        loadForums();
        this.commentTreeView = new TreeView<>();
        loadCommentTreeView();
    }

    public void loadForums() {
        forumListView.getItems().clear();
        List<Forum> forumList = userHibernateController.getAllRecords(Forum.class);
        ObservableList<Forum> observableForumList = FXCollections.observableArrayList();
        observableForumList.addAll(forumList);
        forumListView.setItems(observableForumList);
    }

    public void loadCommentTreeView() {
        List<Comment> comments = userHibernateController.getAllRecords(Comment.class);
    }

    public void createComment(Comment comment) {
        if (selectedItem == null) {
            commentTreeView = new TreeView<>();
            commentTreeView.setRoot(new TreeItem<>(comment));
            commentTreeView.refresh();
        } else {
            selectedItem.getChildren().add(new TreeItem<>(comment));
            selectedItem.setExpanded(true);
        }
    }

    public void commentButton() {
        if (commentField.getText().isEmpty()) {
            FxUtils.generateAlert(Alert.AlertType.WARNING, "Comment", "Error", "Please write a comment");
        } else if (selectedForum == null) {
            FxUtils.generateAlert(Alert.AlertType.WARNING, "Comment", "Error", "Please select a forum");
        } else {
            Comment comment = new Comment(selectedForum, user, commentField.getText());
            createComment(comment);
            userHibernateController = new UserHibernateController(entityManagerFactory);
            userHibernateController.save(comment);
            loadCommentTreeView();
            commentField.clear();
        }
    }

    public void createForumButton() {
        if (forumTitleField.getText().isEmpty()) {
            FxUtils.generateAlert(Alert.AlertType.WARNING, "Forum", "Error", "Please enter forum title");
        } else {
            Forum forum = new Forum(forumTitleField.getText(), user);
            userHibernateController = new UserHibernateController(entityManagerFactory);
            userHibernateController.save(forum);
            loadForums();
            forumTitleField.clear();
            forumListView.refresh();
        }
    }

    public void selectForum() {
        selectedForum = forumListView.getSelectionModel().getSelectedItem();
    }


    public void commentSelected() {
        try {
            selectedItem = commentTreeView.getSelectionModel().getSelectedItem();
        } catch (NullPointerException ex) {
            System.out.println("Error selecting comment: " + ex.getMessage());
        }
    }


}

