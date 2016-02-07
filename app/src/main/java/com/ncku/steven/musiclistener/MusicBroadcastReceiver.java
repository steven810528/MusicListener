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
    String artistName;
    String album;
    String track;
    //boolean isPlaying;
    static Record r=new Record();
    SensorRecorder sr_tmp=new SensorRecorder();

    @Override
    public void onReceive(Context context, Intent intent) {

        track = intent.getStringExtra("track");
        artistName = intent.getStringExtra("artist");
        album = intent.getStringExtra("album");
        //isPlaying = intent.getBooleanExtra("playing", false);

        String userIMEI="user_tmp";

        //save the previous record when the buffer is not empty
        if(CollectService.sr.isWorking)
        {
            Log.d("#my", "============================================");
            Log.d("#my", "!!");
            r.addSensorData(CollectService.sr);
            CollectService.sr.reset();
            this.save2Cloud();
            this.sr_tmp.reset();
            this.sr_tmp.start();

        }
        else
        {
            Log.d("#my", "============================================");
            Log.d("#my", "find broadcast");
            Log.d("#my", "");

        }

        //CollectService.sr.start();
        r.update(userIMEI, track, album, artistName);

    }
    public void save2Cloud()
    {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis()) ; // 獲取當前時間
        String date = formatter.format(curDate);


        Log.d("#my","start save");
        Log.d("#my", this.r.userIMEI);
        Log.d("#my", this.r.musicName);
        Log.d("#my", Integer.toString(this.r.time));
        Log.d("#my", date);


        ParseObject testObject = new ParseObject("record");
        testObject.put("User", r.userIMEI);
        testObject.put("Track", r.musicName);
        testObject.put("How_Long", r.time);
        testObject.put("When", date);
        //testObject.saveInBackground();

        try{
            testObject.save();
        }
        catch (ParseException e)
        {
            Log.d("#my_error", e.toString());
        }
        Log.d("#my", "done");

    }

}
