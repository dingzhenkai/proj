package com.sjtu.se2017.positivetime.view.activity;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.sjtu.se2017.positivetime.R;
import com.sjtu.se2017.positivetime.model.AppInfo;
import com.sjtu.se2017.positivetime.model.application.ATapplicaion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Featureapp extends AppCompatActivity {

    private Drawable Icon;
    private String label,packageName,email;
    private ArrayList<AppInfo> ShowList;

    ATapplicaion aTapplicaion = ATapplicaion.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        //        WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_featureapp);
        email = aTapplicaion.getEmail();

        onResume();



    }



    //每次重新进入界面的时候加载listView
    @SuppressLint("SetTextI18n")
    @Override
    protected void onResume() {
        super.onResume();


        List<Map<String,Object>> datalist = null;


        datalist = getDataList(ShowList);

        ListView listView = (ListView)findViewById(R.id.AppStatisticsList);
        SimpleAdapter adapter = new SimpleAdapter(this,datalist,R.layout.inner_list,
                new String[]{"label","icon"},
                new int[]{R.id.label,R.id.icon});
        listView.setAdapter(adapter);

        adapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object o, String s) {
                if(view instanceof ImageView && o instanceof Drawable){

                    ImageView iv=(ImageView)view;
                    iv.setImageDrawable((Drawable)o);
                    return true;
                }
                else return false;
            }
        });

//        TextView textView = (TextView)findViewById(R.id.text1);
//        textView.setText("运行总时间: " + DateUtils.formatElapsedTime(totalTime / 1000));
    }

    private List<Map<String,Object>> getDataList(ArrayList<AppInfo> ShowList) {
        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("label","全部应用");
        map.put("icon",R.drawable.use);
        dataList.add(map);
        int size = ShowList.size();

        for(int i=0; i<size; i++) {
           // try {
                //PackageManager pm = getPackageManager();
                map = new HashMap<String,Object>();
                //packageName = ShowList.get(i).getAppName();
                //ApplicationInfo applicationInfo = pm.getApplicationInfo(packageName, 0);
                //label = (String)pm.getApplicationLabel(applicationInfo);
                map.put("label",ShowList.get(i).getAppName());
                //Icon = pm.getApplicationIcon(ShowList.get(i).getAppName());
                map.put("icon",ShowList.get(i).getImage());
                dataList.add(map);

            //} catch (PackageManager.NameNotFoundException  e) {
            //    e.printStackTrace();
            //}

        }

        return dataList;
    }



}
