package server;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {

    private static final int PORT = 5562;
    private static final List<ClientHandler> clients = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Server gestartet. Wartet auf Clients auf Port " + PORT + "...");

        while (true) {
            Socket socket = serverSocket.accept();
            ClientHandler clientHandler = new ClientHandler(socket);
            clients.add(clientHandler);
            new Thread(clientHandler).start();
        }
    }

    public static void broadcast(String message, ClientHandler sender) {
        for (ClientHandler c : clients) {
            if (c != sender) {
                c.sendMessage(message);
            }
        }
    }

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
        private final Socket socket;
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
            out.println(message);
        }

        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                // Name vom Client abfragen
                out.println("NAME?");
                username = in.readLine();
                System.out.println(username + " hat den Chat betreten");

                broadcast(username + " hat den Chat betreten", this);
                updateUserList();

                String msg;
                while ((msg = in.readLine()) != null) {
                    if (msg.equalsIgnoreCase("EXIT")) break;
                    broadcast(username + ": " + msg, this);
                }

            } catch (IOException e) {
                System.out.println(username + " hat den Chat unerwartet verlassen");
            } finally {
                try { socket.close(); } catch (IOException ignored) {}
                clients.remove(this);
                broadcast(username + " hat den Chat verlassen", this);
                updateUserList();
                System.out.println(username + " entfernt. Aktuelle Clients: " + clients.size());
            }
        }
    }
}
