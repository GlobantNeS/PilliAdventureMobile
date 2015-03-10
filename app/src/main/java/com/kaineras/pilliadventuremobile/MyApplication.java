package com.kaineras.pilliadventuremobile;

import android.app.Application;
import android.content.Context;

/**
 * Created the first version by kaineras on 10/03/15.
 */
public class MyApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }
    public static Context getContext() {
        return mContext;
    }
}