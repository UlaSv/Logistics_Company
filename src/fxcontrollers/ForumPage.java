package fxcontrollers;

import hib.HibernateController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.*;
import utils.FxUtils;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDate;
import java.util.*;

public class ForumPage {
    @FXML
    public TreeView<Comment> commentTreeView;
    @FXML
    public TextArea commentField;
    @FXML
    public ListView<Forum> forumListView;
    @FXML
    public TextField forumTitleField;
    @FXML
    public Button createForumButton;
    @FXML
    public Button updateForumButton;
    @FXML
    public Button deleteForumButton;
    @FXML
    public Button createCommentButton;
    @FXML
    public Button updateCommentButton;
    @FXML
    public Button deleteCommentButton;

    private User user;
    private Forum selectedForum;
    private EntityManagerFactory entityManagerFactory;
    private HibernateController hibernateController;
    private TreeItem<Comment> selectedComment;

    public void setInfo(User user, EntityManagerFactory entityManagerFactory) {
        this.user = user;
        this.entityManagerFactory = entityManagerFactory;
        hibernateController = new HibernateController(this.entityManagerFactory);
        if (user.getClass() == Manager.class && !((Manager) user).getIsAdmin() || user.getClass() == Manager.class ||user.getClass() == Driver.class)
            userFunctions();
        loadForums();
    }
    private void userFunctions() {
        updateForumButton.setVisible(false);
        deleteCommentButton.setVisible(false);
        updateCommentButton.setVisible(false);
        deleteForumButton.setVisible(false);
    }

    public void loadForums() {
        forumListView.getItems().clear();
        List<Forum> forumList = hibernateController.getAllRecords(Forum.class);
        ObservableList<Forum> observableForumList = FXCollections.observableArrayList();
        observableForumList.addAll(forumList);
        forumListView.setItems(observableForumList);
    }

    public void createComment() {
        if (commentField.getText().isEmpty()) {
            FxUtils.generateAlert(Alert.AlertType.WARNING, "Comment", "Error", "Please write a comment");
        } else if (selectedForum == null) {
            FxUtils.generateAlert(Alert.AlertType.WARNING, "Comment", "Error", "Please select a forum");
        } else {
            Comment parentComment = null;
            if (selectedComment != null) {
                parentComment = selectedComment.getValue();
                Comment comment = new Comment(selectedForum, user, commentField.getText(), parentComment);
                parentComment.getReplies().add(comment);
            }
            Comment comment = new Comment(selectedForum, user, commentField.getText(), parentComment);
            user.getMyComments().add(comment);
            hibernateController.save(comment);
            populateTreeView(hibernateController.getCommentsForForum(selectedForum.getId()));
            commentField.clear();
        }
    }

    public void populateTreeView(List<Comment> comments) {
        TreeItem<Comment> rootItem = new TreeItem<>(null);
        Map<Comment, TreeItem<Comment>> commentMap = new HashMap<>();
        // Create TreeItem for each comment and map it to its Comment object
        for (Comment comment : comments) {
            TreeItem<Comment> commentItem = new TreeItem<>(comment);
            commentMap.put(comment, commentItem);
            // If comment has no parent, add it as a root node to the TreeView
            if (comment.getParentComment() == null) {
                rootItem.getChildren().add(commentItem);
            }
        }
        // Add each comment as a child to its parent comment's TreeItem
        for (Comment comment : comments) {
            Comment parentComment = comment.getParentComment();
            if (parentComment != null) {
                TreeItem<Comment> parentItem = commentMap.get(parentComment);
                parentItem.getChildren().add(commentMap.get(comment));
            }
        }
        commentTreeView.setRoot(rootItem);
    }

    public void createForum() {
        if (forumTitleField.getText().isEmpty()) {
            FxUtils.generateAlert(Alert.AlertType.WARNING, "Forum", "Error", "Please enter forum title");
        } else {
            Forum forum = new Forum(forumTitleField.getText(), user);
            user.getMyForums().add(forum);
            hibernateController = new HibernateController(entityManagerFactory);
            hibernateController.save(forum);
            loadForums();
            forumTitleField.clear();
            forumListView.refresh();
        }
    }

    public void updateForum() {
        if (forumTitleField.getText().isEmpty()) {
            FxUtils.generateAlert(Alert.AlertType.WARNING, "Update Forum", "Error", "Please enter new forum title");
        } else {
            Forum forum = hibernateController.getEntityById(Forum.class, selectedForum.getId());
            forum.setTitle(forumTitleField.getText());
            forum.setDateModified(LocalDate.now());
            forum.setUser(user);
            hibernateController.update(forum);
            forumTitleField.clear();
            loadForums();
        }
    }

    public void deleteForum() {
        Forum forum = hibernateController.getEntityById(Forum.class, selectedForum.getId());
        List<Comment> commentsToDelete = new ArrayList<>();
        for (Comment comment : forum.getComments()) {
            collectCommentsToDelete(comment, commentsToDelete);
        }
        user.getMyForums().remove(forum);
        user.getMyComments().removeAll(commentsToDelete);

        hibernateController.update(user);
        //commentsToDelete.forEach(removeComment -> hibernateController.delete(removeComment));
        hibernateController.delete(forum);

        forumListView.getItems().remove(forum);
        forumListView.getSelectionModel().clearSelection();

        commentTreeView.setRoot(new TreeItem<>(null));
    }


    public void selectForum() {
        selectedForum = forumListView.getSelectionModel().getSelectedItem();
        if (selectedForum != null) {
            populateTreeView(hibernateController.getCommentsForForum(selectedForum.getId()));
        } else {
            FxUtils.generateAlert(Alert.AlertType.INFORMATION, "", "There is no forum here", "");
        }
    }


    public void commentSelected() {
        try {
            selectedComment = commentTreeView.getSelectionModel().getSelectedItem();
        } catch (NullPointerException ex) {
            System.out.println("Error selecting comment: " + ex.getMessage());
        }
    }


    public void updateComment() {
        if (commentField.getText().isEmpty()) {
            FxUtils.generateAlert(Alert.AlertType.WARNING, "Update Comment", "Error", "Please enter updated comment text");
        } else {
            Comment comment = hibernateController.getEntityById(Comment.class, selectedComment.getValue().getId());
            comment.setCommentText(commentField.getText());
            comment.setDateModified(LocalDate.now());
            comment.setUser(user);
            hibernateController.update(comment);
            commentField.clear();
            commentTreeView.refresh();
        }
    }

    public void deleteComment() {
        Comment comment = selectedComment.getValue();
        Comment parentComment = comment.getParentComment();
        if (parentComment != null) {
            parentComment.getReplies().remove(comment);
        }
        List<Comment> commentsToDelete = new ArrayList<>();
        collectCommentsToDelete(comment, commentsToDelete);
        user.getMyComments().removeAll(commentsToDelete);
        selectedComment.getParent().getChildren().remove(selectedComment);
        commentsToDelete.forEach(removeComment -> hibernateController.delete(removeComment));
        commentTreeView.refresh();
    }

    private void collectCommentsToDelete(Comment comment, List<Comment> commentsToDelete) {
        commentsToDelete.add(comment);
        for (Comment reply : comment.getReplies()) {
            collectCommentsToDelete(reply, commentsToDelete);
        }
    }
}

