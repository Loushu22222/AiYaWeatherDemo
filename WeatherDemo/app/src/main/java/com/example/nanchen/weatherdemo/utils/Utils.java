package com.example.nanchen.weatherdemo.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.example.nanchen.weatherdemo.bean.WeatherDataBean;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 异步任务工具类
 * Created by nanchen on 2016/6/16.
 */
public class Utils {

    private ProgressDialog pd;

    /**
     * 从网络获取数据
     * @param path 网址
     * @return  返回字符串
     */
    public String getData(String path){
        InputStream in = null;
        StringBuffer sbf = null;
        HttpURLConnection conn = null;
        BufferedReader br = null;
        try {
            URL url = new URL(path);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(10*1000);
            if(conn.getResponseCode() == 200){
                in = conn.getInputStream();
                br = new BufferedReader(new InputStreamReader(in,"utf-8"));
                sbf = new StringBuffer();
                String line;
                while ((line = br.readLine())!=null){
                    sbf.append(line);
                }
                return sbf.toString();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br!=null){
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (in!=null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (conn!=null){
                conn.disconnect();
            }
        }
        return null;
    }

    /**
     * 判断是否有网
     * @return 返回是否有网
     */
    public boolean netState(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //获取网络状态
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info != null){
            return info.isAvailable();
        }
        return false;
    }


    /**
     * 使用Gson解析Json字符串
     * @param json  Json字符串信息
     * @return  返回数据类
     */
    public WeatherDataBean parseJsonUseGson(String json){
        Gson gson = new Gson();
        return gson.fromJson(json,WeatherDataBean.class);
    }

    /**
     * 解析返回数据是否有用对于输入的城市名
     * @return
     */
    public boolean parseJson(String json){
        try {
            JSONObject obj = new JSONObject(json);
            JSONObject result = obj.getJSONObject("result");
            if(!result.getString("x").equals("null")){
                Log.e("city",result.getString("x"));
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 通过城市接口返回的数据解析成城市名字数组存在数组中
     * @param json
     * @return
     */
    public List<String> parseJsonCity(String json){
        List<String> list = null;
        try {
            list = new ArrayList<>();
            JSONObject obj = new JSONObject(json);
            JSONArray city_info = obj.getJSONArray("city_info");
            for (int i = 0;i<city_info.length();i++){
                JSONObject obj2 = city_info.getJSONObject(i);
                list.add(obj2.getString("city"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 显示刷新数据进度对话框
     * @param context
     */
    public void showProgressDialog(Context context){
        pd = new ProgressDialog(context);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);//设置为转圈圈
        pd.setMessage("正在玩命加载中....");
        pd.setCanceledOnTouchOutside(false);
        pd.show();
    }

    /**
     * 退出进度对话框
     */
    public void dismissDialog(){
        if (pd != null){
            pd.dismiss();
        }
    }
}
