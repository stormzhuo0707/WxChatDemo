package com.example.wxchatdemo.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.wxchatdemo.R;
import com.example.wxchatdemo.tools.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class SearchSortAdapter extends BaseAdapter{

    //自定义handler机制
    private ImageHandler imgHandler = new ImageHandler();
    private Bitmap img;
    private ViewHolder viewHolder;
    private String newText;
    private List<User> list = null;;
    private List<Map<String, String>> data = null;
    private Context mContext;

    public SearchSortAdapter(Context mContext, List<Map<String, String>> data, List<User> list,
                             String newText, ListView mListView) {
        this.mContext = mContext;
        this.list = list;
        this.data = data;
        this.newText = newText;
    }

    public int getCount() {
        return this.list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup arg2) {

        final User user = list.get(position);
        if (view == null) {
            viewHolder = new ViewHolder();
            //获取listview对应的item布局
            view = LayoutInflater.from(mContext).inflate(R.layout.search_item, null);
            //初始化组件
            viewHolder.search_img = (ImageView) view.findViewById(R.id.search_img);
            viewHolder.search_text = (TextView) view.findViewById(R.id.search_text);
            viewHolder.search_list = (LinearLayout) view.findViewById(R.id.search_list);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        if (this.list.get(position).getName().equals("新的朋友") || this.list.get(position).getName().equals("标签")
                || this.list.get(position).getName().equals("群聊") || this.list.get(position).getName().equals("公众号")) {

        }else {
            Map<String, String> map = data.get(0);
            Thread thread1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    img =  getImg(map.get(list.get(position).getName()));
                    Message msg = imgHandler.obtainMessage();
                    msg.what = 0;
                    msg.obj = img;
                    imgHandler.sendMessage(msg);
                }
            });
            thread1.start();
            try {
                thread1.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            viewHolder.search_img.setImageBitmap(img);
            viewHolder.search_text.setText(this.list.get(position).getName());
        }
        return view;
    }

    final static class ViewHolder {
        TextView search_text;
        ImageView search_img;
        LinearLayout search_list;
    }
    /**
     * 从服务器读取图片流数据，并转换为Bitmap格式
     * @return Bitmap
     */
    private Bitmap getImg(String url){
        Bitmap img = null;
        try {
            URL imgUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) imgUrl.openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(1000 * 6);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Charset", "UTF-8");
            conn.connect();
            //输出流写参数
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            String param = getParam();
            dos.writeBytes(param);
            dos.flush();
            dos.close();
            int resultCode = conn.getResponseCode();
            if(HttpURLConnection.HTTP_OK == resultCode){
                InputStream is = conn.getInputStream();
                img = BitmapFactory.decodeStream(is);
                is.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }

    /**
     * 测试参数
     * @return
     */
    private String getParam(){
        JSONObject jsObj = new JSONObject();
        try {
            jsObj.put("picFormat", "jpg");
            jsObj.put("testParam", "9527");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsObj.toString();
    }

    /**
     * 异步线程请求到的图片数据，利用Handler，在主线程中显示
     */
    class ImageHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 0:
                    img = (Bitmap)msg.obj;
                    break;
                default:
                    break;
            }
        }
    }
}
