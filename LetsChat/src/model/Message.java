package model;

public class Message {
    private final String sender;
    private final String text;
    private final boolean isOwn;

    public Message(String sender, String text, boolean isOwn) {
        this.sender = sender;
        this.text = text;
        this.isOwn = isOwn;
    }

    public String getSender() { return sender; }
    public String getText() { return text; }
    public boolean isOwn() { return isOwn; }
}
