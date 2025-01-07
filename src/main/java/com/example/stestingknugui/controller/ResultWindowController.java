package com.example.stestingknugui.controller;

import com.example.stestingknugui.model.TestResult;
import com.example.stestingknugui.service.LoadResultsService;
import com.example.stestingknugui.util.WindowCenterer;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;


public class ResultWindowController {

    @FXML
    private TableView<TestResult> resultsTable;

    @FXML
    private TableColumn<TestResult, String> nameColumn;

    @FXML
    private TableColumn<TestResult, String> surnameColumn;

    @FXML
    private TableColumn<TestResult, String> dateCompletionColumn;

    @FXML
    private TableColumn<TestResult, Integer> resultColumn;

    @FXML
    private TableColumn<TestResult, String> startTimeColumn;

    @FXML
    private TableColumn<TestResult, String> endTimeColumn;

    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
        dateCompletionColumn.setCellValueFactory(new PropertyValueFactory<>("dateCompletion"));
        resultColumn.setCellValueFactory(new PropertyValueFactory<>("result"));
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
    }

    public void loadResults(int testingGroupId, String jwtToken) {
        LoadResultsService service = new LoadResultsService(testingGroupId, jwtToken);

        service.setOnSucceeded(event -> {
            resultsTable.getItems().addAll(service.getValue());
        });

        service.setOnFailed(event -> {
            // Обработка ошибок
        });

        new Thread(service).start();
    }

    public void open(int testingGroupId, String jwtToken) {
        try {
            // Загружаем FXML файл для отображения результатов
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/stestingknugui/result_window.fxml"));
            Parent root = loader.load();

            // Получаем контроллер для работы с результатами
            ResultWindowController resultController = loader.getController();
            resultController.loadResults(testingGroupId, jwtToken); // Передаем ID группы и токен

            // Создаем сцену и показываем окно
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            // Центрируем окно
            WindowCenterer.centerOnScreen(stage);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();  // Или можно использовать logging
        }
    }

}
