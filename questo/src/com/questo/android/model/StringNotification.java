package com.questo.android.model;

public class StringNotification implements Notification {

    private String notif;

    public StringNotification(String notif) {
        this.notif = notif;
    }
    
    public String toString() {
        return this.notif;
    }
}
