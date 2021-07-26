package com.games.ms.model;

public class Chats {

    String sender;
    String receiver;
    String message;
    String id;
    String User_id;
    String SenderImage;

    public String getSenderImage() {
        return SenderImage;
    }

    public void setSenderImage(String senderImage) {
        SenderImage = senderImage;
    }

    public String getUser_id() {
        return User_id;
    }

    public void setUser_id(String user_id) {
        User_id = user_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
