package com.kaiakz.pichat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

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

        ImageButton clear = findViewById(R.id.paint_tool_clear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paintView.clear();
            }
        });

        ImageButton close = findViewById(R.id.paint_tool_close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageButton send = findViewById(R.id.paint_tool_send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bitmap bmp = paintView.save();
                intent.putExtra("BMP", bmp);
                setResult(1, intent);
                finish();
            }
        });
    }

}
