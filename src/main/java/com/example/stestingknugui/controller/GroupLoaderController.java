package com.example.stestingknugui.controller;
import com.example.stestingknugui.service.GroupService;
import com.example.stestingknugui.util.DialogGroup;
import com.example.stestingknugui.util.WindowCenterer;
import javafx.scene.Parent;
import com.example.stestingknugui.model.Group;
import com.example.stestingknugui.service.LoadGroupsService;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.List;
import java.util.Optional;


public class GroupLoaderController {
    public static void loadGroups(String jwtToken, VBox gruopVBox) {
        LoadGroupsService loadGroupsRequest = new LoadGroupsService(jwtToken);

        loadGroupsRequest.setOnSucceeded(event -> displayGroups(loadGroupsRequest.getValue(), gruopVBox,  jwtToken));
        loadGroupsRequest.setOnFailed(event -> showError("Не удалось загрузить группы", loadGroupsRequest.getException().getMessage()));

        new Thread(loadGroupsRequest).start();
    }

    private static void displayGroups(List<Group> groups, VBox gruopVBox, String jwtToken) {

        gruopVBox.getChildren().clear();
        for (Group group : groups) {
            //
            //Label label = new Label("ID: " + group.getId() + ", Name: " + group.getName());
            Label label = new Label(group.getName());
            label.setStyle("-fx-font-size: 12px; -fx-padding: 5px;");


            label.setOnMouseClicked((MouseEvent event) -> {
                if (event.getButton() == javafx.scene.input.MouseButton.PRIMARY) {
                    // Левая кнопка мыши — открыть тесты
                    openTestsWindow(group.getId(), jwtToken);
                } else if (event.getButton() == javafx.scene.input.MouseButton.SECONDARY) {
                    // Правая кнопка мыши — редактировать название группы
                    Optional<String> newName = DialogGroup.showEditGroupDialog(group.getName());
                    newName.ifPresent(name -> {
                        if (GroupService.editGroup(group.getId(), name, jwtToken)) {
                            Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "Название успешно изменено!", ButtonType.OK);
                            successAlert.showAndWait();
                            Group updatedGroup = new Group(group.getId(), name);  // Новый объект с обновленным именем

                            label.setText("ID: " + updatedGroup.getId() + ", Name: " + updatedGroup.getName());
//                            label.setText(updatedGroup.getName());

                            loadGroups(jwtToken, gruopVBox); // Обновляем список групп
                        } else {
                            DialogGroup.showError("Ошибка при редактировании группы.");
                        }
                    });
                }
            });

            gruopVBox.getChildren().add(label);
        }
    }

    private static void openTestsWindow(int groupId, String jwtToken) {
        try {
            FXMLLoader loader = new FXMLLoader(GroupLoaderController.class.getResource("/com/example/stestingknugui/tests_window.fxml"));
            Parent root = loader.load();

            TestsWindowController controller = loader.getController();
            controller.setJwtToken(jwtToken);
            controller.loadTests(groupId, jwtToken);
            //System.out.println("ID выброной группы: " + groupId);

            Stage stage = new Stage();
            stage.setTitle("Тесты группы");
            stage.setScene(new Scene(root));
            // Центрируем окно
            WindowCenterer.centerOnScreen(stage);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Не удалось открыть окно тестов");
            alert.showAndWait();
        }
    }

    private static void showError(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
