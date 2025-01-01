package com.example.stestingknugui.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CreateAnswerOptionService {

    private static final String CREATE_ANSWER_URL = "http://localhost:8087/api/answer-option";  // URL для создания варианта ответа

    // Метод для создания варианта ответа
    public static void createAnswerOption(String questionId, String answerText, String isCorrect, String jwtToken) {
        String jsonBody = "{\"answerText\": \"" + answerText + "\", \"isCorrect\": \"" + isCorrect +
                "\", \"question\": { \"id\": \"" + questionId + "\" } }";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(CREATE_ANSWER_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + jwtToken)
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpClient client = HttpClient.newHttpClient();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode responseJson = objectMapper.readTree(response.body());

                String answerId = responseJson.get("id").asText();
                //System.out.println("Создан вариант ответа с ID: " + answerId);
            }
//            else {
//                System.out.println("Ошибка при создании варианта ответа. Код ответа: " + response.statusCode());
//                System.out.println("Ответ: " + response.body());
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
