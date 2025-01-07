package com.example.stestingknugui.controller;

import com.example.stestingknugui.util.WindowCenterer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

public class StudentTestListViewController {

    @FXML
    private ListView<String> testListView;

    @FXML
    private Button selectButton;

    private ObservableList<String> testList = FXCollections.observableArrayList();
    private List<Integer> testIds = new ArrayList<>();
    private int selectedTestId;

    private int selectedGroupId;
    private String selectedGroupName;

    public void setSelectedGroupData(int groupId, String groupName) {
        this.selectedGroupId = groupId;
        this.selectedGroupName = groupName;
        //System.out.println("Передан ID группы: " + groupId);
        //System.out.println("Передано имя группы: " + groupName);

        loadTests();
    }

    @FXML
    public void initialize() {
        testListView.setItems(testList);


        // Включить кнопку "Выбрать" только при выборе теста
        testListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            selectButton.setDisable(newVal == null);
        });
    }

    private void loadTests() {
        try {
            // Создаем запрос для получения тестов группы
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8087/api/testing-group/group/" + selectedGroupId))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            //System.out.println("Эндпоинт /api/testing-group/group/ + selectedGroupId Возвращает: " + response.body());


            if (response.statusCode() == 200) {
                JSONArray tests = new JSONArray(response.body());

                // Добавляем уникальные тесты в список
                for (int i = 0; i < tests.length(); i++) {
                    JSONObject testEntry = tests.getJSONObject(i);
                    JSONObject testing = testEntry.getJSONObject("testing");

                    String testName = testing.getString("name");
                    int testId = testing.getInt("id");


                    // Добавляем тест в список
                    if (!testList.contains(testName)) {
                        testList.add(testName);
                        testIds.add(testId);
                        //System.out.println("Добавлен тест: " + testName + " с ID: " + testId);

                    }
                }
            } else {
                showError("Ошибка загрузки данных", "Не удалось получить список тестов. Код ответа: " + response.statusCode());
            }
        } catch (Exception e) {
            showError("Ошибка соединения", "Не удалось подключиться к серверу.");
            e.printStackTrace();
        }
    }

    @FXML
    private void onSelectTest() {
        String selectedTestName = testListView.getSelectionModel().getSelectedItem();
        if (selectedTestName != null) {
            selectedTestId = testIds.get(testListView.getSelectionModel().getSelectedIndex());
            //System.out.println("Выбран тест: " + selectedTestName + " с ID " + selectedTestId);
            //moveToNextWindow();

            //Получение теста-группа
            // Теперь нужно найти правильный id testing-group для выбранной группы и теста
            int testingGroupId = getTestingGroupId(selectedGroupId, selectedTestId);
            if (testingGroupId != -1) {
                moveToNextWindow(testingGroupId);
            } else {
                showError("Ошибка", "Не найдено соответствие для выбранного теста и группы.");
            }
        }
    }


    private int getTestingGroupId(int selectedGroupId, int selectedTestId) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8087/api/testing-group/testing-group/all"))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JSONArray testingGroups = new JSONArray(response.body());
                for (int i = 0; i < testingGroups.length(); i++) {
                    JSONObject testingGroup = testingGroups.getJSONObject(i);
                    JSONObject group = testingGroup.getJSONObject("group");
                    JSONObject testing = testingGroup.getJSONObject("testing");

                    int groupId = group.getInt("id");
                    int testId = testing.getInt("id");

                    if (groupId == selectedGroupId && testId == selectedTestId) {
                        return testingGroup.getInt("id"); // Возвращаем ID найденного testing-group
                    }
                }
            } else {
                showError("Ошибка загрузки данных", "Не удалось получить данные о testing-group. Код ответа: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1; // Возвращаем -1, если не нашли совпадений
    }

    private void moveToNextWindow(int testingGroupId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/stestingknugui/StudentDataView.fxml"));
            Parent root = loader.load();

            StudentDataViewController controller = loader.getController();
//            System.out.println("-------------------------------");
//            System.out.println("Тест-Группа: " + testingGroupId);
//            System.out.println("-------------------------------");
            controller.setData(selectedGroupId, selectedGroupName, selectedTestId, testListView.getSelectionModel().getSelectedItem(), testingGroupId);

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
