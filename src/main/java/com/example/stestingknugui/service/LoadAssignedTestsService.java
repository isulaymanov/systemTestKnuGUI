package com.example.stestingknugui.service;

import com.example.stestingknugui.model.AssignedTest;
import javafx.concurrent.Task;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class LoadAssignedTestsService extends Task<List<AssignedTest>> {
    private final String jwtToken;

    public LoadAssignedTestsService(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    @Override
    protected List<AssignedTest> call() throws Exception {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8087/api/testing-group/testing-group/all"))
                .header("Authorization", "Bearer " + jwtToken)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            JSONArray jsonArray = new JSONArray(response.body());
            Map<String, List<String>> testGroupsMap = new HashMap<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject testingGroup = jsonArray.getJSONObject(i);
                JSONObject testing = testingGroup.getJSONObject("testing");
                JSONObject group = testingGroup.getJSONObject("group");

                String testName = testing.getString("name");

                String groupName = group.getString("name");

                testGroupsMap.computeIfAbsent(testName, k -> new ArrayList<>()).add(groupName);
            }

            List<AssignedTest> assignedTests = new ArrayList<>();
            for (Map.Entry<String, List<String>> entry : testGroupsMap.entrySet()) {
                String testName = entry.getKey();
                String groups = String.join(",", entry.getValue());
                assignedTests.add(new AssignedTest(testName, entry.getValue()));

            }

            return assignedTests;
        } else {
            throw new Exception("Ошибка загрузки назначенных тестов: " + response.statusCode());
        }
    }
}
