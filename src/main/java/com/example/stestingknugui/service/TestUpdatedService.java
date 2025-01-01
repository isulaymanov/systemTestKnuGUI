package com.example.stestingknugui.service;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
public class TestUpdatedService {
    public static boolean editTest(int testId, String newName, String jwtToken) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            String json = "{ \"name\": \"" + newName + "\" }"; // Формируем тело запроса
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8087/api/testing/edit/" + testId)) // Указываем URL
                    .header("Authorization", "Bearer " + jwtToken) // Добавляем токен
                    .header("Content-Type", "application/json") // Указываем тип контента
                    .method("PATCH", HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8)) // Устанавливаем метод PATCH
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString()); // Отправка запроса

            if (response.statusCode() == 200 || response.statusCode() == 201) {
                return true;
            } else {
                //System.out.println("Error: " + response.statusCode() + " " + response.body());
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}