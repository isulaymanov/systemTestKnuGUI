package com.example.stestingknugui.service;

import com.example.stestingknugui.model.TestResult;
import javafx.concurrent.Task;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class LoadResultsService extends Task<List<TestResult>> {

    private final int testingGroupId;
    private final String jwtToken;

    public LoadResultsService(int testingGroupId, String jwtToken) {
        this.testingGroupId = testingGroupId;
        this.jwtToken = jwtToken;
    }

    @Override
    protected List<TestResult> call() throws Exception {

        HttpClient client = HttpClient.newHttpClient();

        //System.out.println("Тест-Группа: " + testingGroupId);

        URI uri = URI.create("http://localhost:8087/api/attempt-student/testing-group/" + testingGroupId);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Authorization", "Bearer " + jwtToken)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        //System.out.println("Ответ сервера: " + response.body());

        if (response.statusCode() == 200) {
            JSONArray jsonArray = new JSONArray(response.body());

            List<TestResult> testResults = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                // Извлекаем данные и создаем объект TestResult
                int id = jsonObject.getInt("id");
                String name = jsonObject.getString("name");
                String surname = jsonObject.getString("surname");
                String dateCompletion = jsonObject.getString("dateCompletion");
                String result = jsonObject.getString("result");
                String startTime = jsonObject.getString("startTime");
                String endTime = jsonObject.getString("endTime");

                testResults.add(new TestResult(id, name, surname, dateCompletion, result, startTime, endTime));
                //System.out.println("Результаты: "+ testResults);

            }

            return testResults;
        } else {
            throw new Exception("Ошибка загрузки результатов: " + response.statusCode());
        }
    }
}
