package client;

import client.view.ChatView;
import client.controller.ChatController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.control.TextInputDialog;

public class ChatClient extends Application {
    private ChatController controller;

    @Override
    public void start(Stage primaryStage) {
        TextInputDialog dialog = new TextInputDialog("Benutzer");
        dialog.setTitle("Login"); dialog.setHeaderText("Gib deinen Namen ein");
        dialog.setContentText("Name:");
        String username = dialog.showAndWait().orElse("User");

        ChatView view = new ChatView();
        controller = new ChatController(view, username);

        Scene scene = new Scene(view.createLayout(), 700, 400);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setTitle("LetsChat");

        primaryStage.setOnCloseRequest((WindowEvent e) -> controller.closeConnection());
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
