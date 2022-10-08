package com.example.wxchatdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class AppStart extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_start); //设置布局
        //延迟一秒后跳转页面
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /*页面跳转到微信包括按钮的启动页面*/
                Intent intent = new Intent(com.example.wxchatdemo.AppStart.this, com.example.wxchatdemo.Welcome.class);
                startActivity(intent);
                com.example.wxchatdemo.AppStart.this.finish(); //结束当前activity
            }
        }, 2000);
    }
}