package com.sjtu.se2017.positivetime.view.activity;


import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.sjtu.se2017.positivetime.R;
import com.sjtu.se2017.positivetime.model.AppSearchInfo;
import com.sjtu.se2017.positivetime.model.UserSearchInfo;
import com.sjtu.se2017.positivetime.model.application.ATapplicaion;

import java.util.ArrayList;
import java.util.List;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;


/**
 * Created by Administrator on 2017/7/7.
 */

public class UserActivity extends Activity {
    Context context;
    List<UserSearchInfo> adapterDatas;
    ListView listView;
    private AppAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        context = this;

        adapterDatas = new ArrayList<UserSearchInfo>();

        findSimilarusers(ATapplicaion.getInstance().getEmail());

        listView = (ListView) findViewById(R.id.UserSearchInfoList);
        adapter = new UserActivity.AppAdapter(adapterDatas,context);
        listView.setAdapter(adapter);
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

    public void findSimilarusers(String email){
    }

    private class AppAdapter extends BaseAdapter {
        private List<UserSearchInfo> userSearchInfos;
        private LayoutInflater inflater;
        ImageView avatar;
        TextView username;
        TextView achievements;

        public AppAdapter() {}

        public AppAdapter(List<UserSearchInfo> UserSearchInfos,Context context) {
            this.userSearchInfos = userSearchInfos;
            this.inflater=LayoutInflater.from(context);
        }
        @Override
        public int getCount() {
            return userSearchInfos.size();
        }

        @Override
        public Object getItem(int position) {
            return userSearchInfos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final UserSearchInfo userSearchInfos = adapterDatas.get(position);
            convertView = inflater.inflate(R.layout.usersearchinfo_listitem, null);
            avatar = (ImageView) convertView.findViewById(R.id.avatar);
            username = (TextView) convertView.findViewById(R.id.username);
            achievements = (TextView) convertView.findViewById(R.id.achievements);

            avatar.setImageDrawable(userSearchInfos.getAvatar());
            username.setText(userSearchInfos.getUsername());
            achievements.setText(userSearchInfos.achievementsToString());

            return convertView;
        }
    }


}
