package com.example.wxchatdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Welcome extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome); //设置布局
    }

    //登录按钮点击事件处理方法
    public void welcome_login(View v) {
        Intent intent = new Intent();
        /* 页面跳转到登录界面*/
        intent.setClass(com.example.wxchatdemo.Welcome.this, LoginUser.class);
        startActivity(intent);
        this.finish(); //结束当前activity
    }

    //注册按钮点击事件处理方法
    public void welcome_register(View v) {
        Intent intent = new Intent();
        /*页面跳转到注册界面*/
        intent.setClass(com.example.wxchatdemo.Welcome.this, com.example.wxchatdemo.Reigister.class);
        startActivity(intent);
        this.finish(); //结束当前activity
    }

}
