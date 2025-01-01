package com.example.stestingknugui.controller;

import com.example.stestingknugui.service.CreateAnswerOptionService;
import com.example.stestingknugui.service.CreateQuestionService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.control.*;

import java.io.IOException;

public class QuestionCreationController {

    @FXML
    private TextField questionTextField;

    @FXML
    private TextField answer1Field;
    @FXML
    private TextField answer2Field;
    @FXML
    private TextField answer3Field;
    @FXML
    private TextField answer4Field;

    @FXML
    private CheckBox isCorrect1CheckBox;
    @FXML
    private CheckBox isCorrect2CheckBox;
    @FXML
    private CheckBox isCorrect3CheckBox;
    @FXML
    private CheckBox isCorrect4CheckBox;

    @FXML
    private Button finishTest;

    private String testId;
    private String jwtToken;

    public void setTestId(String testId) {
        this.testId = testId;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public static void open(String testId, String jwtToken) {
        try {
            FXMLLoader loader = new FXMLLoader(QuestionCreationController.class.getResource("/com/example/stestingknugui/QuestionCreation.fxml"));
            Parent root = loader.load();
            QuestionCreationController controller = loader.getController();

            controller.setTestId(testId);
            controller.setJwtToken(jwtToken);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Создание вопросов");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void addQuestion() {
        String questionText = questionTextField.getText().trim();
        String answer1 = answer1Field.getText().trim();
        String answer2 = answer2Field.getText().trim();
        String answer3 = answer3Field.getText().trim();
        String answer4 = answer4Field.getText().trim();

        if (questionText.isEmpty() || answer1.isEmpty() || answer2.isEmpty() || answer3.isEmpty() || answer4.isEmpty()) {
            //System.out.println("Ошибка: все поля должны быть заполнены.");
            return;
        }

        if (testId == null || jwtToken == null) {
            //System.out.println("Ошибка: testId или jwtToken не установлен.");
            return;
        }

        String questionId = CreateQuestionService.createQuestion(testId, questionText, jwtToken);

        if (questionId != null) {
            CreateAnswerOptionService.createAnswerOption(questionId, answer1, Boolean.toString(isCorrect1CheckBox.isSelected()), jwtToken);
            CreateAnswerOptionService.createAnswerOption(questionId, answer2, Boolean.toString(isCorrect2CheckBox.isSelected()), jwtToken);
            CreateAnswerOptionService.createAnswerOption(questionId, answer3, Boolean.toString(isCorrect3CheckBox.isSelected()), jwtToken);
            CreateAnswerOptionService.createAnswerOption(questionId, answer4, Boolean.toString(isCorrect4CheckBox.isSelected()), jwtToken);

            //System.out.println("Вопрос и ответы успешно добавлены.");
            clearFields();
        }
        //else {
            //System.out.println("Ошибка при создании вопроса.");
        //}
    }


    @FXML
    public void finishTest() {

//        if (finishTest == null) {
//            System.out.println("finishTest is null");
//        }

        //System.out.println(getClass().getResource("/com/example/stestingknugui/dashboard.fxml"));

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/stestingknugui/dashboard.fxml"));
            Parent root = loader.load();

            PanelController controller = loader.getController();
            controller.setJwtToken(jwtToken);

            Stage stage = new Stage();
            stage.setTitle("Панель");
            stage.setScene(new Scene(root));
            stage.show();

            ((Stage) finishTest.getScene().getWindow()).close();

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Не удалось открыть панель");
            alert.showAndWait();
        }
    }

    private void clearFields() {
        questionTextField.clear();
        answer1Field.clear();
        answer2Field.clear();
        answer3Field.clear();
        answer4Field.clear();

        isCorrect1CheckBox.setSelected(false);
        isCorrect2CheckBox.setSelected(false);
        isCorrect3CheckBox.setSelected(false);
        isCorrect4CheckBox.setSelected(false);
    }
}
