package com.example.stestingknugui.service;

import javafx.concurrent.Task;
import javafx.scene.control.Label;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class LoadCurrentUser {
    public static void loadCurrentUser(String jwtToken, Label userInfoLabel) {
        Task<JSONObject> task = new Task<>() {
            @Override
            protected JSONObject call() throws Exception {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8087/api/currentuser"))
                        .header("Authorization", "Bearer " + jwtToken)
                        .GET()
                        .build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == 200) {
                    return new JSONObject(response.body());
                } else {
                    throw new Exception("Ошибка: " + response.statusCode());
                }
            }
        };

        task.setOnSucceeded(event -> {
            JSONObject userData = task.getValue();
            String fullName = userData.getString("lastName") + " " +
                    userData.getString("name") + " " +
                    userData.getString("middleName");
            userInfoLabel.setText(fullName);
        });

        task.setOnFailed(event -> userInfoLabel.setText("Не удалось загрузить информацию о пользователе"));

        new Thread(task).start();
    }
}
