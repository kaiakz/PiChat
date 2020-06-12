package com.kaiakz.pichat;

import android.graphics.Bitmap;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Message {
    private String sender;
    private String timestamp;
    private Bitmap bitmap;

    Message(Bitmap bitmap, String sender) {
        this.bitmap = bitmap;
        this.sender = sender;
        this.timestamp = new SimpleDateFormat("HH:mm:ss", Locale.CHINA).format(new Date(System.currentTimeMillis()));
    }

    public String getSender() {
        return sender;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
