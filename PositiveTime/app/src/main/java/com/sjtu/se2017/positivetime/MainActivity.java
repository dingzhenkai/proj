package com.sjtu.se2017.positivetime;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Administrator on 2017/6/29.
 */

public class MainActivity extends AppCompatActivity {
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_main);

        ATapplicaion aTapplicaion = (ATapplicaion)getApplication();
        int AT = aTapplicaion.getAT();
    }
}
