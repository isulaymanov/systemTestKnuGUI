package com.example.stestingknugui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Stage preloaderStage = new Stage();
        preloaderStage.initStyle(StageStyle.UNDECORATED);

        FXMLLoader preloaderLoader = new FXMLLoader(getClass().getResource("preloader.fxml"));
        Scene preloaderScene = new Scene(preloaderLoader.load());
        preloaderStage.setScene(preloaderScene);

        preloaderStage.initStyle(StageStyle.TRANSPARENT);
        preloaderScene.setFill(null);

        preloaderStage.show();

        new Thread(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            javafx.application.Platform.runLater(() -> {
                preloaderStage.close();
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


