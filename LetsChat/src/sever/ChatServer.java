package sever;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private static List<ClientHandler> clients = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(5560); // Port anpassen
        System.out.println("Server gestartet. Wartet auf Clients...");

        while (true) {
            Socket socket = serverSocket.accept();
            ClientHandler clientHandler = new ClientHandler(socket);
            clients.add(clientHandler);
            new Thread(clientHandler).start();
        }
    }

    // Nachricht an alle außer Sender
    public static void broadcast(String message, ClientHandler sender) {
        for (ClientHandler c : clients) {
            if (c != sender) {
                c.sendMessage(message);
            }
        }
    }

    // Online-Liste aktualisieren
    public static void updateUserList() {
        StringBuilder userList = new StringBuilder("USERS:");
        for (ClientHandler c : clients) {
            userList.append(c.getUsername()).append(",");
        }
        for (ClientHandler c : clients) {
            c.sendMessage(userList.toString());
        }
    }

    static class ClientHandler implements Runnable {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private String username;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public String getUsername() {
            return username;
        }

        public void sendMessage(String message) {
            if (out != null) {
                out.println(message);
            }
        }

        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                // Name vom Client empfangen
                out.println("NAME?");
                username = in.readLine();
                System.out.println(username + " hat den Chat betreten");
                broadcast(username + " hat den Chat betreten", this);
                updateUserList();

                String msg;
                while ((msg = in.readLine()) != null) {
                    if ("EXIT".equalsIgnoreCase(msg.trim())) {
                        // Client möchte rausgehen
                        break;
                    }
                    broadcast(username + ": " + msg, this);
                }
            } catch (IOException e) {
                System.out.println(username + " hat den Chat verlassen (Verbindung unterbrochen)");
            } finally {
                try { socket.close(); } catch (IOException ignored) {}
                clients.remove(this);
                broadcast(username + " hat den Chat verlassen", this);
                updateUserList();
                System.out.println(username + " wurde aus der Online-Liste entfernt");
            }
        }
    }
}
