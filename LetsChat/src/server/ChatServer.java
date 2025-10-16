package server;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private static final List<ClientHandler> clients = Collections.synchronizedList(new ArrayList<>());
    private static final int PORT = 5565;

    public static void main(String[] args) {
        System.out.println("Server gestartet auf Port " + PORT);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket socket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(socket);
                clients.add(handler);
                new Thread(handler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void broadcast(String message, ClientHandler sender) {
        synchronized (clients) {
            for (ClientHandler c : clients) {
                if (c != sender) {
                    c.sendMessage(message);
                }
            }
        }
    }

    public static void updateUserList() {
        synchronized (clients) {
            StringBuilder sb = new StringBuilder("USERS:");
            for (ClientHandler c : clients) {
                sb.append(c.getUsername()).append(",");
            }
            String userListMsg = sb.toString();
            for (ClientHandler c : clients) {
                c.sendMessage(userListMsg);
            }
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
            if (out != null) out.println(message);
        }

        @Override
        public void run() {
            try {
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                // Name vom Client empfangen
                out.println("NAME?");
                username = in.readLine();
                System.out.println(username + " hat den Chat betreten");

                broadcast(username + " hat den Chat betreten", this);
                updateUserList();

                String msg;
                while ((msg = in.readLine()) != null) {
                    if ("EXIT".equalsIgnoreCase(msg)) break;
                    broadcast(username + ": " + msg, this);
                }

            } catch (IOException e) {
                System.out.println(username + " hat den Chat unerwartet verlassen");
            } finally {
                cleanup();
            }
        }

        private void cleanup() {
            try { socket.close(); } catch (IOException ignored) {}
            clients.remove(this);
            broadcast(username + " hat den Chat verlassen", this);
            updateUserList();
            System.out.println(username + " ist aus dem Chat gegangen");
        }
    }
}
