package com.andy.music.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.andy.music.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 导航面板适配器
 * Created by Andy on 2015/4/30.
 */
public class PanelViewAdapter extends BaseAdapter {

    private Context context;
    private List<HashMap<String, Object>> data;
    private int resource;

    public PanelViewAdapter(Context context, List<HashMap<String, Object>> data, int resource) {
        this.context = context;
        this.data = data;
        this.resource = resource;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(resource, null);
            holder.icon = (ImageView) convertView.findViewById(R.id.iv_nav_panel_icon);
            holder.title = (TextView) convertView.findViewById(R.id.tv_nav_panel_title);
            convertView.setTag(holder);
        }
        Map<String, Object> map = data.get(position);
        if (map != null) {
            int icon = (int) map.get("icon");
            String title = (String) map.get("title");
            holder.icon.setImageResource(icon);
            holder.title.setText(title);
        }
        return convertView;
    }

    private static final class ViewHolder {
        ImageView icon;
        TextView title;
    }


}
