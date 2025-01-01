package com.example.stestingknugui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StudentTestResultViewController {

    @FXML
    private Label studentInfoLabel;

    @FXML
    private Label testInfoLabel;

    @FXML
    private Label scoreLabel;

    private int testingGroupId;

    public void setData(String groupName, String testName, String studentName, String studentSurname, int correctAnswers, int totalQuestions, int testingGroupId) {
        studentInfoLabel.setText("Студент: " + studentName + " " + studentSurname);
        testInfoLabel.setText("Тест: " + testName + "\nГруппа: " + groupName + "");
        scoreLabel.setText("Результат: " + correctAnswers + " из " + totalQuestions);


        this.testingGroupId = testingGroupId;

        sendResultToServer(studentName, studentSurname, correctAnswers, totalQuestions);
    }

    private void sendResultToServer(String studentName, String studentSurname, int correctAnswers, int totalQuestions) {
        try {
            // Получаем текущее время
            String currentTime = getCurrentTime();
            String endTime = getEndTime();

            // Создаем тело запроса в формате JSON
            String jsonInputString = String.format(
                    "{\n" +
                            "  \"name\": \"%s\",\n" +
                            "  \"surname\": \"%s\",\n" +
                            "  \"dateCompletion\": \"%s\",\n" +
                            "  \"result\": \"%d/%d\",\n" +
                            "  \"startTime\": \"%s\",\n" +
                            "  \"endTime\": \"%s\",\n" +
                            "  \"testingGroup\": {\n" +
                            "    \"id\": %d\n" +
                            "  }\n" +
                            "}",
                    studentName, studentSurname, currentTime, correctAnswers, totalQuestions, currentTime, endTime, testingGroupId);

            // Отправка POST-запроса
            URL url = new URL("http://localhost:8087/api/attempt-student");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            // Отправка данных в теле запроса
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Получаем ответ от сервера (для проверки успешности запроса)
            int responseCode = connection.getResponseCode();
//            if (responseCode == HttpURLConnection.HTTP_OK) {
//                System.out.println("Результат успешно отправлен на сервер.");
//            } else {
//                System.out.println("Ошибка отправки результата. Код ответа: " + responseCode);
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        return sdf.format(new Date());
    }

    private String getEndTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date currentDate = new Date();
        long tenMinutesLater = currentDate.getTime() + 10 * 60 * 1000; // 10 минут в миллисекундах
        return sdf.format(new Date(tenMinutesLater));
    }

    @FXML
    private void onClose() {
        Stage stage = (Stage) scoreLabel.getScene().getWindow();
        stage.close();
    }
}
