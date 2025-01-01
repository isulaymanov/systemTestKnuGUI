package com.example.stestingknugui.service;

import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class LoginService extends Task<String> {

    private final String username;
    private final String password;

    public LoginService(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    protected String call() throws Exception {
        HttpClient client = HttpClient.newHttpClient();

        JSONObject requestBody = new JSONObject();
        requestBody.put("username", username);
        requestBody.put("password", password);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8087/api/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString(), StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            JSONObject responseBody = new JSONObject(response.body());
            return responseBody.getString("jwtToken");  // Возвращаем JWT токен
        } else {
            throw new Exception("Ошибка авторизации: " + response.statusCode());
        }
    }

    @Override
    protected void failed() {
        super.failed();
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText("Не удалось выполнить вход");
        alert.setContentText(getException().getMessage());
        alert.showAndWait();
    }
}
