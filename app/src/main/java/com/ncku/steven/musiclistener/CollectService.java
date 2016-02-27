package com.ncku.steven.musiclistener;

import android.app.Service;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyPingCallback;
import com.kinvey.android.callback.KinveyUserCallback;
import com.kinvey.java.User;
import com.parse.Parse;
import com.parse.ParseObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by steven on 2016/1/31.
 */
public class CollectService extends Service{
    static String TAG="#CollectService";
    static int interval=30;
    //static Handler handler;
    TimerTask task = new TimerTask() {
        Long tmp;
        @Override
        public void run()
        {

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");
            Date curDate = new Date(System.currentTimeMillis()) ; // 獲取當前時間

            if(SensorRecorder.isWorking){
            //String date = formatter.format(curDate);
            //Long tmp;
            try {
                tmp = (curDate.getTime() - MusicBroadcastReceiver.sr.start.getTime()) / 1000;
                if(tmp>interval)
                {
                    //save the record
                    MusicBroadcastReceiver.sr.setEndStamp();
                    MusicBroadcastReceiver.r.addSensorData(MusicBroadcastReceiver.sr);
                    MusicBroadcastReceiver.r.save2Cloud();
                    MusicBroadcastReceiver.sr.reset();
                    MusicBroadcastReceiver.sr.setStartStamp();

                }
            }
            catch (Exception e)
            {
                Log.d(TAG,e.toString());
            }
            }

        }
    };
    //For kin storage
    static Client mClient;

    static boolean isWorking=false;

    static MusicBroadcastReceiver mbr=null;
    static String user="default_user";
    static SensorManager sensorManager;

    static Date startDate=null;
    static Date endDate=null;

    static SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");


    @Override
    public void onCreate()
    {

        isWorking=true;

        //get user ID
        this.user=Build.FINGERPRINT;
        Log.d("#my_getting_fingerprint", this.user);

        try {
            Parse.enableLocalDatastore(this);
            Parse.initialize(this);
/*
        Parse.initialize(new Parse.Configuration.Builder(this)
                        .applicationId("musicListener")
                        .clientKey("rslab")
                        .server("http://140.116.96.70:1337/musicListener")
                        .build()
        );*/
            mClient = new Client.Builder("kid_-14qslXdAl","8ae8636fb9fb4b5c8a6856542b726247"
                    , this.getApplicationContext()).build();
            mClient.ping(new KinveyPingCallback() {
                public void onFailure(Throwable t) {
                    Log.e("#my_client", "Kinvey Ping Failed", t);
                }

                public void onSuccess(Boolean b) {
                    Log.d("#my_client", "Kinvey Ping Success");
                }
            });

            mClient.user().create(this.user, this.user,new KinveyUserCallback(){

                @Override
                public void onFailure(Throwable t) {
                    CharSequence text = "Could not sign up.";
                    Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onSuccess(User u){
                    CharSequence text = u.getUsername() + ", your account has been created.";
                    Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                }
            });


            mClient.user().login(this.user, this.user, new KinveyUserCallback() {
                @Override
                public void onFailure(Throwable t) {
                    CharSequence text = "Wrong username or password.";
                    Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onSuccess(User u) {
                    CharSequence text = "Welcome back," + u.getUsername() + ".";
                    Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                }
            });

        }
        catch (Exception e)
        {
            Log.d("#my_connect",e.toString());
        }

        //save the log
        this.startDate = new Date(System.currentTimeMillis()); // 獲取當前時間
        String date = formatter.format(this.startDate);

        ParseObject logObject = new ParseObject("System_Log");
        logObject.put("Type", "CollectService");
        logObject.put("User", this.user);
        logObject.put("Status", "Start");
        logObject.put("When", date);
        logObject.put("Survival", "NULL");
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
        flags =  START_STICKY;

        //
        Timer t=new Timer();
        t.schedule(task,0,interval*1000/2);



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
        this.mbr=null;
        this.sensorManager=null;
        this.isWorking=false;
        super.onDestroy();
    }

}
