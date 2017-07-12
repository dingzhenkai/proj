package com.sjtu.se2017.positivetime.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mancj.materialsearchbar.MaterialSearchBar;
import com.sjtu.se2017.positivetime.R;
import com.sjtu.se2017.positivetime.model.AppSearchInfo;

import java.util.List;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

/**
 * Created by Administrator on 2017/7/7.
 */

public class AppActivity extends Activity{
    Context context;
    MaterialSearchBar materialSearchBar;
    List<AppSearchInfo> adapterDatas;
    ListView listView;
    private AppAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);
        context = this;
        materialSearchBar = (MaterialSearchBar)findViewById(R.id.materialSearchBar);
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener(){
            @Override
            public void onButtonClicked(int buttonCode){
                //Log.v("test",buttonCode+"");
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                search(text.toString());
                //startSearch(text.toString(), true, null, true);

                listView = (ListView) findViewById(R.id.AppSearchInfoList);
                adapter = new AppActivity.AppAdapter(adapterDatas,context);
                listView.setAdapter(adapter);
            }

            @Override
            public void onSearchStateChanged(boolean enabled) {
            }
        });

/*
        try {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse("market://details?id="+AppActivity.this.getPackageName()));
            startActivity(i);
        } catch (Exception e) {
            Toast.makeText(AppActivity.this, "您的手机上没有安装Android应用市场", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }*/
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

    public void search(String text){

    }

    private class AppAdapter extends BaseAdapter {
        private List<AppSearchInfo> appSearchInfos;
        private LayoutInflater inflater;
        ImageView app_icon;
        TextView app_name;
        TextView installNum;
        TextView category;
        TextView weight;
        MaterialRatingBar materialRatingBar;
        public AppAdapter() {}

        public AppAdapter(List<AppSearchInfo> appSearchInfos,Context context) {
            this.appSearchInfos = appSearchInfos;
            this.inflater=LayoutInflater.from(context);
        }
        @Override
        public int getCount() {
            return appSearchInfos.size();
        }

        @Override
        public Object getItem(int position) {
            return appSearchInfos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final AppSearchInfo appSearchInfo = adapterDatas.get(position);
            convertView = inflater.inflate(R.layout.appsearchinfo_listitem, null);
            app_icon = (ImageView) convertView.findViewById(R.id.app_icon);
            app_name = (TextView) convertView.findViewById(R.id.app_name);
            installNum = (TextView) convertView.findViewById(R.id.installNum);
            category = (TextView) convertView.findViewById(R.id.category);
            weight = (TextView) convertView.findViewById(R.id.weight);
            materialRatingBar = (MaterialRatingBar) convertView.findViewById(R.id.materialRatingBar);

            app_icon.setImageDrawable(appSearchInfo.getImage());
            app_name.setText(appSearchInfo.getAppName());
            installNum.setText(appSearchInfo.getInstallNum());
            category.setText(appSearchInfo.getCategoryId()+"category");
            weight.setText(appSearchInfo.getWeight()+"");
            materialRatingBar.setRating(appSearchInfo.getWeight());

            return convertView;
        }
    }
}
