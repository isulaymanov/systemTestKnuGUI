package com.example.stestingknugui.controller;

import com.example.stestingknugui.service.GroupService;
import com.example.stestingknugui.service.LoadCurrentUser;
import com.example.stestingknugui.util.DialogGroup;
import com.example.stestingknugui.util.DialogHelper;
import com.example.stestingknugui.util.WindowCenterer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PanelController {

    @FXML
    private VBox testsVBox;

    @FXML
    private VBox gruopVBox;

    @FXML
    private VBox testinggruopVBox;

    @FXML
    private Label userInfoLabel;

    @FXML
    private Button createGroupButton;

    @FXML
    private Button createTestButton;

    @FXML
    private Button createTestingGroup;

    @FXML
    private Button logoutButton;

    private String jwtToken;

    public void setJwtToken(String jwtToken) {
        //System.out.println("Получен токен: " + jwtToken);
        this.jwtToken = jwtToken;
        loadContent();
    }

    @FXML
    public void initialize() {
        createGroupButton.setOnAction(event -> DialogHelper.showCreateGroupDialog(jwtToken, gruopVBox));
        createTestButton.setOnAction(event -> openJwtWindow(jwtToken));
        createTestingGroup.setOnAction(event -> openTestAssignmentWindow(jwtToken));
        logoutButton.setOnAction(event -> logout());


    }

    private void loadContent() {
        TestLoaderController.loadTests(jwtToken, testsVBox);
        GroupLoaderController.loadGroups(jwtToken, gruopVBox);
        AssignedTestsLoaderController.loadAssignedTests(jwtToken, testinggruopVBox);
        LoadCurrentUser.loadCurrentUser(jwtToken, userInfoLabel);
    }

    private void openJwtWindow(String jwtToken) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/stestingknugui/TestCreation.fxml"));
            Parent root = loader.load();
            TestCreationController controller = loader.getController();
            controller.setJwtToken(jwtToken);

            Stage stage = new Stage();
            stage.setTitle("Тест");
            stage.setScene(new Scene(root));
            // Центрируем окно
            WindowCenterer.centerOnScreen(stage);
            stage.show();

            ((Stage) createTestButton.getScene().getWindow()).close();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Не удалось открыть панель");
            alert.showAndWait();
        }
    }

    private void openTestAssignmentWindow(String jwtToken) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/stestingknugui/TestAssignment.fxml"));
            Parent root = loader.load();
            TestAssignmentController controller = loader.getController();
            controller.setJwtToken(jwtToken);

            Stage stage = new Stage();
            stage.setTitle("Назначить тест");
            stage.setScene(new Scene(root));
            // Центрируем окно
            WindowCenterer.centerOnScreen(stage);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Не удалось открыть окно назначения теста");
            alert.showAndWait();
        }
    }


    private void logout() {
        //System.out.println("Завершение приложения");
        javafx.application.Platform.exit();
    }


}



