package com.example.wxchatdemo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

public class Loading extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading); //设置布局
        //一秒后结束当前activity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Loading.this.finish();
            }
        }, 1000);
    }
}