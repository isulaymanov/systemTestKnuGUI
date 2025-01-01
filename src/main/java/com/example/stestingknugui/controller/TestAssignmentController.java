package com.example.stestingknugui.controller;

import com.example.stestingknugui.service.TestingGroupService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import java.util.ArrayList;
import java.util.List;

public class TestAssignmentController {

    @FXML
    private ListView<String> testsListView;

    @FXML
    private VBox groupsVBox;

    @FXML
    private Button assignTestButton;

    private String jwtToken;

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
        loadContent();
    }

    @FXML
    public void initialize() {
        //System.out.println("Инициализация контроллера TestAssignmentController");
        assignTestButton.setOnAction(event -> assignTest());

    }


    private void loadContent() {
        TestingGroupService.loadTests(jwtToken, testsListView);

        List<String> groups = TestingGroupService.loadGroups(jwtToken);
        for (String group : groups) {
            CheckBox checkBox = new CheckBox(group);
            groupsVBox.getChildren().add(checkBox);
        }



    }

    private void assignTest() {
        String selectedTest = testsListView.getSelectionModel().getSelectedItem();
        List<String> selectedGroups = new ArrayList<>();

        for (var node : groupsVBox.getChildren()) {
            if (node instanceof CheckBox checkBox && checkBox.isSelected()) {
                selectedGroups.add(checkBox.getText());
            }
        }

        if (selectedTest == null || selectedGroups.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Выберите тест и хотя бы одну группу.");
            alert.showAndWait();
            return;
        }

        boolean success = TestingGroupService.assignTest(jwtToken, selectedTest, selectedGroups);
        if (success) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Тест успешно назначен.");
            alert.showAndWait();

            AssignedTestsLoaderController.refreshAssignedTests();

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Ошибка назначения теста.");
            alert.showAndWait();
        }
    }
}

