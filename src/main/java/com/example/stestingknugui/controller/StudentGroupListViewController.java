package com.example.stestingknugui.controller;

import com.example.stestingknugui.util.WindowCenterer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.Parent;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

public class StudentGroupListViewController {

    @FXML
    private ListView<String> groupListView;

    @FXML
    private Button selectButton;

    private ObservableList<String> groupList = FXCollections.observableArrayList();
    private Map<String, Integer> groupIdMap = new HashMap<>(); // Хранит соответствие имени группы и её ID
    private int selectedGroupId;

    @FXML
    public void initialize() {
        loadGroups();
        groupListView.setItems(groupList);

        // Enable "Выбрать" button only when a group is selected
        groupListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            selectButton.setDisable(newVal == null);
        });
    }

    private void loadGroups() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8087/api/testing-group/testing-group/all"))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JSONArray groups = new JSONArray(response.body());
                Set<String> uniqueGroupNames = new HashSet<>(); // Используем Set для хранения уникальных имен групп
                for (int i = 0; i < groups.length(); i++) {
                    JSONObject groupEntry = groups.getJSONObject(i);
                    JSONObject group = groupEntry.getJSONObject("group");

                    String groupName = group.getString("name");
                    int groupId = group.getInt("id");

                    // Добавляем только уникальные группы
                    if (uniqueGroupNames.add(groupName)) {
                        groupList.add(groupName);
                        groupIdMap.put(groupName, groupId); // Сохраняем ID группы
                    }
                }
            } else {
                showError("Ошибка загрузки данных", "Не удалось получить список групп. Код ответа: " + response.statusCode());
            }
        } catch (Exception e) {
            showError("Ошибка соединения", "Не удалось подключиться к серверу.");
            e.printStackTrace();
        }
    }


    @FXML
    private void onSelectGroup() {
        String selectedGroupName = groupListView.getSelectionModel().getSelectedItem();
        if (selectedGroupName != null) {
            selectedGroupId = groupIdMap.get(selectedGroupName); // Получаем правильный ID группы
            //System.out.println("Выбрана группа: " + selectedGroupName + " с ID " + selectedGroupId);
            moveToNextWindow(selectedGroupName);
        }
    }

    private void moveToNextWindow(String selectedGroupName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/stestingknugui/TestListView.fxml"));
            Parent root = loader.load();

            StudentTestListViewController controller = loader.getController();
            controller.setSelectedGroupData(selectedGroupId, selectedGroupName);

            Stage stage = (Stage) selectButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            // Центрируем окно
            WindowCenterer.centerOnScreen(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
