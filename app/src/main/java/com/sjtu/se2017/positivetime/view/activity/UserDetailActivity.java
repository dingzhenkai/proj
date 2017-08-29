package com.sjtu.se2017.positivetime.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sjtu.se2017.positivetime.R;
import com.sjtu.se2017.positivetime.model.application.ATapplicaion;
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
    private ImageView featureapp,bubble;
    private TextView totalAT,averageATbyday,averageUTimebyday;
    private String viewemail,myownemail;
    int total,averageAT,averageUtime;


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

        ATapplicaion aTapplicaion = ATapplicaion.getInstance();

        viewemail = getIntent().getStringExtra("email");//email是从相似用户列表传输过来的 用户点击了哪个就传输哪个
        myownemail = aTapplicaion.getEmail(); //myownemail represent who is the user

        averageATbyday = (TextView) findViewById(R.id.averageATbyday);
        averageUTimebyday = (TextView) findViewById(R.id.averageUTimebyday);

        ((TextView)findViewById(R.id.email)).setText(viewemail);

        averageAT = 12;
        averageUtime =12;

        averageATbyday.setText(""+averageAT);
        averageUTimebyday.setText(""+averageUtime);

        star = (Button) findViewById(R.id.button);
        boolean isfollow = true;                                 //check if myownemail follows viewemail
        if (isfollow){
            star.setText("已关注");
        }
        else{
            star.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    star.setText("已关注");
                /*
                myownemail follows viewmail
                 */

                }

            });
        }


        bubble = (ImageView) findViewById(R.id.bubble);
        bubble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserDetailActivity.this,BubbleActivity.class);
                intent.putExtra("email",viewemail);
                startActivity(intent);
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
