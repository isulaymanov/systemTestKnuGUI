package com.example.stestingknugui.util;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Optional;

public class DialogTest {

    // Метод для отображения диалогового окна редактирования названия теста
    public static Optional<String> showEditTestDialog(String currentName) {
        // Создаем окно с полем для ввода
        TextField textField = new TextField(currentName); // Текущее название теста в поле
        textField.setPromptText("Введите новое название теста");

        // Создаем диалог
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Редактирование названия теста");
        alert.setHeaderText("Введите новое название теста:");

        // Создаем кнопки
        ButtonType saveButton = new ButtonType("Сохранить");
        ButtonType cancelButton = new ButtonType("Отмена");

        // Настроим диалог для добавления текстового поля и кнопок
        alert.getButtonTypes().setAll(saveButton, cancelButton);
        VBox vBox = new VBox(10, textField);
        alert.getDialogPane().setContent(vBox);

        // Показать диалог и вернуть результат
        Optional<ButtonType> result = alert.showAndWait();

        // Если нажата кнопка "Сохранить", возвращаем введенное название
        if (result.isPresent() && result.get() == saveButton) {
            String newName = textField.getText().trim();
            if (!newName.isEmpty()) {
                return Optional.of(newName); // Возвращаем новое название
            } else {
                showError("Ошибка", "Название не может быть пустым.");
            }
        }

        return Optional.empty(); // Возвращаем пустое значение, если отменено или пустое имя
    }

    // Метод для отображения ошибки
    public static void showError(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
