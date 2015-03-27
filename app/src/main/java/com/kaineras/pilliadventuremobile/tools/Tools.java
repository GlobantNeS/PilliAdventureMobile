package com.kaineras.pilliadventuremobile.tools;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.android.volley.toolbox.ImageLoader;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.kaineras.pilliadventuremobile.R;
import com.kaineras.pilliadventuremobile.custom.CustomImageView;
import com.kaineras.pilliadventuremobile.pojo.ImagesProperties;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created the first version by kaineras on 3/02/15.
 */
public class Tools {


    private DatabaseHelper mDBHelper = null;
    private static final String LOG_TAG = Tools.class.getSimpleName();
    private BitmapLruCache bitmapLruCache;
    private SharedPreferences prefs;
    private static final String SCHEME = "http";
    private static final String BASE_URL = "pilli-adventure.com";
    private static final String LANGUAGE_PATH = "espa";
    private static final String IMAGE_ENDPOINT = "comics";


    public Tools() {
        bitmapLruCache=new BitmapLruCache(BitmapLruCache.getDefaultLruCacheSize());

    }

    public void loadFragment(FragmentManager fm,int container, Fragment f) {
        FragmentTransaction fragmentTransaction;
        fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(container, f);
        fragmentTransaction.commit();
    }

    public void loadImageFromInternet(CustomImageView nivComic, String url) {
        ImageLoader imageLoader;
        imageLoader = VolleySingleton.getInstance().getImageLoader();
        if(!url.isEmpty()) {
            imageLoader.get(url, ImageLoader.getImageListener(nivComic, 0, 0));
        }
    }


    public URL constructURLIma(String language, String key) throws MalformedURLException {
        Uri.Builder builder = new Uri.Builder();
        if ("espa".equals(language)) {
            builder.scheme(SCHEME).authority(BASE_URL).appendPath(LANGUAGE_PATH).appendPath(IMAGE_ENDPOINT).appendPath(key);
        } else {
            builder.scheme(SCHEME).authority(BASE_URL).appendPath(IMAGE_ENDPOINT).appendPath(key);
        }
        Uri uri = builder.build();
        return new URL(uri.toString());
    }

    public Map<String, String> getPreferences(Context context) {
        Map<String, String> settings = new HashMap<>();
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        settings.put("username", prefs.getString("username_preference", context.getString(R.string.default_username)));
        String language = prefs.getString("language_preference", context.getString(R.string.default_language));
        if("Spanish".equals(language) || "Español".equals(language)) {
            settings.put("language", "espa");
        }else{
            settings.put("language", prefs.getString("language_preference", context.getString(R.string.default_language)));
        }

        if (prefs.getBoolean("save_last_comic", false)) {
            settings.put("save", "1");
            settings.put("text_last_comic",prefs.getString("text_last_comic","none"));
        } else {
            settings.put("save", "0");
        }
        if (prefs.getBoolean("notifications", false)) {
            settings.put("notif", "1");
        } else {
            settings.put("notif", "0");
        }
        return settings;
    }

    public void savePreferenceLastPage(String comic)
    {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("text_last_comic",comic);
        editor.commit();
    }

    public boolean existImageInUrl(URL urlToCheck) {
        boolean result = false;
        URLConnection connection;
        URL url;
        url = urlToCheck;
        try {
            connection = url.openConnection();
        } catch (IOException e) {
            Log.d(LOG_TAG, e.toString());
            Logger.getLogger(Tools.class.getName()).log(Level.SEVERE, null, e);
            return result;
        }
        try {
            InputStream inputStream=connection.getInputStream();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            bitmapLruCache = VolleySingleton.getInstance().getBitmapLruCache();
            bitmapLruCache.putBitmap(url.toString(),BitmapFactory.decodeStream(bufferedInputStream));
            result = true;
        } catch (IOException e) {
            Log.d(LOG_TAG, e.toString());
            Logger.getLogger(Tools.class.getName()).log(Level.SEVERE, null, e);
            return result;
        }
        return result;
    }

    public String getYesterday(Calendar calendar) {
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        return calendarToString(calendar);
    }

    public String getTomorrow(Calendar calendar) {
        calendar.add(Calendar.DAY_OF_YEAR, +1);
        return calendarToString(calendar);
    }

    public String calendarToString(Calendar c) {
        String year = String.valueOf(c.get(Calendar.YEAR));
        String month = String.valueOf(c.get(Calendar.MONTH) + 1);
        String day = String.valueOf(c.get(Calendar.DATE));
        if (month.length() == 1) {
            month = "0" + month;
        }
        if (day.length() == 1) {
            day = "0" + day;
        }
        return year + "-" + month + "-" + day;
    }

    public Calendar stringToCalendar(String tmpDate) {
        Calendar c=Calendar.getInstance();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        try {
            c.setTime(simpleDateFormat.parse(tmpDate));
        } catch (ParseException e) {
            Log.d(LOG_TAG, e.toString());
            Logger.getLogger(Tools.class.getName()).log(Level.SEVERE, null, e);
        }
        return c;
    }



    /////////////DATA BASE TOOLS


    private DatabaseHelper getDBHelper(Context context) {
        if (mDBHelper == null) {
            mDBHelper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
        }
        return mDBHelper;
    }

    public void saveImagePropertiesDB(Context context,ImagesProperties imagesProperties)  {
        mDBHelper=getDBHelper(context);
        mDBHelper.saveImageProperties(imagesProperties);
    }

    public void cleanDB(Context context){
        mDBHelper=getDBHelper(context);
        mDBHelper.deleteImagePropertiesAfterToday();
    }

    public int existImageDB(Context context,String dateImage, String language){
        int result = -1;
        mDBHelper=getDBHelper(context);
        ImagesProperties imageProp;
        try {
            imageProp = mDBHelper.getImageByNameAndLanguage(dateImage, language);
        } catch (SQLException e) {
            Log.d(LOG_TAG, e.toString());
            Logger.getLogger(Tools.class.getName()).log(Level.SEVERE, null, e);
            return  result;
        }
        if(imageProp != null){
            result = "0".equals(imageProp.getExist())? 0 : 1;
        }
        return result;
    }
}