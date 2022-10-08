package com.example.wxchatdemo;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wxchatdemo.tools.User;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainWeixin extends FragmentActivity implements View.OnClickListener {
    private WeixinFragment firstFragment = null;// 用于显示微信界面
    private ContactListFragment secondFragment = null;// 用于显示通讯录界面
    private FindFragment thirdFragment = null;// 用于显示发现界面
    private SelfFragment fourthFragment = null;// 用于显示我界面

    private View firstLayout = null;// 微信显示布局
    private View secondLayout = null;// 通讯录显示布局
    private View thirdLayout = null;// 发现显示布局
    private View fourthLayout = null;// 我显示布局

    /*声明组件变量*/
    private ImageView weixinImg = null;
    private ImageView contactImg = null;
    private ImageView findImg = null;
    private ImageView selfImg = null;

    private TextView weixinText = null;
    private TextView contactText = null;
    private TextView  findText = null;
    private TextView selfText = null;

    String[] imgUrl;
    String[] name;
    /*声明或创建集合，用于处理数据*/
    public static ArrayList<User> list;
    public static ArrayList<Integer> list2;
    public static List<Map<String, String>> data = new ArrayList<Map<String, String>>();

    //自定义的一个Hander消息机制
    private MyHander myhander = new MyHander();
    //微信号，用于查找微信消息列表
    private String number;

    private FragmentManager fragmentManager = null;// 用于对Fragment进行管理
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*获取登录activity传过来的微信号*/
        Intent intent = getIntent();
        number = intent.getStringExtra("weixin_number");
        /*开启一个线程，用微信号向服务器请求通讯录数据*/
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                httpUrlConnPost(String.valueOf(number));
            }
        });
        thread1.start();
        /*等待线性处理完成*/
        try {
            thread1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        initData(); //初始化数据
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//要求窗口没有title
        super.setContentView(R.layout.main_weixin);
        // 初始化布局元素
        initViews();
        fragmentManager = getFragmentManager();//用于对Fragment进行管理
        // 设置默认的显示界面
        setTabSelection(0);
    }

    /**
     * 在这里面获取到每个需要用到的控件的实例，并给它们设置好必要的点击事件
     */
    @SuppressLint("NewApi")
    public void initViews() {
        fragmentManager = getFragmentManager();
        firstLayout = findViewById(R.id.weixin_layout);
        secondLayout = findViewById(R.id.contacts_layout);
        thirdLayout = findViewById(R.id.find_layout);
        fourthLayout = findViewById(R.id.self_layout);

        weixinImg = (ImageView) findViewById(R.id.weixin_img);
        contactImg = (ImageView) findViewById(R.id.contact_img);
        findImg = (ImageView) findViewById(R.id.find_img);
        selfImg = (ImageView) findViewById(R.id.self_img);

        weixinText = (TextView) findViewById(R.id.weixin_text);
        contactText = (TextView) findViewById(R.id.contact_text);
        findText = (TextView) findViewById(R.id.find_text);
        selfText = (TextView) findViewById(R.id.self_text);

        //处理点击事件
        firstLayout.setOnClickListener(this);
        secondLayout.setOnClickListener(this);
        thirdLayout.setOnClickListener(this);
        fourthLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.weixin_layout:
                setTabSelection(0);// 当点击了微信时，选中第1个tab
                break;
            case R.id.contacts_layout:
                setTabSelection(1);// 当点击了通讯录时，选中第2个tab
                break;
            case R.id.find_layout:
                setTabSelection(2);// 当点击了发现时，选中第3个tab
                break;
            case R.id.self_layout:
                setTabSelection(3);// 当点击了我时，选中第4个tab
                break;
            default:
                break;
        }

    }

    /**
     * 根据传入的index参数来设置选中的tab页 每个tab页对应的下标。0表示微信，1表示通讯录，2表示发现，3表示我
     */
    @SuppressLint("NewApi")
    private void setTabSelection(int index) {
        clearSelection();// 每次选中之前先清除掉上次的选中状态
        FragmentTransaction transaction = fragmentManager.beginTransaction();// 开启一个Fragment事务
        hideFragments(transaction);// 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        switch (index) {
            case 0:
                // 当点击了我的微信tab时改变控件的图片和文字颜色
                weixinImg.setImageResource(R.drawable.tab_weixin_pressed);//修改布局中的图片
                weixinText.setTextColor(Color.parseColor("#0090ff"));//修改字体颜色

                if (firstFragment == null) {

                    // 如果FirstFragment为空，则创建一个并添加到界面上
                    firstFragment = new WeixinFragment(number);
                    transaction.add(R.id.fragment, firstFragment);

                } else {
                    // 如果FirstFragment不为空，则直接将它显示出来
                    transaction.show(firstFragment);//显示的动作
                }
                break;
            // 以下和firstFragment类同
            case 1:
                contactImg.setImageResource(R.drawable.tab_address_pressed);
                contactText.setTextColor(Color.parseColor("#0090ff"));
                if (secondFragment == null) {
                    secondFragment = new ContactListFragment();
                    transaction.add(R.id.fragment, secondFragment);
                } else {
                    transaction.show(secondFragment);
                }
                break;
            case 2:
                findImg.setImageResource(R.drawable.tab_find_frd_pressed);
                findText.setTextColor(Color.parseColor("#0090ff"));
                if (thirdFragment == null) {
                    thirdFragment = new FindFragment();
                    transaction.add(R.id.fragment, thirdFragment);
                } else {
                    transaction.show(thirdFragment);
                }
                break;
            case 3:
                selfImg.setImageResource(R.drawable.tab_settings_pressed);
                selfText.setTextColor(Color.parseColor("#0090ff"));
                if (fourthFragment == null) {
                    fourthFragment = new SelfFragment();
                    transaction.add(R.id.fragment, fourthFragment);
                } else {
                    transaction.show(fourthFragment);
                }
                break;
        }
        transaction.commit();

    }

    /**
     * 清除掉所有的选中状态
     */
    private void clearSelection() {
        weixinImg.setImageResource(R.drawable.tab_weixin_normal);
        weixinText.setTextColor(Color.parseColor("#82858b"));

        contactImg.setImageResource(R.drawable.tab_address_normal);
        contactText.setTextColor(Color.parseColor("#82858b"));

        findImg.setImageResource(R.drawable.tab_find_frd_normal);
        findText.setTextColor(Color.parseColor("#82858b"));

        selfImg.setImageResource(R.drawable.tab_settings_normal);
        selfText.setTextColor(Color.parseColor("#82858b"));
    }

    /**
     * 将所有的Fragment都设置为隐藏状态 用于对Fragment执行操作的事务
     */
    @SuppressLint("NewApi")
    private void hideFragments(FragmentTransaction transaction) {
        if (firstFragment != null) {
            transaction.hide(firstFragment);
        }
        if (secondFragment != null) {
            transaction.hide(secondFragment);
        }
        if (thirdFragment != null) {
            transaction.hide(thirdFragment);
        }
        if (fourthFragment != null) {
            transaction.hide(fourthFragment);
        }
    }

    //封装一个AlertDialog
    private void exitDialog() {
        Dialog dialog = new AlertDialog.Builder(this)
                .setTitle("温馨提示")
                .setMessage("您确定要退出程序吗?")
                .setPositiveButton("关闭微信", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        finish();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                }).create();
        dialog.show();//显示对话框
    }

    /**
     * 返回菜单键监听事件
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {//如果是返回按钮
            exitDialog();
        }
        return super.onKeyDown(keyCode, event);
    }

    //搜索按钮处理事件
    public void search(View v) {
        /*跳转到微信启动页*/
        Intent intent = new Intent();
        intent.setClass(com.example.wxchatdemo.MainWeixin.this,
                com.example.wxchatdemo.Search.class);
        startActivity(intent);
    }

    //加号点击事件处理方法
    public void buttonTopRight(View v) {
        Intent intent = new Intent(com.example.wxchatdemo.MainWeixin.this,
                com.example.wxchatdemo.MainTopRightDialog.class);
        startActivity(intent);
    }

    private void initData() {
        //把从服务器获取解析的数据添加到map中，方便处理
        Map<String, String> map = new HashMap<String, String>();
        for (int i = 0; i < imgUrl.length; i ++) {
            map.put(name[i], imgUrl[i]);
        }
        data.add(map);
        //名字要提取出来在添加到list中，因为要进行字母排序
        list = new ArrayList<>();
        if (imgUrl.length != 0) {
            for (int i = 0; i < imgUrl.length; i++) {
                list.add(new User(name[i]));
            }
            Collections.sort(list); // 对list进行排序，需要让User实现Comparable接口重写compareTo方法
            //四个标签排序后再进行添加，好进行条件判断分离出来
            list.add(0,new User("新的朋友"));
            list.add(1,new User("群聊"));
            list.add(2,new User("标签"));
            list.add(3,new User("公众号"));
            //四个标签图片不需要再服务器获取，直接移动端实现即可
            list2 = new ArrayList<>();
            list2.add(R.drawable.newfriend);
            list2.add(R.drawable.groupchat);
            list2.add(R.drawable.sign);
            list2.add(R.drawable.publicnum);
        }
    }

    // 1.编写一个发送请求的方法
    // 发送请求的主要方法
    public void httpUrlConnPost(String number) {
        HttpURLConnection urlConnection = null;
        URL url;
        try {
            // 请求的URL地地址
            url = new URL(
                    "http://100.2.178.10:8080/AndroidServer1_war_exploded/Contact");
            urlConnection = (HttpURLConnection) url.openConnection();// 打开http连接
            urlConnection.setConnectTimeout(3000);// 连接的超时时间
            urlConnection.setUseCaches(false);// 不使用缓存
            // urlConnection.setFollowRedirects(false);是static函数，作用于所有的URLConnection对象。
            urlConnection.setInstanceFollowRedirects(true);// 是成员函数，仅作用于当前函数,设置这个连接是否可以被重定向
            urlConnection.setReadTimeout(3000);// 响应的超时时间
            urlConnection.setDoInput(true);// 设置这个连接是否可以写入数据
            urlConnection.setDoOutput(true);// 设置这个连接是否可以输出数据
            urlConnection.setRequestMethod("POST");// 设置请求的方式
            urlConnection.setRequestProperty("Content-Type",
                    "application/json;charset=UTF-8");// 设置消息的类型
            urlConnection.connect();// 连接，从上述至此的配置必须要在connect之前完成，实际上它只是建立了一个与服务器的TCP连接
            JSONObject json = new JSONObject();// 创建json对象
            //json.put("title", URLEncoder.encode(title, "UTF-8"));// 使用URLEncoder.encode对特殊和不可见字符进行编码
            json.put("number", URLEncoder.encode(number, "UTF-8"));// 把数据put进json对象中
            String jsonstr = json.toString();// 把JSON对象按JSON的编码格式转换为字符串
            // ------------字符流写入数据------------
            OutputStream out = urlConnection.getOutputStream();// 输出流，用来发送请求，http请求实际上直到这个函数里面才正式发送出去
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));// 创建字符流对象并用高效缓冲流包装它，便获得最高的效率,发送的是字符串推荐用字符流，其它数据就用字节流
            bw.write(jsonstr);// 把json字符串写入缓冲区中
            bw.flush();// 刷新缓冲区，把数据发送出去，这步很重要
            out.close();
            bw.close();// 使用完关闭
            Log.i("aa", urlConnection.getResponseCode()+"");
            //以下判斷是否訪問成功，如果返回的状态码是200则说明访问成功
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {// 得到服务端的返回码是否连接成功
                // ------------字符流读取服务端返回的数据------------
                InputStream in = urlConnection.getInputStream();
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(in));
                String str = null;
                StringBuffer buffer = new StringBuffer();
                while ((str = br.readLine()) != null) {// BufferedReader特有功能，一次读取一行数据
                    System.out.println("测试：" + str);
                    buffer.append(str);
                }
                in.close();
                br.close();
                JSONObject rjson = new JSONObject(buffer.toString());
                String str1 = rjson.getJSONObject("json").get("img").toString();
                imgUrl = str1.split("\r\n");
                String str2 = rjson.getJSONObject("json").get("name").toString();
                name = str2.split("\r\n");
                boolean result = rjson.getBoolean("json");// 从rjson对象中得到key值为"json"的数据，这里服务端返回的是一个boolean类型的数据
                System.out.println("json:===" + result);
                //如果服务器端返回的是true，则说明注册成功，否则注册失败
                if (result) {// 判断结果是否正确
                    //在Android中http请求，必须放到线程中去作请求，但是在线程中不可以直接修改UI，只能通过hander机制来完成对UI的操作
                    myhander.sendEmptyMessage(1);
                    Log.i("用户：", "登录成功");
                } else {
                    myhander.sendEmptyMessage(2);
                    System.out.println("222222222222222");
                    Log.i("用户：", "登录失败");
                }
            } else {
                myhander.sendEmptyMessage(2);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("aa", e.toString());
            System.out.println("11111111111111111");
            myhander.sendEmptyMessage(2);
        } finally {
            urlConnection.disconnect();// 使用完关闭TCP连接，释放资源
        }
    }

    // 在Android中不可以在线程中直接修改UI，只能借助Handler机制来完成对UI的操作
    class MyHander extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //判断hander的内容是什么，如果是1则说明注册成功，如果是2说明注册失败
            switch (msg.what) {
                case 1:
                    Log.i("aa", msg.what + "");
                    break;
                case 2:
                    Log.i("aa", msg.what + "");
            }
        }
    }
}
