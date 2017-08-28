package com.sjtu.se2017.positivetime.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.icu.util.Calendar;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.sjtu.se2017.positivetime.dao.ATDao;
import com.sjtu.se2017.positivetime.model.application.ATapplicaion;
import com.sjtu.se2017.positivetime.model.application.Constants;
import com.sjtu.se2017.positivetime.view.activity.MainActivity;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by bonjour on 17-7-5.
 * 此service实现了broadcastreceiver，用于保存at
 */

public class UpdateUIService extends Service implements Constants {

    private Handler handler = new Handler();
    private Timer timer;
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

                    MainActivity.getInstance().Update();
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
        }
    };
}
