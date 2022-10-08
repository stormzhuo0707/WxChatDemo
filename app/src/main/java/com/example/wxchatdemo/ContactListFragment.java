package com.example.wxchatdemo;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.wxchatdemo.adapter.SortAdapter;
import com.example.wxchatdemo.tools.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ContactListFragment extends Fragment {
    /* 声明组件*/
    private ListView listView;
    private SideBar sideBar;
    /*声明或创建集合，用于处理数据*/
    public static ArrayList<User> list;
    private ArrayList<Integer> list2;
    public static List<Map<String, String>> data = new ArrayList<Map<String, String>>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //获取fragment布局
        View view = inflater.inflate(R.layout.contactlist_fragment, container, false);
        /*初始化组件*/
        listView = (ListView) view.findViewById(R.id.listView);
        sideBar = (SideBar) view.findViewById(R.id.side_bar);
        /*获取activity中的通讯录信息*/
        list = MainWeixin.list;
        list2 = MainWeixin.list2;
        data = MainWeixin.data;
        /*创建自定义适配器，并设置给listview*/
        SortAdapter adapter = new SortAdapter(getActivity().getApplicationContext(), list, list2, data);
        listView.setAdapter(adapter);
        sideBar.setOnStrSelectCallBack(new SideBar.ISideBarSelectCallBack() {
            @Override
            public void onSelectStr(int index, String selectStr) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getName() == "新的朋友" || list.get(i).getName() == "群聊" ||
                            list.get(i).getName() == "标签" || list.get(i).getName() == "公众号"  )
                        continue;
                    if (selectStr.equalsIgnoreCase(list.get(i).getFirstLetter())) {
                        listView.setSelection(i); // 选择到首字母出现的位置
                        return;
                    }
                }
            }
        });
        return view;
    }
}
