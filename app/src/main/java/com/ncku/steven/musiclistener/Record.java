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

    float accX;
    float accY;
    float accZ;

    float magX;
    float magY;
    float magZ;

    float oriA;
    float oriP;
    float oriR;

    float gyrX;
    float gyrY;
    float gyrZ;

    float light;

    float rotX;
    float rotY;
    float rotZ;

    float noise;
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

        this.accX=sr.total_Acc[0]/(float)sr.numAcc;
        this.accY=sr.total_Acc[1]/(float)sr.numAcc;
        this.accZ=sr.total_Acc[2]/(float)sr.numAcc;

        this.magX=sr.total_Mag[0]/(float)sr.numMag;
        this.magY=sr.total_Mag[1]/(float)sr.numMag;
        this.magZ=sr.total_Mag[2]/(float)sr.numMag;

        this.oriA=sr.total_Ori[0]/(float)sr.numOri;
        this.oriP=sr.total_Ori[1]/(float)sr.numOri;
        this.oriR=sr.total_Ori[2]/(float)sr.numOri;

        this.gyrX=sr.total_Ori[0]/(float)sr.numGyr;
        this.gyrY=sr.total_Ori[1]/(float)sr.numGyr;
        this.gyrZ=sr.total_Ori[2]/(float)sr.numGyr;

        this.light=sr.total_Light/sr.numLight;

        this.rotX=sr.total_Rot[0]/(float)sr.numRot;
        this.rotY=sr.total_Rot[1]/(float)sr.numRot;
        this.rotZ=sr.total_Rot[2]/(float)sr.numRot;

        this.noise=sr.total_Noise/sr.numNoise;
    }
    void reset()
    {

    }

}
