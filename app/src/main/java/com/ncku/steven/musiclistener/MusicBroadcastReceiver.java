package com.ncku.steven.musiclistener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by steven on 2016/2/7.
 */
public class MusicBroadcastReceiver extends BroadcastReceiver {
    static String userIMEI="user_default";
    static String artistName="def";
    static String album="def";
    static String track="def";
    static Record r=new Record();

    static SensorRecorder sr=new SensorRecorder();
    static BroadcastHandler bh=new BroadcastHandler();
    @Override
    public void onReceive(Context context, Intent intent) {
        bh.handle(userIMEI,intent);
    }
    public void setUser(String s)
    {
        this.userIMEI=s;
    }
}
