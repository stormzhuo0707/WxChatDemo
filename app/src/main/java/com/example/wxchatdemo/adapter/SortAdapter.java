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

public class SortAdapter extends BaseAdapter{

    //自定义handler机制
    private ImageHandler imgHandler = new ImageHandler();
    private Bitmap img;
    private ViewHolder viewHolder;
    private List<User> list = null;
    private List<Integer> list2 = null;
    private List<Map<String, String>> data = null;
    private Context mContext;

    public SortAdapter(Context mContext, List<User> list, List<Integer> list2, List<Map<String, String>> data) {
        this.mContext = mContext;
        this.list = list;
        this.list2 = list2;
        this.data = data;
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
            view = LayoutInflater.from(mContext).inflate(R.layout.contactlist_item, null);
            //初始化组件
            viewHolder.img = (ImageView) view.findViewById(R.id.img);
            viewHolder.name = (TextView) view.findViewById(R.id.name);
            viewHolder.catalog = (TextView) view.findViewById(R.id.catalog);
            viewHolder.contact_count = (TextView) view.findViewById(R.id.contact_count);
            viewHolder.divider = (View) view.findViewById(R.id.divider);
            view.setTag(viewHolder);
        } else {
            System.out.println("position=" + position);
            viewHolder = (ViewHolder) view.getTag();
        }
        //0~3是标签单独处理
        if (position == 0 || position == 1 || position == 2 || position == 3) {
            viewHolder.catalog.setVisibility(View.GONE);
            viewHolder.img.setImageResource(list2.get(position));
            viewHolder.name.setText(this.list.get(position).getName());
            viewHolder.divider.setVisibility(View.GONE);
            viewHolder.contact_count.setVisibility(View.GONE);
        }else  {
                //根据position获取首字母作为目录catalog
                String catalog = list.get(position).getFirstLetter();
                //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
                if(position == getPositionForSection(catalog)){
                    viewHolder.catalog.setVisibility(View.VISIBLE);
                    viewHolder.catalog.setText(user.getFirstLetter().toUpperCase());
                }else{
                    viewHolder.catalog.setVisibility(View.GONE);
                }
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
                viewHolder.img.setImageBitmap(img);
                viewHolder.name.setText(this.list.get(position).getName());
                if (position == list.size() - 1) {
                    viewHolder.divider.setVisibility(View.VISIBLE);
                    viewHolder.contact_count.setVisibility(View.VISIBLE);
                    viewHolder.contact_count.setText(Integer.toString(position - 4) + "个朋友");
                }else {
                    viewHolder.divider.setVisibility(View.GONE);
                    viewHolder.contact_count.setVisibility(View.GONE);
                }
        }
        return view;
    }

    final static class ViewHolder {
        TextView catalog;
        ImageView img;
        TextView name;
        TextView contact_count;
        View divider;
    }

    /**
     * 获取catalog首次出现位置
     */
    public int getPositionForSection(String catalog) {
        for (int i = 0; i < getCount(); i++) {
            if (list.get(i).getName() != "新的朋友" && list.get(i).getName() != "群聊" &&
            list.get(i).getName() != "标签" && list.get(i).getName() != "公众号") {
                String sortStr = list.get(i).getFirstLetter();
                if (catalog.equalsIgnoreCase(sortStr)) {
                    return i;
                }
            }
        }
        return -1;
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