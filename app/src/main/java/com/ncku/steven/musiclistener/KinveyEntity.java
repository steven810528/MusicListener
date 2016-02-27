package com.ncku.steven.musiclistener;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

/**
 * Created by steven on 2016/2/24.
 */
public class KinveyEntity extends GenericJson {
    @Key("User")
    String userIMEI="ddd";
    @Key("Track")
    String track;
    @Key
    String date;
    @Key
    String album;
    @Key
    String artist;
    @Key
    int time;
    @Key
    double accX;

    @Key
    double accY;
    @Key
    double accZ;
    @Key
    double magX;
    @Key
    double magY;
    @Key
    double magZ;
    @Key
    double oriA;
    @Key
    double oriP;
    @Key
    double oriR;
    @Key
    double gyrX;
    @Key
    double gyrY;
    @Key
    double gyrZ;
    @Key
    double light;
    @Key
    double rotX;
    @Key
    double rotY;
    @Key
    double rotZ;
    @Key
    double noise;
/*
    @Key("_kmd")
    private KinveyMetaData meta; // Kinvey metadata, OPTIONAL
    @Key("_acl")*/
    //private KinveyMetaData.AccessControlList acl; //Kinvey access control, OPTIONAL

    KinveyEntity(Record r,String date)
    {

        this.userIMEI=r.userIMEI;
        this.track=r.musicName;
        this.time=r.time;
        this.accX=r.accX;
        this.accY=r.accY;
        this.accZ=r.accZ;
        this.magX=r.magX;
        this.magY=r.magY;
        this.magZ=r.magZ;
        this.oriA=r.oriA;
        this.oriP=r.oriP;
        this.oriR=r.oriR;
        this.gyrX=r.gyrX;
        this.gyrY=r.gyrY;
        this.gyrZ=r.gyrZ;
        this.light=r.light;
        this.rotX=r.rotX;
        this.rotY=r.rotY;
        this.rotZ=r.rotZ;

        this.noise=r.noise;
        this.setDate(date);
    }

    public void setDate(String date)
    {
        this.date=date;
    }

}
