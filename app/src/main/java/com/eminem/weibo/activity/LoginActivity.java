package com.eminem.weibo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.eminem.weibo.BaseActivity;
import com.eminem.weibo.R;
import com.eminem.weibo.adapter.TabViewPagerAdapter;
import com.eminem.weibo.base.ui.activity.EnvSettingActivity;
import com.eminem.weibo.fragment.LoginFragment;
import com.eminem.weibo.fragment.RegisterFragment;
import com.eminem.weibo.utils.SPUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class LoginActivity extends BaseActivity {
    @BindView(R.id.login_tabs)
    TabLayout loginTabs;
    @BindView(R.id.login_viewpager)
    ViewPager loginViewpager;
    private Unbinder unbinder;
    private boolean isLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(attachLayoutRes());
        unbinder = ButterKnife.bind(this);
        initViews();
        //        intent2Activity(MainActivity.class);
    }


    private void setupViewPager(ViewPager viewPager) {
        TabViewPagerAdapter adapter = new TabViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(LoginFragment.getInstance("0"), "用户登录");
        adapter.addFrag(RegisterFragment.getInstance("1"), "用户注册");
        viewPager.setAdapter(adapter);
        loginTabs.setupWithViewPager(loginViewpager);
        if (isLogin) {

        }
    }


    protected int attachLayoutRes() {
        return R.layout.activity_login;
    }

    protected void initViews() {
        isLogin = getIntent().getBooleanExtra("is", false);
        if (SPUtils.isLogin(LoginActivity.this)) {
//            String userJson = PreferencesUtils.getString(getContext(), "user");
//            User user = JsonParser.parseJsonObject(userJson, User.class);
//            BaseApplication.setUser(user);
//            ActivityTools.startToNextActivity(this, MainActivity.class);
//            finish();
            setupViewPager(loginViewpager);
        } else {
            setupViewPager(loginViewpager);
        }
    }

    public void toEnv(View view) {
        EnvSettingActivity.launcher(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
