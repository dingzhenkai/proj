package com.sjtu.se2017.positivetime.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;

import com.sjtu.se2017.positivetime.R;
import com.sjtu.se2017.positivetime.model.application.ATapplicaion;
import com.xw.repo.BubbleSeekBar;

import java.util.Locale;


/**
 * Created by Administrator on 2017/7/7.
 */

public class PlanActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);

        final BubbleSeekBar bubbleSeekBar = (BubbleSeekBar) findViewById(R.id.bubbleSeekBar);
        bubbleSeekBar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListenerAdapter() {
            @Override
            public void getProgressOnFinally(int progress, float progressFloat) {
                String s = String.format(Locale.CHINA, "onFinally int:%d, float:%.1f", progress, progressFloat);
                ATapplicaion.getInstance().setPTotalWeight(100-progress);
                ATapplicaion.getInstance().setNTotalWeight(progress);
                //Log.v("test",ATapplicaion.getInstance().getNTotalWeight()+"");
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
}
