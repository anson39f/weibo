package com.eminem.weibo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.eminem.weibo.R;
import com.eminem.weibo.api.remote.BaseAppApi;
import com.eminem.weibo.base.net.ApiException;
import com.eminem.weibo.base.net.HttpListener;
import com.eminem.weibo.base.ui.fragment.BaseRxFragment;
import com.eminem.weibo.bean.User;
import com.eminem.weibo.utils.ToastUtils;
import com.eminem.weibo.widget.ClearEditText;
import com.eminem.weibo.widget.ImageLoader;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author .
 * @TODO 注册
 * @email
 */
public class RegisterFragment extends BaseRxFragment implements TakePhoto.TakeResultListener, InvokeListener {

    @BindView(R.id.ed_passwd)
    ClearEditText edPasswd;
    @BindView(R.id.cdName)
    ClearEditText cdName;
    @BindView(R.id.cENickName)
    ClearEditText cENickName;
    @BindView(R.id.cEEmail)
    ClearEditText cEEmail;
    @BindView(R.id.iv_head)
    CircleImageView ivHead;
    private String type;
    private TakePhoto takePhoto;
    private InvokeParam invokeParam;

    private String url;

    public static Fragment getInstance(String type) {
        RegisterFragment fragment = new RegisterFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_regist;
    }

    @OnClick({R.id.next, R.id.iv_head})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.next:
                register();
                break;
            case R.id.iv_head:
                getTakePhoto().onPickFromGallery();
                break;

        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTakePhoto().onCreate(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        //设置压缩规则，最大500kb
        takePhoto.onEnableCompress(new CompressConfig.Builder().setMaxSize(500 * 1024).create(), true);
        return takePhoto;
    }

    @Override
    public void takeSuccess(TResult result) {
        url = result.getImage().getCompressPath();
        ImageLoader.loadImage(Glide.with(this), ivHead, url);
        Log.i(TAG, "takeSuccess：" + result.getImage().getCompressPath());
    }

    @Override
    public void takeFail(TResult result, String msg) {
        Log.i(TAG, "takeFail:" + msg);
    }

    @Override
    public void takeCancel() {
        Log.i(TAG, getResources().getString(R.string.msg_operation_canceled));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(getActivity(), type, invokeParam, this);
    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }

    private void register() {
        Bundle bundle = getArguments();
        type = bundle.getString("type");
        String name = cdName.getText().toString();
        String passwd = edPasswd.getText().toString();
        String nickName = cENickName.getText().toString();
        String email = cEEmail.getText().toString();
        if (name.equals("")) {
            ToastUtils.showShortToast(mContext, "请输入账号");
        } else if (name.equals("")) {
            ToastUtils.showShortToast(mContext, "请输入昵称");
        } else if (passwd.equals("")) {
            ToastUtils.showShortToast(mContext, "请输入密码");
        } else if (email.equals("")) {
            ToastUtils.showShortToast(mContext, "请输入邮箱");
        } else if (TextUtils.isEmpty(url)) {
            ToastUtils.showShortToast(mContext, "请选择头像");
        } else {
            BaseAppApi.register(this, url, name, passwd, nickName, email,
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
                        public void onSuccess(User response) {
                            ToastUtils.showShortToast(mContext, "注册成功");
                        }
                    });
        }
    }

    @Override
    public void initUI() {

    }

    @Override
    public void initAction() {

    }

    @Override
    protected void updateViews(boolean isRefresh) {

    }
}
