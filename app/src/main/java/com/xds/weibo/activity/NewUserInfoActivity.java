package com.xds.weibo.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xds.weibo.BaseApplication;
import com.xds.weibo.R;
import com.xds.weibo.adapter.UserInfoAdapter;
import com.xds.weibo.api.remote.BaseAppApi;
import com.xds.weibo.base.net.ApiException;
import com.xds.weibo.base.net.HttpListener;
import com.xds.weibo.bean.User;
import com.xds.weibo.fragment.userinfofragment.UserPhotoFragment;
import com.xds.weibo.fragment.userinfofragment.UserWeiboFragment;
import com.xds.weibo.utils.ToastUtils;
import com.xds.weibo.widget.ImageLoader;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;
import static com.xds.weibo.BaseApplication.getContext;

public class NewUserInfoActivity extends AppCompatActivity {
    public static final String TAG = "NewUserInfoActivity";
    //用户个人信息
    private ImageView iv_user_head, backdrop;
    private TextView tv_user_name;
    private TextView tv_user_fans;
    private TextView tv_user_desc;
    // 用户相关信息
    private boolean isCurrentUser;
    private User user;
    private String screenName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //        setStatusBarColor();

        //沉浸式状态栏效果
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_new_user_info);
        screenName = getIntent().getStringExtra("screen_name");

        if (TextUtils.isEmpty(screenName)) {
            isCurrentUser = true;
            user = getContext().currentUser;
        }

        initUserInfo();
        initCL();
        loadData();

    }

    private void setStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }

    }

    private void loadData() {
        if (isCurrentUser) {
            // 如果是当前授权用户,直接设置信息
            setUserInfo();
        } else {
            // 如果是查看他人,调用获取用户信息接口
            loadUserInfo();
        }
        invalidateOptionsMenu();
    }

    private void setUserInfo() {
        if (user == null) {
            return;
        }
        tv_user_name.setText(user.getName());
        Glide.with(this).load(user.getAvatar_hd()).apply(bitmapTransform(new CropCircleTransformation()).placeholder(R.drawable.head_pistion)).into(iv_user_head);
        ImageLoader.loadImage(Glide.with(this), backdrop, user.getAvatar_hd());
        //        tv_user_fans.setText("关注  " + user.getFriends_count() + " | " + "粉丝  " + user.getFollowers_count());
        tv_user_desc.setText("简介:" + user.getDescription());
    }

    private void loadUserInfo() {
        BaseAppApi.getUser(isCurrentUser ? user.getIdstr() : screenName,
                new HttpListener<User>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        ToastUtils.showShortToast(getContext(), e.getMessage());
                    }

                    @Override
                    public void onSuccess(User response) {
                        user = response;
                        setUserInfo();
                    }
                });
    }


    private void initUserInfo() {
        iv_user_head = (ImageView) findViewById(R.id.iv_user_head);
        backdrop = (ImageView) findViewById(R.id.backdrop);
        tv_user_name = (TextView) findViewById(R.id.tv_user_name);
        tv_user_fans = (TextView) findViewById(R.id.tv_user_fans);
        tv_user_desc = (TextView) findViewById(R.id.tv_user_desc);

    }

    private void initCL() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        //设置CollapsingToolbarLayout的标题文字
        collapsingToolbar.setTitle(" ");
        //设置ViewPager
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        //设置tablayout，viewpager上的标题
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.orange));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        UserInfoAdapter adapter = new UserInfoAdapter(getSupportFragmentManager());
        //        adapter.addFragment(new UserHomeFragment(), "主页");
        adapter.addFragment(new UserWeiboFragment(), "微博");
        adapter.addFragment(new UserPhotoFragment(), "相册");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!isCurrentUser) {
            getMenuInflater().inflate(R.menu.toolbar_actions, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_star:
                User currentUser = BaseApplication.getContext().currentUser;
                BaseAppApi.attention(currentUser.getIdstr(), screenName, new HttpListener<Void>() {
                    @Override
                    public void onSuccess(Void response) {

                        ToastUtils.showShortToast(NewUserInfoActivity.this, "关注成功");
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        ToastUtils.showShortToast(NewUserInfoActivity.this, e.getMessage());
                    }
                });
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public String getScreenName() {
        return isCurrentUser ? user.getIdstr() : screenName;
    }
}
