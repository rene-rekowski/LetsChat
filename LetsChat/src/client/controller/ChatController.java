package client.controller;

import client.view.ChatView;
import java.io.*;
import java.net.*;
import javafx.application.Platform;

public class ChatController {
    private final ChatView view;
    private final String username;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public ChatController(ChatView view, String username) {
        this.view = view;
        this.username = username;
        setupConnection();
        setupInputListener();
    }

    private void setupConnection() {
        new Thread(() -> {
            try {
                socket = new Socket("192.168.178.22", 5562);
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                if ("NAME?".equals(in.readLine())) {
                    out.println(username);
                }

                Platform.runLater(() -> view.inputField.setDisable(false));

                String msg;
                while ((msg = in.readLine()) != null) {
                    String finalMsg = msg;
                    Platform.runLater(() -> {
                        if (finalMsg.startsWith("USERS:")) {
                            String[] users = finalMsg.substring(6).split(",");
                            view.userListView.getItems().setAll(users);
                        } else {
                            boolean isOwn = finalMsg.startsWith(username + ":");
                            String sender = finalMsg.split(":")[0];
                            String text = finalMsg.substring(finalMsg.indexOf(":")+1).trim();
                            view.appendMessage(sender, text, isOwn);
                        }
                    });
                }
            } catch (IOException e) {
                Platform.runLater(() -> view.appendMessage("System", "Verbindung zum Server fehlgeschlagen!", false));
            }
        }).start();
    }

    private void setupInputListener() {
        view.inputField.setOnAction(e -> {
            String msg = view.inputField.getText();
            if (out != null && !msg.isEmpty()) {
                out.println(msg);
                view.appendMessage(username, msg, true);
                view.inputField.clear();
            }
        });
    }

    public void closeConnection() {
        try {
            if (out != null) out.println("EXIT");
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
        } catch (IOException e) { e.printStackTrace(); }
    }
}
