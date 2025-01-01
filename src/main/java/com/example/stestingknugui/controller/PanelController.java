package com.example.stestingknugui.controller;

import com.example.stestingknugui.service.GroupService;
import com.example.stestingknugui.service.LoadCurrentUser;
import com.example.stestingknugui.util.DialogGroup;
import com.example.stestingknugui.util.DialogHelper;
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





//package com.example.stestingknugui.controller;
//
//import com.example.stestingknugui.entity.AssignedTest;
//import com.example.stestingknugui.service.GroupService;
//import com.example.stestingknugui.service.LoadAssignedTestsService;
//import com.example.stestingknugui.service.LoadGroupsService;
//import com.example.stestingknugui.service.LoadTestsService;
//import com.example.stestingknugui.util.DialogGroup;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.scene.control.Alert;
//import javafx.scene.control.Alert.AlertType;
//import javafx.scene.control.Button;
//import javafx.scene.control.Label;
//import javafx.scene.layout.VBox;
//import javafx.concurrent.Task;
//import javafx.stage.Stage;
//import org.json.JSONObject;
//import javafx.scene.control.ButtonType;
//
//import java.net.URI;
//import java.net.http.HttpClient;
//import java.net.http.HttpRequest;
//import java.net.http.HttpResponse;
//import java.util.List;
//
//
//
//public class PanelController {
//    @FXML
//    private VBox testsVBox;
//
//    @FXML
//    private VBox gruopVBox;
//
//    @FXML
//    private VBox testinggruopVBox;
//
//    @FXML
//    private Label userInfoLabel;
//
//    @FXML
//    private Button createGroupButton;
//
//    @FXML
//    private Button createTestButton;
//
//
//    private String jwtToken;
//
//    public void setJwtToken(String jwtToken) {
//        this.jwtToken = jwtToken;
//        loadTests();
//        loadCurrentUser();
//        loadGroup();
//        loadAssignedTests();
//        System.out.println(jwtToken);
//    }
//
//
//    @FXML
//    public void initialize() {
//        createGroupButton.setOnAction(event -> showCreateGroupDialog());
//        createTestButton.setOnAction(event -> openJwtWindow(jwtToken));
//
//    }
//
//    private void showCreateGroupDialog() {
//        DialogGroup.showCreateGroupDialog().ifPresent(groupName -> {
//            if (GroupService.createGroup(groupName, jwtToken)) {
//                Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "Группа успешно создана!", ButtonType.OK);
//                successAlert.showAndWait();
//            } else {
//                DialogGroup.showError("Ошибка при создании группы.");
//            }
//        });
//    }
//
//
//    private void loadTests() {
//        LoadTestsService loadTestsRequest = new LoadTestsService(jwtToken);
//
//        loadTestsRequest.setOnSucceeded(event -> {
//            List<String> testNames = loadTestsRequest.getValue();
//            displayTests(testNames);
//        });
//
//        loadTestsRequest.setOnFailed(event -> {
//            Alert alert = new Alert(AlertType.ERROR);
//            alert.setTitle("Ошибка");
//            alert.setHeaderText("Не удалось загрузить тесты");
//            alert.setContentText(loadTestsRequest.getException().getMessage());
//            alert.showAndWait();
//        });
//
//        new Thread(loadTestsRequest).start();
//    }
//
//    private void displayTests(List<String> testNames) {
//        testsVBox.getChildren().clear();
//        for (String testName : testNames) {
//            Label label = new Label(testName);
//            label.setStyle("-fx-font-size: 12px; -fx-padding: 5px;");
//            testsVBox.getChildren().add(label);
//        }
//    }
//
//    private void loadGroup(){
//        LoadGroupsService loadGroupsRequest = new LoadGroupsService(jwtToken);
//
//        loadGroupsRequest.setOnSucceeded(event -> {
//            List<String> groupNames = loadGroupsRequest.getValue();
//            displayGroup(groupNames);
//        });
//        loadGroupsRequest.setOnFailed(event -> {
//            Alert alert = new Alert(AlertType.ERROR);
//            alert.setHeaderText("Ощибка");
//            alert.setContentText(loadGroupsRequest.getException().getMessage());
//            alert.showAndWait();
//        });
//
//        new Thread(loadGroupsRequest).start();
//    }
//
//    private void displayGroup(List<String> groupNames) {
//        if (groupNames == null || groupNames.isEmpty()) {
//            return;
//        }
//        gruopVBox.getChildren().clear();
//        for (String groupName : groupNames) {
//            Label label = new Label(groupName);
//            label.setStyle("-fx-font-size: 12px; -fx-padding: 5px;");
//            gruopVBox.getChildren().add(label);
//        }
//    }
//
//    private void loadAssignedTests() {
//        LoadAssignedTestsService loadAssignedTestsRequest = new LoadAssignedTestsService(jwtToken);
//
//        loadAssignedTestsRequest.setOnSucceeded(event -> {
//            List<AssignedTest> assignedTests = loadAssignedTestsRequest.getValue();
//            displayAssignedTests(assignedTests);
//        });
//
//        loadAssignedTestsRequest.setOnFailed(event -> {
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Ошибка");
//            alert.setHeaderText("Не удалось загрузить назначенные тесты");
//            alert.setContentText(loadAssignedTestsRequest.getException().getMessage());
//            alert.showAndWait();
//        });
//
//        new Thread(loadAssignedTestsRequest).start();
//    }
//
//    private void displayAssignedTests(List<AssignedTest> assignedTests) {
//        testinggruopVBox.getChildren().clear();
//        for (AssignedTest assignedTest : assignedTests) {
//            Label label = new Label(assignedTest.toString());
//            label.setStyle("-fx-font-size: 12px; -fx-padding: 5px;");
//            testinggruopVBox.getChildren().add(label);
//        }
//    }
//
//
//
//
//    private void loadCurrentUser() {
//        Task<JSONObject> task = new Task<>() {
//            @Override
//            protected JSONObject call() throws Exception {
//                HttpClient client = HttpClient.newHttpClient();
//                HttpRequest request = HttpRequest.newBuilder()
//                        .uri(URI.create("http://localhost:8087/api/currentuser"))
//                        .header("Authorization", "Bearer " + jwtToken)
//                        .GET()
//                        .build();
//                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//                if (response.statusCode() == 200) {
//                    return new JSONObject(response.body());
//                } else {
//                    throw new Exception("Ошибка: " + response.statusCode());
//                }
//            }
//        };
//
//        task.setOnSucceeded(event -> {
//            JSONObject userData = task.getValue();
//            String fullName = userData.getString("lastName") + " " +
//                    userData.getString("name") + " " +
//                    userData.getString("middleName");
//            userInfoLabel.setText(fullName);
//        });
//
//        task.setOnFailed(event -> {
//            userInfoLabel.setText("Не удалось загрузить информацию о пользователе");
//        });
//
//        new Thread(task).start();
//    }
//
//
//    private void openJwtWindow(String jwtToken) {
//
//        System.out.println(getClass().getResource("/com/example/stestingknugui/TestCreation.fxml"));
//
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/stestingknugui/TestCreation.fxml"));
//            Parent root = loader.load();
//
//            TestCreationController controller = loader.getController();
//            controller.setJwtToken(jwtToken);
//
//            Stage stage = new Stage();
//            stage.setTitle("Тест");
//            stage.setScene(new Scene(root));
//            stage.show();
//
//            ((Stage) createTestButton.getScene().getWindow()).close();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            Alert alert = new Alert(AlertType.ERROR);
//            alert.setTitle("Ошибка");
//            alert.setHeaderText("Не удалось открыть панель");
//            alert.showAndWait();
//        }
//    }
//}
