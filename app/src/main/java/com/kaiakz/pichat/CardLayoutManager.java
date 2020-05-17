package com.kaiakz.pichat;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

public class CardLayoutManager extends RecyclerView.LayoutManager {
    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
        removeAllViews();
        detachAndScrapAttachedViews(recycler);
        int itemCount = getItemCount();

        if (itemCount > 3) {
            for(int pos = 3; pos >= 0; pos--) {
                final View view = recycler.getViewForPosition(pos);
                addView(view);
                measureChildWithMargins(view, 0, 0);
                int widthSpace = getWidth() - getDecoratedMeasuredWidth(view);
                int heightSpace = getHeight() - getBottomDecorationHeight(view);

                layoutDecoratedWithMargins(view, widthSpace / 2, heightSpace / 2, widthSpace / 2 + getDecoratedMeasuredWidth(view), heightSpace / 2 + getDecoratedMeasuredHeight(view));

                if (pos == 3) {
                    view.setScaleX(1 - (pos - 1) * 0.1f);
                    view.setScaleY(1 - (pos -1 ) * 0.1f);
                    view.setTranslationY((pos - 1) * view.getMeasuredHeight() / 14);
                }  else if (pos > 0) {
                    view.setScaleX(1 - pos * 0.1f);
                    view.setScaleY(1 - pos * 0.1f);
                    view.setTranslationY(pos * view.getMeasuredHeight() / 14);
                } else {
                    // 设置 mTouchListener 的意义就在于我们想让处于顶层的卡片是可以随意滑动的
                    // 而第二层、第三层等等的卡片是禁止滑动的
//                    view.setOnTouchListener(mOnTouchListener);
                }
            }
        } else {
            for (int pos = itemCount - 1; pos >= 0; pos--) {
                final View view = recycler.getViewForPosition(pos);
                addView(view);
                measureChildWithMargins(view, 0, 0);
                int widthSpace = getWidth() - getDecoratedMeasuredWidth(view);
                int heightSpace = getHeight() - getBottomDecorationHeight(view);

                layoutDecoratedWithMargins(view, widthSpace / 2, heightSpace / 2, widthSpace / 2 + getDecoratedMeasuredWidth(view), heightSpace / 2 + getDecoratedMeasuredHeight(view));

                if (pos > 0) {
                    view.setScaleX(1 - pos * 0.1f);
                    view.setScaleY(1 - pos * 0.1f);
                    view.setTranslationY(pos * view.getMeasuredHeight() / 14);
                } else {
//                    view.setOnTouchListener(mOnTouchListener);
                }
            }
        }
    }

//    private View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
//
//        @Override
//        public boolean onTouch(View v, MotionEvent event) {
//            RecyclerView.ViewHolder childViewHolder =
//
//            return false;
//        }
//    }

}
