/**
 */
package com.servehttp.janiserver;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.apache.cordova.PluginResult.Status;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;

import com.servehttp.janiserver.SimpleAlarm;
import android.content.Context;
import java.util.Calendar;
import java.util.Date;

public class SimpleAlarmCordovaPlugin extends CordovaPlugin {

  private static final String TAG = "MyCordovaPlugin";

  //alarm plugin app
  SimpleAlarm alarm;

  public void initialize(CordovaInterface cordova, CordovaWebView webView) {
    super.initialize(cordova, webView);

    Context context = this.cordova.getActivity().getApplicationContext();
    this.alarm = new SimpleAlarm(context);
    Log.d(TAG, "*****Initializing MyCordovaPlugin*****");
  }

  public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
    if(action.equals("echo")) {
      String phrase = args.getString(0);
      // Echo back the first argument
      Log.d(TAG, phrase);
    } else if(action.equals("getDate")) {
      // An example of returning data back to the web layer
      final PluginResult result = new PluginResult(PluginResult.Status.OK, (new Date()).toString());
      callbackContext.sendPluginResult(result);

    } else if (action.equals("init")) { // inicializa el objeto de SimpleAlarm
      Log.d(TAG, "onInitPlugin");


    } else if (action.equals("createAlarm")) { //crea una nueva alarma
      Log.d(TAG, "createAlarmPlugin");
      long id = args.getLong(0);
      long date = args.getLong(1);
      String title = args.getString(2);
      String message = args.getString(3);

      // crea el onjeto calendar con base al date
      Calendar c = Calendar.getInstance();
      c.setTimeInMillis(date);

      // this.alarm.createSimpleAlarm(id, c, title, message); //crea la alarma y la habilita

      this.cordova.getThreadPool().execute(alarmCreator(id, c, title, message));

    } else if (action.equals("cancelAlarm")) {  //necesita el id para cancelar la alrma
      Log.d(TAG, "cancelAlarm");

      Long idAlarm = args.getLong(0);
      this.alarm.cancel(idAlarm);
    }

    return true;
  }

  public Runnable alarmCreator(final Long id, final Calendar calendar, final String title, final String message){

    Runnable mRun = new Runnable() {
      @Override
      public void run() {
        alarm.createSimpleAlarm(id, calendar, title, message);
      }
    };

    return mRun;
  }

}
