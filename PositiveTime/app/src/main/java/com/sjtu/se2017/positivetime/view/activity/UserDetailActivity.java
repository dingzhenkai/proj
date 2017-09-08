package com.sjtu.se2017.positivetime.view.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.sjtu.se2017.positivetime.R;
import com.sjtu.se2017.positivetime.service.util.BarUtils;
import com.sjtu.se2017.positivetime.view.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.sjtu.se2017.positivetime.R.id.toolbar;

public class UserDetailActivity extends BaseActivity {

    @BindView(R.id.head)
    CircleImageView mHead;
    @BindView(R.id.main_fl_title)
    RelativeLayout mMainFlTitle;
    @BindView(R.id.main_tv_toolbar_title)
    TextView mMainTvToolbarTitle;
    @BindView(toolbar)
    Toolbar mToolbar;
    @BindView(R.id.main_abl_app_bar)
    AppBarLayout mMainAblAppBar;
    private Button star;
    private TextView totalAT,averageATbyday,averageUTimebyday;
    private String email;


    @Override
    protected int bindLayout() {
        return R.layout.activity_userdetail;
    }

    @Override
    protected void initView() {
        BarUtils.setColor(this, Color.parseColor("#5DC9D3"), 0);
        ButterKnife.bind(this);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);

        email = getIntent().getStringExtra("email");//email是从相似用户列表传输过来的 用户点击了哪个就传输哪个

        totalAT = (TextView) findViewById(R.id.totalAT);
        averageATbyday = (TextView) findViewById(R.id.averageATbyday);
        averageUTimebyday = (TextView) findViewById(R.id.averageUTimebyday);

        ((TextView)findViewById(R.id.email)).setText(email);
        totalAT.setText("2h");
        averageATbyday.setText("1h");
        averageUTimebyday.setText("5h");

        star = (Button) findViewById(R.id.button);
        star.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                star.setText("已关注");
            }

        });


    }

    @Override
    protected void initListener() {
        super.initListener();
        mMainAblAppBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int halfScroll = appBarLayout.getTotalScrollRange() / 2;
                int offSetAbs = Math.abs(verticalOffset);
                float percentage;
                if (offSetAbs < halfScroll) {
                    mMainTvToolbarTitle.setText("正时间");
                    percentage = 1 - (float) offSetAbs / (float) halfScroll;
                } else {
                    mMainTvToolbarTitle.setText("个人中心");
                    percentage = (float) (offSetAbs - halfScroll) / (float) halfScroll;
                }
                mToolbar.setAlpha(percentage);
            }
        });
    }
}
