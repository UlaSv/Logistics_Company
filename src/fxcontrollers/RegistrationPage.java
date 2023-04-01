package fxcontrollers;

import hib.UserHibernateController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Driver;
import model.Manager;
import utils.FxUtils;

import javax.persistence.EntityManagerFactory;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class RegistrationPage implements Initializable {
    @FXML
    public TextField loginField;
    @FXML
    public TextField passwordField;
    @FXML
    public TextField repeatPaswField;
    @FXML
    public TextField nameField;
    @FXML
    public TextField surnameField;
    @FXML
    public DatePicker birthdateField;
    @FXML
    public TextField phoneField;
    @FXML
    public RadioButton radioManager;
    @FXML
    public RadioButton radioDriver;
    @FXML
    public DatePicker employmentDateField;
    @FXML
    public DatePicker medDateField;
    @FXML
    public TextField medCertField;
    @FXML
    public TextField driverLicenseField;
    @FXML
    public ToggleGroup userType;
    @FXML
    public CheckBox isAdminField;
    @FXML
    public TextField emailField;
    @FXML
    public TextField emergencyPhoneNr;

    private boolean goingBackToLogin = true;

    private EntityManagerFactory entityManagerFactory;
    private UserHibernateController userHibernateController;

    public void setData(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
        userHibernateController = new UserHibernateController(this.entityManagerFactory);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        isAdminField.setVisible(false);
        disableFields();
    }

    public void disableFields() {
        if (radioDriver.isSelected()) {
            //disable stuff
            emailField.setDisable(true);
            employmentDateField.setDisable(true);
            medDateField.setDisable(false);
            medCertField.setDisable(false);
            driverLicenseField.setDisable(false);
            emergencyPhoneNr.setDisable(false);
        } else if (radioManager.isSelected()) {
            emailField.setDisable(false);
            employmentDateField.setDisable(false);
            medDateField.setDisable(true);
            medCertField.setDisable(true);
            driverLicenseField.setDisable(true);
            emergencyPhoneNr.setDisable(true);
        } else {
            emailField.setDisable(true);
            employmentDateField.setDisable(true);
            medDateField.setDisable(true);
            medCertField.setDisable(true);
            driverLicenseField.setDisable(true);
            emergencyPhoneNr.setDisable(true);
        }
    }

    public void createUser() throws IOException, SQLException {
        if (fieldIsEmpty()) {
            FxUtils.generateAlert(Alert.AlertType.WARNING, "User registration", "Error", "Please fill all fields");
        } else if (radioManager.isSelected()) {
            userHibernateController.save(new Manager(loginField.getText(), passwordField.getText(), nameField.getText(), surnameField.getText(), birthdateField.getValue(), phoneField.getText(), emailField.getText(), employmentDateField.getValue(), isAdminField.isSelected()));
        } else if (radioDriver.isSelected()) {
            userHibernateController.save(new Driver(loginField.getText(), passwordField.getText(), nameField.getText(), surnameField.getText(), birthdateField.getValue(), phoneField.getText(), driverLicenseField.getText(), emergencyPhoneNr.getText(), medCertField.getText(), medDateField.getValue()));
        }
        FxUtils.generateAlert(Alert.AlertType.INFORMATION, "User registration", "User created:", "SUCCESSFULLY!");
        isAllowedToGoToLoginPage();
    }

    public void returnToLogin() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginPage.class.getResource("../fxml/login-page.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) loginField.getScene().getWindow();
        stage.setTitle("Logistics");
        stage.setScene(scene);
        stage.show();
    }

    public boolean fieldIsEmpty() {
        boolean isEmpty = false;
        List<TextField> fields = new ArrayList<>(Arrays.asList(loginField, passwordField, nameField, surnameField, phoneField));
        for (TextField field : fields) {
            if (setFieldStyle(field, field.getText().isEmpty())) {
                isEmpty = true;
            }
        }
        if (setFieldStyle(birthdateField.getEditor(), birthdateField.toString().isEmpty())) {
            isEmpty = true;
        }

        switch (getUserType()) {
            case "Driver" -> {
                List<TextField> fieldsForDriver = new ArrayList<>(Arrays.asList(medCertField, emergencyPhoneNr));
                for (TextField field : fieldsForDriver) {
                    if (setFieldStyle(field, field.getText().isEmpty())) {
                        isEmpty = true;
                    }
                }
                if (setFieldStyle(medDateField.getEditor(), medDateField.toString().isEmpty())) {
                    isEmpty = true;
                }
            }
            case "Manager" -> {
                if (setFieldStyle(employmentDateField.getEditor(), employmentDateField.toString().isEmpty())) {
                    isEmpty = true;
                }
                if (setFieldStyle(emailField, emailField.getText().isEmpty())) {
                    isEmpty = true;
                }
            }
            default -> {
                FxUtils.generateAlert(Alert.AlertType.INFORMATION, "User registration", "Error", "Please select type of user");
                isEmpty = true;
            }
        }
        return isEmpty;
    }

    private boolean setFieldStyle(TextField field, boolean isEmpty) {
        field.setStyle("-fx-border-color: " + (isEmpty ? "#B22222" : "#999292") + ";");
        return isEmpty;
    }

    private String getUserType() {
        if (radioDriver.isSelected()) {
            return "Driver";
        } else if (radioManager.isSelected()) {
            return "Manager";
        }
        return "";
    }

    public void setAllowedToGoToLoginPage(boolean value) {
        this.goingBackToLogin = value;
    }

    public void isAllowedToGoToLoginPage() throws IOException {
        if (goingBackToLogin)
            returnToLogin();
        else {
            FXMLLoader fxmlLoader = new FXMLLoader(MainPage.class.getResource("../fxml/main-page.fxml"));
            MainPage mainPage = fxmlLoader.getController();
            mainPage.loadUsers();
            Stage stage = (Stage) loginField.getScene().getWindow();
            stage.close();
        }
    }

    public void returnAction() throws IOException {
        isAllowedToGoToLoginPage();
    }
}
