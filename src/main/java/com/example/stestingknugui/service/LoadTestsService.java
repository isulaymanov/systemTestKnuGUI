package com.example.stestingknugui.service;

import com.example.stestingknugui.model.Group;
import com.example.stestingknugui.model.TestingGroup;
import com.example.stestingknugui.model.Testing;
import javafx.concurrent.Task;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class LoadTestsService extends Task<List<TestingGroup>> {

    private final int groupId;
    private final String jwtToken;

    public LoadTestsService(int groupId, String jwtToken) {
        this.groupId = groupId;
        this.jwtToken = jwtToken;
    }

    @Override
    protected List<TestingGroup> call() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8087/api/testing-group/group/" + groupId);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Authorization", "Bearer " + jwtToken)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            JSONArray jsonArray = new JSONArray(response.body());
            List<TestingGroup> testingGroups = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject groupObject = jsonArray.getJSONObject(i);
                JSONObject testingObject = groupObject.getJSONObject("testing");
                JSONObject group = groupObject.getJSONObject("group");

                int testId = testingObject.getInt("id");
                String testName = testingObject.getString("name");
                String description = testingObject.getString("description");
                int passDate = testingObject.getInt("passDate");
                int limitDate = testingObject.getInt("limitDate");

                Testing testing = new Testing(testId, testName, description, passDate, limitDate);

                int groupId = group.getInt("id");
                String groupName = group.getString("name");


                Group groupEntity = new Group(groupId, groupName);

                int testingGroupId = groupObject.getInt("id");
                TestingGroup testingGroup = new TestingGroup(testingGroupId, groupEntity, testing);

                testingGroups.add(testingGroup);
            }

            return testingGroups;
        } else {
            throw new Exception("Ошибка загрузки тестов: " + response.statusCode());
        }
    }
}
