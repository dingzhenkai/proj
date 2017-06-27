package com.sjtu.se2017.positivetime;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.content.pm.PackageManager;
import com.sjtu.se2017.positivetime.model.AppInfo;

import java.util.List;

public class SetWeight extends Activity {

    private ListView listView;
    private Context context;
    private AppInfoDao appInfoDao;
    private List<AppInfo> adapterDatas;
    private AppAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.weight_set);

        appInfoDao = new AppInfoDao();
        try {
            adapterDatas = appInfoDao.getAllApps(context);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        listView = (ListView) findViewById(R.id.AppInfoList);
        adapter = new AppAdapter(adapterDatas,context);
        listView.setAdapter(adapter);
    }

    private class AppAdapter extends BaseAdapter {
        private List<AppInfo> appInfos;
        private LayoutInflater inflater;
        public AppAdapter() {}

        public AppAdapter(List<AppInfo> appInfos,Context context) {
            this.appInfos = appInfos;
            this.inflater=LayoutInflater.from(context);
        }
        @Override
        public int getCount() {
            return appInfos.size();
        }

        @Override
        public Object getItem(int position) {
            return appInfos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            AppInfo appInfo = adapterDatas.get(position);
            convertView = inflater.inflate(R.layout.listitem, null);
            ImageView app_icon = (ImageView) convertView.findViewById(R.id.app_icon);
            TextView app_name = (TextView) convertView.findViewById(R.id.app_name);
            app_icon.setImageDrawable(appInfo.getImage());
            app_name.setText(appInfo.getAppName());
            return convertView;
        }
    }
}
