package com.sjtu.se2017.positivetime;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class SetWeight extends Activity {

    private ListView listView;
    private Context context;
    private AppInfoDao infoDao;
    private List<String> adapterDatas;
    private AppAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.weight_set);
        updateData();
        initListView();
    }

    private void updateData() {
        infoDao = new AppInfoDao();
        adapterDatas = infoDao.getAllApps(context);
    }

    private void initListView() {
        setTitle("");
        listView = (ListView) findViewById(R.id.AppInfoList);
        adapter = new AppAdapter();
        listView.setAdapter(adapter);
    }

    private class AppAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return adapterDatas.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = new TextView(context);
            }
            TextView textView = (TextView) convertView;
            textView.setPadding(8, 8, 8, 8);
            textView.setTextSize(18);
            textView.setText(adapterDatas.get(position));
            return textView;
        }
    }
}
