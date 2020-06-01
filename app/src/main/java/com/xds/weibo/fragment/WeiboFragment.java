package com.xds.weibo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RadioButton;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.xds.weibo.BaseApplication;
import com.xds.weibo.BaseFragment;
import com.xds.weibo.R;
import com.xds.weibo.activity.SearchActivity;
import com.xds.weibo.adapter.StatusAdapter;
import com.xds.weibo.api.ResData;
import com.xds.weibo.api.remote.BaseService;
import com.xds.weibo.base.net.RetrofitService;
import com.xds.weibo.base.utils.ActivityTools;
import com.xds.weibo.bean.Status;
import com.xds.weibo.utils.TitleBuilder;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class WeiboFragment extends BaseFragment {
    private SwipeToLoadLayout swipeToLoadLayout;
    private ListView lvHome;
    private View view;
    private StatusAdapter adapter;
    private List<Status> statuses = new ArrayList<>();
    private int curPage = 1;
    private GestureDetector gestureDetector;
    private View view_search;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initView();
        initData(1);
        ButterKnife.bind(this, view);
        return view;
    }

    private void initView() {
        view_search = View.inflate(getContext(), R.layout.include_searchview, null);
        view = View.inflate(activity, R.layout.frag_weibo, null);
        swipeToLoadLayout = (SwipeToLoadLayout) view.findViewById(R.id.swipeToLoadLayout);
        lvHome = (ListView) view.findViewById(R.id.swipe_target);
        new TitleBuilder(view)
                //                .setLeftImage(R.drawable.title_camre)
                //                .setRightImage(R.drawable.title_sacn)
                .setTitleText("首页")
                .setRightOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //                        ToastUtils.showToast(activity, "扫一扫", Toast.LENGTH_LONG);
                    }
                })
                .setLeftOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //                        ToastUtils.showToast(activity, "相机", Toast.LENGTH_SHORT);
                    }
                });


        lvHome.addHeaderView(view_search);
        adapter = new StatusAdapter(activity, statuses);
        lvHome.setAdapter(adapter);
        swipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData(1);
                swipeToLoadLayout.setRefreshing(false);

            }
        });
        view_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityTools.startToNextActivity(getActivity(), SearchActivity.class);
            }
        });
        swipeToLoadLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                initData(curPage + 1);
                swipeToLoadLayout.setLoadingMore(false);
            }
        });

        //双击事件处理
        RadioButton radioButton = (RadioButton) getActivity().findViewById(R.id.rb_home);
        radioButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return gestureDetector.onTouchEvent(event);
            }
        });
        gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                //                ToastUtils.showToast(getActivity(), "双击事件", Toast.LENGTH_LONG);
                lvHome.setSelection(0);
                swipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        initData(1);
                        swipeToLoadLayout.setRefreshing(false);

                    }
                });
                return super.onDoubleTap(e);
            }
        });


    }

    private void initData(final int page) {
        if (BaseApplication.getContext().currentUser == null) {
            return;
        }
        RetrofitService.getService(BaseService.class).followedUserNewWeibo(BaseApplication.getContext().currentUser.getIdstr()).subscribeOn(Schedulers.io())
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

}
