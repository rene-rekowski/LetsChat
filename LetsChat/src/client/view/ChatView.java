package client.view;

import client.controller.ChatController;
import client.model.ChatModel;
import client.model.Message;
import client.model.User;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Stage;

public class ChatView {
    private final ChatModel model;
    private final ChatController controller;
    private TextFlow chatArea;
    private TextField inputField;
    private ListView<User> userListView;

    public ChatView(ChatModel model, ChatController controller) {
        this.model = model;
        this.controller = controller;
    }

    public void start(Stage stage) {
        chatArea = new TextFlow();
        ScrollPane chatScroll = new ScrollPane(chatArea);
        chatScroll.setFitToWidth(true);
        chatScroll.setPrefWidth(550);
        chatScroll.setPrefHeight(350);

        inputField = new TextField();
        inputField.setPromptText("Nachricht schreiben...");
        inputField.setDisable(true);
        inputField.setOnAction(e -> {
            String msg = inputField.getText();
            controller.sendMessage(msg);
            inputField.clear();
            refreshChat();
        });

        userListView = new ListView<>();
        userListView.setPrefWidth(150);
        userListView.setCellFactory(lv -> new UserListCell());

        HBox mainLayout = new HBox(10, chatScroll, userListView);
        VBox root = new VBox(10, mainLayout, inputField);

        Scene scene = new Scene(root, 700, 400);
        stage.setScene(scene);
        stage.setTitle("LetsChat");
        stage.setResizable(false);
        stage.show();

        inputField.setDisable(false);
        refreshChat();
        refreshUsers();
    }

    private void refreshChat() {
        Platform.runLater(() -> {
            chatArea.getChildren().clear();
            for (Message m : model.getMessages()) {
                Text sender = new Text(m.getSender() + ": ");
                sender.setFont(Font.font("Arial", FontWeight.BOLD, 12));
                Text text = new Text(m.getText() + "\n");
                chatArea.getChildren().addAll(sender, text);
            }
        });
    }

    private void refreshUsers() {
        Platform.runLater(() -> userListView.getItems().setAll(model.getUsers()));
    }
}
