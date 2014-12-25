package com.andy.music.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.andy.music.R;
import com.andy.music.data.CursorAdapter;
import com.andy.music.entity.MusicList;
import com.andy.music.entity.TagConstants;
import com.andy.music.function.MusicPlayService;
import com.andy.music.view.SettingActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 侧滑菜单模块
 * Created by Andy on 2014/11/29.
 */
public class SlideMenuFragment extends Fragment implements AdapterView.OnItemClickListener {

    private ListView menuListView;
    private List<Map<String, ?>> menuList;
    private ListAdapter adapter;

    /**
     * 上次保存的播放模式
     */
    private String playSchema;

    private static final String[] menuItems = {"扫描歌曲", "列表循环", "更换背景", "睡眠", "设置", "退出"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 初始化变量
        menuListView = (ListView) view.findViewById(R.id.lv_menu_list);
        menuList = new ArrayList<Map<String, ?>>();

        // 初始化菜单
        String[] from = {"icon", "item"};
        int[] to = {R.id.iv_menu_item_icon, R.id.tv_menu_item};
        adapter = new SimpleAdapter(getActivity(), getData(), R.layout.menu_list_cell, from, to);
        menuListView.setAdapter(adapter);

        // 设置监听事件
        menuListView.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                // 扫描歌曲
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // 由于在数据库中建立列表的的时间比较长，故单独放在一个线程中
                        // 获得查询游标
                        Cursor searchCursor = CursorAdapter.get(null);

                        // 将游标中的数据存到数据库
                        MusicList.getInstance(MusicList.MUSIC_LIST_LOCAL).setList(searchCursor);

                        // 关闭 Cursor，释放资源
                        searchCursor.close();
                    }
                }).start();
                Toast.makeText(getActivity(), "扫描完成！", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                // 播放方式
                setPlaySchema(view);
                break;
            case 2:
                // 更换背景
                Toast.makeText(getActivity(), "更换背景", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                // 睡眠
                new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Log.d(TagConstants.TAG, "hourOfDay-->"+hourOfDay+";   minute-->"+minute);
                        long min = hourOfDay*60+minute;
                        Toast.makeText(getActivity(), "将在"+min+"分钟后退出！", Toast.LENGTH_SHORT).show();
                        exitAtTime(min * 60000);
                    }
                }, 0, 30, true).show();
//                final EditText timeToExit = new EditText(getActivity());
//                new AlertDialog.Builder(getActivity())
//                        .setTitle("请输入睡眠时间(分)")
//                        .setView(timeToExit)
//                        .setNegativeButton("取消",null)
//                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                String time = timeToExit.getText().toString();
//                                if (time!=null && !time.equals("") && !time.equals("0")) {
//                                    exitAtTime(Integer.valueOf(time));
//                                    Toast.makeText(getActivity(), "将在"+time+"分钟后退出", Toast.LENGTH_SHORT).show();
//                                } else {
//                                    Toast.makeText(getActivity(), "输入有误！", Toast.LENGTH_SHORT).show();
//                                }
//
//                            }
//                        })
//                        .show();
                break;
            case 4:
                // 设置
                Toast.makeText(getActivity(), "设置", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), SettingActivity.class));
                // TODO 关闭菜单
                break;
            case 5:
                // 退出
                Toast.makeText(getActivity(), "退出", Toast.LENGTH_SHORT).show();
                System.exit(0);
                break;
            default:
                break;
        }
    }

    /**
     * 在设定时间后退出
     * @param time  距离退出的时间
     */
    private void exitAtTime(long time) {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                System.exit(0);
            }
        };
        if (time>0) {
            new Timer().schedule(task, time);
        } else {
            Log.d(TagConstants.TAG, "时间小于0");
        }
    }

    private void setPlaySchema(View view) {
        SharedPreferences pref = getActivity().getSharedPreferences("play_setting", Context.MODE_PRIVATE);
        int lastSchema = pref.getInt("play_schema", MusicPlayService.MUSIC_PLAY_SCHEMA_ORDER);
        int currentSchema = 0;

        switch (lastSchema) {
            case MusicPlayService.MUSIC_PLAY_SCHEMA_ORDER:
                playSchema = "随机播放";
                currentSchema = MusicPlayService.MUSIC_PLAY_SCHEMA_RANDOM;
                break;
            case MusicPlayService.MUSIC_PLAY_SCHEMA_RANDOM:
                playSchema = "循环播放";
                currentSchema = MusicPlayService.MUSIC_PLAY_SCHEMA_LIST_CIRCULATE;
                break;
            case MusicPlayService.MUSIC_PLAY_SCHEMA_LIST_CIRCULATE:
                playSchema = "单曲循环";
                currentSchema = MusicPlayService.MUSIC_PLAY_SCHEMA_SINGLE_CIRCULATE;
                break;
            case MusicPlayService.MUSIC_PLAY_SCHEMA_SINGLE_CIRCULATE:
                playSchema = "顺序播放";
                currentSchema = MusicPlayService.MUSIC_PLAY_SCHEMA_ORDER;
                break;
            default: break;
        }
        ((TextView) view.findViewById(R.id.tv_menu_item)).setText(playSchema);
        SharedPreferences.Editor  editor = pref.edit();
        editor.putInt("play_schema", currentSchema);
        editor.commit();
    }

    private List<Map<String, ?>> getData() {

        Map<String, Object> map1 = new HashMap<String, Object>();
        Map<String, Object> map2 = new HashMap<String, Object>();
        Map<String, Object> map3 = new HashMap<String, Object>();
        Map<String, Object> map4 = new HashMap<String, Object>();
        Map<String, Object> map5 = new HashMap<String, Object>();
        Map<String, Object> map6 = new HashMap<String, Object>();

        map1.put("item", menuItems[0]);
        map1.put("icon", R.drawable.ic_launcher);

        map2.put("item", getPlaySchema());
        map2.put("icon", R.drawable.ic_launcher);

        map3.put("item", menuItems[2]);
        map3.put("icon", R.drawable.ic_launcher);

        map4.put("item", menuItems[3]);
        map4.put("icon", R.drawable.ic_launcher);

        map5.put("item", menuItems[4]);
        map5.put("icon", R.drawable.ic_launcher);

        map6.put("item", menuItems[5]);
        map6.put("icon", R.drawable.ic_launcher);

        menuList.add(map1);
        menuList.add(map2);
        menuList.add(map3);
        menuList.add(map4);
        menuList.add(map5);
        menuList.add(map6);
        return menuList;
    }

    private String getPlaySchema() {
        SharedPreferences pref = getActivity().getSharedPreferences("play_setting", Context.MODE_PRIVATE);
        int schema = pref.getInt("play_schema", MusicPlayService.MUSIC_PLAY_SCHEMA_ORDER);
        String playSchema = null;
        switch (schema) {
            case MusicPlayService.MUSIC_PLAY_SCHEMA_ORDER:
                playSchema = "顺序播放";
                break;
            case MusicPlayService.MUSIC_PLAY_SCHEMA_RANDOM:
                playSchema = "随机播放";
                break;
            case MusicPlayService.MUSIC_PLAY_SCHEMA_LIST_CIRCULATE:
                playSchema = "循环播放";
                break;
            case MusicPlayService.MUSIC_PLAY_SCHEMA_SINGLE_CIRCULATE:
                playSchema = "单曲循环";
                break;
            default: break;
        }
        return playSchema;
    }


}
