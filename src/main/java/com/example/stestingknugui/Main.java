package com.example.stestingknugui;

import com.example.stestingknugui.util.WindowCenterer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Parent;
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

        // Центрируем окно
        WindowCenterer.centerOnScreen(preloaderStage);

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
                    // Центрируем окно
                    WindowCenterer.centerOnScreen(primaryStage);
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


