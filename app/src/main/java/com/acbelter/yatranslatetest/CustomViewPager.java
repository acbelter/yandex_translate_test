/*
 * Created by acbelter <acbelter@gmail.com>
 */

package com.acbelter.yatranslatetest;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class CustomViewPager extends ViewPager {
    private boolean mSwipePagingEnabled;

    public CustomViewPager(Context context) {
        super(context);
        mSwipePagingEnabled = true;
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        mSwipePagingEnabled = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mSwipePagingEnabled && super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return mSwipePagingEnabled && super.onInterceptTouchEvent(event);
    }

    public void setSwipePagingEnabled(boolean enabled) {
        mSwipePagingEnabled = enabled;
    }

    public boolean isSwipePagingEnabled() {
        return mSwipePagingEnabled;
    }
}