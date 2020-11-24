package com.wl.im.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Created on 2020/11/23 13:57
 *
 * @author wanglei
 * @Description
 * @projectName wl-IM
 */
public class MyImApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 500, 500);
        scene.getStylesheets().add(getClass().getResource("/MyImApp.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
