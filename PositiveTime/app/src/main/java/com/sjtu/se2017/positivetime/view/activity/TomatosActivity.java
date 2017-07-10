package com.sjtu.se2017.positivetime.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dualcores.swagpoints.SwagPoints;
import com.sjtu.se2017.positivetime.R;
import com.sjtu.se2017.positivetime.model.application.ATapplicaion;

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
    Boolean ifLock;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tomatos);

        //init
        swagPoints = (SwagPoints)findViewById(R.id.seekbar_point);
        tvTimer = (TextView) findViewById(R.id.tvTimer);
        button = (Button)findViewById(R.id.button);
        button.setText("start");
        ifLock = false;

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (button.getText() == "start") {
                    button.setEnabled(false);
                    button.setText("finish");
                    swagPoints.setVisibility(View.INVISIBLE);
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
                            tvTimer.setText("00:00:00");
                            button.setEnabled(true);
                            ATapplicaion aTapplicaion = ATapplicaion.getInstance();
                            aTapplicaion.setPTime(aTapplicaion.getPTime()+swagPoints.getPoints()*1000*60*100);//100是权重
                        }
                    };
                    countDownTimer.start();
                    ifLock = true;
                }else if(button.getText() == "finish"){
                    finish();
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
    }
}
