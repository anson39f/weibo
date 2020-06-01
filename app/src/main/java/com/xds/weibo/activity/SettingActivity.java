package com.xds.weibo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.xds.weibo.R;
import com.xds.weibo.utils.TitleBuilder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends AppCompatActivity {

    @BindView(R.id.loginout)
    LinearLayout loginout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        new TitleBuilder(this)
                .setTitleText("设置")
                .setLeftImage(R.drawable.navigationbar_back)
                .setLeftOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
    }

    @OnClick(R.id.loginout)
    public void onViewClicked() {
        //        new Handler().postDelayed(new Runnable() {
        //
        //            @Override
        //            public void run() {
        //                Process.killProcess(Process.myPid());
        //                System.exit(0);
        //            }
        //        }, 300);

        Intent intent_login = new Intent();
        intent_login.setClass(SettingActivity.this, LoginActivity.class);
        intent_login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent_login);
        finish();
    }
}
