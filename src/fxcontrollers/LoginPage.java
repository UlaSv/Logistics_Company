package fxcontrollers;

import hib.HibernateController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;
import utils.FxUtils;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginPage implements Initializable {
    @FXML
    public TextField loginField;
    @FXML
    public TextField passwordField;

    private EntityManagerFactory entityManagerFactory;
    private HibernateController hibernateController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("LogisticsCorp");
            hibernateController = new HibernateController(entityManagerFactory);
        } catch (Exception e) {
            FxUtils.generateAlert(Alert.AlertType.ERROR, "Cannot connect to database", "", "");
        }
    }

    public void validate() throws Exception {
        if (loginField.getText().isEmpty() || passwordField.getText().isEmpty()) {
            if (loginField.getText().isEmpty()) {
                loginField.setStyle("-fx-border-color:  #B22222;");
            }
            if (passwordField.getText().isEmpty()) {
                passwordField.setStyle("-fx-border-color:  #B22222;");
            }
            FxUtils.generateAlert(Alert.AlertType.ERROR, "", "Please fill all fields", "");

        } else {
            loginField.setStyle("-fx-border-color: #999292;");
            passwordField.setStyle("-fx-border-color: #999292;");
            User user = hibernateController.findUserByCredentials(loginField.getText(), passwordField.getText());
            if (user == null) {
                FxUtils.generateAlert(Alert.AlertType.WARNING, "Login error", "No such user", "");
            } else {
                FXMLLoader fxmlLoader = new FXMLLoader(MainPage.class.getResource("../fxml/main-page.fxml"));
                Parent parent = fxmlLoader.load();
                MainPage mainPage = fxmlLoader.getController();
                mainPage.setInfo(user, entityManagerFactory);
                Scene scene = new Scene(parent);
                Stage stage = (Stage) loginField.getScene().getWindow();
                stage.setTitle("FreightSys");
                stage.setScene(scene);
                stage.show();
            }
        }
    }

    public void registration() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(RegistrationPage.class.getResource("../fxml/registration-page.fxml"));
        Parent parent = fxmlLoader.load();
        RegistrationPage registrationPage = fxmlLoader.getController();
        registrationPage.setData(entityManagerFactory);
        Scene scene = new Scene(parent);
        Stage stage = (Stage) loginField.getScene().getWindow();
        stage.setTitle("Logistics");
        stage.setScene(scene);
        stage.show();
    }
}
