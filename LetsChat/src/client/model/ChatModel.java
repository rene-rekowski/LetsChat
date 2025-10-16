package client.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ChatModel {
    private final ObservableList<Message> messages = FXCollections.observableArrayList();
    private final ObservableList<User> users = FXCollections.observableArrayList();

    public ObservableList<Message> getMessages() { return messages; }
    public ObservableList<User> getUsers() { return users; }

    public void addMessage(Message m) { messages.add(m); }
    public void addUser(User u) { users.add(u); }
    public void removeUser(User u) { users.remove(u); }
}
