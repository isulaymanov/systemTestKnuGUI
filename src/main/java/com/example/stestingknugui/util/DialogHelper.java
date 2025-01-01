package com.example.stestingknugui.util;

import com.example.stestingknugui.controller.GroupLoaderController;
import com.example.stestingknugui.service.GroupService;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;

import java.util.Optional;

public class DialogHelper {

    public static void showCreateGroupDialog(String jwtToken, VBox gruopVBox) {
        Optional<String> groupName = DialogGroup.showCreateGroupDialog();
        groupName.ifPresent(name -> {
            if (GroupService.createGroup(name, jwtToken)) {
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "Группа успешно создана!", ButtonType.OK);
                successAlert.showAndWait();
                GroupLoaderController.loadGroups(jwtToken, gruopVBox);
            } else {
                showError("Ошибка при создании группы.");
            }
        });
    }

    public static void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.showAndWait();
    }
}
