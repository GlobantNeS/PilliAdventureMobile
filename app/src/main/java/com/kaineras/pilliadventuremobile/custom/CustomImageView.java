package com.kaineras.pilliadventuremobile.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created the first version by kaineras on 3/03/15.
 */
public class CustomImageView extends ImageView {
    private int mDrawableWidth;
    private int mDrawableHeight;
    private int widthSize;
    private int heightSize;
    private boolean mAdjustViewBoundsL;
    private int mMaxWidthL = Integer.MAX_VALUE;
    private int mMaxHeightL = Integer.MAX_VALUE;
    private int pleft;
    private int pright;
    private int ptop;
    private int pbottom;
    private int w;
    private int h;
    private View relatedView;
    private float desiredAspect;
    private boolean resizeWidth;
    private boolean resizeHeight;
    private static final double ASPECT = 0.0000001;

    public CustomImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        // hack for acces some private field of parent :-(
        Field f;
        try {
            f = android.widget.ImageView.class.getDeclaredField("mAdjustViewBounds");
            f.setAccessible(true);
            setAdjustViewBounds((Boolean) f.get(this));

            f = android.widget.ImageView.class.getDeclaredField("mMaxWidth");
            f.setAccessible(true);
            setMaxWidth((Integer) f.get(this));

            f = android.widget.ImageView.class.getDeclaredField("mMaxHeight");
            f.setAccessible(true);
            setMaxHeight((Integer) f.get(this));
        } catch (Exception e) {
            Log.e("FAIL TO ACCESS PARENT", e.toString());
            Logger.getLogger(CustomImageView.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomImageView(Context context) {
        super(context);
    }

    public void setAdjustViewBounds(boolean adjustViewBounds) {
        super.setAdjustViewBounds(adjustViewBounds);
        mAdjustViewBoundsL = adjustViewBounds;
    }

    public void setMaxWidth(int maxWidth) {
        super.setMaxWidth(maxWidth);
        mMaxWidthL = maxWidth;
    }

    public void setMaxHeight(int maxHeight) {
        super.setMaxHeight(maxHeight);
        mMaxHeightL = maxHeight;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getDrawable() == null) {
            setMeasuredDimension(0, 0);
            return;
        }
        initializeVariables();
        adjustViewBounds(widthMeasureSpec, heightMeasureSpec);
        loadPaddings();
        if (resizeWidth || resizeHeight) {
            resizing(widthMeasureSpec, heightMeasureSpec);

        } else {
            notResizing(widthMeasureSpec, heightMeasureSpec);
        }

         setMeasuredDimension(widthSize, heightSize);

        if (relatedView != null) {
            relatedView.getLayoutParams().width = widthSize;
            relatedView.getLayoutParams().height = heightSize;
        }

    }

    private void resizing(int widthMeasureSpec, int heightMeasureSpec) {
        // Get the max possible width given our constraints
        widthSize = resolveAdjustedSize(w + pleft + pright,
                mMaxWidthL, widthMeasureSpec);

        // Get the max possible height given our constraints
        heightSize = resolveAdjustedSize(h + ptop + pbottom,
                mMaxHeightL, heightMeasureSpec);

        if (desiredAspect != 0.0f) {
            // See what our actual aspect ratio is
            float actualAspect = (float) (widthSize - pleft - pright) /
                    (heightSize - ptop - pbottom);

            if (Math.abs(actualAspect - desiredAspect) > ASPECT) {
                resizeWidth(desiredAspect, resizeWidth);
                resizeHeight(desiredAspect, resizeHeight);


            }
        }
    }

    private void adjustViewBounds(int widthMeasureSpec, int heightMeasureSpec) {
        if (mDrawableWidth > 0) {
            w = mDrawableWidth;
            h = mDrawableHeight;
            if (w <= 0) {
                w = 1;
            }
            if (h <= 0) {
                h = 1;
            }

            // We are supposed to adjust view bounds to match the aspect
            // ratio of our drawable. See if that is possible.
            if (mAdjustViewBoundsL) {

                int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
                int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);

                resizeWidth = widthSpecMode != MeasureSpec.EXACTLY;
                resizeHeight = heightSpecMode != MeasureSpec.EXACTLY;

                desiredAspect = (float) w / (float) h;
            }
        }
    }

    private void initializeVariables() {
        mDrawableWidth = getDrawable().getIntrinsicWidth();
        mDrawableHeight = getDrawable().getIntrinsicHeight();
        w = 0;
        h = 0;

        // Desired aspect ratio of the view's contents (not including padding)
        desiredAspect = 0.0f;
        // We are allowed to change the view's width
        resizeWidth = false;
        // We are allowed to change the view's height
        resizeHeight = true;
    }

    private void notResizing(int widthMeasureSpec, int heightMeasureSpec) {
        w += pleft + pright;
        h += ptop + pbottom;

        w = Math.max(w, getSuggestedMinimumWidth());
        h = Math.max(h, getSuggestedMinimumHeight());

        widthSize = resolveSize(w, widthMeasureSpec);
        heightSize = resolveSize(h, heightMeasureSpec);
    }

    private void resizeHeight(float desiredAspect, boolean resizeHeight) {
        // Try adjusting height to be proportional to width
        if (resizeHeight) {
            int newHeight = (int) ((widthSize - pleft - pright) / desiredAspect) + ptop + pbottom;
            if (newHeight > 0) {
                heightSize = Math.min(newHeight, mMaxHeightL);
                widthSize = (int) (desiredAspect * (heightSize - ptop - pbottom)) + pleft + pright;
            }
        }
    }

    private void resizeWidth(float desiredAspect, boolean resizeWidth) {
        // Try adjusting width to be proportional to height
        if (resizeWidth) {
            int newWidth = (int) (desiredAspect * (heightSize - ptop - pbottom)) + pleft + pright;
            if (newWidth > 0) {
                widthSize = Math.min(Math.min(newWidth, mMaxWidthL), widthSize);
                heightSize = (int) ((widthSize - pleft - pright) / desiredAspect) + ptop + pbottom;
            }
        }
    }

    private void loadPaddings() {
        pleft = getPaddingLeft();
        pright = getPaddingRight();
        ptop = getPaddingTop();
        pbottom = getPaddingBottom();
    }

    private int resolveAdjustedSize(int desiredSize, int maxSize, int measureSpec) {
        int result = desiredSize;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                result = Math.min(desiredSize, maxSize);
                break;
            case MeasureSpec.AT_MOST:
                result = Math.min(Math.min(desiredSize, specSize), maxSize);
                break;
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
            default:
                break;
        }
        return result;
    }
}