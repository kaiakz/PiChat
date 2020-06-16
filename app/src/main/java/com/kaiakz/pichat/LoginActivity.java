package com.kaiakz.pichat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameET;
    private EditText emailET;
    private EditText codeET;
    private Button sendBtn;
    private Button loginBtn;

    private Handler handler;

    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameET = findViewById(R.id.edittext_username);
        emailET = findViewById(R.id.edittext_email);
        codeET = findViewById(R.id.edittext_pwd);
        loginBtn = findViewById(R.id.btn_login);
        sendBtn = findViewById(R.id.btn_sc);

        dataInputStream = ((PiChat)getApplication()).getDataInputStream();
        dataOutputStream = ((PiChat)getApplication()).getDataOutputStream();

        final Intent intent = new Intent(this, MainActivity.class);

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {

                switch (msg.what) {
                    case 200:
                        // Start MainActivity
                        Toast.makeText(getApplicationContext(), "Login Successfully", Toast.LENGTH_SHORT).show();
                        intent.putExtra("USERNAME", (String) msg.obj);
                        startActivity(intent);
                        loginBtn.setEnabled(true);
                        break;
                    case 400:
                        Toast.makeText(getApplicationContext(), "Error Code", Toast.LENGTH_SHORT).show();
                        loginBtn.setEnabled(true);
                        break;
                    default:
                        sendBtn.setEnabled(true);
                        break;

                }
                return true;
            }
        });


        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailET.getText().toString().trim();
                if (email.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please fill your email", Toast.LENGTH_SHORT).show();
                    return;
                }
                Pattern pattern = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}");//\w表示a-z，A-Z，0-9(\\转义符)
                Matcher matcher = pattern.matcher(email);
                if (matcher.matches()) {
                    emailme(email);
                    sendBtn.setEnabled(false);
                } else {
                    Toast.makeText(getApplicationContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
                }
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameET.getText().toString().trim();
                String c = codeET.getText().toString().trim();

                if (username.equals("") || c.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please fill your username & code", Toast.LENGTH_SHORT).show();
                    return;
                }

                int code = Integer.parseInt(c);

                loginBtn.setEnabled(false);
                login(username, code);
            }
        });
    }

    private void login(final String username, final int code) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.i("CODE: " + code, "USERNAME: " + username);
                    dataOutputStream.writeInt(0);
                    dataOutputStream.flush();
                    dataOutputStream.writeInt(code);
                    dataOutputStream.flush();
                    dataOutputStream.writeUTF(username);
                    dataOutputStream.flush();

                    boolean res = false;
                    res = dataInputStream.readBoolean();

                    Message msg = new Message();
                    if (res) {
                        Log.i("LOGIN", "LOGIN OK");
                        msg.what = 200;
                        msg.obj = username;
                    } else {
                        msg.what = 400;
                    }
                    handler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void emailme(final String email) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    dataOutputStream.writeInt(-1);
                    dataOutputStream.flush();
                    dataOutputStream.writeUTF(email);
                    dataOutputStream.flush();
                    Thread.sleep(10000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                msg.what = 201;
                handler.sendMessage(msg);
            }
        }).start();
    }

}