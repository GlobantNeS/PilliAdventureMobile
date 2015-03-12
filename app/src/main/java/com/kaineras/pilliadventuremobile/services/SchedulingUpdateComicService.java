package com.kaineras.pilliadventuremobile.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.kaineras.pilliadventuremobile.PillisActivity;
import com.kaineras.pilliadventuremobile.R;
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
    public static final String TAG = "Scheduling Update New Comic";
    private NotificationManager mNotificationManager;
    private static final String LOG_TAG = SchedulingUpdateComicService.class.getSimpleName();

    public SchedulingUpdateComicService() {
        super("SchedulingUpdateComicService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            if(checkNewComic()){
                sendNotification(getString(R.string.text_body_alert) + dateImage);
            }
        } catch (MalformedURLException e) {
            Log.d(LOG_TAG, e.toString());
            Logger.getLogger(SchedulingUpdateComicService.class.getName()).log(Level.SEVERE, null, e);
        }

        AlarmReceiver.completeWakefulIntent(intent);
    }

    // Post a notification indicating whether a new comic was found.
    private void sendNotification(String msg) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, PillisActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(getString(R.string.text_new_comic_alert))
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(msg);
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    private boolean checkNewComic() throws MalformedURLException {
        Tools tools = new Tools();
        Calendar calendar = Calendar.getInstance();
        dateImage = tools.calendarToString(calendar);
        Map settings = tools.getPreferences(this);
        String language = String.valueOf(settings.get("language"));
        return tools.existImageInUrl(tools.constructURLIma(language, dateImage + ".jpg"));
    }
}