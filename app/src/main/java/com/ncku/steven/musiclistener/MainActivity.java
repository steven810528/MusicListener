package com.ncku.steven.musiclistener;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    //MusicBroadcastReceiver mbr=null;

    //static SensorRecorder sr=new SensorRecorder();
    //static ContextRecord cr=new ContextRecord();

    //String Song;
    Button start_B;
    Button stop_B;
    Intent intent;

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

        //Parse.enableLocalDatastore(this);
        //Parse.initialize(this);
        start_B = (Button)findViewById(R.id.start_button);
        stop_B  = (Button)findViewById(R.id.stop_button);
        start_B.setOnClickListener(myListener);
        stop_B.setOnClickListener(myListener);

        //
        //啟動服務
        intent = new Intent(MainActivity.this, CollectService.class);
        //intent.setAction("steven.push");
        this.startService(intent);

        //Intent intent_maintain = new Intent(MainActivity.this, MaintainService.class);
        //startService(intent_maintain);

        //this.onPause();
        //this.onStop();
        //this.onDestroy();
    }

    @Override
    protected void onResume()
    {
        //mbr = new  MusicBroadcastReceiver();
        //IntentFilter intentFilter = new  IntentFilter();
        /*
        intentFilter.addAction("com.android.music.metachanged" );
        intentFilter.addAction("com.android.music.queuechanged");
        intentFilter.addAction("com.android.music.playbackcomplete");
        intentFilter.addAction("com.android.music.playstatechanged");

        */
        //intentFilter.addAction("com.htc.music.metachanged");
        //intentFilter.addAction("com.htc.music.queuechanged");
        //intentFilter.addAction("com.htc.music.playbackcomplete");
        //intentFilter.addAction("com.htc.music.playstatechanged");
        /*
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
        */
        //registerReceiver(mbr, intentFilter);
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

    private Button.OnClickListener myListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v){
            if(v.getId()==R.id.start_button)
            {
                //Intent intent = new Intent(MainActivity.this, CollectService.class);
                //intent.setAction("steven.push");
                intent = new Intent(MainActivity.this, CollectService.class);
                startService(intent);
                Toast.makeText(MainActivity.this, "已開始運行", Toast.LENGTH_SHORT).show();
            }
            else if(v.getId()==R.id.stop_button)
            {
                //Intent intent = new Intent(MainActivity.this, CollectService.class);
                //intent.setAction("steven.push");
                intent = new Intent(MainActivity.this, CollectService.class);
                stopService(intent);
                Toast.makeText(MainActivity.this, "已停止運行", Toast.LENGTH_SHORT).show();
            }
        }
    };

}


