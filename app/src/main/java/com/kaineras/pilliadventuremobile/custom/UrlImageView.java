package com.ebay.app.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;


public class UrlImageView extends ImageView {

    private String mUrl = null;

    public UrlImageView(Context context) {
        super(context);
    }

    public UrlImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UrlImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }
}
