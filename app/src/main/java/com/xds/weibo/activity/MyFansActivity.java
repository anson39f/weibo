package com.xds.weibo.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.xds.weibo.BaseActivity;
import com.xds.weibo.BaseApplication;
import com.xds.weibo.R;
import com.xds.weibo.adapter.UserListAdapter;
import com.xds.weibo.api.ResData;
import com.xds.weibo.api.remote.BaseService;
import com.xds.weibo.base.net.RetrofitService;
import com.xds.weibo.bean.Attention;
import com.xds.weibo.bean.User;
import com.xds.weibo.utils.TitleBuilder;
import com.xds.weibo.widget.LoadMoreFooterView;
import com.xds.weibo.widget.RefreshHeaderView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @TODO
 * @date 2020/5/31.
 * @email
 */
public class MyFansActivity extends BaseActivity {

    @BindView(R.id.swipe_refresh_header)
    RefreshHeaderView swipeRefreshHeader;
    @BindView(R.id.swipe_target)
    ListView listView;
    @BindView(R.id.swipe_load_more_footer)
    LoadMoreFooterView swipeLoadMoreFooter;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout swipeToLoadLayout;
    private UserListAdapter adapter;
    private List<User> users = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_attention);
        ButterKnife.bind(this);
        new TitleBuilder(this)
                .setTitleText("我的粉丝")
                .setLeftImage(R.drawable.navigationbar_back)
                .setLeftOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MyFansActivity.this.finish();
                    }
                });
        initView();
        initData();
    }

    private void initData() {
        RetrofitService.getService(BaseService.class).getFan(BaseApplication.getContext().currentUser.getIdstr()).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                /*回调线程*/
                .observeOn(AndroidSchedulers.mainThread())
                /*结果判断*/
                .subscribe(new Observer<ResData<List<Attention>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResData<List<Attention>> listResData) {
                        users.clear();
                        for (Attention user : listResData.result) {
                            users.add(user.followedUser);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void initView() {
        adapter = new UserListAdapter(this, users);
        listView.setAdapter(adapter);
        swipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeToLoadLayout.setRefreshing(false);

            }
        });

        swipeToLoadLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                swipeToLoadLayout.setLoadingMore(false);
            }
        });


    }
}
