package com.example.stestingknugui.controller;
import com.example.stestingknugui.model.TestingGroup;
import com.example.stestingknugui.service.LoadTestsService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.util.List;

public class TestsWindowController {

    @FXML
    private ListView<String> testListView;

    @FXML
    private Button viewResultsButton;

    private List<TestingGroup> tests;
    private TestingGroup selectedTest;
    private String jwtToken;

    public TestsWindowController() {
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public void initialize() {
        testListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedTest = tests.get(testListView.getSelectionModel().getSelectedIndex());
                viewResultsButton.setDisable(false);
            }
        });

        viewResultsButton.setOnAction(event -> openResultWindow(selectedTest));
    }

    public void loadTests(int groupId, String jwtToken) {
        LoadTestsService service = new LoadTestsService(groupId, jwtToken);

        service.setOnSucceeded(event -> {
            tests = service.getValue();
            for (TestingGroup testGroup : tests) {
                testListView.getItems().add(testGroup.getTesting().getName());
            }
        });

        service.setOnFailed(event -> {
            // Обработка ошибок
        });

        new Thread(service).start();
    }

    private void openResultWindow(TestingGroup test) {
        ResultWindowController resultController = new ResultWindowController();
        resultController.open(test.getId(), jwtToken);
    }
}
