package com.example.stestingknugui.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;


public class GroupService {

    private static final String CREATE_GROUP_URL = "http://localhost:8087/api/group/create";

    public static boolean createGroup(String groupName, String jwtToken) {
        String jsonBody = "{\"name\": \"" + groupName + "\"}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(CREATE_GROUP_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + jwtToken)  // Используем JWT для авторизации
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 200;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean editGroup(int groupId, String newName, String jwtToken) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            String json = "{ \"name\": \"" + newName + "\" }";
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8087/api/group/edit/" + groupId))
                    .header("Authorization", "Bearer " + jwtToken)
                    .header("Content-Type", "application/json")
                    .method("PATCH", HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

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
