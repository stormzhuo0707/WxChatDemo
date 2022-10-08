package com.example.wxchatdemo;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.wxchatdemo.adapter.SearchSortAdapter;
import com.example.wxchatdemo.tools.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Search extends AppCompatActivity {
    //声明组件
    private SearchView mSearchView;
    private ListView mListView;
    private LinearLayout label;
    private LinearLayout top;
    //创建集合用户处理搜索功能
    private  List<User> list = MainWeixin.list;
    private List<User> list1 = null;
    public static List<Map<String, String>> data = MainWeixin.data;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        /* 隐藏自带标题*/
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN //全屏显示
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR; //因为背景为浅色所以将状态栏字体设置为黑色
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        //初始化组件
        mListView = (ListView) findViewById(R.id.listView);
        label = (LinearLayout) findViewById(R.id.label);
        top = (LinearLayout) findViewById(R.id.top);
        mSearchView = (SearchView) findViewById(R.id.searview);
        //设置searchview适配器
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }
            //获取输入内容
            @Override
            public boolean onQueryTextChange(String newText) {
                //如果newText是长度为0的字符串，则显示下面的指定搜索内容，不显示listview和联系人
                if (TextUtils.isEmpty(newText)){
                    label.setVisibility(View.VISIBLE);
                    mListView.setVisibility(View.GONE);
                    top.setVisibility(View.GONE);
                }else { //否则显示listview显示结果，其他都隐藏掉
                    label.setVisibility(View.GONE);
                    mListView.setVisibility(View.VISIBLE);
                    System.out.println("+++++" + newText);
                    list1 = new ArrayList<>();//创建集合，存储检索到的数据
                    /*检索数据*/
                    for (int i = 0; i < list.size(); i ++) {
                        char[] inputText = newText.toCharArray();
                        char[] chars = list.get(i).getName().toCharArray();
                        for (int j = 0; j < inputText.length; j++) {
                            if (j < chars.length) {
                                if (inputText[j] == chars[j] ||
                                        newText.equalsIgnoreCase(list.get(i).getFirstLetter())) {
                                    if (j == (inputText.length - 1)) {
                                        top.setVisibility(View.VISIBLE);
                                        list1.add(new User(list.get(i).getName()));
                                    }
                                }else {
                                    break;
                                }
                            }
                        }
                    }
                    if (list1.size() != 0) {
                        //创建自定义的适配器，用于把数据显示在组件上
                        BaseAdapter adapter = new SearchSortAdapter(getApplicationContext(), data,
                                list1, newText, mListView);
                        //设置适配器
                        mListView.setAdapter(adapter);
                    } else {
                        mListView.setVisibility(View.GONE);
                    }
                }
                return false;
            }
        });
    }

    //取消返回
    public void back(View v) {
        this.finish();
    }
}
