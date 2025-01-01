package com.example.stestingknugui.service;

import javafx.scene.control.ListView;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.io.InputStream;
import java.util.ArrayList;
public class TestingGroupService {

    private static final Map<String, Integer> testIdMap = new HashMap<>();
    private static final Map<String, Integer> groupIdMap = new HashMap<>();

    public static void loadTests(String jwtToken, ListView<String> testsListView) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL("http://localhost:8087/api/testing/all").openConnection();
            connection.setRequestProperty("Authorization", "Bearer " + jwtToken);
            connection.setRequestMethod("GET");

            if (connection.getResponseCode() == 200) {
                JSONArray tests = new JSONArray(new String(connection.getInputStream().readAllBytes()));
                for (int i = 0; i < tests.length(); i++) {
                    JSONObject test = tests.getJSONObject(i);
                    int id = test.getInt("id");
                    String name = test.getString("name");

                    testIdMap.put(name, id);
                    testsListView.getItems().add(name);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<String> loadGroups(String jwtToken) {
        List<String> groups = new ArrayList<>();
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL("http://localhost:8087/api/group/all").openConnection();
            connection.setRequestProperty("Authorization", "Bearer " + jwtToken);
            connection.setRequestMethod("GET");

            if (connection.getResponseCode() == 200) {
                JSONArray groupsArray = new JSONArray(new String(connection.getInputStream().readAllBytes()));
                for (int i = 0; i < groupsArray.length(); i++) {
                    JSONObject group = groupsArray.getJSONObject(i);
                    int id = group.getInt("id");
                    String name = group.getString("name");

                    groupIdMap.put(name, id);
                    groups.add(name);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return groups;
    }


    public static boolean assignTest(String jwtToken, String testName, List<String> groupNames) {
        try {
            Integer testId = testIdMap.get(testName);
            if (testId == null) {
                //System.err.println("Ошибка: Тест с именем '" + testName + "' не найден.");
                return false;
            }

            for (String groupName : groupNames) {
                Integer groupId = groupIdMap.get(groupName);
                if (groupId == null) {
                    //System.err.println("Ошибка: Группа с именем '" + groupName + "' не найдена.");
                    continue;
                }

                JSONObject requestBody = new JSONObject();
                requestBody.put("group", new JSONObject().put("id", groupId));
                requestBody.put("testing", new JSONObject().put("id", testId));

                HttpURLConnection connection = (HttpURLConnection) new URL("http://localhost:8087/api/testing-group").openConnection();
                connection.setRequestProperty("Authorization", "Bearer " + jwtToken);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                connection.getOutputStream().write(requestBody.toString().getBytes());

                int responseCode = connection.getResponseCode();
                //System.out.println("Код ответа для группы '" + groupName + "': " + responseCode);

                if (responseCode >= 200 && responseCode < 300) {
                    //System.out.println("Тест успешно назначен для группы: " + groupName);
                } else {
                    InputStream errorStream = connection.getErrorStream();
                    if (errorStream != null) {
                        String errorResponse = new String(errorStream.readAllBytes());
                        //System.err.println("Ошибка API для группы '" + groupName + "': " + errorResponse);
                    }
//                    else {
//                        System.err.println("Ошибка API для группы '" + groupName + "', код ответа: " + responseCode);
//                    }
                    return false;
                }
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}
