package com.sjtu.se2017.positivetime.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.sjtu.se2017.positivetime.R;
import com.sjtu.se2017.positivetime.dao.AppInfoDao;
import com.sjtu.se2017.positivetime.model.Statistics.AppInformation;
import com.sjtu.se2017.positivetime.model.Statistics.StatisticsInfo;
import com.sjtu.se2017.positivetime.model.application.ATapplicaion;
import com.sjtu.se2017.positivetime.model.application.Constants;
import com.sjtu.se2017.positivetime.controller.MyWindowManager;
import com.sjtu.se2017.positivetime.view.activity.MainActivity;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class FloatWindowService extends Service implements Constants {

    private Handler handler = new Handler();
    private Timer timer;
    private AppInfoDao appInfoDao = new AppInfoDao(this);


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(getApplicationContext())) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.permission_alert), Toast.LENGTH_LONG).show();
            Intent permissionIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivity(new Intent(this,MainActivity.class));
            startActivity(permissionIntent);
            Toast toast=Toast.makeText(getApplicationContext(), "此权限用于打开悬浮窗", Toast.LENGTH_SHORT);    //显示toast信息
            toast.show();
        }
        if (timer == null) {
            timer = new Timer();
            timer.scheduleAtFixedRate(new RefreshTask(), 0L, (long) TIME_SPAN);
        }
        //int result = super.onStartCommand(intent, flags, startId);
        //return result;
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Service被终止的同时也停止定时器继续运行
        timer.cancel();
        timer = null;
        MyWindowManager.getInstance().removeAllWindow(getApplicationContext());
    }

    class RefreshTask extends TimerTask {
        private int style;
        private long totalTime;
        private ArrayList<AppInformation> Tmplist;
        private String label;
        private String tmp;
        private int weight;
        private long usetime;
        long PTime;
        long NTime;

        @Override
        public void run() {

            this.style = StatisticsInfo.DAY;
            StatisticsInfo statisticsInfo = new StatisticsInfo(getApplicationContext(),this.style);
            Tmplist = statisticsInfo.getShowList();
            int size = Tmplist.size();
            PTime = 0;
            NTime = 0;
            for(int i=0;i<size;i++){
                label = Tmplist.get(i).getLabel();
                usetime = Tmplist.get(i).getUsedTimebyDay();
                weight = appInfoDao.checkweight(label);
                if(weight > 50){
                    PTime += (weight-50)*usetime;
                } else {
                    NTime += (50-weight)*usetime;
                }
            }
            ATapplicaion aTapplicaion = ATapplicaion.getInstance();
            aTapplicaion.setPTime(PTime);
            aTapplicaion.setNTime(NTime);


            //int offset = c.getColumnIndex("weight");
            //num = c.getInt(offset)

            // 当前没有悬浮窗显示，则创建悬浮窗。
            if (!MyWindowManager.getInstance().isWindowShowing()) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        MyWindowManager.getInstance().initData();
                        MyWindowManager.getInstance().createWindow(getApplicationContext());
                    }
                });
            }
            // 当前有悬浮窗显示，则更新内存数据。
            else {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        MyWindowManager.getInstance().updateViewData(getApplicationContext());
                    }
                });
            }

            Intent intent = new Intent(FloatWindowService.this, WatchDogService.class);
            if(aTapplicaion.getAT() < 0){
                startService(intent);
            }else{
                stopService(intent);
            }
        }
    }

}
