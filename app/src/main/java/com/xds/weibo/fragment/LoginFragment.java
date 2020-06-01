package com.xds.weibo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.xds.weibo.BaseApplication;
import com.xds.weibo.R;
import com.xds.weibo.activity.MainActivity;
import com.xds.weibo.api.remote.BaseAppApi;
import com.xds.weibo.base.net.ApiException;
import com.xds.weibo.base.net.HttpListener;
import com.xds.weibo.base.ui.fragment.BaseRxFragment;
import com.xds.weibo.base.utils.ActivityTools;
import com.xds.weibo.base.utils.JsonParser;
import com.xds.weibo.base.utils.PreferencesUtils;
import com.xds.weibo.bean.User;
import com.xds.weibo.utils.SPUtils;
import com.xds.weibo.utils.ToastUtils;
import com.xds.weibo.widget.EditTextWithDel;
import com.xds.weibo.widget.PaperButton;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginFragment extends BaseRxFragment {
    @BindView(R.id.userph)
    EditTextWithDel userph;
    @BindView(R.id.rela_name)
    RelativeLayout relaName;
    @BindView(R.id.codeicon)
    ImageView codeicon;
    @BindView(R.id.userpass)
    EditTextWithDel userpass;
    @BindView(R.id.rela_pass)
    RelativeLayout relaPass;
    @BindView(R.id.bt_login)
    PaperButton btLogin;
    @BindView(R.id.ed_confrim)
    EditTextWithDel edConfirm;
    private String type;

    public static Fragment getInstance(String s) {
        LoginFragment fragment = new LoginFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", s);
        fragment.setArguments(bundle);
        return fragment;
    }

    @OnClick(R.id.bt_login)
    void login() {
        //        ActivityTools.startToNextActivity(getContext(), MainActivity.class);
        //        ToastUtils.showShortToast(mContext, "登陆成功");
        //        finish();
        if (userph.getText().toString().equals("")) {
            ToastUtils.showShortToast(mContext, "请先输入账号");
        } else if (userpass.getText().toString().equals("")) {
            ToastUtils.showShortToast(mContext, "请先输入密码");
        } else {
            BaseAppApi.login(userph.getText().toString(), userpass.getText().toString(),
                    new HttpListener<User>() {
                        @Override
                        public void onStart() {
                            super.onStart();
                        }

                        @Override
                        public void onError(ApiException e) {
                            super.onError(e);
                            showToast(e.getMessage());
                        }

                        @Override
                        public void onSuccess(User user) {
                            //                            user.type = type;
//                            BaseApplication.setUser(user);
                            ((BaseApplication) getActivity().getApplication()).currentUser = user;
                            SPUtils.setLogin(mContext, true);
                            PreferencesUtils.clear(getContext(), "user");
                            PreferencesUtils.putString(getContext(), "user", JsonParser.toJson(user));
                            ActivityTools.startToNextActivity(getActivity(), MainActivity.class);
                            finish();
                        }
                    });

        }
    }


    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_login;
    }

    @Override
    public void initUI() {
        Bundle bundle = getArguments();
        type = bundle.getString("type");
        SPUtils.setGrade(mContext, true);
        if (SPUtils.getUserPsd(mContext) != null) {
            userpass.setText(SPUtils.getUserPsd(mContext));
            userph.setText(SPUtils.getUserId(mContext));
        }

    }

    @Override
    public void initAction() {

    }

    @Override
    protected void updateViews(boolean isRefresh) {

    }


}
