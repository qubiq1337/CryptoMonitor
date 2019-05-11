package com.example.cryptomonitor.events;

public abstract class Event {
    boolean mIsHandled = false;

    public boolean isHandled() {
        return mIsHandled;
    }

    public void handled(){
        mIsHandled = true;
    }
}
