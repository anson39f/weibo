package com.xds.weibo.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.view.View;

import com.xds.weibo.BaseActivity;
import com.xds.weibo.R;
import com.xds.weibo.adapter.UserInfoAdapter;
import com.xds.weibo.fragment.SearchUserFragment;
import com.xds.weibo.fragment.SearchWeiboFragment;
import com.xds.weibo.utils.TitleBuilder;
import com.sina.weibo.sdk.utils.LogUtil;

import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends BaseActivity {

    @BindView(R.id.searchView)
    SearchView mSearchView;
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    private SearchWeiboFragment searchWeiboFragment;
    private SearchUserFragment searchUserFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        initView();
        loadData();
    }

    private void initView() {
        new TitleBuilder(this)
                .setTitleText("搜索")
                .setLeftImage(R.drawable.navigationbar_back)
                .setLeftOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

        searchWeiboFragment = SearchWeiboFragment.newInstance("");
        searchUserFragment = SearchUserFragment.newInstance("");
        //设置ViewPager
        setupViewPager(viewpager);
        //设置tablayout，viewpager上的标题
        tabLayout.setupWithViewPager(viewpager);
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.orange));

        /*------------------ SearchView有三种默认展开搜索框的设置方式，区别如下： ------------------*/
        //设置搜索框直接展开显示。左侧有放大镜(在搜索框中) 右侧有叉叉 可以关闭搜索框
        mSearchView.setIconified(false);
        //设置搜索框直接展开显示。左侧有放大镜(在搜索框外) 右侧无叉叉 有输入内容后有叉叉 不能关闭搜索框
        mSearchView.setIconifiedByDefault(false);
        //设置搜索框直接展开显示。左侧有无放大镜(在搜索框中) 右侧无叉叉 有输入内容后有叉叉 不能关闭搜索框
        mSearchView.onActionViewExpanded();
    }

    private void setupViewPager(ViewPager viewPager) {
        UserInfoAdapter adapter = new UserInfoAdapter(getSupportFragmentManager());
        adapter.addFragment(searchWeiboFragment, "微博");
        adapter.addFragment(searchUserFragment, "用户");
        viewPager.setAdapter(adapter);
    }

    private void loadData() {

        mSearchView.setIconifiedByDefault(false);//设置搜索图标是否显示在搜索框内
        //1:回车
        //2:前往
        //3:搜索
        //4:发送
        //5:下一項
        //6:完成
        mSearchView.setImeOptions(2);//设置输入法搜索选项字段，默认是搜索，可以是：下一页、发送、完成等
        //        mSearchView.setInputType(1);//设置输入类型
        //        mSearchView.setMaxWidth(200);//设置最大宽度
        mSearchView.setQueryHint("搜索微博或者账户");//设置查询提示字符串
        //        mSearchView.setSubmitButtonEnabled(true);//设置是否显示搜索框展开时的提交按钮
        //设置SearchView下划线透明
        setUnderLinetransparent(mSearchView);
        setListener();
    }

    private void setListener() {

        // 设置搜索文本监听
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                LogUtil.d("SearchActivity", "=====query=" + query);
                return false;
            }

            //当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                LogUtil.d("SearchActivity", "=====newText=" + newText);
                searchWeiboFragment.searchKey(newText);
                searchUserFragment.searchKey(newText);
                return false;
            }
        });
    }

    /**
     * 设置SearchView下划线透明
     **/
    private void setUnderLinetransparent(SearchView searchView) {
        try {
            Class<?> argClass = searchView.getClass();
            // mSearchPlate是SearchView父布局的名字
            Field ownField = argClass.getDeclaredField("mSearchPlate");
            ownField.setAccessible(true);
            View mView = (View) ownField.get(searchView);
            mView.setBackgroundColor(Color.TRANSPARENT);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
