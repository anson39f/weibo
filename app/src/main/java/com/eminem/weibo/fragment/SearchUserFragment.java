package com.eminem.weibo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.eminem.weibo.BaseFragment;
import com.eminem.weibo.R;
import com.eminem.weibo.adapter.UserListAdapter;
import com.eminem.weibo.api.ResData;
import com.eminem.weibo.api.remote.BaseService;
import com.eminem.weibo.base.net.RetrofitService;
import com.eminem.weibo.bean.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class SearchUserFragment extends BaseFragment {
    private SwipeToLoadLayout swipeToLoadLayout;
    private ListView lvHome;
    private View view;
    private UserListAdapter adapter;
    private List<User> users = new ArrayList<>();
    private String key = "";

    public static SearchUserFragment newInstance(String key) {
        SearchUserFragment fragment = new SearchUserFragment();
        Bundle bundle = new Bundle();
        bundle.putString("key", key);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initView();
        initData(key);
        ButterKnife.bind(this, view);
        return view;
    }

    private void initView() {
        view = View.inflate(activity, R.layout.frag_search_weibo, null);
        swipeToLoadLayout = (SwipeToLoadLayout) view.findViewById(R.id.swipeToLoadLayout);
        lvHome = (ListView) view.findViewById(R.id.swipe_target);


        adapter = new UserListAdapter(activity, users);
        lvHome.setAdapter(adapter);
        swipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData("");
                swipeToLoadLayout.setRefreshing(false);

            }
        });
        swipeToLoadLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                initData(key);
                swipeToLoadLayout.setLoadingMore(false);
            }
        });

    }

    private void initData(String key) {
        RetrofitService.getService(BaseService.class).searchUser(key).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                /*回调线程*/
                .observeOn(AndroidSchedulers.mainThread())
                /*结果判断*/
                .subscribe(new Observer<ResData<List<User>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResData<List<User>> listResData) {
                        users.clear();
                        users.addAll(listResData.result);
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

    public void searchKey(String newText) {
        initData(newText);
    }
}
