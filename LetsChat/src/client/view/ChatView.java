package client.view;

import client.controller.ChatController;
import client.model.ChatModel;
import client.model.Message;
import client.model.User;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class ChatView {
    private final ChatModel model;
    private final ChatController controller;
    private TextField inputField;
    private ListView<User> userListView;
    private ListView<Message> chatListView;

    public ChatView(ChatModel model, ChatController controller) {
        this.model = model;
        this.controller = controller;
    }

    public void start(Stage stage) {
        // Chatbereich
        chatListView = new ListView<>(model.getMessages());
        chatListView.setPrefWidth(550);
        chatListView.setPrefHeight(350);
        chatListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Message m, boolean empty) {
                super.updateItem(m, empty);
                if (empty || m == null) {
                    setText(null);
                } else {
                    setText(m.getSender() + ": " + m.getText());
                }
            }
        });

        // Online-Benutzer
        userListView = new ListView<>(model.getUsers());
        userListView.setPrefWidth(150);
        userListView.setCellFactory(lv -> new UserListCell());

        // Eingabefeld
        inputField = new TextField();
        inputField.setPromptText("Nachricht schreiben...");
        inputField.setOnAction(e -> {
            String msg = inputField.getText();
            controller.sendMessage(msg);
            inputField.clear();
        });

        HBox mainLayout = new HBox(10, chatListView, userListView);
        VBox root = new VBox(10, mainLayout, inputField);

        Scene scene = new Scene(root, 700, 400);
        stage.setScene(scene);
        stage.setTitle("LetsChat");
        stage.setResizable(false);
        stage.show();
    }
}
