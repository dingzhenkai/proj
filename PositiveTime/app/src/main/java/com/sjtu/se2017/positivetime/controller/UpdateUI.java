package com.sjtu.se2017.positivetime.controller;

import android.app.Activity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sjtu.se2017.positivetime.model.Statistics.AppInformation;
import com.sjtu.se2017.positivetime.model.application.ATapplicaion;

import java.util.ArrayList;

/**
 * Created by bonjour on 17-7-5.
 */

public class UpdateUI extends Activity {

    private long ptime;
    private long ntime;
    private long totaltime;
    private int style;
    private ArrayList<AppInformation> Tmplist;
    private String label;
    private String tmp;
    private int weight;
    private long usetime;
    private long AT;
    private TextView PView;
    private TextView NView;
    private static UpdateUI instance;

    public static UpdateUI getInstance() {
        if (instance == null) {
            instance = new UpdateUI();
        }
        return instance;
    }

    public void Update() {


        ATapplicaion aTapplicaion = ATapplicaion.getInstance();
        ptime = aTapplicaion.getPTime();
        ntime = aTapplicaion.getNTime();
        PView = aTapplicaion.getPView();
        NView = aTapplicaion.getNView();
        if(ptime == 0) ptime = 1;
        if(ntime == 0) ntime = 1;



        //创建一个线程
        new Thread(new Runnable() {

            @Override
            public void run() {


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        long total = ptime + ntime ;
                        float tmp = total;
                        float pweight = ptime / tmp ;
                        float nweight = ntime / tmp ;
                        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(

                                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, pweight );
                        PView.setLayoutParams(param);
                        String msg = pweight + " ";
                        PView.setText(msg);
                        param = new LinearLayout.LayoutParams(

                                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, nweight );
                        NView.setLayoutParams(param);
                        String msg2 = nweight + " ";
                        NView.setText(msg2);
                    }
                });

            }
        }).start();
    }
}

