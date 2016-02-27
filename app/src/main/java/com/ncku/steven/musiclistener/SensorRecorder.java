package com.ncku.steven.musiclistener;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by steven on 2016/2/7.
 */
public class SensorRecorder implements SensorEventListener {
    static String TAG="#SensorRecorder";
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

    AudioRecord noise_Sensor;
    static  final  int  SAMPLE_RATE_IN_HZ =  8000 ;
    static  final  int  channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_MONO;
    static  final  int  audioEncoding = AudioFormat.ENCODING_PCM_16BIT;

    static  final  int  BUFFER_SIZE = AudioRecord.getMinBufferSize(SAMPLE_RATE_IN_HZ,
            channelConfiguration, audioEncoding);
    Object mLock;

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

        try {
            //noise_Sensor.release();
            noise_Sensor = new AudioRecord(MediaRecorder.AudioSource.MIC,
                    SAMPLE_RATE_IN_HZ, channelConfiguration,
                    audioEncoding, BUFFER_SIZE);
            mLock = new Object();
            //noise_Sensor.startRecording();
        }
        catch (Exception e)
        {
            Log.d(TAG+"noise",e.toString());
            Log.d(TAG+"noise",Boolean.toString(noise_Sensor==null));

        }

        new  Thread( new  Runnable() {
            @Override
            public  void  run() {

                noise_Sensor.startRecording();
                short [] buffer =  new  short [BUFFER_SIZE];
                while  (SensorRecorder.isWorking) {
                    //r是實際讀取的數據長度，一般而言r會小於buffersize
                    int  r = noise_Sensor.read(buffer,  0 , BUFFER_SIZE);
                    long  v =  0 ;
                    // 將 buffer 內容取出，進行平方和運算
                    for  ( int  i =  0 ; i < buffer.length; i++) {
                        v += buffer[i] * buffer[i];
                    }
                    // 平方和除以數據總長度，得到音量大小。
                    double  mean = v / ( double ) r;
                    double  volume =  10  * Math.log10(mean);
                    //Log.d(TAG,  "分貝值:"  + volume);
                    SensorRecorder.updateNoise(volume);
                    Log.d(TAG + "noise", Double.toString(volume));
                    // 大概一秒十次
                    synchronized  (mLock) {
                        try  {
                            mLock.wait( 1000 );
                        }  catch  (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                //noise_Sensor.stop();
                //noise_Sensor.release();
                //noise_Sensor =  null ;
            }
        }).start();
        //this.noise_Sensor=new MediaRecorder();
        //this.noise_Sensor.setAudioSource(MediaRecorder.AudioSource.MIC);

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

        try {
            this.noise_Sensor.stop();
            this.noise_Sensor.release();
        }
        catch (Exception e)
        {
            Log.d(TAG,e.toString());
        }
            this.noise_Sensor=null;
        //this.noise_Sensor.stop();
        //this.noise_Sensor.release();
        //this.noise_Sensor=null;

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
    static void updateNoise(double n)
    {
        numNoise++;
        total_Noise+=(float)n;
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

