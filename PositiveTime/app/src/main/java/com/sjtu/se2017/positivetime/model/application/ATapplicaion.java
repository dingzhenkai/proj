package com.sjtu.se2017.positivetime.model.application;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.TextView;

import com.sjtu.se2017.positivetime.model.Statistics.AppInformation;
import com.sjtu.se2017.positivetime.service.util.Utils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/6/29.
 */

public class ATapplicaion extends Application {
    static private String email;
    static private long PTime;
    static private long NTime;
    static private int PTotalWeight;
    static private int NTotalWeight;
    private static ATapplicaion instance;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPTotalWeight() {
        return PTotalWeight;
    }

    public void setPTotalWeight(int PTotalWeight) {
        this.PTotalWeight = PTotalWeight;
    }

    public int getNTotalWeight() {
        return NTotalWeight;
    }

    public void setNTotalWeight(int NTotalWeight) {
        this.NTotalWeight = NTotalWeight;
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
        return (PTime*PTotalWeight - NTime*NTotalWeight)/100;
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
        Utils.init(this);
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        } else {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    static private ArrayList<AppInformation> PreList;
    public void setPreList(ArrayList<AppInformation> PreList) { this.PreList = PreList; }

    public ArrayList<AppInformation> getPreList() { return PreList; }
}