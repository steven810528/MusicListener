package com.ncku.steven.musiclistener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.Parse;
import com.parse.ParseObject;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {

    MusicBroadcastReceiver mbr=null;

    static SensorRecorder sr=new SensorRecorder();
    //static ContextRecord cr=new ContextRecord();

    //String Song;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */
        Parse.enableLocalDatastore(this);
        Parse.initialize(this);
        sr.init();


        //
        //啟動服務
        Intent intent = new Intent(MainActivity.this, CollectService.class);
        startService(intent);



    }

    @Override
    protected void onResume() {
        mbr = new  MusicBroadcastReceiver();
        IntentFilter intentFilter = new  IntentFilter();
        intentFilter.addAction("com.android.music.metachanged" );
        intentFilter.addAction("com.android.music.queuechanged");
        intentFilter.addAction("com.android.music.playbackcomplete");
        intentFilter.addAction("com.android.music.playstatechanged");
        intentFilter.addAction("com.android.music.metachanged");
        intentFilter.addAction("com.android.music.playstatechanged");
        intentFilter.addAction("com.android.music.playbackcomplete");
        intentFilter.addAction("com.android.music.queuechanged");
        intentFilter.addAction("com.htc.music.metachanged");
        intentFilter.addAction("fm.last.android.metachanged");
        intentFilter.addAction("com.sec.android.app.music.metachanged");
        intentFilter.addAction("com.nullsoft.winamp.metachanged");
        intentFilter.addAction("com.amazon.mp3.metachanged");
        intentFilter.addAction("com.miui.player.metachanged");
        intentFilter.addAction("com.real.IMP.metachanged");
        intentFilter.addAction("com.sonyericsson.music.metachanged");
        intentFilter.addAction("com.rdio.android.metachanged");
        intentFilter.addAction("com.samsung.sec.android.MusicPlayer.metachanged");
        intentFilter.addAction("com.andrew.apollo.metachanged");
        intentFilter.addAction("com.kugou.android.music.metachanged");
        intentFilter.addAction("com.ting.mp3.playinfo_changed");

        registerReceiver(mbr, intentFilter);
        super.onResume();
    }
    @Override
    protected void onPause() {
        /*
        if (mbr != null ){
            unregisterReceiver(mbr);
        }*/
        super .onPause();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}

class MusicBroadcastReceiver extends BroadcastReceiver {
    String artistName;
    String album;
    String track;
    //boolean isPlaying;
    Record r=new Record();
    SensorRecorder sr_tmp=new SensorRecorder();

    @Override
    public void onReceive(Context context, Intent intent) {

        track = intent.getStringExtra("track");
        artistName = intent.getStringExtra("artist");
        album = intent.getStringExtra("album");
        //isPlaying = intent.getBooleanExtra("playing", false);

        String userIMEI="user_tmp";

        Log.d("MBR_onR", track);

        Log.d("BOOL", Boolean.toString(MainActivity.sr.isWorking));
        //save the previous record when the buffer is not empty
        if(CollectService.sr.isWorking)
        {
            Log.d("isworking", "!!");
            //r.addSensorData(MainActivity.sr);
            r.addSensorData(CollectService.sr);


            //
            //MainActivity.sr.reset();
            //
            CollectService.sr.reset();
            //

            save2Cloud(r);

            this.sr_tmp.reset();
            this.sr_tmp.start();

        }

        //
        //MainActivity.sr.isWorking = true;
        //
        CollectService.sr.start();
        //

        r.update(userIMEI, track, album, artistName);
        //this.sr_tmp.start();

    }
    public void save2Cloud(Record r)
    {
        //Log.d("@my","start save");
        //Log.d("@my",r.userIMEI);
        //Log.d("@my",r.musicName);
        //Log.d("@my",Integer.toString(r.time));

        ParseObject testObject = new ParseObject("record");
        testObject.put("User", r.userIMEI);
        testObject.put("Music", r.musicName);
        testObject.put("Time", r.time);
        testObject.saveInBackground();
        //Log.d("@my", "done");

    }

}

class Record {
    String userIMEI;
    String musicName;
    String album;
    String artist;

    int time;

    double accX;
    double accY;
    double accZ;

    double magX;
    double magY;
    double magZ;

    double light;
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
        this.accX=sr.avgAcc[0];
        this.accY=sr.avgAcc[1];
        this.accZ=sr.avgAcc[2];
        this.magX=sr.avgMag[0];
        this.magY=sr.avgMag[1];
        this.magZ=sr.avgMag[2];
        this.light=sr.avgLight;
        this.noise=sr.avgNoise;
    }
    void reset()
    {

    }

}
class SensorRecorder {

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


