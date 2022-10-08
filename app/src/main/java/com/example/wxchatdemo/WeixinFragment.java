package com.example.wxchatdemo;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.example.wxchatdemo.adapter.ImageAdapter;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressLint("ValidFragment")
public class WeixinFragment extends Fragment {
    //微信号，用于查找微信消息列表
    private String number;
    // 声明组件
    private ListView listView;
    // 创建集合用于存储服务器发来的显示微信消息列表的一些信息
    private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    //自定义的一个Hander消息机制
    private MyHander myhander = new MyHander();
    /*有参构造方法，参数为微信号*/
    @SuppressLint("ValidFragment")
    WeixinFragment(String number) {
        this.number = number;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 开一个线程完成网络请求操作
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                httpUrlConnPost(String.valueOf(number));
            }
        });
        thread1.start();
        /*等待网络请求线程完成*/
        try {
            thread1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //获取fragment布局
        View view = inflater.inflate(R.layout.weixin_fragment, container, false);
        //初始化组件
        listView = view.findViewById(R.id.listView);
        //创建自定义的适配器，用于把数据显示在组件上
        BaseAdapter adapter = new ImageAdapter(getActivity().getApplicationContext(), list);
        //设置适配器
        listView.setAdapter(adapter);
        return view;
    }

    // 1.编写一个发送请求的方法
    // 发送请求的主要方法
    public void httpUrlConnPost(String number) {
        HttpURLConnection urlConnection = null;
        URL url;
        try {
            // 请求的URL地地址
            url = new URL(
                    "http://100.2.178.10:8080/AndroidServer1_war_exploded/WeixinInformation");
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
            Log.i("aa", urlConnection.getResponseCode() + "");
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
                String str1 = rjson.getJSONObject("json").get("titleimg").toString();
                String[] pic = str1.split("\r\n");
                String str2 = rjson.getJSONObject("json").get("title").toString();
                String[] title = str2.split("\r\n");
                String str3 = rjson.getJSONObject("json").get("content").toString();
                String[] content = str3.split("\r\n");
                String str4 = rjson.getJSONObject("json").get("time").toString();
                String[] time = str4.split("\r\n");
                String str5 = rjson.getJSONObject("json").get("showcode").toString();
                String[] pic2 = str5.split("\r\n");
                for (int i = 0; i < pic.length; i++) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("pic", pic[i]);
                    System.out.println("网址:" + pic[i]);
                    map.put("title", title[i]);
                    System.out.println("网址:" + title[i]);
                    map.put("content", content[i]);
                    map.put("time", time[i]);
                    map.put("code", pic2[i]);
                    list.add(map);//将map放到list集合中
                }
                boolean result = rjson.getBoolean("json");// 从rjson对象中得到key值为"json"的数据，这里服务端返回的是一个boolean类型的数据
                System.out.println("json:===" + result);
                //如果服务器端返回的是true，则说明跳转微信页成功，跳转微信页失败
                if (result) {// 判断结果是否正确
                    //在Android中http请求，必须放到线程中去作请求，但是在线程中不可以直接修改UI，只能通过hander机制来完成对UI的操作
                    myhander.sendEmptyMessage(1);
                    Log.i("用户：", "跳转微信页成功");
                } else {
                    myhander.sendEmptyMessage(2);
                    System.out.println("222222222222222");
                    Log.i("用户：", "跳转微信页失败");
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
            //判断hander的内容是什么，如果是1则说明跳转微信页成功，如果是2说明跳转微信页失败
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