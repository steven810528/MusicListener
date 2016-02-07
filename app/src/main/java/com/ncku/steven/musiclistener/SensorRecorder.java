package com.ncku.steven.musiclistener;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by steven on 2016/2/7.
 */
public class SensorRecorder {

    boolean isWorking=false;

    int time;

    static Timer t = new Timer();
    TimerTask task=new TimerTask() {
        @Override
        public void run() {
            if(isWorking)
            time++;
        }
    };

    int numAcc=0;
    double avgAcc[]=new double[3];
    int numMag=0;
    double avgMag[]=new double[3];
    int numLight=0;
    double avgLight=0;
    int numNoise=0;
    double avgNoise=0;



    void init()
    {
        //take sec as unit
        this.t.schedule(task,0,1000);
    }
    void start()
    {
        isWorking=true;
    }

    void stop()
    {
        isWorking=false;
    }
    void reset()
    {
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
}
