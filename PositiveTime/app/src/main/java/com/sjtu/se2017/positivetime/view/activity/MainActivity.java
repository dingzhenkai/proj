package com.sjtu.se2017.positivetime.view.activity;


import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.KeyEvent;
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
import android.widget.Toast;

import com.sjtu.se2017.positivetime.R;
import com.sjtu.se2017.positivetime.dao.AppInfoDao;
import com.sjtu.se2017.positivetime.model.ContentAdapter;
import com.sjtu.se2017.positivetime.model.ContentModel;
import com.sjtu.se2017.positivetime.model.Statistics.AppInformation;
import com.sjtu.se2017.positivetime.model.application.ATapplicaion;
import com.sjtu.se2017.positivetime.service.FloatWindowService;
import com.sjtu.se2017.positivetime.service.UpdateUIService;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {
    private DrawerLayout drawerLayout;
    private RelativeLayout rightLayout;
    private List<ContentModel> list;
    private ContentAdapter adapter;
    private ListView listView;
    private FragmentManager fm;
    private TextView PView,NView;
    RelativeLayout Playout,Nlayout;
    private long ptime,ntime;
    private AppInfoDao appInfoDao = new AppInfoDao(this);
    private static MainActivity instance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //init parameters
        initData();
        //getActionBar().hide();


        // open floatwindowservice  get floatwindow permission
        Intent intent = new Intent(this, FloatWindowService.class);
        startService(intent);
        //get usage_stats permission
        try {
            if(!isStatAccessPermissionSet(this)) {
                startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));   //查看是否为应用设置了权限
                Toast toast=Toast.makeText(getApplicationContext(), "此权限用于计算各app使用时间", Toast.LENGTH_SHORT);    //显示toast信息
                toast.show();
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        adapter = new ContentAdapter(this, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                switch ((int) id) {
                    case 1:
                        Intent intent = new Intent(MainActivity.this, AppStatisticsList.class);
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
        rightLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(Gravity.RIGHT);
            }
        });

        startService(new Intent(this, UpdateUIService.class));
    }

    private void initData() {
        list = new ArrayList<ContentModel>();
        list.add(new ContentModel(R.mipmap.doctoradvice2, "查看数据", 1));
        list.add(new ContentModel(R.mipmap.infusion_selected, "设置权重", 2));
        list.add(new ContentModel(R.mipmap.doctoradvice2, "悬浮窗", 3));
        list.add(new ContentModel(R.mipmap.mypatient_selected, "登录/注册", 4));

        instance = this;
        PView = (TextView)findViewById(R.id.PView);
        NView = (TextView)findViewById(R.id.NView);
        Playout = (RelativeLayout)findViewById(R.id.Playout);
        Nlayout = (RelativeLayout)findViewById(R.id.Nlayout);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        rightLayout = (RelativeLayout) findViewById(R.id.right);
        listView = (ListView) findViewById(R.id.left_listview);
        fm = getSupportFragmentManager();
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

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private boolean isStatAccessPermissionSet(Context c) throws PackageManager.NameNotFoundException {
        PackageManager pm = c.getPackageManager();
        ApplicationInfo info = pm.getApplicationInfo(c.getPackageName(),0);
        AppOpsManager aom = (AppOpsManager) c.getSystemService(Context.APP_OPS_SERVICE);
        aom.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,info.uid,info.packageName);
        return aom.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,info.uid,info.packageName)
                == AppOpsManager.MODE_ALLOWED;
    }

    public void Update() {
        ATapplicaion aTapplicaion = ATapplicaion.getInstance();
        ptime = aTapplicaion.getPTime();
        ntime = aTapplicaion.getNTime();

        //创建一个线程
        new Thread(new Runnable() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        long total = ptime + ntime ;
                        float tmp = total;
                        float pweight,nweight;
                        if(total == 0) {
                            pweight = (float)1/2;
                            nweight = (float)1/2;
                        } else{
                            pweight = ptime / tmp ;
                            nweight = ntime / tmp ;
                        }
                        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT, nweight );
                        Playout.setLayoutParams(param);
                        PView.setText(pweight+"");
                        param = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT, pweight );
                        Nlayout.setLayoutParams(param);
                        NView.setText(nweight+"");
                    }
                });
            }
        }).start();
    }

    public static MainActivity getInstance(){
        return instance;
    }
}
