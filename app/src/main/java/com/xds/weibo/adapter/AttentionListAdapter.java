package com.xds.weibo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.xds.weibo.BaseApplication;
import com.xds.weibo.R;
import com.xds.weibo.api.remote.BaseAppApi;
import com.xds.weibo.base.net.ApiException;
import com.xds.weibo.base.net.HttpListener;
import com.xds.weibo.bean.User;
import com.xds.weibo.utils.ToastUtils;
import com.xds.weibo.widget.ImageLoader;

import java.util.List;

/**
 */

public class AttentionListAdapter extends BaseAdapter {
    private Context context;
    private List<User> datas;
    private OnItemClickListener onItemClickListener;

    public AttentionListAdapter(Context context, List<User> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_attention_user_list, null);
            holder.v_divider = convertView.findViewById(R.id.v_divider);
            holder.ll_content = convertView.findViewById(R.id.ll_content);
            holder.iv_head = (ImageView) convertView.findViewById(R.id.iv_head);
            holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            holder.tvAttention = (TextView) convertView.findViewById(R.id.tvAttention);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final User item = (User) getItem(position);

        //        holder.iv_head.setImageResource(item.getAvatar_hd());
        ImageLoader.loadImage(Glide.with(context), holder.iv_head, item.getAvatar_hd());
        holder.tvName.setText(item.getName());

        //        holder.v_divider.setVisibility(item.isShowTopDivider() ? View.VISIBLE : View.GONE);
        holder.ll_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onClick(position, item);
                }
                //                ToastUtils.showToast(context, "item click position = " + position, Toast.LENGTH_SHORT);
            }
        });
        holder.tvAttention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseAppApi.unAttention(BaseApplication.getContext().currentUser.getIdstr(),
                        item.getIdstr(), new HttpListener<Void>() {
                            @Override
                            public void onSuccess(Void response) {
                                ToastUtils.showToast(context, "取消关注成功", Toast.LENGTH_SHORT);

                            }

                            @Override
                            public void onError(ApiException e) {
                                super.onError(e);
                                ToastUtils.showToast(context, e.getMessage(), Toast.LENGTH_SHORT);

                            }
                        });
                //                ToastUtils.showToast(context, "item click position = " + position, Toast.LENGTH_SHORT);
            }
        });


        return convertView;
    }

    public static class ViewHolder {
        public View v_divider;
        public View ll_content;
        public ImageView iv_head;
        public TextView tvName;
        public TextView tvAttention;
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onClick(int position, User item);
    }
}
