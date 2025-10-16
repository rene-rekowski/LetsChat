package client.controller;

import client.model.ChatModel;
import client.model.Message;
import client.model.User;
import javafx.application.Platform;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;

public class ChatController {
    private final ChatModel model;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private final String serverIP;
    private final int serverPort;
    private final String username;

    public ChatController(ChatModel model, String serverIP, int serverPort, String username) {
        this.model = model;
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        this.username = username;
    }

    public void connect() throws IOException {
        socket = new Socket(serverIP, serverPort);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        // Name senden
        if ("NAME?".equals(in.readLine())) {
            out.println(username);
        }

        // Thread für eingehende Nachrichten
        new Thread(this::listen).start();
    }

    private void listen() {
        String msg;
        try {
            while ((msg = in.readLine()) != null) {
                final String finalMsg = msg;
                if (finalMsg.startsWith("USERS:")) {
                    String[] names = finalMsg.substring(6).split(",");
                    Platform.runLater(() -> {
                        model.getUsers().clear();
                        for (String n : names) {
                            if (!n.isBlank()) model.addUser(new User(n));
                        }
                    });
                } else {
                    Platform.runLater(() -> {
                        model.addMessage(new Message(finalMsg.split(":")[0],
                                finalMsg.substring(finalMsg.indexOf(":") + 1).trim()));
                    });
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void sendMessage(String text) {
        if (out != null && !text.isEmpty()) {
            out.println(text);
            model.addMessage(new Message(username, text));
        }
    }

    public void disconnect() {
        try {
            if (out != null) out.println("EXIT");
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
