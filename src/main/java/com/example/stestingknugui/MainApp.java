package com.example.stestingknugui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Создаем Stage для прелоадера
        Stage preloaderStage = new Stage();
        preloaderStage.initStyle(StageStyle.UNDECORATED); // Убираем рамки и кнопки

        // Загружаем FXML для прелоадера
        FXMLLoader preloaderLoader = new FXMLLoader(getClass().getResource("preloader.fxml"));
        Scene preloaderScene = new Scene(preloaderLoader.load());
        preloaderStage.setScene(preloaderScene);

        // Устанавливаем прозрачный фон для прелоадера
        preloaderStage.initStyle(StageStyle.TRANSPARENT);
        preloaderScene.setFill(null);

        preloaderStage.show();

        // Имитация задержки прелоадера (3-5 секунд)
        new Thread(() -> {
            try {
                Thread.sleep(5000); // Задержка 4 секунды
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // После задержки переключаем на главное окно
            javafx.application.Platform.runLater(() -> {
                preloaderStage.close(); // Закрываем прелоадер
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("start.fxml"));
                    Scene mainScene = new Scene(loader.load());
                    primaryStage.setTitle("Система авторизации");
                    primaryStage.setScene(mainScene);
                    primaryStage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }).start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}


