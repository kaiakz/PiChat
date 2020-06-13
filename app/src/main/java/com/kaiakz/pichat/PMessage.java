package com.kaiakz.pichat;

import android.graphics.Bitmap;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

class PMessage {
    private String sender;
    private String timestamp;
    private Bitmap bitmap;

    PMessage(Bitmap bitmap, String sender) {
        this.bitmap = bitmap;
        this.sender = sender;
        this.timestamp = new SimpleDateFormat("HH:mm:ss", Locale.CHINA).format(new Date(System.currentTimeMillis()));
    }

    String getSender() {
        return sender;
    }

    String getTimestamp() {
        return timestamp;
    }

    Bitmap getBitmap() {
        return bitmap;
    }
}
