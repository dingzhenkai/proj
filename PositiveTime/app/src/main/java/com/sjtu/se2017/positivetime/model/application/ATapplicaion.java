package com.sjtu.se2017.positivetime.model.application;

import android.app.Application;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/6/29.
 */

public class ATapplicaion extends Application {
    static private long PTime;
    static private long NTime;
    static private int PTotalWeight;
    static private int NTotalWeight;
    private static ATapplicaion instance;

    public static int getPTotalWeight() {
        return PTotalWeight;
    }

    public static void setPTotalWeight(int PTotalWeight) {
        ATapplicaion.PTotalWeight = PTotalWeight;
    }

    public static int getNTotalWeight() {
        return NTotalWeight;
    }

    public static void setNTotalWeight(int NTotalWeight) {
        ATapplicaion.NTotalWeight = NTotalWeight;
    }

    public void setPTime(long PTime){
        this.PTime = PTime;
    }

    public void setNTime(long NTime){
        this.NTime = NTime;
    }

    public long getPTime(){
        return PTime;
    }

    public long getNTime(){
        return NTime;
    }

    public long getAT(){
        return PTime-NTime;
    }

    public float getPWeight(){
        return PTime/(PTime+NTime);
    }

    public float getNWeight(){
        return NTime/(PTime+NTime);
    }

    static public ATapplicaion getInstance() {
        return instance;
    }

    @Override
    public void onTerminate(){
        super.onTerminate();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}