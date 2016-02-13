package com.ncku.steven.musiclistener;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

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
    Sensor sensor;

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");


/*
    static Timer t = new Timer();
    TimerTask task=new TimerTask() {
        @Override
        public void run() {
            if(isWorking) {

                time++;
            }
        }
    };*/

    int numAcc=0;
    double avgAcc[]=new double[3];
    int numMag=0;
    double avgMag[]=new double[3];
    int numLight=0;
    double avgLight=0;
    int numNoise=0;
    double avgNoise=0;

    SensorRecorder()
    {
        this.init();
    }

    void init()
    {
        //take sec as unit
        //this.t.schedule(task,0,1000);
    }
    void setStart()
    {
        this.start= new Date(System.currentTimeMillis()) ;
    }
    void setEnd()
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
        this.isWorking=true;
        this.setStart();
    }

    void stop()
    {
        this.isWorking=false;
        this.setEnd();
        //this.t.cancel();
    }
    void reset()
    {
        this.start=null;
        this.end=null;
        this.time=0;
        this.numAcc=0;
        this.numMag=0;
        for(int i=0;i<3;i++) {
            avgAcc[i] =0;
            avgMag[i] =0;
        }
        this.numLight=0;
        this.avgLight=0;
        this.numNoise=0;
        this.avgNoise=0;
        //this.stop();
    }
    void updateAcc(double val[])
    {
        for(int i=0;i<3;i++) {
            avgAcc[i] = (avgAcc[i] * numAcc + val[i]) / ++numAcc;
            //numAcc++;
        }
    }
    void updateMag(double val[])
    {
        for(int i=0;i<3;i++) {
            avgMag[i] = (avgMag[i] * numMag + val[i]) / ++numMag;
            //numMag++;
        }
    }
    void updateLight(double l)
    {
        avgLight=(avgLight*numLight+l)/++numLight;
    }
    void updateNoise(double n)
    {
        avgNoise=(avgNoise*numNoise+n)/++numNoise;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

    }
}
