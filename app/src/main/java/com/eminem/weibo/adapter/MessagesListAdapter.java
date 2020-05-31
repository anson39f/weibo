package com.eminem.weibo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eminem.weibo.R;
import com.eminem.weibo.bean.Message;
import com.eminem.weibo.widget.ImageLoader;

import java.util.List;

/**
 */

public class MessagesListAdapter extends BaseAdapter {
    private Context context;
    private List<Message> datas;
    private OnItemClickListener onItemClickListener;

    public MessagesListAdapter(Context context, List<Message> datas) {
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
            convertView = View.inflate(context, R.layout.item_message_list, null);
            holder.v_divider = convertView.findViewById(R.id.v_divider);
            holder.ll_content = convertView.findViewById(R.id.ll_content);
            holder.iv_head = (ImageView) convertView.findViewById(R.id.iv_head);
            holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            holder.tvMessage = (TextView) convertView.findViewById(R.id.tvMessage);
            holder.tvTime = (TextView) convertView.findViewById(R.id.tvTime);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Message item = (Message) getItem(position);

        //        holder.iv_head.setImageResource(item.getAvatar_hd());
        ImageLoader.loadImage(Glide.with(context), holder.iv_head, item.user.getAvatar_hd());
        holder.tvName.setText(item.user.getName());
        holder.tvMessage.setText(item.content);
        holder.tvTime.setText(item.createTime);

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


        return convertView;
    }

    public static class ViewHolder {
        public View v_divider;
        public View ll_content;
        public ImageView iv_head;
        public TextView tvName;
        public TextView tvMessage;
        public TextView tvTime;
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onClick(int position, Message item);
    }
}
