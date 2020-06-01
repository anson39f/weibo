package com.xds.weibo.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.xds.weibo.BaseActivity;
import com.xds.weibo.R;
import com.xds.weibo.adapter.MessageDetailsAdapter;
import com.xds.weibo.bean.Comment;
import com.xds.weibo.bean.CommentsResponse;
import com.xds.weibo.utils.TitleBuilder;

import java.util.ArrayList;
import java.util.List;

public class MessageDetailsActivity extends BaseActivity {
    private SwipeToLoadLayout swipeToLoadLayout;
    private ListView lvMessage;
    private MessageDetailsAdapter adapter;
    private List<Comment> comments = new ArrayList<>();
    private int curPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_details);
        initView();
        loadData(1);
    }

    private void initView() {
        new TitleBuilder(this)
                .setTitleText("所有微博")
                .setLeftImage(R.drawable.navigationbar_back)
                .setRightText("设置")
                .setLeftOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

//        swipeToLoadLayout = (SwipeToLoadLayout) findViewById(R.id.swipeToLoadLayout);
        lvMessage = (ListView) findViewById(R.id.swipe_target);
        adapter = new MessageDetailsAdapter(this, comments);
        lvMessage.setAdapter(adapter);

//        swipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                loadData(1);
//                swipeToLoadLayout.setRefreshing(false);
//
//            }
//        });
//
//        swipeToLoadLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
//            @Override
//            public void onLoadMore() {
//                loadData(curPage + 1);
//                swipeToLoadLayout.setLoadingMore(false);
//            }
//        });
    }

    private void loadData(final int page) {

    }

    private void addComments(CommentsResponse resBean) {
        for (Comment comment : resBean.getComments()) {
            if (!comments.contains(comment)) {
                comments.add(comment);
            }
        }
        adapter.notifyDataSetChanged();

    }

}
