package com.example.stestingknugui.controller;

import com.example.stestingknugui.util.WindowCenterer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class StudentDataViewController {

    @FXML
    private Label groupLabel;

    @FXML
    private Label testLabel;

    @FXML
    private TextField nameField;

    @FXML
    private TextField surnameField;

    @FXML
    private Button startTestButton;

    private int selectedGroupId;
    private String selectedGroupName;
    private int selectedTestId;
    private String selectedTestName;

    private int testingGroupId;

    public void setData(int groupId, String groupName, int testId, String testName, int testingGroupId) {
        this.selectedGroupId = groupId;
        this.selectedGroupName = groupName;
        this.selectedTestId = testId;
        this.selectedTestName = testName;
        this.testingGroupId = testingGroupId;

        groupLabel.setText(selectedGroupName);
        testLabel.setText(selectedTestName);
    }

    @FXML
    private void onStartTest() {
        String name = nameField.getText().trim();
        String surname = surnameField.getText().trim();

        if (name.isEmpty() || surname.isEmpty()) {
            showError("Ошибка ввода", "Пожалуйста, заполните все поля.");
            return;
        }

        //System.out.println("Начало теста для студента: " + name + " " + surname);
        moveToTestPassingWindow(name, surname);
    }

    private void moveToTestPassingWindow(String name, String surname) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/stestingknugui/TestPassingView.fxml"));
            Parent root = loader.load();

            StudentTestPassingViewController controller = loader.getController();
            controller.setData(selectedGroupId, selectedGroupName, selectedTestId, selectedTestName, name, surname, testingGroupId);

            Stage stage = (Stage) startTestButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            // Центрируем окно
            WindowCenterer.centerOnScreen(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
