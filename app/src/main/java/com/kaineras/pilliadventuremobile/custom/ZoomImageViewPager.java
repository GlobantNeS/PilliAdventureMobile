package com.kaineras.pilliadventuremobile.custom;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;


public class ZoomImageViewPager extends ViewPager {

    public ZoomImageViewPager(Context context) {
        super(context);
    }

    public ZoomImageViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        if(v != this && v instanceof ZoomImageView) {
            return ((ZoomImageView)v).canScroll(dx);
        }
        return super.canScroll(v, checkV, dx, x, y);
    }
}
