package com.xds.weibo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.xds.weibo.BaseApplication;
import com.xds.weibo.R;
import com.xds.weibo.api.remote.BaseAppApi;
import com.xds.weibo.base.net.ApiException;
import com.xds.weibo.base.net.HttpListener;
import com.xds.weibo.bean.User;
import com.xds.weibo.utils.TitleBuilder;
import com.xds.weibo.utils.ToastUtils;
import com.xds.weibo.widget.ClearEditText;
import com.xds.weibo.widget.ImageLoader;
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
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class UserInfoEditActivity extends AppCompatActivity implements TakePhoto.TakeResultListener, InvokeListener {

    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.ll_userinfo)
    LinearLayout llUserinfo;
    @BindView(R.id.cENickName)
    ClearEditText cENickName;
    private TakePhoto takePhoto;
    private InvokeParam invokeParam;

    private String url;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTakePhoto().onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_edit);
        ButterKnife.bind(this);
        new TitleBuilder(this)
                .setTitleText("编辑个人信息")
                .setLeftImage(R.drawable.navigationbar_back)
                .setLeftOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
        initView();
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

    private void initView() {
        user = BaseApplication.getContext().currentUser;
        Glide.with(this).load(user.getAvatar_hd()).apply(bitmapTransform(new CropCircleTransformation())).into(ivAvatar);
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
        ImageLoader.loadImage(Glide.with(this), ivAvatar, url);
        BaseAppApi.updateAvatar(url, user.getIdstr(),
                new HttpListener<User>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        ToastUtils.showShortToast(UserInfoEditActivity.this, e.getMessage());
                    }

                    @Override
                    public void onSuccess(User response) {
                        setResult(RESULT_OK);
                        ToastUtils.showShortToast(UserInfoEditActivity.this, "修改成功");
                    }
                });
    }

    @Override
    public void takeFail(TResult result, String msg) {
    }

    @Override
    public void takeCancel() {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(this, type, invokeParam, this);
    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }

    @OnClick({R.id.ll_userinfo, R.id.next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_userinfo:
                getTakePhoto().onPickFromGallery();
                break;
            case R.id.next:
                final String nickName = cENickName.getText().toString().trim();
                if (TextUtils.isEmpty(nickName)) {
                    ToastUtils.showShortToast(UserInfoEditActivity.this, "请输入昵称");
                    return;
                }
                BaseAppApi.updateNickname(user.getIdstr(), nickName,
                        new HttpListener<User>() {
                            @Override
                            public void onStart() {
                                super.onStart();
                            }

                            @Override
                            public void onError(ApiException e) {
                                super.onError(e);
                                ToastUtils.showShortToast(UserInfoEditActivity.this, e.getMessage());
                            }

                            @Override
                            public void onSuccess(User response) {
                                setResult(RESULT_OK);
                                user.setName(nickName);
                                BaseApplication.getContext().currentUser = user;
                                ToastUtils.showShortToast(UserInfoEditActivity.this, "修改成功");
                            }
                        });
                break;
        }
    }
}
