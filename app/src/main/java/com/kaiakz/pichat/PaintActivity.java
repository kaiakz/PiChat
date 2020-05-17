package com.kaiakz.pichat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class PaintActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paint);

        ImageButton red = findViewById(R.id.paint_tool_red);
        final PaintView p = findViewById(R.id.paint_pad);
        red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p.setColor(PaintView.ColorState.RED);
            }
        });

        ImageButton blue = findViewById(R.id.paint_tool_blue);
        blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p.setColor(PaintView.ColorState.BLUE);
            }
        });

        ImageButton green = findViewById(R.id.paint_tool_green);
        green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p.setColor(PaintView.ColorState.GREEN);
            }
        });

        ImageButton black = findViewById(R.id.paint_tool_black);
        black.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p.setColor(PaintView.ColorState.BLACK);
            }
        });

        ImageButton white = findViewById(R.id.paint_tool_white);
        white.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p.setColor(PaintView.ColorState.WHITE);
            }
        });

        ImageButton clear = findViewById(R.id.paint_tool_clear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p.clear();
            }
        });

        ImageButton close = findViewById(R.id.paint_tool_close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
