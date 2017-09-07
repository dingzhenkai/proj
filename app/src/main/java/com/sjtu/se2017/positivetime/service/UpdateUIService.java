package com.sjtu.se2017.positivetime.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.sjtu.se2017.positivetime.dao.ATDao;
import com.sjtu.se2017.positivetime.dao.AppInfoDao;
import com.sjtu.se2017.positivetime.model.Statistics.AppInformation;
import com.sjtu.se2017.positivetime.model.Statistics.StatisticsInfo;
import com.sjtu.se2017.positivetime.model.Uploadinfo;
import com.sjtu.se2017.positivetime.model.application.ATapplicaion;
import com.sjtu.se2017.positivetime.model.application.Constants;
import com.sjtu.se2017.positivetime.view.activity.MainActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by bonjour on 17-7-5.
 * 此service实现了broadcastreceiver，用于保存at
 */

public class UpdateUIService extends Service implements Constants {

    private Handler handler = new Handler();
    private Timer timer;
    public ArrayList<Uploadinfo> Uploadlist;
    public long AT;
    ATDao atDao;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (timer == null) {
            timer = new Timer();
            timer.scheduleAtFixedRate(new RefreshTask(), 0L, (long)TIME_SPAN);
        }
        initTimePrompt();
        //int result = super.onStartCommand(intent, flags, startId);
        //return result;
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Service被终止的同时也停止定时器继续运行
        timer.cancel();
        timer = null;;
    }


    class RefreshTask extends TimerTask {


        @Override
        public void run() {

            handler.post(new Runnable() {
                @Override
                public void run() {

                    if(MainActivity.getInstance()!=null){MainActivity.getInstance().Update();}
                }
            });
            Intent intent = new Intent(UpdateUIService.this, WatchDogService.class);
            if(ATapplicaion.getInstance().getAT() < 0){
                startService(intent);
            }else{
                stopService(intent);
            }
        }
    }

    private void initTimePrompt() {//用于注册broadcast receiver
        IntentFilter timeFilter = new IntentFilter();
        timeFilter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(mTimeReceiver, timeFilter);
    }
    public void Upload(){

        int style;
        ArrayList<AppInformation> Tmplist;
        String email,label;
        ArrayList<Uploadinfo> list;
        AppInfoDao appInfoDao = new AppInfoDao(this);
        ATDao atDao = new ATDao(this);

        /*Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, -1 * 24);
        SimpleDateFormat yes = new SimpleDateFormat("yyyy-MM-dd");
        String yesterday = yes.format(calendar.getTime());*/
        //System.out.println(yesterday+"");
        android.icu.util.Calendar cal = android.icu.util.Calendar.getInstance();
        int day = cal.get(android.icu.util.Calendar.DAY_OF_YEAR) -1;
        int hour = 23;

        AT = atDao.checkAT(day,hour);

        StatisticsInfo statisticsInfo = new StatisticsInfo(getApplicationContext(),StatisticsInfo.YESTERDAY);
        Tmplist = statisticsInfo.getShowList();
        int size = Tmplist.size();
        //Calendar calendar = Calendar.getInstance();
        //calendar.
        Date d = new Date();
        //System.out.println(d);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateNowStr = sdf.format(d);
        //System.out.println(dateNowStr);

        ATapplicaion aTapplicaion = ATapplicaion.getInstance();

        email = aTapplicaion.getEmail();

        PackageManager pm = getPackageManager();

        Uploadlist = new ArrayList<>();
        for(int i=0;i<size;i++) {
            Uploadinfo uploadlist = new Uploadinfo();
            uploadlist.setEmail(email);
            String p = Tmplist.get(i).getPackageName();
            uploadlist.setPackageName(Tmplist.get(i).getPackageName());
            uploadlist.setDay(dateNowStr);
            try {
                ApplicationInfo applicationInfo = pm.getApplicationInfo(Tmplist.get(i).getPackageName(), 0);
                label = (String)pm.getApplicationLabel(applicationInfo);
                uploadlist.setAppname(label);
                uploadlist.setWeight(appInfoDao.checkweight(label));

            } catch (PackageManager.NameNotFoundException  e) {
                e.printStackTrace();
            }
            uploadlist.setFrequency(Tmplist.get(i).getTimes());
            uploadlist.setDuration((int) Tmplist.get(i).getUsedTimebyDay());


            Uploadlist.add(uploadlist);
            String dedug = dateNowStr + "";
            Log.e("ryze", dedug);
        }
        //LoginActivity.DownloadTask t= new LoginActivity.DownloadTask();
        //t.execute();

    }

    private BroadcastReceiver mTimeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Calendar cal = Calendar.getInstance();
            int day = cal.get(Calendar.DAY_OF_YEAR);
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            int min = cal.get(Calendar.MINUTE);
            if (min == 0) {
                ATDao atDao = new ATDao(UpdateUIService.this);
                atDao.insertOrUpdate(day,hour,ATapplicaion.getInstance().getAT()/1000);
            }
            if(hour == 23){
                Upload();
            }
        }
    };
}
