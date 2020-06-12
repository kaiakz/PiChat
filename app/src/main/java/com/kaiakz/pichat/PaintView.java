package com.kaiakz.pichat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import java.util.HashMap;

public class PaintView extends View {
//    public ViewGroup.LayoutParams params;
//    private Path path;

    private HashMap<ColorState, Path> paths;

    public Paint paint;
    private float preX;
    private float preY;

    private ColorState state;

    public enum ColorState {
        WHITE(Color.WHITE), BLACK(Color.BLACK), RED(Color.RED), BLUE(Color.BLUE), GREEN(Color.GREEN);
        private int color;
        private ColorState(int color) {
            this.color = color;
        }
        public int getColor() {
            return color;
        }
    }

    public PaintView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public PaintView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PaintView(Context context) {
        super(context);
        init(context);
    }

    public void init(Context context) {

//        view_width = context.getResources().getDisplayMetrics().widthPixels;
//        view_height = context.getResources().getDisplayMetrics().heightPixels;

        paint = new Paint();

        state = ColorState.BLACK;
        paths = new HashMap<>();
        paths.put(ColorState.BLACK, new Path());
        paths.put(ColorState.BLUE, new Path());
        paths.put(ColorState.GREEN, new Path());
        paths.put(ColorState.RED, new Path());
        paths.put(ColorState.WHITE, new Path());

        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(8f);


//        params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float pointX = event.getX();
        float pointY = event.getY();
        Path path = paths.get(state);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(pointX, pointY);
                preX = pointX;
                preY = pointY;
                return true;
            case MotionEvent.ACTION_MOVE:
//                path.lineTo(pointX, pointY);
                float dx = Math.abs(pointX - preX);
                float dy = Math.abs(pointY - preY);
                if (dx >= 5 || dy >= 5) {
                    path.quadTo(preX, preY, (pointX + preX) / 2, (pointY + preY) / 2);
                    preX = pointX;
                    preY = pointY;
                }
                break;
//            case MotionEvent.ACTION_UP:
//                cacheCanvas.drawPath(path, paint);
//                path.reset();
            default:
                return false;
        }
//        postInvalidate();
//        return false;
        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
        for (ColorState key : paths.keySet()) {
            paint.setColor(key.getColor());
            canvas.drawPath(paths.get(key), paint);
        }
//        canvas.drawColor(Color.WHITE);
//        canvas.drawPath(path, paint);
//        canvas.drawBitmap(cacheBitmap, 0, 0, paint);
//        canvas.save();
//        canvas.restore();
    }

    public void setColor(ColorState color) {
        state = color;
    }

    public void clear() {
        for (ColorState key : paths.keySet()) {
            paths.get(key).reset();
        }
        invalidate();
    }

    public Bitmap save() {
        Bitmap bitmap = Bitmap.createBitmap(this.getWidth(), this.getHeight(), Bitmap.Config.ARGB_8888);
        this.draw(new Canvas(bitmap));
        return bitmap;
    }

}
