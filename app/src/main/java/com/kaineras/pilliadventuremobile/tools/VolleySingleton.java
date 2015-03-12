package com.kaineras.pilliadventuremobile.tools;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.kaineras.pilliadventuremobile.MyApplication;


/**
 * Created the first version by kaineras on 10/03/15.
 */
public class VolleySingleton {
    private static VolleySingleton mInstance = null;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private BitmapLruCache bitmapLruCache;

    private VolleySingleton(){
        mRequestQueue = Volley.newRequestQueue(MyApplication.getContext());
        bitmapLruCache = new BitmapLruCache();
        mImageLoader = new ImageLoader(this.mRequestQueue,this.bitmapLruCache);
    }

    public static VolleySingleton getInstance(){
        if(mInstance == null){
            mInstance = new VolleySingleton();
        }
        return mInstance;
    }

    public BitmapLruCache getBitmapLruCache() {
        return this.bitmapLruCache;
    }

    public ImageLoader getImageLoader(){
        return this.mImageLoader;
    }

}
