package com.qymage.sys.common.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * @author crazyZhangxl on 2018/11/5.
 * Describe:
 */
public class ObservableAlphaScrollView extends NestedScrollView {
    private OnAlphaScrollChangeListener mOnAlphaScrollChangeListener;

    public ObservableAlphaScrollView(@NonNull Context context) {
        super(context);
    }

    public ObservableAlphaScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ObservableAlphaScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnAlphaScrollChangeListener(OnAlphaScrollChangeListener onAlphaScrollChangeListener) {
        this.mOnAlphaScrollChangeListener = onAlphaScrollChangeListener;
    }

    @Override
    protected void onScrollChanged(int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        super.onScrollChanged(scrollX, scrollY, oldScrollX, oldScrollY);
        if (mOnAlphaScrollChangeListener != null) {
            mOnAlphaScrollChangeListener.onVerticalScrollChanged(scrollX, scrollY, oldScrollX, oldScrollY);
        }
    }


    public interface OnAlphaScrollChangeListener {
        void onVerticalScrollChanged(int scrollX, int scrollY, int oldScrollX, int oldScrollY);
    }
}
