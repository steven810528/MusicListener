package com.ncku.steven.musiclistener;

import android.content.Intent;
import android.util.Log;

/**
 * Created by steven on 2016/3/1.
 */
public class BroadcastHandler {
    static String userIMEI="user_default";
    static String artistName="def";
    static String album="def";
    static String track="def";

    static String source="def";

    static int coldTime=0;

    void handle(String user,Intent intent)
    {
        this.userIMEI=user;

        Log.d("#Br", "============================================");
        try {
            Log.d("#Br", "get broadcast");
            Log.d("#Br", "status="+CollectService.isWorking+CollectService.mbr.sr.isWorking);
            Log.d("#Br", intent.getAction().toString());
            Log.d("#Br", intent.getStringExtra("track"));
        }
        catch (Exception e)
        {
            Log.d("#Br", e.toString());
        }
        Log.d("#Br", "============================================");

///////////////////////////////////////////////////
        if(intent.getAction().toString().indexOf("htc")>0)
        {
            Log.d("#Br", "HTC");
            this.setSource("htc");
            this.htcHandler(intent);
        }
        else if(intent.getAction().toString().indexOf("spotify")>0)
        {
            Log.d("#Br", "SPOTIFY");
            this.setSource("spotify");
            this.spotifyHandler(intent);
        }
        else
        {
            Log.d("#Br", "GOOGLE");
            this.setSource("google");
            this.googleHandler(intent);
        }
    }
    ///////////////////////////////////////////
    void htcHandler(Intent intent)
    {
        //save the previous record when the buffer is not empty
        //
        if(CollectService.isWorking==true&&CollectService.mbr.sr.isWorking==true && intent.getAction().toString().indexOf("metachanged")>0)
        {
            this.commandNext(intent);
        }
        //start to play
        else if(CollectService.isWorking==true&&CollectService.mbr.sr.isWorking==false&& intent.getAction().toString().indexOf("playstatechanged")>0)
        {
            this.commandStart(intent);
        }
        //stop to play
        else if(CollectService.isWorking==true&&CollectService.mbr.sr.isWorking==true&& intent.getAction().toString().indexOf("playstatechanged") > 0) {
            this.commandStop();
        }
        else if (CollectService.isWorking == false) {
            Log.d("#my", "else case");
        }
    }
    void spotifyHandler(Intent intent)
    {
        //Log.d("#my_status",Boolean.toString(intent.getBooleanExtra("playing",false)));
        if(CollectService.isWorking==true&&this.track=="def") {
            commandStart(intent);
        }
        else if(CollectService.isWorking==true&&intent.getBooleanExtra("playing", false)==false)
        {
            commandStop();
            this.track="def";
        }
        else if(CollectService.isWorking==true&&intent.getStringExtra("track") !=track)
        {
            commandNext(intent);
        }
        else
        {
            Log.d("#my", "else case");
        }
    }
    void googleHandler(Intent intent)
    {
        if(CollectService.isWorking==true&&CollectService.mbr.sr.isWorking==true && intent.getAction().toString().indexOf("metachanged")>0)
        {
            this.commandNext(intent);
        }
        //start to play
        else if(CollectService.isWorking==true&&CollectService.mbr.sr.isWorking==false&& intent.getAction().toString().indexOf("playstatechanged")>0)
        {
            this.commandStart(intent);
        }
        //stop to play
        else if(CollectService.isWorking==true&&CollectService.mbr.sr.isWorking==true&& intent.getAction().toString().indexOf("playstatechanged") > 0) {
            this.commandStop();
        }
        else if (CollectService.isWorking == false) {
            Log.d("#my", "else case");
        }
    }
    ///////////////////////////////////////////
    void commandStart(Intent intent)
    {
        Log.d("#my", "start to play");

        CollectService.mbr.sr.start();

        this.track = intent.getStringExtra("track");
        this.artistName = intent.getStringExtra("artist");
        this.album = intent.getStringExtra("album");
        CollectService.mbr.r.update(userIMEI, track, album, artistName);

        Log.d("#my", " ");
    }
    void commandNext(Intent intent)
    {
        CollectService.mbr.sr.setEndStamp();
        if(CollectService.mbr.sr.time>coldTime) {
            Log.d("#my", "next song");
            CollectService.mbr.sr.setEndStamp();
            CollectService.mbr.r.addSensorData(CollectService.mbr.sr);
            CollectService.mbr.r.save2Cloud();
            CollectService.mbr.sr.reset();
            CollectService.mbr.sr.setStartStamp();

            this.track = intent.getStringExtra("track");
            this.artistName = intent.getStringExtra("artist");
            this.album = intent.getStringExtra("album");
            CollectService.mbr.r.update(userIMEI, track, album, artistName);

            Log.d("#my", " ");
        }

        this.track = intent.getStringExtra("track");
        this.artistName = intent.getStringExtra("artist");
        this.album = intent.getStringExtra("album");
        CollectService.mbr.r.update(userIMEI, track, album, artistName);

    }
    void commandStop()
    {
        Log.d("#my", "stop to play");

        CollectService.mbr.sr.stop();
        CollectService.mbr.r.addSensorData(CollectService.mbr.sr);
        CollectService.mbr.r.save2Cloud();
        CollectService.mbr.sr.reset();

    }
    void setSource(String source)
    {
        if(this.source!=source)
        {
            Log.d("#my","changed");
            //CollectService.isWorking=false;
            //CollectService.mbr.sr.isWorking=false;
            //CollectService.mbr.sr.unregisterSensor();
            CollectService.mbr.sr.reset();
            CollectService.mbr.sr.isWorking=false;
            this.track="def";

            this.source=source;
        }
    }




}
