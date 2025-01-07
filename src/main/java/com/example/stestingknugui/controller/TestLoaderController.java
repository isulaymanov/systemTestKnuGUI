package com.example.stestingknugui.controller;
import com.example.stestingknugui.service.LoadTestsQuestionAllService;
//import com.example.stestingknugui.util.HttpUtils;
import com.example.stestingknugui.service.TestUpdatedService;
import com.example.stestingknugui.util.DialogTest;
import com.example.stestingknugui.util.WindowCenterer;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TestLoaderController {
    public static void loadTests(String jwtToken, VBox testsVBox) {
        LoadTestsQuestionAllService loadTestsRequest = new LoadTestsQuestionAllService(jwtToken);
        loadTestsRequest.setOnSucceeded(event -> displayTests(loadTestsRequest.getValue(), testsVBox, jwtToken));
        loadTestsRequest.setOnFailed(event -> showError("Не удалось загрузить тесты", loadTestsRequest.getException().getMessage()));
        new Thread(loadTestsRequest).start();
    }

    private static void displayTests(List<Map<String, Object>> tests, VBox testsVBox, String jwtToken) {
        testsVBox.getChildren().clear();
        for (Map<String, Object> test : tests) {
            HBox testRow = new HBox();
            testRow.setSpacing(10);

            String testName = (String) test.get("name");
            int testId = (int) test.get("id");

            Label label = new Label(testName);
            label.setStyle("-fx-font-size: 12px; -fx-padding: 5px;");

            label.setOnMouseClicked(event -> {
                if (event.getButton() == javafx.scene.input.MouseButton.PRIMARY) {
                    openQuestionsAnswersWindow(testId, jwtToken);
                } else if (event.getButton() == javafx.scene.input.MouseButton.SECONDARY) {
                    Optional<String> newName = DialogTest.showEditTestDialog(testName);
                    newName.ifPresent(name -> {
                        if (TestUpdatedService.editTest(testId, name, jwtToken)) {
                            Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "Название теста успешно изменено!", ButtonType.OK);
                            successAlert.showAndWait();
                            label.setText(name);
                        } else {
                            DialogTest.showError("Ошибка", "Название не может быть пустым.");

                        }
                    });
                }
            });


            testRow.getChildren().addAll(label);
            testsVBox.getChildren().add(testRow);
        }
    }


    private static void openQuestionsAnswersWindow(int testId, String jwtToken) {
        try {
            FXMLLoader loader = new FXMLLoader(TestLoaderController.class.getResource("/com/example/stestingknugui/questions_answers.fxml"));
            Parent root = loader.load();

            QuestionsAnswersController controller = loader.getController();
            controller.setJwtToken(jwtToken);
            controller.setTestId(testId);

            Stage stage = new Stage();
            stage.setTitle("Вопросы и ответы");
            stage.setScene(new Scene(root));
            // Центрируем окно
            WindowCenterer.centerOnScreen(stage);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Не удалось открыть окно вопросов и ответов");
            alert.showAndWait();
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

