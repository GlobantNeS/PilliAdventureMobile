package com.kaineras.pilliadventuremobile.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created the first version by kaineras on 12/03/15.
 */
public class BootReceiver  extends BroadcastReceiver {
        AlarmReceiver alarm = new AlarmReceiver();
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
                alarm.setAlarm(context);
            }
        }
    }
