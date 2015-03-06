package com.kaineras.pilliadventuremobile.custom;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.SlidingPaneLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * Created the first version by kaineras on 12/02/15.
 */
public class PagerEnabledSlidingPaneLayout extends SlidingPaneLayout {
    private float mInitialMotionX;

    private float mEdgeSlop;

    public PagerEnabledSlidingPaneLayout(Context context) {
        this(context, null);
    }

    public PagerEnabledSlidingPaneLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PagerEnabledSlidingPaneLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        ViewConfiguration config = ViewConfiguration.get(context);
        mEdgeSlop = config.getScaledEdgeSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final float x = ev.getX();
        final float y = ev.getY();
        switch (MotionEventCompat.getActionMasked(ev)) {
            case MotionEvent.ACTION_DOWN:
                mInitialMotionX = ev.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                if (mInitialMotionX > mEdgeSlop && !isOpen() && canScroll(this, false,Math.round(x - mInitialMotionX), Math.round(x), Math.round(y))) {
                    return cancelEvent(ev);
                }
                break;
            default:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    private boolean cancelEvent(MotionEvent ev) {
        MotionEvent cancelEvent = getMotionEvent(ev);
        return super.onInterceptTouchEvent(cancelEvent);
    }

    private MotionEvent getMotionEvent(MotionEvent ev) {
        MotionEvent cancelEvent = MotionEvent.obtain(ev);
        cancelEvent.setAction(MotionEvent.ACTION_CANCEL);
        return cancelEvent;
    }
}