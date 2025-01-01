package com.example.stestingknugui.util;

import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class DialogGroup {
    public static Optional<String> showCreateGroupDialog() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Создать группу");
        dialog.setHeaderText("Введите название группы:");
        dialog.setContentText("Название:");

        Optional<String> result = dialog.showAndWait();
        return result.filter(name -> !name.trim().isEmpty());
    }

    public static Optional<String> showEditGroupDialog(String currentName) {
        TextInputDialog dialog = new TextInputDialog(currentName);
        dialog.setTitle("Редактировать группу");
        dialog.setHeaderText("Введите новое название группы:");
        dialog.setContentText("Название:");

        return dialog.showAndWait().filter(name -> !name.trim().isEmpty());
    }


    public static void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.showAndWait();
    }
}
