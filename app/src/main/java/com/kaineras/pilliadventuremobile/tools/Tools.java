package com.kaineras.pilliadventuremobile.tools;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.kaineras.pilliadventuremobile.R;
import com.kaineras.pilliadventuremobile.pojo.EnglishImagesProperties;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created the first version by kaineras on 3/02/15.
 */
public class Tools {

    private DatabaseHelper mDBHelper = null;


    public Tools()
    {

    }

    public void loadFragment(FragmentManager fm,Fragment f,int container,String namestack)
    {
        FragmentTransaction fragmentTransaction;
        fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.addToBackStack(namestack);
        fragmentTransaction.replace(container, f);
        fragmentTransaction.commit();
    }

    public static void loadImageFromInternet(Context context,NetworkImageView nivComic, String url) {
        RequestQueue mRequestQueue;
        ImageLoader imageLoader;
        mRequestQueue = Volley.newRequestQueue(context);
        imageLoader = new ImageLoader(mRequestQueue,new BitmapLruCache(BitmapLruCache.getDefaultLruCacheSize()));
        nivComic.setImageUrl(url,imageLoader);
    }



    public URL constructURLIma(String language,String key) throws MalformedURLException {
        final String SCHEME = "http";
        final String BASE_URL = "pilli-adventure.com";
        final String LANGUAGE_PATH = "espa";
        final String IMAGE_ENDPOINT = "comics";

        Uri.Builder builder =new Uri.Builder();
        if(language.equals("espa"))
            builder.scheme(SCHEME).authority(BASE_URL).appendPath(LANGUAGE_PATH).appendPath(IMAGE_ENDPOINT).appendPath(key);
        else
            builder.scheme(SCHEME).authority(BASE_URL).appendPath(IMAGE_ENDPOINT).appendPath(key);
        Uri uri=builder.build();

        return  new URL(uri.toString());
    }

    public HashMap<String,String> getPreferences(Context context) {
        HashMap<String,String> settings=new HashMap<String,String>();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        settings.put("username",prefs.getString("username_preference",context.getString(R.string.default_username)));
        settings.put("language",prefs.getString("language_preference",context.getString(R.string.default_language)));
        if(prefs.getBoolean("save_last_comic",false))
            settings.put("save","1");
        else
            settings.put("save","0");

        return  settings;
        }

    public List<String> getUrlsByMonth(Context context,String year, String month) throws SQLException {
        mDBHelper=getDBHelper(context);
        List<EnglishImagesProperties> urls=mDBHelper.getImageByMonth(year, month);
        List<String> resultUrls=new ArrayList<String>();
        for(EnglishImagesProperties eipTemp:urls)
            resultUrls.add(eipTemp.getName()+".jpg");
        return  resultUrls;

    }

    public String getDBLastComic(Context context) throws SQLException {
        mDBHelper=getDBHelper(context);
        EnglishImagesProperties eipTemp=mDBHelper.getLastImage();
        return  eipTemp.getName();
    }

    private DatabaseHelper getDBHelper(Context context) {
        if (mDBHelper == null) {
            mDBHelper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
        }
        return mDBHelper;
    }

    public boolean existImage(URL urlToCheck)
    {
        boolean result=false;
        URLConnection connection = null;
        InputStreamReader stream = null;
        URL url;
        url = urlToCheck;
        try {
            connection=url.openConnection();
        } catch (IOException e) {
            return result;
        }
        try {
            stream = new InputStreamReader(connection.getInputStream());
            result=true;
        } catch (IOException e) {
            return result;
        }
        return  result;
    }

    public String getYesterday(Calendar calendar) {
        calendar.add(Calendar.DAY_OF_YEAR,-1);
        return calendarToString(calendar);
    }

    public String calendarToString(Calendar c)
    {
        String year = String.valueOf(c.get(Calendar.YEAR));
        String month = String.valueOf(c.get(Calendar.MONTH)+1);
        String day = String.valueOf(c.get(Calendar.DATE));
        if(month.length()==1)
            month="0"+month;
        if(day.length()==1)
            day="0"+day;
        return year+"-"+month+"-"+day;
    }
}