package com.kaiakz.pichat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton fbs;
    Intent intent;

    List<PMessage> mlist = new ArrayList<>();
    MessageAdapter adapter;

    Handler handler;

    String username = "ME";

    DataOutputStream dataOutputStream;
    DataInputStream dataInputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = getIntent().getStringExtra("USERNAME");

        intent = new Intent(this, PaintActivity.class);

        RecyclerView recyclerView = findViewById(R.id.message_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new MessageAdapter(mlist);
        recyclerView.setAdapter(adapter);

        fbs = findViewById(R.id.floatingActionButton);
        fbs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(intent, 1);
            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                    if (msg.what == 100) {
                        Toast.makeText(getApplicationContext(), "Received A New Picture", Toast.LENGTH_SHORT).show();
//                        byte[] b = (byte[])msg.obj;
//                        Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
//                        mlist.add(new PMessage(bitmap, ));
                        adapter.notifyDataSetChanged();
                    }
                    return true;
                }
            });

        dataInputStream = ((PiChat)getApplication()).getDataInputStream();
        dataOutputStream = ((PiChat)getApplication()).getDataOutputStream();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
//                    socket = new Socket("10.0.2.2", 8011);
//                    dataInputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
//                    dataOutputStream = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                    while (true) {
                        String sender = dataInputStream.readUTF();
                        int len = dataInputStream.readInt();
                        byte[] b = new byte[len];
                        dataInputStream.readFully(b);
                        Message msg = new Message();
                        msg.what = 100;
                        Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                        mlist.add(new PMessage(bitmap, sender));
                        handler.sendMessage(msg);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1) {
            if (data != null) {
                final byte[] bmpBytes = data.getByteArrayExtra("BMP");
                assert bmpBytes != null;
                Bitmap bitmap = BitmapFactory.decodeByteArray(bmpBytes, 0, bmpBytes.length);
                mlist.add(new PMessage(bitmap, username));
                adapter.notifyDataSetChanged();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            dataOutputStream.writeInt(1);
                            dataOutputStream.flush();
//                            dataOutputStream.writeUTF(username);
//                            dataOutputStream.flush();
                            dataOutputStream.writeInt(bmpBytes.length);
                            dataOutputStream.flush();
                            dataOutputStream.write(bmpBytes);
                            dataOutputStream.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        }
    }


}
