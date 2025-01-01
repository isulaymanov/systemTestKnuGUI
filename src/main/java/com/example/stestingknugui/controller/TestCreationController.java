package com.example.stestingknugui.controller;

import com.example.stestingknugui.service.CreateTestService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;

public class TestCreationController {

    @FXML
    private TextField testNameField;
    @FXML
    private TextField testDescriptionField;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;

    private String jwtToken; // Поле для хранения токена

    /**
     * Метод для установки токена.
     *
     * @param jwtToken Токен авторизации.
     */
    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    @FXML
    public void createTest() {
        String name = testNameField.getText();
        String description = testDescriptionField.getText();
        Integer startDate = startDatePicker.getValue() != null
                ? Integer.parseInt(startDatePicker.getValue().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                : null;

        Integer endDate = endDatePicker.getValue() != null
                ? Integer.parseInt(endDatePicker.getValue().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                : null;


        if (jwtToken == null || jwtToken.isEmpty()) {
            //System.out.println("Ошибка: токен не установлен.");
            return;
        }

        if (name.isEmpty() || description.isEmpty() || startDate == null || endDate == null) {
            //System.out.println("Ошибка: заполните все поля.");
            return;
        }

        String testId = CreateTestService.createTest(name, description, startDate, endDate, jwtToken);

        if (testId != null) {
            //System.out.println("Тест успешно создан: " + testId);

            Stage stage = (Stage) testNameField.getScene().getWindow();
            stage.close();
            QuestionCreationController.open(testId, jwtToken);
        }
//        else {
//            System.out.println("Ошибка при создании теста.");
//        }
    }
}

