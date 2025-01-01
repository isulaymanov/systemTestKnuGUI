package com.example.stestingknugui.service;

import javafx.concurrent.Task;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class LoadTestsAllService extends Task<List<String>> {
    private final String jwtToken;
    public LoadTestsAllService(String jwtToken) {
        this.jwtToken = jwtToken;
    }
    @Override
    protected List<String> call() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8087/api/testing/all"))
                .header("Authorization", "Bearer " + jwtToken)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            JSONArray jsonArray = new JSONArray(response.body());
            List<String> testNames = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject test = jsonArray.getJSONObject(i);
                testNames.add(test.getString("name"));
            }
            return testNames;
        } else {
            throw new Exception("Ошибка загрузки тестов: " + response.statusCode());
        }
    }
}
