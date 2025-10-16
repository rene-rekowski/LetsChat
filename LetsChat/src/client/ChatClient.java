package client;

import java.io.*;
import java.net.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ChatClient extends Application {
    private PrintWriter out;
    private BufferedReader in;
    private TextFlow chatArea;
    private TextField inputField;
    private ListView<String> userListView;
    private String username;
    private Socket socket;

    @Override
    public void start(Stage primaryStage) {
        // Name abfragen
        TextInputDialog dialog = new TextInputDialog("Benutzer");
        dialog.setTitle("Login");
        dialog.setHeaderText("Gib deinen Namen ein");
        dialog.setContentText("Name:");
        username = dialog.showAndWait().orElse("User");

        chatArea = new TextFlow();
        chatArea.setPrefHeight(350);
        ScrollPane chatScroll = new ScrollPane(chatArea);
        chatScroll.setFitToWidth(true);
        chatScroll.setPrefWidth(550); // Chatbereich größer
        chatScroll.setPrefHeight(350);

        inputField = new TextField();
        inputField.setPromptText("Nachricht schreiben...");
        inputField.setDisable(true);

        userListView = new ListView<>();
        userListView.setPrefWidth(150); // Online-Liste kleiner
        // Custom CellFactory für grünen Punkt
        userListView.setCellFactory(lv -> new ListCell<String>() {
            private final Label label = new Label();
            private final Circle statusCircle = new Circle(5, Color.GREEN);

            @Override
            protected void updateItem(String user, boolean empty) {
                super.updateItem(user, empty);
                if (empty || user == null || user.isBlank()) {
                    setText(null);
                    setGraphic(null);
                } else {
                    label.setText(user);
                    HBox hbox = new HBox(5, label, statusCircle);
                    setGraphic(hbox);
                }
            }
        });

        inputField.setOnAction(e -> {
            String msg = inputField.getText();
            if (out != null && !msg.isEmpty()) {
                out.println(msg);
                appendMessage(username, msg, true);
                inputField.clear();
            }
        });

        HBox mainLayout = new HBox(10, chatScroll, userListView);
        VBox root = new VBox(10, mainLayout, inputField);

        Scene scene = new Scene(root, 700, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("LetsChat");
        primaryStage.setResizable(false); // Fenstergröße fixieren

        // Fenster-Schließen-Handler hinzufügen
        primaryStage.setOnCloseRequest((WindowEvent e) -> {
            try {
                if (out != null) {
                    out.println("EXIT"); // Server informieren
                }
                if (in != null) in.close();
                if (out != null) out.close();
                if (socket != null) socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                Platform.exit();
            }
        });

        primaryStage.show();

        new Thread(this::setupConnection).start();
    }

    private void setupConnection() {
        try {
            socket = new Socket("192.168.178.22", 5560); // IP vom Server anpassen
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Server fragt nach Name
            if ("NAME?".equals(in.readLine())) {
                out.println(username);
            }

            Platform.runLater(() -> inputField.setDisable(false));

            String msg;
            while ((msg = in.readLine()) != null) {
                String finalMsg = msg;
                Platform.runLater(() -> {
                    if (finalMsg.startsWith("USERS:")) {
                        String[] users = finalMsg.substring(6).split(",");
                        userListView.getItems().setAll(users);
                    } else {
                        boolean isOwn = finalMsg.startsWith(username + ":");
                        appendMessage(finalMsg.split(":")[0], finalMsg.substring(finalMsg.indexOf(":") + 1).trim(), isOwn);
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
            Platform.runLater(() -> appendMessage("System", "Fehler: Verbindung zum Server fehlgeschlagen!", false));
        }
    }

    // Methode zum Hinzufügen von Nachrichten in den Chat
    private void appendMessage(String sender, String message, boolean isOwn) {
        Text name = new Text(sender + ": ");
        name.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        name.setFill(isOwn ? Color.BLUE : Color.BLACK);

        Text msg = new Text(message + "\n");
        msg.setFill(isOwn ? Color.BLUE : Color.BLACK);

        chatArea.getChildren().addAll(name, msg);
        chatArea.layout(); // Scroll aktualisieren
    }

    public static void main(String[] args) {
        launch(args);
    }
}
