package com.example.stestingknugui.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class NavigationHelper {

    private static Stage primaryStage;

    // Метод для установки основного окна
    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    // Метод для переключения сцен
    public static void navigateTo(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(NavigationHelper.class.getResource("/com/example/stestingknugui/student/" + fxmlFile));
            Parent root = loader.load();
            if (primaryStage != null) {
                primaryStage.setScene(new Scene(root));
                primaryStage.show();
            } else {
                throw new IllegalStateException("Primary stage is not set. Call NavigationHelper.setPrimaryStage(stage) in your main application.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
