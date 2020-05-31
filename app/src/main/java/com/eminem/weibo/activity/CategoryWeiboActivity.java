package com.eminem.weibo.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.eminem.weibo.BaseActivity;
import com.eminem.weibo.R;
import com.eminem.weibo.adapter.StatusAdapter;
import com.eminem.weibo.api.ResData;
import com.eminem.weibo.api.remote.BaseService;
import com.eminem.weibo.base.net.RetrofitService;
import com.eminem.weibo.bean.Status;
import com.eminem.weibo.utils.TitleBuilder;
import com.eminem.weibo.widget.LoadMoreFooterView;
import com.eminem.weibo.widget.RefreshHeaderView;

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
public class CategoryWeiboActivity extends BaseActivity {

    @BindView(R.id.swipe_refresh_header)
    RefreshHeaderView swipeRefreshHeader;
    @BindView(R.id.swipe_target)
    ListView listView;
    @BindView(R.id.swipe_load_more_footer)
    LoadMoreFooterView swipeLoadMoreFooter;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout swipeToLoadLayout;
    private StatusAdapter adapter;
    private List<Status> statuses = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_attention);
        String name = getIntent().getStringExtra("name");
        ButterKnife.bind(this);
        new TitleBuilder(this)
                .setTitleText(name + "类的微博")
                .setLeftImage(R.drawable.navigationbar_back)
                .setLeftOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CategoryWeiboActivity.this.finish();
                    }
                });
        initView();
        initData();
    }

    private void initData() {
        String id = getIntent().getStringExtra("id");
        RetrofitService.getService(BaseService.class).categoryNewWeibo(id).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                /*回调线程*/
                .observeOn(AndroidSchedulers.mainThread())
                /*结果判断*/
                .subscribe(new Observer<ResData<List<Status>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResData<List<Status>> listResData) {
                        statuses.clear();
                        statuses.addAll(listResData.result);
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
        adapter = new StatusAdapter(this, statuses);
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
