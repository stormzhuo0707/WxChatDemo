package com.example.wxchatdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class MainTopRightDialog extends Activity {
    private LinearLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_top_right_dialog);
        //设置对话框activity的宽度等于屏幕宽度，一定要设置，不然对话框会显示不全
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);//需要添加的语句
        layout=(LinearLayout)findViewById(R.id.main_dialog_layout);
        layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
        finish();
        return true;
    }
}
