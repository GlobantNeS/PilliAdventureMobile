package com.kaineras.pilliadventuremobile.tools;

import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created the first version by kaineras on 10/02/15.
 */
class BitmapLruCache extends LruCache<String, Bitmap> implements ImageLoader.ImageCache {

    private static final int MB= 1024;
    private static final int CACHE= 8;
    private static final String LOG_TAG = BitmapLruCache.class.getSimpleName();


    public BitmapLruCache() {
        this(getDefaultLruCacheSize());
    }

    public BitmapLruCache(int sizeInKiloBytes) {
        super(sizeInKiloBytes);
    }

    public static int getDefaultLruCacheSize() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / MB);
        return maxMemory / CACHE;
    }

    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getRowBytes() * value.getHeight() / MB;
    }

    @Override
    public Bitmap getBitmap(String url) {
        return get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        if(url!=null && bitmap!=null) {
            put(url, bitmap);
        }else{
            if(url == null) {
                Log.e(LOG_TAG, "URL IS NULL");
            }else{
                Log.e(LOG_TAG, "BITMAP IS NULL");
            }
        }

    }
}
