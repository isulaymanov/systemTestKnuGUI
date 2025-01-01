package com.example.stestingknugui.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CreateQuestionService {

    private static final String CREATE_QUESTION_URL = "http://localhost:8087/api/question";  // URL для создания вопроса

    public static String createQuestion(String testId, String questionText, String jwtToken) {
        String jsonBody = "{\"questionText\": \"" + questionText + "\", \"testing\": { \"id\": \"" + testId + "\" } }";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(CREATE_QUESTION_URL))
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

                String questionId = responseJson.get("id").asText();
                //System.out.println("Создан вопрос с ID: " + questionId);
                return questionId;
            } else {
                //System.out.println("Ошибка при создании вопроса. Код ответа: " + response.statusCode());
                //System.out.println("Ответ: " + response.body());
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

