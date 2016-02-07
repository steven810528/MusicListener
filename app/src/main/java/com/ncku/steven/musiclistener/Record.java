package com.ncku.steven.musiclistener;

/**
 * Created by steven on 2016/2/7.
 */
public class Record {
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
