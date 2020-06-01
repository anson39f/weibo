package com.xds.weibo.activity;

import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.xds.weibo.BaseActivity;
import com.xds.weibo.BaseApplication;
import com.xds.weibo.R;
import com.xds.weibo.api.ResData;
import com.xds.weibo.api.remote.BaseAppApi;
import com.xds.weibo.api.remote.BaseService;
import com.xds.weibo.base.net.ApiException;
import com.xds.weibo.base.net.HttpListener;
import com.xds.weibo.base.net.RetrofitService;
import com.xds.weibo.bean.Category;
import com.xds.weibo.bean.User;
import com.xds.weibo.utils.ToastUtils;
import com.xds.weibo.widget.EditTextWithDel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AddActivity extends BaseActivity {
    @BindView(R.id.content)
    EditTextWithDel content;
    @BindView(R.id.achievement_spinner_acs)
    AppCompatSpinner mSpinner;
    @BindView(R.id.submit)
    com.xds.weibo.widget.PaperButton PaperButton;
    private User user;
    private ArrayAdapter adapter;
    private List<Category> mList;
    private String categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        ButterKnife.bind(this);
        initView();
    }

    List mData = new ArrayList<String>();

    private void initView() {
        mData.clear();
        mData.add("请选择分类");
        adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, mData);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //修改了字体的颜色，大小最好不要在这里设置了
                TextView tv = (TextView) view;
                tv.setTextColor(getActivity().getResources().getColor(R.color.text_main));
//                tv.setTextSize(12.0f)
                if (position != 0 && mList != null && mList.size() > position - 1) {
                    categoryId = mList.get(position - 1).id;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        RetrofitService.getService(BaseService.class).getCategory().subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                /*回调线程*/
                .observeOn(AndroidSchedulers.mainThread())
                /*结果判断*/
                .subscribe(new Observer<ResData<List<Category>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResData<List<Category>> listResData) {
                        mList = listResData.result;
                        for (Category category : listResData.result) {
                            mData.add(category.name);
                            mSpinner.setAdapter(adapter);
                            mSpinner.setSelection(0,true);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void close(View view) {
        finish();
    }

    @OnClick(R.id.submit)
    public void onViewClicked() {
        final String weibo = content.getText().toString().trim();
        if (TextUtils.isEmpty(weibo)) {
            ToastUtils.showShortToast(getContext(), "请输入内容");
            return;
        }
        if (TextUtils.isEmpty(categoryId)) {
            ToastUtils.showShortToast(getContext(), "请选择分类");
            return;
        }
        user = BaseApplication.getContext().currentUser;
        BaseAppApi.publish(user.getIdstr(), weibo, categoryId,
                new HttpListener<Void>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        ToastUtils.showShortToast(getActivity(), e.getMessage());
                    }

                    @Override
                    public void onSuccess(Void response) {
                        setResult(RESULT_OK);
                        ToastUtils.showShortToast(getActivity(), "发布成功");
                        finish();
                    }
                });
    }
}
