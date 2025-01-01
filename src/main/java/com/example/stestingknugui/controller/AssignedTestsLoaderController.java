package com.example.stestingknugui.controller;

import com.example.stestingknugui.model.AssignedTest;
import com.example.stestingknugui.service.LoadAssignedTestsService;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import java.util.List;
public class AssignedTestsLoaderController {
    private static VBox currentVBox;
    private static String currentJwtToken;

    public static void initialize(String jwtToken, VBox testingGroupVBox) {
        currentJwtToken = jwtToken;
        currentVBox = testingGroupVBox;
        loadAssignedTests(jwtToken, testingGroupVBox);
    }

    public static void refreshAssignedTests() {
        if (currentJwtToken != null && currentVBox != null) {
            loadAssignedTests(currentJwtToken, currentVBox);
        } else {
            throw new IllegalStateException("AssignedTestsLoaderController не инициализирован.");
        }
    }

    public static void loadAssignedTests(String jwtToken, VBox testinggruopVBox) {
        currentJwtToken = jwtToken;
        currentVBox = testinggruopVBox;

        LoadAssignedTestsService loadAssignedTestsRequest = new LoadAssignedTestsService(jwtToken);

        loadAssignedTestsRequest.setOnSucceeded(event -> displayAssignedTests(loadAssignedTestsRequest.getValue(), testinggruopVBox));
        loadAssignedTestsRequest.setOnFailed(event -> showError("Не удалось загрузить назначенные тесты", loadAssignedTestsRequest.getException().getMessage()));

        new Thread(loadAssignedTestsRequest).start();
    }

    private static void displayAssignedTests(List<AssignedTest> assignedTests, VBox testinggruopVBox) {
        testinggruopVBox.getChildren().clear();
        for (AssignedTest assignedTest : assignedTests) {
            Label label = new Label(assignedTest.toString());
            label.setStyle("-fx-font-size: 12px; -fx-padding: 5px;");
            testinggruopVBox.getChildren().add(label);
        }
    }

    private static void showError(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }


}
