package com.eminem.weibo.base.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.eminem.weibo.BaseActivity;
import com.eminem.weibo.R;
import com.eminem.weibo.base.config.Env;
import com.eminem.weibo.base.config.UriProvider;
import com.eminem.weibo.utils.TitleBuilder;
import com.eminem.weibo.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class EnvSettingActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {


    @BindView(R.id.dev)
    RadioButton dev;
    @BindView(R.id.test)
    RadioButton test;
    @BindView(R.id.product)
    RadioButton product;
    @BindView(R.id.custom)
    RadioButton custom;
    @BindView(R.id.uri_setting)
    RadioGroup radioGroup;
    @BindView(R.id.host)
    EditText host;
    @BindView(R.id.save)
    TextView save;
    private Unbinder unbinder;

    public static void launcher(Context context) {
        context.startActivity(new Intent(context, EnvSettingActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(attachLayoutRes());
        unbinder = ButterKnife.bind(this);
        initViews();
    }

    protected int attachLayoutRes() {
        return R.layout.activity_env_settting;
    }


    protected void initViews() {
        new TitleBuilder(this)
                .setTitleText("服务器设置")
                .setLeftImage(R.drawable.navigationbar_back)
                .setLeftOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
        Env.UriSetting uriSetting = Env.instance().getUriSetting();
        radioGroup.setOnCheckedChangeListener(this);
        if (Env.UriSetting.Dev == uriSetting) {
            radioGroup.check(R.id.dev);
        } else if (Env.UriSetting.Test == uriSetting) {
            radioGroup.check(R.id.test);
        } else if (Env.UriSetting.Demo == uriSetting) {
            radioGroup.check(R.id.demo);
        } else if (Env.UriSetting.Product == uriSetting) {
            radioGroup.check(R.id.product);
        } else if (Env.UriSetting.Custom == uriSetting) {
            radioGroup.check(R.id.custom);
        }
        host.setText(UriProvider.API_HOST);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();

    }


    public boolean savaCustom() {
        if (radioGroup.getCheckedRadioButtonId() == R.id.custom) {
            String sHost = host.getText().toString().trim();
            if (TextUtils.isEmpty(sHost)) {
                ToastUtils.showToast(this, "服务器地址不能为空", Toast.LENGTH_SHORT);
                return false;
            }
            Env.instance().setHost(sHost);
        }
        return true;

    }


    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.dev:
                host.setText(UriProvider.API_HOST_DEV);
                host.setEnabled(false);
                break;
            case R.id.test:
                host.setText(UriProvider.API_HOST_TEST);
                host.setEnabled(false);
                break;
            case R.id.demo:
                host.setText(UriProvider.API_HOST_DEMO);
                host.setEnabled(false);
                break;
            case R.id.product:
                host.setText(UriProvider.API_HOST_PRODUCT);
                host.setEnabled(false);
                break;
            case R.id.custom:
                host.setEnabled(true);
                host.setText(Env.instance().getHost());
                break;
        }
    }


    @OnClick(R.id.save)
    public void onViewClicked() {
        Env.UriSetting uriSetting = Env.instance().getUriSetting();
        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.dev:
                uriSetting = Env.UriSetting.Dev;
                break;
            case R.id.test:
                uriSetting = Env.UriSetting.Test;
                break;
            case R.id.demo:
                uriSetting = Env.UriSetting.Demo;
                break;
            case R.id.product:
                uriSetting = Env.UriSetting.Product;
                break;
            case R.id.custom:
                if (!savaCustom()) {
                    return;
                }
                uriSetting = Env.UriSetting.Custom;
                break;

        }
        Env.instance().setUriSetting(uriSetting);
        UriProvider.init(uriSetting);
        ToastUtils.showShortToast(this, String.format("已切换为“%s”环境:%s", uriSetting.getNickname(),
                host.getText().toString()));
        finish();
    }
}
