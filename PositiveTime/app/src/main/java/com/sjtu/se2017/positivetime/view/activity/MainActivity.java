package com.sjtu.se2017.positivetime.view.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sjtu.se2017.positivetime.R;
import com.sjtu.se2017.positivetime.dao.AppInfoDao;
import com.sjtu.se2017.positivetime.model.ContentAdapter;
import com.sjtu.se2017.positivetime.model.ContentModel;
import com.sjtu.se2017.positivetime.model.Statistics.AppInformation;
import com.sjtu.se2017.positivetime.model.Statistics.StatisticsInfo;
import com.sjtu.se2017.positivetime.model.application.ATapplicaion;
import com.sjtu.se2017.positivetime.service.FloatWindowService;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {
    private Button openwindow;
    private Button viewdata;
    private Button setweight;
    private DrawerLayout drawerLayout;
    private RelativeLayout rightLayout;
    private List<ContentModel> list;
    private ContentAdapter adapter;
    private ImageView leftMenu, rightMenu;
    private ListView listView;
    private FragmentManager fm;
    private TextView PView;
    private TextView NView;
    private long ptime;
    private long ntime;
    private long totaltime;
    private int style;
    private ArrayList<AppInformation> Tmplist;
    private String label;
    private String tmp;
    private int weight;
    private long usetime;
    private long AT;
    private AppInfoDao appInfoDao = new AppInfoDao(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //      getActionBar().hide();

        startService(new Intent(this, FloatWindowService.class));
        ptime=0;
        ntime=0;
        totaltime=0;

        this.style = StatisticsInfo.DAY;
        StatisticsInfo statisticsInfo = new StatisticsInfo(getApplicationContext(),this.style);
        Tmplist = statisticsInfo.getShowList();
        int size = Tmplist.size();
        for(int i=0;i<size;i++){
            label = Tmplist.get(i).getLabel();
            usetime = Tmplist.get(i).getUsedTimebyDay();
            weight = appInfoDao.checkweight(label);
            AT += (weight-50)*usetime;
            ptime += usetime*(weight/100.0);
        }
        totaltime = statisticsInfo.getTotalTime();
        ntime = totaltime - ptime;
        PView = (TextView) findViewById(R.id.PView);
        NView = (TextView) findViewById(R.id.NView);
        ATapplicaion aTapplicaion = (ATapplicaion)getApplication();


        float pweight = ptime / totaltime ;
        float nweight = ntime / totaltime ;
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, pweight );
        PView.setLayoutParams(param);
        param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, nweight );
        NView.setLayoutParams(param);

        leftMenu = (ImageView) findViewById(R.id.leftmenu);
        rightMenu = (ImageView) findViewById(R.id.rightmenu);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        rightLayout = (RelativeLayout) findViewById(R.id.right);
        listView = (ListView) findViewById(R.id.left_listview);
        fm = getSupportFragmentManager();

        initData();
        adapter = new ContentAdapter(this, list);
        listView.setAdapter(adapter);
        leftMenu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                switch ((int) id) {
                    case 1:
                        Intent intent = new Intent(MainActivity.this, Viewauth.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(MainActivity.this, SetWeightActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        intent = new Intent(MainActivity.this, FloatingWindow.class);
                        startActivity(intent);
                        break;
                    case 4:
                        intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;

                    default:
                        break;
                }

                drawerLayout.closeDrawer(Gravity.LEFT);
            }
        });
        rightMenu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.RIGHT);
            }
        });
        rightLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(Gravity.RIGHT);
            }
        });

    }

    private void initData() {
        list = new ArrayList<ContentModel>();

        list.add(new ContentModel(R.mipmap.doctoradvice2, "查看数据", 1));
        list.add(new ContentModel(R.mipmap.infusion_selected, "设置权重", 2));
        list.add(new ContentModel(R.mipmap.doctoradvice2, "悬浮窗", 3));
        list.add(new ContentModel(R.mipmap.mypatient_selected, "登录/注册", 4));

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
