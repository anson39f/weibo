package com.eminem.weibo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.eminem.weibo.R;
import com.eminem.weibo.bean.Category;

import java.util.List;

/**
 */

public class CategoryListAdapter extends BaseAdapter {
    private Context context;
    private List<Category> datas;
    private OnItemClickListener onItemClickListener;

    public CategoryListAdapter(Context context, List<Category> datas) {
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
            convertView = View.inflate(context, R.layout.item_category, null);
            holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Category item = (Category) getItem(position);
        holder.tvName.setText(String.format("#%s", item.name));

        holder.tvName.setOnClickListener(new View.OnClickListener() {
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
        public TextView tvName;
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onClick(int position, Category item);
    }
}
