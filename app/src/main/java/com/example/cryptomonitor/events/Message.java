package com.example.cryptomonitor.events;

public class Message extends Event {
    private final String mMessageText;

    public Message(String messageText) {
        this.mMessageText = messageText;
    }

    public String getMessageText() {
        mIsHandled = true;
        return mMessageText;
    }
}
