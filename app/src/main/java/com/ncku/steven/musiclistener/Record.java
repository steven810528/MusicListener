package com.ncku.steven.musiclistener;

import android.util.Log;

import com.kinvey.android.AsyncAppData;
import com.kinvey.java.core.KinveyClientCallback;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by steven on 2016/2/7.
 */
public class Record {
    String TAG="record_log";

    //@Key("User")
    String userIMEI;
    //@Key("Track")
    String musicName;
    //String when;
    String album;
    String artist;
    //@Key
    int time;
    //@Key
    String date;
    //@Key
    double accX;
    //@Key
    double accY;
    //@Key
    double accZ;
    //@Key
    double magX;
    //@Key
    double magY;
    //@Key
    double magZ;
    //@Key
    double oriA;
    //@Key
    double oriP;
    //@Key
    double oriR;
    //@Key
    double gyrX;
    //@Key
    double gyrY;
    //@Key
    double gyrZ;
    //@Key
    double light;
    //@Key
    double rotX;
    //@Key
    double rotY;
    //@Key
    double rotZ;
    //@Key
    double noise;
    Record(){

    }
    Record(String userIMEI,
                  String musicName,
                  String album,
                  String artist)
    {
        this.userIMEI=userIMEI;
        this.musicName=musicName;
        this.album=album;
        this.artist=artist;
    }
    void update(String userIMEI,
                String musicName,
                String album,
                String artist)
    {
        this.userIMEI=userIMEI;
        this.musicName=musicName;
        this.album=album;
        this.artist=artist;
    }
    void addSensorData(SensorRecorder sr)
    {
        this.time=sr.time;

        this.accX=sr.total_Acc[0]/(float)sr.numAcc;
        this.accY=sr.total_Acc[1]/(float)sr.numAcc;
        this.accZ=sr.total_Acc[2]/(float)sr.numAcc;

        this.magX=sr.total_Mag[0]/(float)sr.numMag;
        this.magY=sr.total_Mag[1]/(float)sr.numMag;
        this.magZ=sr.total_Mag[2]/(float)sr.numMag;

        this.oriA=sr.total_Ori[0]/(float)sr.numOri;
        this.oriP=sr.total_Ori[1]/(float)sr.numOri;
        this.oriR=sr.total_Ori[2]/(float)sr.numOri;

        this.gyrX=sr.total_Ori[0]/(float)sr.numGyr;
        this.gyrY=sr.total_Ori[1]/(float)sr.numGyr;
        this.gyrZ=sr.total_Ori[2]/(float)sr.numGyr;

        this.light=sr.total_Light/sr.numLight;

        this.rotX=sr.total_Rot[0]/(float)sr.numRot;
        this.rotY=sr.total_Rot[1]/(float)sr.numRot;
        this.rotZ=sr.total_Rot[2]/(float)sr.numRot;

        this.noise=sr.total_Noise/(double)sr.numNoise;
    }
    void save2Cloud()
    {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis()) ; // 獲取當前時間
        String date = formatter.format(curDate);
        this.setDate(date);
        //Parse
        this.save2Par();
        //Kin
        //this.save2Kinvey(date);
        this.save2Kin();

    }
    void save2Par()
    {
        ParseObject testObject = new ParseObject("record");
        testObject.put("User", this.userIMEI);
        testObject.put("Track", this.musicName);
        testObject.put("How_Long", this.time);
        testObject.put("Acc_X", Double.toString(this.accX));
        testObject.put("Acc_Y", Double.toString(this.accY));
        testObject.put("Acc_Z", Double.toString(this.accZ));
        testObject.put("Mag_X", Double.toString(this.magX));
        testObject.put("Mag_Y", Double.toString(this.magY));
        testObject.put("Mag_Z", Double.toString(this.magZ));
        testObject.put("Ori_A", Double.toString(this.oriA));
        testObject.put("Ori_P", Double.toString(this.oriP));
        testObject.put("Ori_R", Double.toString(this.oriR));
        testObject.put("Gyr_X", Double.toString(this.gyrX));
        testObject.put("Gyr_Y", Double.toString(this.gyrY));
        testObject.put("Gyr_Z", Double.toString(this.gyrZ));

        testObject.put("Light", Double.toString(this.light));

        testObject.put("Rot_X", Double.toString(this.rotX));
        testObject.put("Rot_Y", Double.toString(this.rotY));
        testObject.put("Rot_Z", Double.toString(this.rotZ));

        testObject.put("Noise", Double.toString(this.noise));


        testObject.put("When", this.date);
        try{
            //testObject.saveInBackground();
            testObject.save();
            //testObject.s
        }
        catch (ParseException e)
        {
            Log.d("#my_error_save", e.toString());
        }
        Log.d("#my", "done");
    }
    void save2Kin()
    {
        /*
        Log.d(TAG+"kinF",this.musicName);
        AsyncAppData<Record> myevents = CollectService.mClient.appData("eventsCollection",Record.class);
        myevents.save(this, new KinveyClientCallback<Record>() {
            @Override
            public void onFailure(Throwable e) {
                //Log.d("#EN",k.getUser());
                Log.e(TAG, "failed to save event data", e);
            }

            @Override
            public void onSuccess(Record r) {
                Log.d(TAG, "saved data for entity " + r.musicName);
            }
        });
        Log.d(TAG+"kinF",this.musicName);
        */
        AsyncAppData<KinveyEntity> myevents = CollectService.mClient.appData("eventsCollection",KinveyEntity.class);

        KinveyEntity k=new KinveyEntity(this,date);

        Log.d("Tag", k.userIMEI);
        myevents.save(k, new KinveyClientCallback<KinveyEntity>() {
            @Override
            public void onFailure(Throwable e) {
                //Log.d("#EN",k.getUser());
                Log.e(TAG, "failed to save event data", e);
            }

            @Override
            public void onSuccess(KinveyEntity r) {
                Log.d(TAG, "saved data for entity " + r.userIMEI);
            }
        });

    }
    void setDate(String date)
    {
        this.date=date;
    }





}
