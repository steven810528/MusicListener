package com.ncku.steven.musiclistener;

/**
 * Created by steven on 2016/2/7.
 */
public class Record {
    String userIMEI;
    String musicName;
    String album;
    String artist;

    String start;
    String end;
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
        this.accX=sr.total_Acc[0]/(double)sr.numAcc;
        this.accY=sr.total_Acc[1]/(double)sr.numAcc;
        this.accZ=sr.total_Acc[2]/(double)sr.numAcc;

        this.magX=sr.total_Mag[0]/sr.numMag;
        this.magY=sr.total_Mag[1]/sr.numMag;
        this.magZ=sr.total_Mag[2]/sr.numMag;

        this.light=sr.total_Light/sr.numLight;
        this.noise=sr.total_Noise/sr.numNoise;
    }
    void reset()
    {

    }

}
