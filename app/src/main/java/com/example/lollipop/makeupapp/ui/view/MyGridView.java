package com.example.lollipop.makeupapp.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

/**
 * Created by Lollipop on 2017/8/29.
 */

public class MyGridView extends GridView {
    private OnTouchInvalidPositionListener mTouchInvalidPositionListener;

    public MyGridView(Context context) {
        super(context);
    }

    public MyGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyGridView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    /**
     * 空白处点击事件
     */
    public interface OnTouchInvalidPositionListener {
        boolean onTouchInvalidPosition(int motionEvent);
    }

    public void setOnTouchInvalidPositionListener(OnTouchInvalidPositionListener listener) {
        mTouchInvalidPositionListener = listener;
    }

    /**
     * 复写onTouchEvent，判断点击位置是不是非GirdView的item（空白部分）
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mTouchInvalidPositionListener == null){
            return super.onTouchEvent(ev);
        }
        if (!isEnabled()){
            return isClickable() || isLongClickable();
        }
        final int motionPosition = pointToPosition((int)ev.getX(), (int)ev.getY());
        if (ev.getActionMasked() == MotionEvent.ACTION_UP) {
            if (motionPosition == INVALID_POSITION) {
                super.onTouchEvent(ev);
                return mTouchInvalidPositionListener.onTouchInvalidPosition(ev.getActionMasked());
            }
        }
        return super.onTouchEvent(ev);
    }
}
