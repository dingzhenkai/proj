package com.sjtu.se2017.positivetime.view.activity;


import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.sjtu.se2017.positivetime.R;

public class MainActivity extends AppCompatActivity {
    private Button openwindow;
    private Button viewdata;
    private Button setweight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setweight = (Button) findViewById(R.id.setweight);
        setweight.setOnClickListener(new View.OnClickListener() {

            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SetWeightActivity.class);
                startActivity(intent);
            }
        });
        viewdata = (Button) findViewById(R.id.viewdata);
        viewdata.setOnClickListener(new View.OnClickListener() {

            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Viewauth.class);
                startActivity(intent);
            }
        });
        openwindow = (Button) findViewById(R.id.openwindow);
        openwindow.setOnClickListener(new View.OnClickListener() {

            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FloatingWindow.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
