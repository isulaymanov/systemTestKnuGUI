package com.example.stestingknugui.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import com.example.stestingknugui.model.StudentQuestion;

import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;


public class  StudentTestPassingViewController {

    @FXML
    private Label questionLabel;

    @FXML
    private VBox answersContainer;

    private List<StudentQuestion> questions;
    private int currentQuestionIndex = 0;
    private int correctAnswers = 0;
    private ToggleGroup answerGroup;

    private int selectedGroupId;
    private String selectedGroupName;
    private int selectedTestId;
    private String selectedTestName;
    private String studentName;
    private String studentSurname;

    private int testingGroupId; // Новый параметр для testingGroupId


    public void setData(int groupId, String groupName, int testId, String testName, String name, String surname, int testingGroupId) {
        this.selectedGroupId = groupId;
        this.selectedGroupName = groupName;
        this.selectedTestId = testId;
        this.selectedTestName = testName;
        this.studentName = name;
        this.studentSurname = surname;
        this.testingGroupId = testingGroupId;
        loadQuestions();

//        if (questions != null) {
//            System.out.println("TotalQuestions: " + questions.size());
//        } else {
//            System.out.println("Questions are not loaded yet.");
//        }


    }

    private JSONArray fetchQuestionsFromServer() throws Exception {
        URL url = new URL("http://localhost:8087/api/question/testing/" + selectedTestId);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        Scanner scanner = new Scanner(connection.getInputStream());
        StringBuilder response = new StringBuilder();
        while (scanner.hasNext()) {
            response.append(scanner.nextLine());
        }
        scanner.close();

        return new JSONArray(response.toString());
    }

    private void loadQuestions() {
        try {
            JSONArray jsonQuestions = fetchQuestionsFromServer();
            if (jsonQuestions == null || jsonQuestions.length() == 0) {
                //System.out.println("No questions received from the server.");
                return;
            }
            questions = new ArrayList<>();
            for (int i = 0; i < jsonQuestions.length(); i++) {
                JSONObject questionObj = jsonQuestions.getJSONObject(i);
                StudentQuestion question = new StudentQuestion(
                        questionObj.getInt("id"),
                        questionObj.getString("questionText")
                );
                questions.add(question);
            }

            displayQuestion();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayQuestion() {
        if (currentQuestionIndex >= questions.size()) {
            finishTest();
            return;
        }

        StudentQuestion question = questions.get(currentQuestionIndex);
        questionLabel.setText((currentQuestionIndex + 1) + ". " + question.getText());

        loadAnswerOptions(question.getId());
    }

    private void loadAnswerOptions(int questionId) {
        try {
            URL url = new URL("http://localhost:8087/api/answer-option/question/" + questionId);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            Scanner scanner = new Scanner(connection.getInputStream());
            StringBuilder response = new StringBuilder();
            while (scanner.hasNext()) {
                response.append(scanner.nextLine());
            }
            scanner.close();

            JSONArray jsonAnswers = new JSONArray(response.toString());
            answersContainer.getChildren().clear();
            answerGroup = new ToggleGroup();
            for (int i = 0; i < jsonAnswers.length(); i++) {
                JSONObject answerObj = jsonAnswers.getJSONObject(i);
                RadioButton radioButton = new RadioButton(answerObj.getString("answerText"));
                radioButton.setToggleGroup(answerGroup);
                radioButton.setUserData(answerObj.getBoolean("isCorrect"));
                answersContainer.getChildren().add(radioButton);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onNextQuestion() {
        RadioButton selected = (RadioButton) answerGroup.getSelectedToggle();
        if (selected != null) {
            boolean isCorrect = (boolean) selected.getUserData();
            if (isCorrect) {
                correctAnswers++;
            }
        }
        currentQuestionIndex++;
        displayQuestion();
    }

    @FXML
    private void onFinishTest() {
        finishTest();
    }

    private void finishTest() {
        //System.out.println("Тест завершён! Результат: " + correctAnswers + "/" + questions.size());
        moveToResultWindow();
    }

    private void moveToResultWindow() {
        try {
            //System.out.println("Загрузка TestResultView.fxml...");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/stestingknugui/TestResultView.fxml"));
            Parent root = loader.load();
            //System.out.println("FXML загружен успешно!");

            StudentTestResultViewController controller = loader.getController();
            controller.setData(selectedGroupName, selectedTestName, studentName, studentSurname, correctAnswers, questions.size(), testingGroupId);
            //System.out.println("Контроллер установлен!");

            Stage stage = (Stage) answersContainer.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
            //System.out.println("Сцена обновлена!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}