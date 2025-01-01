package com.example.stestingknugui.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CreateTestService {

    private static final String CREATE_TEST_URL = "http://localhost:8087/api/testing/create";

    public static String createTest(String testName, String testDescription, Integer testPassDate, Integer testLimitDate, String jwtToken) {
        String jsonBody = "{\"name\": \"" + testName + "\", \"description\": \"" + testDescription +
                "\", \"passDate\": \"" + testPassDate + "\", \"limitDate\": \"" + testLimitDate + "\" }";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(CREATE_TEST_URL))
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

                String id = responseJson.get("id").asText();
                //System.out.println("Создан тест с ID: " + id);
                return id;
            } else {
                //System.out.println("Ошибка при создании теста. Код ответа: " + response.statusCode());
                //System.out.println("Ответ: " + response.body());
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}


