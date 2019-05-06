package com.example.cryptomonitor.events;

public class FinishEvent extends Event {
    public void accepted() {
        mIsHandled = true;
    }
}
