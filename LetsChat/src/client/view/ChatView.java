package client.view;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.TextFlow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class ChatView {
    public TextFlow chatArea = new TextFlow();
    public TextField inputField = new TextField();
    public ListView<String> userListView = new ListView<>();

    public BorderPane createLayout() {
        chatArea.setPrefHeight(350);
        ScrollPane chatScroll = new ScrollPane(chatArea);
        chatScroll.setFitToWidth(true);
        chatScroll.setPrefWidth(550);

        inputField.setPromptText("Nachricht schreiben...");
        inputField.setDisable(true);

        userListView.setPrefWidth(150);
        userListView.setCellFactory(lv -> new ListCell<String>() {
            private final Label label = new Label();
            private final Circle statusCircle = new Circle(5, Color.GREEN);

            @Override
            protected void updateItem(String user, boolean empty) {
                super.updateItem(user, empty);
                if (empty || user == null || user.isBlank()) {
                    setText(null); setGraphic(null);
                } else {
                    label.setText(user);
                    HBox hbox = new HBox(5, label, statusCircle);
                    setGraphic(hbox);
                }
            }
        });

        HBox mainLayout = new HBox(10, chatScroll, userListView);
        VBox root = new VBox(10, mainLayout, inputField);
        BorderPane pane = new BorderPane();
        pane.setCenter(root);
        return pane;
    }

    public void appendMessage(String sender, String message, boolean isOwn) {
        Text name = new Text(sender + ": ");
        name.setFill(isOwn ? Color.BLUE : Color.BLACK);
        Text msg = new Text(message + "\n");
        msg.setFill(isOwn ? Color.BLUE : Color.BLACK);
        chatArea.getChildren().addAll(name, msg);
        chatArea.layout();
    }
}
