package com.xds.weibo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.xds.weibo.BaseApplication;
import com.xds.weibo.BaseFragment;
import com.xds.weibo.R;
import com.xds.weibo.activity.MyAttentionActivity;
import com.xds.weibo.activity.MyFansActivity;
import com.xds.weibo.activity.NewUserInfoActivity;
import com.xds.weibo.activity.SettingActivity;
import com.xds.weibo.activity.UserInfoEditActivity;
import com.xds.weibo.adapter.UserItemAdapter;
import com.xds.weibo.api.remote.BaseAppApi;
import com.xds.weibo.base.net.ApiException;
import com.xds.weibo.base.net.HttpListener;
import com.xds.weibo.base.utils.ActivityTools;
import com.xds.weibo.bean.User;
import com.xds.weibo.bean.UserItem;
import com.xds.weibo.utils.TitleBuilder;
import com.xds.weibo.utils.ToastUtils;
import com.xds.weibo.widget.WrapHeightListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static android.app.Activity.RESULT_OK;
import static com.bumptech.glide.request.RequestOptions.bitmapTransform;


public class UserFragment extends BaseFragment {

    private LinearLayout ll_userinfo;

    private ImageView iv_avatar;
    private TextView tv_subhead;
    private TextView tv_caption;

    private TextView tv_status_count;
    private TextView tv_follow_count;
    private TextView tv_fans_count;

    private WrapHeightListView lv_user_items;

    private User user;

    Unbinder unbinder;
    private View view;
    private List<UserItem> userItems;
    private UserItemAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = View.inflate(activity, R.layout.frag_user, null);
        initView();
        setItem();

        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        //        Oauth2AccessToken mAccessToken = AccessTokenKeeper.readAccessToken(activity);
        //        String token = mAccessToken.getToken();
        //        String uid = mAccessToken.getUid();
        //        RequestParams params = new RequestParams();
        //        params.put("access_token", token);
        //        params.put("uid", uid);
        if (!hidden) {
            getUser();
        }
    }

    private void getUser() {
        user = ((BaseApplication) activity.getApplication()).currentUser;
        if (user == null) {
            return;
        }
        BaseAppApi.getUser(user.getIdstr(),
                new HttpListener<User>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        ToastUtils.showShortToast(getContext(), e.getMessage());
                    }

                    @Override
                    public void onSuccess(User response) {
                        BaseApplication.getContext().currentUser = response;
                        setUserInfo();
                    }
                });
    }

    private void initView() {
        new TitleBuilder(view)
                .setTitleText("我")
//                .setLeftText("添加好友")
                .setRightText("设置")
                .setRightOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        intent2Activity(SettingActivity.class);

                    }
                })
                .setLeftOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtils.showToast(getActivity(), "添加好友", Toast.LENGTH_LONG);

                    }
                });
        tv_status_count = (TextView) view.findViewById(R.id.tv_status_count);
        tv_follow_count = (TextView) view.findViewById(R.id.tv_follow_count);
        tv_fans_count = (TextView) view.findViewById(R.id.tv_fans_count);

        iv_avatar = (ImageView) view.findViewById(R.id.iv_avatar);
        tv_subhead = (TextView) view.findViewById(R.id.tv_subhead);
        tv_caption = (TextView) view.findViewById(R.id.tv_caption);

        lv_user_items = (WrapHeightListView) view.findViewById(R.id.lv_user_items);
        userItems = new ArrayList<>();
        adapter = new UserItemAdapter(activity, userItems);
        lv_user_items.setAdapter(adapter);
        adapter.setOnItemClickListener(new UserItemAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position, UserItem item) {
                if (item.getSubhead().equals("编辑资料")) {
                    ActivityTools.startToNextActivityForResult(UserFragment.this, UserInfoEditActivity.class, 1);
                } else if (item.getSubhead().equals("我的关注")) {
                    ActivityTools.startToNextActivityForResult(UserFragment.this,
                            MyAttentionActivity.class, 2);
                } else if (item.getSubhead().equals("我的粉丝")) {
                    ActivityTools.startToNextActivityForResult(UserFragment.this,
                            MyFansActivity.class, 3);
                }
            }
        });
    }

    private void setItem() {
        userItems.add(new UserItem(false, R.drawable.push_icon_app_small_1, "我的关注", ""));
        userItems.add(new UserItem(false, R.drawable.push_icon_app_small_2, "我的粉丝", ""));
        userItems.add(new UserItem(false, R.drawable.push_icon_app_small_3, "编辑资料", ""));
        adapter.notifyDataSetChanged();
    }

    private void setUserInfo() {
        user = ((BaseApplication) activity.getApplication()).currentUser;
        if (user == null) {
            return;
        }
        // set data
        tv_subhead.setText(user.getName());
        tv_caption.setText("邮箱:" + user.getDescription());
        Glide.with(activity).load(user.getAvatar_hd()).apply(bitmapTransform(new CropCircleTransformation())).into(iv_avatar);
        tv_status_count.setText("" + user.getStatuses_count());
        tv_follow_count.setText("" + user.getFriends_count());
        tv_fans_count.setText("" + user.getFollowers_count());

        iv_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String screen_name = user.getScreen_name();
                Intent intent = new Intent(activity, NewUserInfoActivity.class);
                intent.putExtra("screen_name", screen_name);
                startActivity(intent);
            }
        });
    }

    @OnClick({R.id.ll_userinfo, R.id.iv_avatar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_userinfo:
            case R.id.iv_avatar:
                String screen_name = user.getScreen_name();
                Intent intent = new Intent(activity, NewUserInfoActivity.class);
                intent.putExtra("screen_name", screen_name);
                startActivity(intent);
                break;

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            getUser();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
