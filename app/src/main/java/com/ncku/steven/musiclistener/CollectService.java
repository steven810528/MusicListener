package com.ncku.steven.musiclistener;

import android.app.Service;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by steven on 2016/1/31.
 */
public class CollectService extends Service{

    static MusicBroadcastReceiver mbr=null;

    static SensorManager sensorManager;

    @Override
    public void onCreate()
    {
        mbr=new MusicBroadcastReceiver();
        try {
            this.mbr.setUser(Build.FINGERPRINT);

        }
        catch (Exception e)
        {
            Log.d("#my_getting_fingerprint", e.toString());
        }
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

        super.onDestroy();
    }

}
