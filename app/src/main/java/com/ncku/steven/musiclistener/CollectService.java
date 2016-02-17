package com.ncku.steven.musiclistener;

import android.app.Service;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.parse.ParseObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by steven on 2016/1/31.
 */
public class CollectService extends Service{

    static MusicBroadcastReceiver mbr=null;
    static String user="default_user";
    static SensorManager sensorManager;

    static Date startDate=null;
    static Date endDate=null;

    static SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");


    @Override
    public void onCreate()
    {
        //get user ID
        this.user=Build.FINGERPRINT;
        Log.d("#my_getting_fingerprint", this.user);
        //save the log
        this.startDate = new Date(System.currentTimeMillis()); // 獲取當前時間
        String date = formatter.format(this.startDate);

        ParseObject logObject = new ParseObject("System_Log");
        logObject.put("Type", "CollectService");
        logObject.put("User", this.user);
        logObject.put("Status", "Start");
        logObject.put("When", date);
        logObject.put("Survival","NULL");
        logObject.saveInBackground();
        //initial receiver
        this.mbr=new MusicBroadcastReceiver();
        this.mbr.setUser(this.user);

        sensorManager = (SensorManager) getApplicationContext()
                .getSystemService(SENSOR_SERVICE);
        //mbr.sr.acc_Sensor=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //mbr.sr.mag_Sensor=sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED);
        //mbr.sr.light_Sensor=sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        //mbr.sr.noise_Sensor=sensorManager.getDefaultSensor(Sensor.TYPE_M);

    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onDestroy() {

        this.endDate = new Date(System.currentTimeMillis()) ; // 獲取當前時間
        String date = formatter.format(this.endDate);
        Long tmp=this.endDate.getTime()-this.startDate.getTime();
        int surTime=tmp.intValue()/1000;
        ParseObject logObject = new ParseObject("System_Log");
        logObject.put("Type", "CollectService");
        logObject.put("User", this.user);
        logObject.put("Status", "Stop");
        logObject.put("When", date);
        logObject.put("Survival",Integer.toString(surTime));
        logObject.saveInBackground();

        super.onDestroy();
    }

}
