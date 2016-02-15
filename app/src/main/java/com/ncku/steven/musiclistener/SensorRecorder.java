package com.ncku.steven.musiclistener;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by steven on 2016/2/7.
 */
public class SensorRecorder implements SensorEventListener {

    static boolean isWorking=false;
    int time;
    static Date start;
    static Date end;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");

    static Sensor acc_Sensor;
    static Sensor mag_Sensor;
    static Sensor light_Sensor;
    static Sensor noise_Sensor;

    static int numAcc=0;
    static double total_Acc[]=new double[3];

    int numMag=0;
    double total_Mag[]=new double[3];
    int numLight=0;
    double total_Light=0;
    int numNoise=0;
    double total_Noise=0;

    SensorRecorder()
    {
        //this.init();
    }
    void setStartStamp()
    {
        this.start= new Date(System.currentTimeMillis()) ;
    }
    void setEndStamp()
    {
        this.end= new Date(System.currentTimeMillis()) ;
        this.setTime();
    }
    void setTime()
    {
        Long tmp = this.end.getTime()-this.start.getTime();
        this.time=tmp.intValue()/1000;

    }
    void start()
    {

        Log.d("#acc_log","start");

        this.isWorking=true;
        this.setStartStamp();
        this.acc_Sensor=CollectService.sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        CollectService.sensorManager.registerListener(this, CollectService.sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        //Log.d("#sen",Boolean.toString(CollectService.sensorManager.getSensorList(Sensor.TYPE_ALL).isEmpty()));


    }
    void stop()
    {
        Log.d("#acc_log","stop");

        this.isWorking=false;
        this.setEndStamp();
        this.acc_Sensor=null;
        CollectService.sensorManager.unregisterListener(this, CollectService.sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
    }
    void reset()
    {
        this.start=null;
        this.end=null;
        this.time=0;
        this.numAcc=0;
        this.numMag=0;
        for(int i=0;i<3;i++) {
            total_Acc[i] =0;
            total_Mag[i] =0;
        }
        this.numLight=0;
        this.total_Light=0;
        this.numNoise=0;
        this.total_Noise=0;
        //this.stop();
    }
    void updateAcc(SensorEvent val)
    {
        this.numAcc++;
        for(int i=0;i<3;i++) {
            this.total_Acc[i] += (double)val.values[i];
        }
    }
    void updateMag(SensorEvent val)
    {
        this.numMag++;
        for(int i=0;i<3;i++) {
            this.total_Mag[i] += (double)val.values[i];
        }
    }
    void updateLight(double l)
    {
        this.numLight++;
        this.total_Light+=l;
    }
    void updateNoise(double n)
    {
        this.numNoise++;
        this.total_Noise+=n;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.equals(this.acc_Sensor)&&this.isWorking==true)
        {
            Log.d("#acc",Float.toString(event.values[0])+"\t"+Float.toString(event.values[1])+"\t"+Float.toString(event.values[2]));
            this.updateAcc(event);
        }


        if(event.sensor.equals(this.acc_Sensor)&& this.isWorking==false)
        {
            Log.d("#acc_error", "!!");
            Log.d("#acc", Float.toString(event.values[0]) + "\t" + Float.toString(event.values[1]) + "\t" + Float.toString(event.values[2]));
        }
        else{
            //Log.d("#acc_error", "@@");
        }

    }
}
