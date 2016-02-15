package com.ncku.steven.musiclistener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by steven on 2016/2/7.
 */
public class MusicBroadcastReceiver extends BroadcastReceiver {
    static String userIMEI="user_deafult";
    static String artistName;
    static String album;
    static String track;

    static Record r=new Record();

    SensorRecorder sr=new SensorRecorder();


    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("#my", "============================================");


        //save the previous record when the buffer is not empty
        //
        if(this.sr.isWorking && intent.getAction().toString().indexOf("metachanged")>0)
        {
            Log.d("#my", "next song");
            this.sr.stop();

            r.addSensorData(this.sr);
            this.save2Cloud();

            this.sr.reset();
            this.sr.start();

            track = intent.getStringExtra("track");
            artistName = intent.getStringExtra("artist");
            album = intent.getStringExtra("album");
            this.r.update(userIMEI,track,album,artistName);

            Log.d("#my", " ");
        }
        //start to play
        else if(this.sr.isWorking ==false && intent.getAction().toString().indexOf("playstatechanged")>0)
        {
            Log.d("#my", "start to play");

            this.sr.start();

            track = intent.getStringExtra("track");
            artistName = intent.getStringExtra("artist");
            album = intent.getStringExtra("album");
            this.r.update(userIMEI,track,album,artistName);

            Log.d("#my", " ");
        }
        //stop to play
        else if(this.sr.isWorking ==true && intent.getAction().toString().indexOf("playstatechanged")>0) {

            Log.d("#my", "stop to play");

            this.sr.stop();

            r.addSensorData(this.sr);
            this.save2Cloud();

            this.sr.reset();

        }
        else
        {
            Log.d("#my", "else case");

        }

    }
    public void save2Cloud()
    {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis()) ; // 獲取當前時間
        String date = formatter.format(curDate);

        Log.d("#my","start save");
        Log.d("#my", this.r.userIMEI);
        Log.d("#my", this.r.musicName);
        Log.d("#my", Long.toString(this.r.time));
        //Log.d("#my", this.r.start);
        //Log.d("#my", this.r.end);
        Log.d("#my", date);

        ParseObject testObject = new ParseObject("record");
        testObject.put("User", r.userIMEI);
        testObject.put("Track", r.musicName);
        testObject.put("How_Long", r.time);
        testObject.put("Acc_X", Double.toString(r.accX));
        testObject.put("Acc_Y", Double.toString(r.accY));
        testObject.put("Acc_Z", Double.toString(r.accZ));
        testObject.put("When", date);

        //

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
    public void setUser(String s)
    {
        this.userIMEI=s;
    }

}
