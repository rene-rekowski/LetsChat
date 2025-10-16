package model;

public class User {
    private final String name;
    private boolean online;

    public User(String name) {
        this.name = name;
        this.online = true;
    }

    public String getName() { return name; }
    public boolean isOnline() { return online; }
    public void setOnline(boolean online) { this.online = online; }
}
