package com.sjtu.se2017.positivetime.controller;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.sjtu.se2017.positivetime.R;
import com.sjtu.se2017.positivetime.dao.ATDao;
import com.sjtu.se2017.positivetime.dao.AppInfoDao;
import com.sjtu.se2017.positivetime.model.Statistics.AppInformation;
import com.sjtu.se2017.positivetime.model.Statistics.StatisticsInfo;
import com.sjtu.se2017.positivetime.model.Uploadinfo;
import com.sjtu.se2017.positivetime.model.application.ATapplicaion;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
    private ATDao atDao = new ATDao(this);

    private long AT;
    ATapplicaion aTapplicaion = ATapplicaion.getInstance();


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
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, -1 * 24);
        SimpleDateFormat yes = new SimpleDateFormat("yyyy-MM-dd");
        String yesterday = yes.format(calendar.getTime());
        System.out.println(yesterday+"");

        AT = atDao.checkAT(yesterday);

        this.style = StatisticsInfo.YESTERDAY;
        StatisticsInfo statisticsInfo = new StatisticsInfo(getApplicationContext(),this.style);
        Tmplist = statisticsInfo.getShowList();
        int size = Tmplist.size();
        //Calendar calendar = Calendar.getInstance();
        //calendar.
        Date d = new Date();
        //System.out.println(d);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateNowStr = sdf.format(d);
        //System.out.println(dateNowStr);

        ATapplicaion aTapplicaion = ATapplicaion.getInstance();

        email = aTapplicaion.getEmail();
        Uploadinfo list = new Uploadinfo();

        PackageManager pm = getPackageManager();

        this.Uploadlist = new ArrayList<>();
        for(int i=0;i<size;i++) {
            list.setEmail(email);
            list.setPackageName(Tmplist.get(i).getPackageName());
            list.setDay(dateNowStr);
            try {
                ApplicationInfo applicationInfo = pm.getApplicationInfo(Tmplist.get(i).getPackageName(), 0);
                label = (String)pm.getApplicationLabel(applicationInfo);
                list.setAppname(label);
                list.setWeight(appInfoDao.checkweight(label));

            } catch (PackageManager.NameNotFoundException  e) {
                e.printStackTrace();
            }
            list.setFrequency(Tmplist.get(i).getTimes());
            list.setDuration((int) Tmplist.get(i).getUsedTimebyDay());


            Uploadlist.add(list);
            String dedug = dateNowStr + "";
            Log.e("ryze", dedug);
        }
    }



}
