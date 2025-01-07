package com.example.stestingknugui.util;

import javafx.stage.Stage;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

public class WindowCenterer {

    public static void centerOnScreen(Stage stage) {
        // Получаем границы экрана
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();

        // Добавляем слушатели для изменения размеров окна
        stage.widthProperty().addListener((observable, oldValue, newValue) -> {
            stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
        });
        stage.heightProperty().addListener((observable, oldValue, newValue) -> {
            stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
        });
    }
}
