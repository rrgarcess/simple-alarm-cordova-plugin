package com.servehttp.janiserver;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by ruben
 */

public class RingtoneService extends Service {

    private static final String TAG = RingtoneService.class.getSimpleName();
    private static final String URI_BASE = RingtoneService.class.getName() + ".";
    public static final String ACTION_DISMISS = URI_BASE + "ACTION_DISMISS";
    public static final String ACTION_START = URI_BASE + "ACTION_START";

    private Ringtone ringtone;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent == null){
            Log.d(TAG, "the intent is null");
            return START_REDELIVER_INTENT;
        }

        String action = intent.getAction();
        Log.d(TAG, "onStartCommand: action " + intent.getAction());

        if (ACTION_START.equals(action)){
            startRingtone();
        } else if (ACTION_DISMISS.equals(action)){
            //aqui detiene la alarma
            dismissRingtone(intent);
        }

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        if (ringtone != null){
            ringtone.stop();
        }
    }

    public void startRingtone(){
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (uri == null){
            uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }

        ringtone = RingtoneManager.getRingtone(this, uri);
        ringtone.play();
    }

    public void dismissRingtone(Intent intent){
        Log.d(TAG, "dismissRingtone");
        if (ringtone != null){
            ringtone.stop();

            NotificationManager managerNotification = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            assert managerNotification != null;
            managerNotification.cancel(intent.getIntExtra(SimpleAlarm.ID_NOTIFICATION, 0));

        }
        stopService(intent);
    }
}
