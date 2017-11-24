package com.servehttp.janiserver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ruben
 */

public class SimpleAlarm {


    private static final String TAG = "SimpleAlarm";
    public static final String ID_NOTIFICATION = "id_notification";
    private Context context;
    private AlarmManager alarmManager;
    private Map<Long, PendingIntent> pendingIntentMap;

    public SimpleAlarm(Context context) {
        this.context = context;
        this.pendingIntentMap = new HashMap<Long, PendingIntent>();
        this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    public void createSimpleAlarm(Long idAlarm, Calendar calendar, String title, String message) {
        Log.d(TAG, "createSimpleAlarm");
        Log.d(TAG, "parameters: id = " + idAlarm + ", title = " + title + ", message = " + message);
        Intent intentReceiver = new Intent(context, AlarmReceiver.class);
        intentReceiver.putExtra(SimpleAlarm.ID_NOTIFICATION, idAlarm);
        intentReceiver.putExtra("title", title);
        intentReceiver.putExtra("message", message);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, idAlarm.intValue(), intentReceiver, PendingIntent.FLAG_UPDATE_CURRENT);

        this.pendingIntentMap.put(idAlarm, pendingIntent);
        if (calendar.getTimeInMillis() > new Date().getTime()){
            this.alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }

        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);

        //Log.d(TAG, "createSimpleAlarm: full date " + new Date(calendar.getTimeInMillis()));
        // Toast.makeText(this.context, "Sonará en " + hour + ":" + minute, Toast.LENGTH_SHORT).show();

    }

    public void cancel(Long idAlarm){
        Log.d(TAG, "cancel");
        if (this.alarmManager != null){
            this.alarmManager.cancel(pendingIntentMap.get(idAlarm));

            Intent ri = new Intent(context, RingtoneService.class);
            ri.setAction(RingtoneService.ACTION_DISMISS);
            context.startService(ri);

            this.pendingIntentMap.remove(idAlarm);
        }
    }

}
