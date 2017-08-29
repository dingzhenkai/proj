package com.sjtu.se2017.positivetime.controller;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.sjtu.se2017.positivetime.R;
import com.sjtu.se2017.positivetime.dao.AppInfoDao;
import com.sjtu.se2017.positivetime.model.Statistics.AppInformation;
import com.sjtu.se2017.positivetime.model.Statistics.StatisticsInfo;
import com.sjtu.se2017.positivetime.model.Uploadinfo;
import com.sjtu.se2017.positivetime.model.application.ATapplicaion;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by bonjour on 17-7-14.
 */

public class Upload extends AppCompatActivity {
    private ArrayList<Uploadinfo> Uploadlist;
    private int style;
    private ArrayList<AppInformation> Tmplist;
    private String email,label;
    private ArrayList<Uploadinfo> list;
    private AppInfoDao appInfoDao = new AppInfoDao(this);

    private static Upload instance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        doit();
    }

    public static Upload getInstance() {
        if (instance == null) {
            instance = new Upload();
        }
        return instance;
    }

    public void doit(){
        this.style = StatisticsInfo.DAY;
        StatisticsInfo statisticsInfo = new StatisticsInfo(getApplicationContext(),this.style);
        Tmplist = statisticsInfo.getShowList();
        int size = Tmplist.size();
        Calendar calendar = Calendar.getInstance();
        calendar.getTime();
        ATapplicaion aTapplicaion = ATapplicaion.getInstance();

        email = aTapplicaion.getEmail();
        Uploadinfo list = new Uploadinfo();

        PackageManager pm = getPackageManager();

        this.Uploadlist = new ArrayList<>();
        for(int i=0;i<size;i++) {
            list.setEmail(email);
            list.setPackageName(Tmplist.get(i).getPackageName());
            list.setDay(calendar.getTime());
            try {
                ApplicationInfo applicationInfo = pm.getApplicationInfo(Tmplist.get(i).getPackageName(), 0);
                label = (String)pm.getApplicationLabel(applicationInfo);
                list.setWeight(appInfoDao.checkweight(label));

            } catch (PackageManager.NameNotFoundException  e) {
                e.printStackTrace();
            }
            list.setFrequency(Tmplist.get(i).getTimes());
            list.setDuration((int) Tmplist.get(i).getUsedTimebyDay());


            Uploadlist.add(list);
            String dedug = Uploadlist.size() + "";
            Log.e("ryze", dedug);
        }
    }



}
