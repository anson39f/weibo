package com.xds.weibo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.xds.weibo.BaseFragment;
import com.xds.weibo.R;
import com.xds.weibo.activity.CategoryWeiboActivity;
import com.xds.weibo.adapter.CategoryListAdapter;
import com.xds.weibo.api.ResData;
import com.xds.weibo.api.remote.BaseService;
import com.xds.weibo.base.net.RetrofitService;
import com.xds.weibo.bean.Category;
import com.xds.weibo.widget.GlideImageLoader;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class SearchFragment extends BaseFragment {
    List<String> images = Arrays.asList(
            "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1348720847,3936234326&fm=26&gp=0.jpg"
            , "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2663025285,784067331&fm=26&gp=0.jpg"
            , "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2034522661,1346507302&fm=26&gp=0.jpg"
            , "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2497504648,3787109365&fm=26&gp=0.jpg");
    @BindView(R.id.banner)
    Banner banner;
    Unbinder unbinder;
    @BindView(R.id.gridView)
    GridView gridView;

    private View view;
    private CategoryListAdapter adapter;
    private List<Category> categorys = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = View.inflate(activity, R.layout.frag_search, null);
        unbinder = ButterKnife.bind(this, view);
        initView();
        initData();
        return view;
    }

    private void initData() {
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
                        categorys.clear();
                        categorys.addAll(listResData.result);
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
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(images);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
        adapter = new CategoryListAdapter(activity, categorys);
        gridView.setAdapter(adapter);
        adapter.setOnItemClickListener(new CategoryListAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position, Category item) {
                //                ActivityTools.startToNextActivity(getActivity(), CategoryWeiboActivity.class, "id"
                //                        , item.id);
                Intent intent = new Intent(getActivity(), CategoryWeiboActivity.class);
                intent.putExtra("id", item.id);
                intent.putExtra("name", item.name);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
