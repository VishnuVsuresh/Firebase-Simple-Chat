package com.chatapp.model;

/**
 * Created by vishnu on 25-01-2018.
 */

public class Chat {
    public String sender;
    public String receiver;
    public String senderUid;
    public String receiverUid;
    public String message;
    public String url;
    public long timestamp;

    public Chat() {
    }

    public Chat(String sender, String receiver, String senderUid, String receiverUid, String message,String url, long timestamp) {
        this.sender = sender;
        this.receiver = receiver;
        this.senderUid = senderUid;
        this.receiverUid = receiverUid;
        this.message = message;
        this.url = url;
        this.timestamp = timestamp;
    }

   }
