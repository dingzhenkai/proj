package com.sjtu.se2017.positivetime.model;

import android.app.Application;

/**
 * Created by Administrator on 2017/6/29.
 */

public class ATapplicaion extends Application {
    private int AT;

    public int getAT(){
        return AT;
    }

    public void setAT(int AT){
        this.AT = AT;
    }

    @Override
    public void onCreate(){
        super.onCreate();
    }

    @Override
    public void onTerminate(){
        super.onTerminate();
    }
}