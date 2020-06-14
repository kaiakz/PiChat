package com.kaiakz.pichat;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Server {
    private ServerSocket serverSocket;
    private HashMap<String, String> info;
    private List<Connection> clients;

    public Server() throws IOException {
        serverSocket = new ServerSocket(8011);
        clients = new ArrayList<>();
        System.out.println("Start Server");
        new Thread(new Listener()).start();
    }

    public void Broadcast(String sender, int from, byte[] b) {
        for (Connection c : clients) {
            if (c == null || c.ID == from) {
                continue;
            }
            try {
               c.sendBitmap(sender, b); 
            } catch (Exception e) {
                e.printStackTrace();
                clients.remove(c);
            }
        }
    }

    class Listener implements Runnable {

        @Override
        public void run() {
            try {
                while (true) {
                    Socket client = serverSocket.accept();
                    System.out.println("New Connection");
                    Connection c = new Connection(client);
                    c.ID = clients.size();                    
                    clients.add(c);
                }
            } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    class Connection {
        private Socket client;
        private DataOutputStream dataOutputStream;
        private DataInputStream dataInputStream;

        public int ID;

        private String sender = "";

        public Connection(Socket socket) throws IOException {
                client = socket;
                dataInputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                dataOutputStream = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                new Thread(new Receiver()).start();
        }

        public void sendBitmap(String sender, byte[] b) throws IOException{
            dataOutputStream.writeUTF(sender);
            dataOutputStream.flush();
            dataOutputStream.writeInt(b.length);
            dataOutputStream.flush();
            dataOutputStream.write(b);
            dataOutputStream.flush();
        }

        class Receiver implements Runnable {

            @Override
            public void run() {
                try {
                    int type;
                    while (true) {
                            type = dataInputStream.readInt();
                            switch (type) {
                                case -1: {    //register
                                    String id = dataInputStream.readUTF();
                                    String pwd = dataInputStream.readUTF();
                                    if (info.containsKey(id)) {
                                        dataOutputStream.writeBoolean(false);
                                    } else {
                                        info.put(id, pwd);
                                        dataOutputStream.writeBoolean(true);
                                    }
                                    break;
                                }
                                case 0: {    //login
                                    String id = dataInputStream.readUTF();
                                    String pwd = dataInputStream.readUTF();
                                    if (info.containsKey(id) && Objects.equals(info.get(id), pwd)) {
                                        dataOutputStream.writeBoolean(true);
                                    } else {
                                        dataOutputStream.writeBoolean(false);
                                    }
                                    break;
                                }
                                case 1: { //Relay
                                    String name = dataInputStream.readUTF();
                                    int len = dataInputStream.readInt();
                                    byte[] b = new byte[len];
                                    dataInputStream.readFully(b);
                                    System.out.println(name + " sent a picture");
                                    // sendBitmap(name, b);
                                    Broadcast(sender, ID, b);
                                    break;
                                }
                            }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        dataInputStream.close();
                        dataInputStream.close();
                        client.shutdownInput();
                        client.shutdownOutput();
                        client.close();                        
                    } catch(Exception ex) {
                        ex.printStackTrace();
                    }
                } finally {
                    clients.set(ID, null);
                }
            }
        }
    }

    public static void main(String[] args) {
        try {
            new Server();
        } catch (Exception e) {
            //TODO: handle exception
            e.printStackTrace();
        }
    }
}
