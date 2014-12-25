package com.andy.music.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.andy.music.R;
import com.andy.music.entity.Music;
import com.andy.music.utility.MusicLocator;

import java.util.List;

/**
 * 音乐列表适配器
 * Adapter的作用就是ListView界面与数据之间的桥梁，当列表里的每一项显示到页面时，
 * 都会调用Adapter的getView方法返回一个View。
 * <p/>
 * 优化的思路两种 :
 * 1. View的重用
 * View的每次创建是比较耗时的，因此对于getview方法传入的convertView应充分利用 != null的判断
 * <p/>
 * 2.ViewHolder的应用
 * View的findViewById()方法也是比较耗时的，因此需要考虑只调用一次，之后就用View.getTag()方法来获得ViewHolder对象。
 * <p/>
 * Created by Andy on 2014/11/15.
 */
public class MusicListAdapter extends BaseAdapter {

    private List<Music> musicList;
    private LayoutInflater inflater;
    private Context context;
    private int resource;

    public MusicListAdapter(Context context, List<Music> musicList, int resource) {
        this.context = context;
        this.musicList = musicList;
        this.resource = resource;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (musicList == null) return 0;
        return musicList.size();
    }

    @Override
    public Object getItem(int position) {
        if (musicList == null) return null;
        return musicList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        /**
         * 优化方法。
         *
         * 优点 : 该方法会回收 convertView (每个 Item 的视图) ，从而达到循环利用的目的
         *
         */

        ViewHolder holder;
        if (convertView == null) {

            // 获取控件对象
            holder = new ViewHolder();
            convertView = inflater.inflate(resource, null);
            holder.cell = (LinearLayout) convertView.findViewById(R.id.ll_music_list_cell);
            holder.locBar = convertView.findViewById(R.id.v_locator_bar);
            holder.number = (TextView) convertView.findViewById(R.id.tv_music_number);
            holder.name = (TextView) convertView.findViewById(R.id.tv_music_name);
            holder.singer = (TextView) convertView.findViewById(R.id.tv_music_singer);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // 当前歌曲信息
        Music music = musicList.get(position);
        String musicName = null;
        String musicSinger = null;
        if (music != null) {
            musicName = music.getName();
            musicSinger = music.getSinger();
        }

        // 为列表项中的变量赋值
        holder.number.setText(position + 1 + "");
        holder.name.setText(musicName);
        holder.singer.setText(musicSinger);

        // 设置歌曲样式
        holder.cell.setBackgroundColor(Color.parseColor("#00000000"));
        holder.locBar.setBackgroundColor(Color.parseColor("#00000000"));
        holder.name.setTextColor(Color.parseColor("#cc000000"));
        holder.singer.setTextColor(Color.parseColor("#78000000"));
        holder.number.setTextColor(Color.parseColor("#78000000"));

//        holder.cell.setBackgroundColor(Color.parseColor("#00000000"));
//        holder.locBar.setBackgroundColor(Color.parseColor("#00000000"));
//        holder.name.setTextColor(Color.parseColor("#ccffffff"));
//        holder.singer.setTextColor(Color.parseColor("#78ffffff"));
//        holder.number.setTextColor(Color.parseColor("#78ffffff"));

        // 设置当前歌曲样式
        if (music != null && music.equals(MusicLocator.getCurrentMusic())) {
            holder.cell.setBackgroundColor(Color.parseColor("#c4d9c6"));
            holder.locBar.setBackgroundColor(Color.parseColor("#729939"));
            holder.name.setTextColor(Color.parseColor("#729939"));
            holder.singer.setTextColor(Color.parseColor("#729939"));
            holder.number.setTextColor(Color.parseColor("#729939"));

//            holder.cell.setBackgroundColor(Color.parseColor("#34000000"));
//            holder.locBar.setBackgroundColor(Color.parseColor("#ec505e"));
//            holder.name.setTextColor(Color.parseColor("#ec505e"));
//            holder.singer.setTextColor(Color.parseColor("#ec505e"));
//            holder.number.setTextColor(Color.parseColor("#ec505e"));
        }

        return convertView;

//        不可取方法
//        convertView = inflater.inflate(R.layout.music_list_cell, null);
//
//        TextView number = (TextView) convertView.findViewById(R.id.tv_music_number);
//        TextView name =(TextView) convertView.findViewById(R.id.tv_music_name);
//        TextView singer =(TextView) convertView.findViewById(R.id.tv_music_singer);
//        ImageButton addToFavor = (ImageButton) convertView.findViewById(R.id.ib_add_to_favorite);
//
//        number.setText(position+1+"");
//        name.setText(musicList.get(position).getName());
//        singer.setText(musicList.get(position).getSinger());
//        addToFavor.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // 添加到我的最爱
//                /// 获得我的最爱列表
//                MusicList favorMusicList = MusicList.getInstance(context, MusicList.MUSIC_LIST_FAVORITE);
//                favorMusicList.add(new Music(musicList.get(position).getId()));
//                Toast.makeText(context, "已添加到我的最爱", Toast.LENGTH_SHORT).show();
//            }
//        });
//        return convertView;
    }


    private static final class ViewHolder {
        LinearLayout cell;
        TextView number, name, singer;
        View locBar;
        ToggleButton itemMenu;
    }

}
