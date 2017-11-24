package com.servehttp.janiserver;

import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;


public class AlarmReceiver extends BroadcastReceiver {

    private static final String TAG = "AlarmReceiver";
    Long currentId;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: start alarm ");
        currentId = intent.getLongExtra(SimpleAlarm.ID_NOTIFICATION, 0L);
        String title = intent.getStringExtra("title");
        String message = intent.getStringExtra("message");
        Log.d(TAG, "onReceive: current id = " + currentId);

        //enciende la pantalla
        PowerManager pm = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
        assert pm != null;
        PowerManager.WakeLock wakeLock = pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), TAG);
        wakeLock.acquire(10 * 60 * 1000L /*10 minutes*/);

        KeyguardManager keyguardManager = (KeyguardManager)context.getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock keyguardLock =  keyguardManager.newKeyguardLock(TAG);
        keyguardLock.disableKeyguard();

        // lanza el servicio de ringtone
        Intent serviceIntent = new Intent(context, RingtoneService.class);
        serviceIntent.setAction(RingtoneService.ACTION_START);
        context.startService(serviceIntent);

        createNotification(context, title, message);

    }

    private void createNotification(Context context, String title, String message) {

        @SuppressLint("WrongConstant")
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        Intent dismissIntent = new Intent(context, RingtoneService.class);
        dismissIntent.putExtra(SimpleAlarm.ID_NOTIFICATION, currentId.intValue());
        dismissIntent.setAction(RingtoneService.ACTION_DISMISS);

        PendingIntent pendingIntent = PendingIntent
                .getService(context, currentId.intValue(), dismissIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Action action = new NotificationCompat
                .Action(android.R.drawable.ic_lock_idle_alarm, "DESCARTAR", pendingIntent);
        builder.addAction(action);
        builder.setDeleteIntent(pendingIntent);

        // Intent mainIntent = new Intent(context, SimpleAlarmActivity.class);
        // PendingIntent activityPending = PendingIntent
        //         .getActivity(context, currentId.intValue(), mainIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        // builder.setContentIntent(activityPending);

        NotificationManager nManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = builder.build();

        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        assert nManager != null;
        nManager.notify(currentId.intValue(), notification);

    }

}
