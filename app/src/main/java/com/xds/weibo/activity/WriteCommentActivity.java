package com.xds.weibo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;

import com.xds.weibo.BaseApplication;
import com.xds.weibo.R;
import com.xds.weibo.api.remote.BaseAppApi;
import com.xds.weibo.base.net.ApiException;
import com.xds.weibo.base.net.HttpListener;
import com.xds.weibo.bean.Status;
import com.xds.weibo.bean.User;
import com.xds.weibo.utils.TitleBuilder;
import com.xds.weibo.utils.ToastUtils;
import com.xds.weibo.widget.EditTextWithDel;
import com.xds.weibo.widget.PaperButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WriteCommentActivity extends AppCompatActivity {

    @BindView(R.id.content)
    EditTextWithDel content;
    @BindView(R.id.submit)
    PaperButton submit;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_comment);
        ButterKnife.bind(this);
        new TitleBuilder(this)
                .setTitleText("发布评论")
                .setLeftImage(R.drawable.navigationbar_back)
                .setLeftOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        WriteCommentActivity.this.finish();
                    }
                });
    }

    @OnClick(R.id.submit)
    public void onViewClicked() {
        String comment = content.getText().toString().trim();
        if (TextUtils.isEmpty(comment)) {
            ToastUtils.showShortToast(this, "请输入评论");
            return;
        }
        user = BaseApplication.getContext().currentUser;
        Status status = (Status) getIntent().getSerializableExtra("status");
        BaseAppApi.comment(user.getIdstr(), comment, status.getId() + "",
                new HttpListener<Void>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        ToastUtils.showShortToast(WriteCommentActivity.this, e.getMessage());
                    }

                    @Override
                    public void onSuccess(Void response) {
                        setResult(RESULT_OK);
                        ToastUtils.showShortToast(WriteCommentActivity.this, "发布成功");
                        finish();
                    }
                });
    }
}
