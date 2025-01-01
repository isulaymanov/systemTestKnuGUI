package com.example.stestingknugui.service;

import javafx.concurrent.Task;
import org.json.JSONArray;
import org.json.JSONObject;

import com.example.stestingknugui.model.Group;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class LoadGroupsService extends Task<List<Group>>{
    private final String jwtToken;

    public LoadGroupsService(String jwtToken){
        this.jwtToken = jwtToken;
    }

    @Override
    protected List<Group> call() throws Exception {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8087/api/group/all"))
                .header("Authorization", "Bearer " + jwtToken)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            JSONArray jsonArray = new JSONArray(response.body());
            List<Group> groups = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject group = jsonArray.getJSONObject(i);

                Integer id = (Integer) group.get("id");
                String name = group.getString("name");

                groups.add(new Group(id, name));
            }

            return groups;
        } else {
            throw new Exception("Ошибка в загрузке групп: " + response.statusCode());
        }
    }

}
