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

    static Sensor acc_Sensor;   //
    static Sensor mag_Sensor;
    static Sensor ori_Sensor;
    static Sensor gyr_Sensor;
    static Sensor light_Sensor;
    //static Sensor pro_Sensor;
    static Sensor rot_Sensor;

    static Sensor noise_Sensor;

    static int numAcc=0;
    static float total_Acc[]=new float[3];
    static int numMag=0;
    static float total_Mag[]=new float[3];
    static int numOri=0;
    static float total_Ori[]=new float[3];
    static int numGyr=0;
    static float total_Gyr[]=new float[3];
    static int numLight=0;
    static float total_Light=0;
    static int numRot=0;
    static float total_Rot[]=new float[3];

    static int numNoise=0;
    static float total_Noise=0;

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
    void registerSensor()
    {
        this.acc_Sensor=CollectService.sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        CollectService.sensorManager.registerListener(this, CollectService.sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION), SensorManager.SENSOR_DELAY_NORMAL);

        this.mag_Sensor=CollectService.sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        CollectService.sensorManager.registerListener(this, CollectService.sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_NORMAL);

        this.ori_Sensor=CollectService.sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        CollectService.sensorManager.registerListener(this, CollectService.sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_NORMAL);

        this.gyr_Sensor=CollectService.sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        CollectService.sensorManager.registerListener(this, CollectService.sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_NORMAL);

        this.light_Sensor=CollectService.sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        CollectService.sensorManager.registerListener(this, CollectService.sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT), SensorManager.SENSOR_DELAY_NORMAL);

        this.rot_Sensor=CollectService.sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        CollectService.sensorManager.registerListener(this, CollectService.sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR), SensorManager.SENSOR_DELAY_NORMAL);
        /*
        this.noise_Sensor=CollectService.sensorManager.getDefaultSensor(Sensor.);
        CollectService.sensorManager.registerListener(this, CollectService.sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT), SensorManager.SENSOR_DELAY_NORMAL);
*/

    }
    void unregisterSensor()
    {
        /*
        CollectService.sensorManager.unregisterListener(this, this.acc_Sensor);
        CollectService.sensorManager.unregisterListener(this, CollectService.sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
        this.acc_Sensor=null;

        CollectService.sensorManager.unregisterListener(this, this.mag_Sensor);
        CollectService.sensorManager.unregisterListener(this, CollectService.sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD));
        this.mag_Sensor=null;

        CollectService.sensorManager.unregisterListener(this, this.light_Sensor);
        CollectService.sensorManager.unregisterListener(this, CollectService.sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT));
        this.light_Sensor=null;*/
        CollectService.sensorManager.unregisterListener(this);
        this.acc_Sensor=null;   //
        this.mag_Sensor=null;
        this.ori_Sensor=null;
        this.gyr_Sensor=null;
        this.light_Sensor=null;
        this.rot_Sensor=null;

    }
    ///////////////////////////////////////////
    void start()
    {

        Log.d("#acc_log", "start");

        this.isWorking=true;
        this.setStartStamp();
        this.registerSensor();
    }
    void stop()
    {
        Log.d("#acc_log", "stop");

        this.isWorking=false;
        this.setEndStamp();
        this.unregisterSensor();
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
    ///////////////////////////////////////////
    void updateAcc(SensorEvent val)
    {
        this.numAcc++;
        for(int i=0;i<3;i++) {
            this.total_Acc[i] += val.values[i];
        }
    }
    void updateMag(SensorEvent val)
    {
        this.numMag++;
        for(int i=0;i<3;i++) {
            this.total_Mag[i] += val.values[i];
        }
    }
    void updateOri(SensorEvent val)
    {
        this.numOri++;
        for(int i=0;i<3;i++) {
            this.total_Ori[i] += val.values[i];
        }
    }
    void updateGyr(SensorEvent val)
    {
        this.numGyr++;
        for(int i=0;i<3;i++) {
            this.total_Gyr[i] += val.values[i];
        }
    }
    void updateLight(SensorEvent val)
    {
        this.numLight++;
        this.total_Light+=val.values[0];
    }
    void updateRot(SensorEvent val)
    {
        this.numRot++;
        for(int i=0;i<3;i++) {
            this.total_Rot[i] += val.values[i];
        }
    }

    void updateNoise(SensorEvent n)
    {
        this.numNoise++;
        this.total_Noise+=n.values[0];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
    @Override
    public void onSensorChanged(SensorEvent event)
    {
        if(event.sensor.equals(this.acc_Sensor)&&this.isWorking==true)
        {
            Log.d("#acc",Float.toString(event.values[0])+"\t"+Float.toString(event.values[1])+"\t"+Float.toString(event.values[2]));
            this.updateAcc(event);
        }
        else if(event.sensor.equals(this.mag_Sensor)&&this.isWorking==true)
        {
            Log.d("#mag",Float.toString(event.values[0])+"\t"+Float.toString(event.values[1])+"\t"+Float.toString(event.values[2]));
            this.updateMag(event);
        }
        else if(event.sensor.equals(this.ori_Sensor)&&this.isWorking==true)
        {
            Log.d("#ori",Float.toString(event.values[0])+"\t"+Float.toString(event.values[1])+"\t"+Float.toString(event.values[2]));
            this.updateOri(event);
        }
        else if(event.sensor.equals(this.gyr_Sensor)&&this.isWorking==true)
        {
            Log.d("#gyr",Float.toString(event.values[0])+"\t"+Float.toString(event.values[1])+"\t"+Float.toString(event.values[2]));
            this.updateGyr(event);
        }
        else if(event.sensor.equals(this.light_Sensor)&&this.isWorking==true)
        {
            Log.d("#light",Float.toString(event.values[0]));
            this.updateLight(event);
        }
        else if(event.sensor.equals(this.rot_Sensor)&&this.isWorking==true)
        {
            Log.d("#rot",Float.toString(event.values[0])+"\t"+Float.toString(event.values[1])+"\t"+Float.toString(event.values[2]));
            this.updateRot(event);
        }
        else
        {
            Log.d("#my_sensor_error",event.toString());
        }



/*

        if(event.sensor.equals(this.acc_Sensor)&& this.isWorking==false)
        {
            Log.d("#acc_error", "!!");
            Log.d("#acc", Float.toString(event.values[0]) + "\t" + Float.toString(event.values[1]) + "\t" + Float.toString(event.values[2]));
        }
        else{
            //Log.d("#acc_error", "@@");
        }
*/
    }
}
