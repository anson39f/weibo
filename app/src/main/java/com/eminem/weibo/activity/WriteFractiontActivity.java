package com.eminem.weibo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;

import com.eminem.weibo.BaseApplication;
import com.eminem.weibo.R;
import com.eminem.weibo.api.remote.BaseAppApi;
import com.eminem.weibo.base.net.ApiException;
import com.eminem.weibo.base.net.HttpListener;
import com.eminem.weibo.bean.Status;
import com.eminem.weibo.bean.User;
import com.eminem.weibo.utils.TitleBuilder;
import com.eminem.weibo.utils.ToastUtils;
import com.eminem.weibo.widget.EditTextWithDel;
import com.eminem.weibo.widget.PaperButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WriteFractiontActivity extends AppCompatActivity {

    @BindView(R.id.content)
    EditTextWithDel content;
    @BindView(R.id.submit)
    PaperButton submit;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_fraction);
        ButterKnife.bind(this);
        new TitleBuilder(this)
                .setTitleText("发布评分")
                .setLeftImage(R.drawable.navigationbar_back)
                .setLeftOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        WriteFractiontActivity.this.finish();
                    }
                });
    }

    @OnClick(R.id.submit)
    public void onViewClicked() {
        String comment = content.getText().toString().trim();
        if (TextUtils.isEmpty(comment)) {
            ToastUtils.showShortToast(this, "请输入评分");
            return;
        }
        user = BaseApplication.getContext().currentUser;
        Status status = (Status) getIntent().getSerializableExtra("status");
        BaseAppApi.fraction(user.getIdstr(), status.getId() + "", comment,
                new HttpListener<Void>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        ToastUtils.showShortToast(WriteFractiontActivity.this, e.getMessage());
                    }

                    @Override
                    public void onSuccess(Void response) {
                        setResult(RESULT_OK);
                        ToastUtils.showShortToast(WriteFractiontActivity.this, "发布成功");
                        finish();
                    }
                });
    }
}
