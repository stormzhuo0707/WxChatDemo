package com.example.wxchatdemo;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.wxchatdemo.adapter.findSortAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FindFragment extends Fragment {
    /* 声明组件*/
    private ListView listView;
    //定义一个map集合存放数据
    private List<Map<String,String>> list = new ArrayList<>();
    //准备图片
    private int[] pic = new int[]{
            R.drawable.friend_img,R.drawable.video_img,
            R.drawable.scan_img,R.drawable.shark_img,
            R.drawable.look_img,R.drawable.search_img,
            R.drawable.direct_seeding_img,R.drawable.shopping_img,
            R.drawable.game_img,R.drawable.small_routine_img,
    };
    //准备文字
    private String data[] =new String[]
            {"朋友圈        ","视频号        " ,"扫一扫        ",
                    "摇一摇        ","看一看        " ,"搜一搜        ",
                    "直播和附近","购物            ","游戏            ","小程序        "};
    //准备图片
    private int[] pic1 = new int[]{
            R.drawable.tab_img,R.drawable.tab_img,R.drawable.tab_img,R.drawable.tab_img,
            R.drawable.tab_img,R.drawable.tab_img,R.drawable.tab_img,R.drawable.tab_img,
            R.drawable.tab_img,R.drawable.tab_img
    };
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.find_fragment, container, false);
        /*初始化组件*/
        listView = (ListView) view.findViewById(R.id.listView);
        //初始化数据
        initData();
        /*创建自定义适配器，并设置给listview*/
        findSortAdapter adapter = new findSortAdapter(getActivity().getApplicationContext(), list);
        listView.setAdapter(adapter);
        return view;
    }
    private void initData() {
        for(int i=0;i<data.length;i++){
            Map<String,String> map = new HashMap<>();
            map.put("pic",String.valueOf(pic[i]));
            map.put("title",data[i]);
            map.put("pic1",String.valueOf(pic1[i]));
            list.add(map);//将map放到list集合中
        }
    }
}
