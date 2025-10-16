package client.model;

import java.util.ArrayList;
import java.util.List;

public class ChatModel {
    private final List<Message> messages = new ArrayList<>();
    private final List<User> users = new ArrayList<>();

    public List<Message> getMessages() { return messages; }
    public List<User> getUsers() { return users; }

    public void addMessage(Message m) { messages.add(m); }
    public void addUser(User u) { users.add(u); }
    public void removeUser(User u) { users.remove(u); }
}
