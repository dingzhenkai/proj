package com.sjtu.se2017.positivetime.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dualcores.swagpoints.SwagPoints;
import com.sjtu.se2017.positivetime.R;
import com.sjtu.se2017.positivetime.model.application.ATapplicaion;
import com.sjtu.se2017.positivetime.service.FloatWindowService;
import com.sjtu.se2017.positivetime.service.UpdateUIService;
import com.sjtu.se2017.positivetime.service.WatchDogService;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/7/7.
 */

public class TomatosActivity extends Activity {
    private CountDownTimer countDownTimer;
    private TextView tvTimer;
    SwagPoints swagPoints;
    Button button;
    String mode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tomatos);

        //init
        swagPoints = (SwagPoints)findViewById(R.id.seekbar_point);
        tvTimer = (TextView) findViewById(R.id.tvTimer);
        button = (Button)findViewById(R.id.button);
        mode = getIntent().getStringExtra("mode");
        button.setText(mode);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(button.getText() == "finish"){
                    finish();
                    stopService(new Intent(TomatosActivity.this, WatchDogService.class));
                    startService(new Intent(TomatosActivity.this, UpdateUIService.class));
                } else {
                    swagPoints.setVisibility(View.INVISIBLE);
                    button.setEnabled(false);
                    button.setText("finish");
                    countDownTimer = new CountDownTimer(swagPoints.getPoints() * 1000 * 60, 1000) {

                        @Override
                        public void onTick(long millisUntilFinished) {
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                            Date date = new Date(millisUntilFinished);
                            String dateStr = simpleDateFormat.format(date);
                            tvTimer.setText(dateStr);
                        }

                        @Override
                        public void onFinish() {
                            ATapplicaion aTapplicaion = ATapplicaion.getInstance();
                            if (mode.equals(getResources().getString(R.string.start_working))) {
                                tvTimer.setText("Great Job!");
                                aTapplicaion.setPTime(aTapplicaion.getPTime() + swagPoints.getPoints() * 1000 * 60 * 100);//100是权重
                            }else if(mode.equals(getResources().getString(R.string.start_relaxing))){
                                tvTimer.setText("Relax Enough!");
                                aTapplicaion.setNTime(aTapplicaion.getNTime() + swagPoints.getPoints() * 1000 * 60 * 100);//100是权重
                            }
                            button.setEnabled(true);
                        }
                    };
                    countDownTimer.start();
                    //java 字符串比较相等要用equals！！！
                    if (mode.equals(getResources().getString(R.string.start_working))){
                        stopService(new Intent(TomatosActivity.this, UpdateUIService.class));//防止再关闭watchdogservice
                        startService(new Intent(TomatosActivity.this, WatchDogService.class));
                    } else if(mode.equals(getResources().getString(R.string.start_relaxing))){

                    }
                }
            }
        });
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }
/*
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(ifLock) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                Toast.makeText(this, "you should be working", Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, TomatosActivity.class));
            }
        }
        return super.onKeyDown(keyCode, event);
    }*/
    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }
}
