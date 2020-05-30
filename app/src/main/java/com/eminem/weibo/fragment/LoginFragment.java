package com.eminem.weibo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.eminem.weibo.BaseApplication;
import com.eminem.weibo.R;
import com.eminem.weibo.activity.MainActivity;
import com.eminem.weibo.api.remote.BaseAppApi;
import com.eminem.weibo.base.net.ApiException;
import com.eminem.weibo.base.net.HttpListener;
import com.eminem.weibo.base.ui.fragment.BaseRxFragment;
import com.eminem.weibo.base.utils.ActivityTools;
import com.eminem.weibo.base.utils.JsonParser;
import com.eminem.weibo.base.utils.PreferencesUtils;
import com.eminem.weibo.bean.User;
import com.eminem.weibo.utils.SPUtils;
import com.eminem.weibo.utils.ToastUtils;
import com.eminem.weibo.widget.EditTextWithDel;
import com.eminem.weibo.widget.PaperButton;

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
            BaseAppApi.login(this, userph.getText().toString(), userpass.getText().toString(), type,
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
