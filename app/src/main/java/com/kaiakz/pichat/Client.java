package com.kaiakz.pichat;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class Client {
    private Socket socket;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;

    private Thread receiver;


    public interface Event {
        void onBitmap(String sender, byte[] b);
    }

    public Client() throws IOException {
        // 10.0.2.2
        socket = new Socket("10.0.2.2", 8011);
        dataInputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        dataOutputStream = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
    }

    public boolean register(String id, String pwd) throws IOException {
        dataOutputStream.writeInt(-1);
        dataOutputStream.flush();
        dataOutputStream.writeUTF(id);
        dataOutputStream.flush();
        dataOutputStream.writeUTF(pwd);
        dataOutputStream.flush();
        return dataInputStream.readBoolean();
    }

    public boolean login(String id, String pwd) throws IOException {
        dataOutputStream.writeInt(0);
        dataOutputStream.flush();
        dataOutputStream.writeUTF(id);
        dataOutputStream.flush();
        dataOutputStream.writeUTF(pwd);
        dataOutputStream.flush();
        return dataInputStream.readBoolean();
    }

    public void startReceiver(Event event) {
        this.receiver = new Thread(new Receiver(event));
        Log.d("????", "startReceiver: I");
        receiver.start();
    }

    public void close() throws IOException {
        receiver.stop();
        dataInputStream.close();
        dataOutputStream.close();
        socket.close();
    }

    public void sendBitmap(String sender, byte[] b) throws IOException {
        dataOutputStream.writeInt(1);
        dataOutputStream.flush();
        dataOutputStream.writeUTF(sender);
        dataOutputStream.flush();
        dataOutputStream.writeInt(b.length);
        dataOutputStream.flush();
        dataOutputStream.write(b);
        dataOutputStream.flush();
    }

    class Receiver implements Runnable {
        private Event event;

        Receiver(Event event) {
            this.event = event;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    String sender = dataInputStream.readUTF();
                    int len = dataInputStream.readInt();
                    byte[] b = new byte[len];
                    dataInputStream.readFully(b);
                    event.onBitmap(sender, b);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    dataOutputStream.close();
                    dataInputStream.close();
                    socket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
