package com.example.stestingknugui.service;
import javafx.concurrent.Task;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoadTestsQuestionAllService extends Task<List<Map<String, Object>>>{
    private final String jwtToken;

    public LoadTestsQuestionAllService(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    @Override
    protected List<Map<String, Object>> call() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8087/api/testing/all"))
                .header("Authorization", "Bearer " + jwtToken)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        //System.out.println("Response: " + response.body());


        if (response.statusCode() == 200) {
            JSONArray jsonArray = new JSONArray(response.body());
            List<Map<String, Object>> tests = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject test = jsonArray.getJSONObject(i);
                Map<String, Object> testMap = new HashMap<>();
                testMap.put("id", test.getInt("id"));
                testMap.put("name", test.getString("name"));
                tests.add(testMap);
            }
            return tests;
        } else {
            throw new Exception("Ошибка загрузки тестов: " + response.statusCode());
        }
    }
}
