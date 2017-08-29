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
        private ArrayList<AppInformation> Tmplist, PreList;
        private String label;
        private String tmp;
        private int weight;
        private long usetime;
        long PTime;
        long NTime;

        public long getPreUsetimeBylabel(ArrayList<AppInformation> Prelist, String nowlabel) {
            int size = Prelist.size();
            for (int i = 0; i < size; i++) {
                if (Prelist.get(i).getLabel() == nowlabel) return Prelist.get(i).getUsedTimebyDay();
            }
            return 0;

        }

        @Override
        public void run() {

            ATapplicaion aTapplicaion = ATapplicaion.getInstance();
            PTime = aTapplicaion.getPTime();
            NTime = aTapplicaion.getNTime();
            PreList = aTapplicaion.getPreList();
            if (PreList == null) {//当天第一次打开会将之前的使用时间数据直接拿来计算
                this.style = StatisticsInfo.DAY;
                StatisticsInfo statisticsInfo = new StatisticsInfo(getApplicationContext(), this.style);
                Tmplist = statisticsInfo.getShowList();
                int size = Tmplist.size();
                aTapplicaion.setPreList(Tmplist);
                //PTime = 0;
                //NTime = 0;
                if(PTime+NTime==0) {
                    for (int i = 0; i < size; i++) {
                        label = Tmplist.get(i).getLabel();
                        usetime = Tmplist.get(i).getUsedTimebyDay();
                        weight = appInfoDao.checkweight(label);
                        if (weight > 50) {
                            PTime += (weight - 50)/50 * usetime;
                        } else {
                            NTime += (50 - weight)/50 * usetime;
                        }
                    }
                    aTapplicaion.setPTime(PTime);
                    aTapplicaion.setNTime(NTime);
                }
            } else {
                this.style = StatisticsInfo.DAY;
                StatisticsInfo statisticsInfo = new StatisticsInfo(getApplicationContext(), this.style);
                Tmplist = statisticsInfo.getShowList();

                int size = Tmplist.size();
                for (int i = 0; i < size; i++) {
                    label = Tmplist.get(i).getLabel();
                    usetime = (Tmplist.get(i).getUsedTimebyDay() - getPreUsetimeBylabel(PreList, label));
                    weight = appInfoDao.checkweight(label);
                    if (weight > 50) {
                        PTime += (weight - 50)/50 * usetime;
                    } else {
                        NTime += (50 - weight)/50 * usetime;
                    }

                }
                aTapplicaion.setPTime(PTime);
                aTapplicaion.setNTime(NTime);
                aTapplicaion.setPreList(Tmplist);

                /*int nTotalWeight = appInfoDao.checkweight(getResources().getString(R.string.NTotalWeight));
                aTapplicaion.setNTotalWeight(nTotalWeight);
                aTapplicaion.setPTotalWeight(100 - nTotalWeight);*/
            }

                //int offset = c.getColumnIndex("weight");
                //num = c.getInt(offset)

            if(ATapplicaion.getInstance().getIfFloatingWindow()) {
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
            }
            }
        }
}
