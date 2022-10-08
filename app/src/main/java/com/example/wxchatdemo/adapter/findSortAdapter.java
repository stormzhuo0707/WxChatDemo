package com.example.wxchatdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wxchatdemo.R;

import java.util.List;
import java.util.Map;

public class findSortAdapter extends BaseAdapter{

    private ViewHolder viewHolder;
    private List<Map<String, String>> data = null;
    private Context mContext;

    public findSortAdapter(Context mContext, List<Map<String, String>> data) {
        this.mContext = mContext;
        this.data = data;
    }

    public int getCount() {
        return this.data.size();
    }

    public Object getItem(int position) {
        return data.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup arg2) {

        if (view == null) {
            viewHolder = new ViewHolder();
            //获取listview对应的item布局
            view = LayoutInflater.from(mContext).inflate(R.layout.find_item, null);
            //初始化组件
            viewHolder.pic = (ImageView) view.findViewById(R.id.pic);
            viewHolder.title = (TextView) view.findViewById(R.id.title);
            viewHolder.pic1 = (ImageView) view.findViewById(R.id.pic1);
            viewHolder.divider = (View) view.findViewById(R.id.divider);
            viewHolder.divider1 = (View) view.findViewById(R.id.divider1);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        if (position == 0 || position == 1 || position == 3 || position == 5 ||
        position == 6 || position == 8) {
            Map<String, String> map = data.get(position);
            viewHolder.pic.setImageResource(Integer.parseInt(map.get("pic")));
            viewHolder.title.setText(map.get("title"));
            viewHolder.pic1.setImageResource(Integer.parseInt(map.get("pic1")));
            viewHolder.divider.setVisibility(View.VISIBLE);
            viewHolder.divider1.setVisibility(View.GONE);
        }else {
            if (position == 9) {
                Map<String, String> map = data.get(position);
                viewHolder.pic.setImageResource(Integer.parseInt(map.get("pic")));
                viewHolder.title.setText(map.get("title"));
                viewHolder.pic1.setImageResource(Integer.parseInt(map.get("pic1")));
                viewHolder.divider.setVisibility(View.GONE);
                viewHolder.divider1.setVisibility(View.VISIBLE);
            }else {
                Map<String, String> map = data.get(position);
                viewHolder.pic.setImageResource(Integer.parseInt(map.get("pic")));
                viewHolder.title.setText(map.get("title"));
                viewHolder.pic1.setImageResource(Integer.parseInt(map.get("pic1")));
                viewHolder.divider.setVisibility(View.GONE);
                viewHolder.divider1.setVisibility(View.GONE);
            }
        }
        return view;
    }

    final static class ViewHolder {
        ImageView pic;
        TextView title;
        ImageView pic1;
        View divider;
        View divider1;
    }
}