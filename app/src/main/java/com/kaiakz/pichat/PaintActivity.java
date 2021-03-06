package com.kaiakz.pichat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class PaintActivity extends AppCompatActivity {

    private PaintView paintView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paint);

        paintView = findViewById(R.id.paint_view);
        initClickListener();
    }

    private void initClickListener() {
        ImageButton red = findViewById(R.id.paint_tool_red);

        red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paintView.setColor(PaintView.ColorState.RED);
            }
        });

        ImageButton blue = findViewById(R.id.paint_tool_blue);
        blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paintView.setColor(PaintView.ColorState.BLUE);
            }
        });

        ImageButton green = findViewById(R.id.paint_tool_green);
        green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paintView.setColor(PaintView.ColorState.GREEN);
            }
        });

        ImageButton black = findViewById(R.id.paint_tool_black);
        black.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paintView.setColor(PaintView.ColorState.BLACK);
            }
        });

        ImageButton white = findViewById(R.id.paint_tool_white);
        white.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paintView.setColor(PaintView.ColorState.WHITE);
            }
        });

        ImageButton yellow = findViewById(R.id.paint_tool_yellow);
        yellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paintView.setColor(PaintView.ColorState.YELLOW);
            }
        });

        ImageButton clear = findViewById(R.id.paint_tool_clear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paintView.clear();
            }
        });

        ImageButton send = findViewById(R.id.paint_tool_send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bitmap bmp = paintView.save();

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 80, stream);
                Log.d("BMP SIZE", stream.size() + " bytes");
                byte[] bmpBytes = stream.toByteArray();
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                intent.putExtra("BMP", bmpBytes);
                setResult(1, intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
