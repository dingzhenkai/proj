package com.sjtu.se2017.positivetime.model.application;

import android.app.Application;

/**
 * Created by Administrator on 2017/6/29.
 */

public class ATapplicaion extends Application {
    private long AT;

    public long getAT(){
        return AT;
    }

    public void setAT(long AT){
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