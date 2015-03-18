package com.kaineras.pilliadventuremobile.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.kaineras.pilliadventuremobile.PillisActivity;
import com.kaineras.pilliadventuremobile.R;
import com.kaineras.pilliadventuremobile.pojo.ImagesProperties;
import com.kaineras.pilliadventuremobile.tools.Tools;

import java.net.MalformedURLException;
import java.util.Calendar;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created the first version by kaineras on 12/03/15.
 */
public class SchedulingUpdateComicService extends IntentService {

    private String dateImage;
    public static final int NOTIFICATION_ID = 1;

    private static final String LOG_TAG = SchedulingUpdateComicService.class.getSimpleName();

    public SchedulingUpdateComicService() {
        super("SchedulingUpdateComicService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            if(checkNewComic()){
                sendNotification(getString(R.string.text_body_alert) +"\n"+  dateImage);
            }
        } catch (MalformedURLException e) {
            Log.d(LOG_TAG, e.toString());
            Logger.getLogger(SchedulingUpdateComicService.class.getName()).log(Level.SEVERE, null, e);
        }

        AlarmReceiver.completeWakefulIntent(intent);
    }

    // Post a notification indicating whether a new comic was found.
    private void sendNotification(String msg) {
        NotificationManager mNotificationManager;
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent=new Intent(this, PillisActivity.class);
        intent.putExtra("NOTIFICATION",true);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setLargeIcon(BitmapFactory.decodeResource(null,R.mipmap.ic_launcher))
                        .setSmallIcon(R.drawable.ic_stat_ic_notification)
                        .setContentTitle(getString(R.string.text_new_comic_alert))
                        .setAutoCancel(true)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(msg);
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    private boolean checkNewComic() throws MalformedURLException {
        Tools tools = new Tools();
        Calendar calendar = Calendar.getInstance();
        boolean result=false;
        if(stateOfConnectivity()){
            dateImage = tools.calendarToString(calendar);
            Map settings = tools.getPreferences(this);
            String language = String.valueOf(settings.get("language"));

            int statusImage = tools.existImageDB(this, dateImage, language);
            if(statusImage == -1){
                result=tools.existImageInUrl(tools.constructURLIma(language, dateImage + ".jpg"));
                ImagesProperties imagesProperties = new ImagesProperties();
                imagesProperties.setName(dateImage);
                imagesProperties.setLang(language);
                imagesProperties.setExist(result ? "1" : "0");
                tools.saveImagePropertiesDB(this, imagesProperties);
            }
        }
        return result;
    }

    private boolean stateOfConnectivity() {
        ConnectivityManager connMgr;
        NetworkInfo networkInfo;
        connMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}