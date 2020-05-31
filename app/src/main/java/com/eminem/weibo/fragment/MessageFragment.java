package com.eminem.weibo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.eminem.weibo.BaseApplication;
import com.eminem.weibo.BaseFragment;
import com.eminem.weibo.R;
import com.eminem.weibo.adapter.MessagesListAdapter;
import com.eminem.weibo.api.ResData;
import com.eminem.weibo.api.remote.BaseService;
import com.eminem.weibo.base.net.RetrofitService;
import com.eminem.weibo.bean.Message;
import com.eminem.weibo.utils.TitleBuilder;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class MessageFragment extends BaseFragment {
    private View view;
    private LinearLayout ll_message_at;
    private LinearLayout ll_message_comment;
    private LinearLayout ll_message_like;
    private SwipeToLoadLayout swipeToLoadLayout;
    private ListView lvHome;
    private MessagesListAdapter adapter;
    private List<Message> messages = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initView();
        initData();
        return view;
    }

    private void initView() {
        view = View.inflate(activity, R.layout.frag_message, null);

        swipeToLoadLayout = (SwipeToLoadLayout) view.findViewById(R.id.swipeToLoadLayout);
        lvHome = (ListView) view.findViewById(R.id.swipe_target);
        new TitleBuilder(view)
                .setTitleText("消息")
                //                .setLeftText("发现群")
                //                .setRightImage(R.drawable.title_message_icon)
                .setRightOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //                        ToastUtils.showToast(activity, "click", Toast.LENGTH_SHORT);
                    }
                })
                .setLeftOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //                        ToastUtils.showToast(activity, "发现群", Toast.LENGTH_SHORT);
                    }
                });
        adapter = new MessagesListAdapter(activity, messages);
        lvHome.setAdapter(adapter);
        swipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
                swipeToLoadLayout.setRefreshing(false);

            }
        });
        swipeToLoadLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                initData();
                swipeToLoadLayout.setLoadingMore(false);
            }
        });
    }

    private void initData() {
        RetrofitService.getService(BaseService.class).message(BaseApplication.getContext().currentUser.getIdstr()).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                /*回调线程*/
                .observeOn(AndroidSchedulers.mainThread())
                /*结果判断*/
                .subscribe(new Observer<ResData<List<Message>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResData<List<Message>> listResData) {
                        messages.clear();
                        messages.addAll(listResData.result);
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

}
