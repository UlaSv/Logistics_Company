package utils;

import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class FxUtils {
    public static void generateAlert(Alert.AlertType alertType, String title, String headerText, String contextText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contextText);
        alert.showAndWait();
    }
}

