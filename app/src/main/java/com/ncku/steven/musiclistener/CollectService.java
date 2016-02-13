package com.ncku.steven.musiclistener;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by steven on 2016/1/31.
 */
public class CollectService extends Service{

    MusicBroadcastReceiver mbr=null;
    //static SensorRecorder sr=new SensorRecorder();

    @Override
    public void onCreate()
    {
        mbr=new MusicBroadcastReceiver();
        //sr.init();

    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        /*
        // 設定每次時間觸發時執行的動作，將這些動作包成物件，放進 TimerTask 型態的參考中。
        TimerTask action = new TimerTask() {
            @Override
            public void run() {
                Log.d("CollectService", "Running");
            }
        };
        // 將定時器物件建立出來。
        Timer timer = new Timer();
        // 利用 schedule() 方法，將執行動作、延遲時間(1秒)、間隔時間(1秒) 輸入方法中。
        // 執行此方法後將會定時執行動作。
        timer.schedule(action, 0, 1000);
        */
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onDestroy() {

        super.onDestroy();
    }

}
