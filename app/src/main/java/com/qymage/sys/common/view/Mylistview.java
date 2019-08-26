package com.qymage.sys.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

public class Mylistview extends ListView{

    public Mylistview(Context context, AttributeSet attrs){
         super(context, attrs);
    }

    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
         int mExpandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
         super.onMeasure(widthMeasureSpec, mExpandSpec);
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            return true; //
        }
        return super.dispatchTouchEvent(ev);

    }

}