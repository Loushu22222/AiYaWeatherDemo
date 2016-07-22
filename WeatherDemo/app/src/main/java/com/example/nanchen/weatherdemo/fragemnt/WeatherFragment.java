package com.example.nanchen.weatherdemo.fragemnt;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nanchen.weatherdemo.R;
import com.example.nanchen.weatherdemo.bean.WeatherDataBean;
import com.example.nanchen.weatherdemo.url.WeatherUrl;
import com.example.nanchen.weatherdemo.utils.Utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by nanchen on 2016/6/16.
 */
public class WeatherFragment extends Fragment {

    private Utils utils;
    private String cityName = "成都";
    private TextView tv_update_time, tv_date, tv_moon, tv_info, tv_wind, tv_low_high, tv_week, tv_humidity;
    private ImageView iv_left, iv_right;
    public static WeatherDataBean dataBean;
    private ProgressBar loadPb;
    private LinearLayout ll_bg;
    private ImageButton ib_refresh;
    private TextView tv_city_name;

    /**
     * 格式化控件
     *
     * @param v
     */
    private void initView(View v) {
        tv_update_time = (TextView) v.findViewById(R.id.fg1_tv_updatetime);
        iv_left = (ImageView) v.findViewById(R.id.fg1_iv_left);
        iv_right = (ImageView) v.findViewById(R.id.fg1_iv_right);
        tv_date = (TextView) v.findViewById(R.id.fg1_tv_date);
        tv_moon = (TextView) v.findViewById(R.id.fg1_tv_moon);
        tv_info = (TextView) v.findViewById(R.id.fg1_tv_info);
        tv_wind = (TextView) v.findViewById(R.id.fg1_tv_wind);
        loadPb = (ProgressBar) v.findViewById(R.id.news_list_pb);
        ll_bg = (LinearLayout) getActivity().findViewById(R.id.ll_bg);
        tv_low_high = (TextView) v.findViewById(R.id.fg1_tv_low_high);
        tv_week = (TextView) v.findViewById(R.id.fg1_tv_week);
        tv_humidity = (TextView) v.findViewById(R.id.fg1_tv_humidity);
        tv_city_name = (TextView) getActivity().findViewById(R.id.menu_tv_name);
        //        ib_refresh = (ImageButton) v.findViewById(R.id.fg1_ib_refresh);
        //
        //        ib_refresh.setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View v) {
        //                loadPb.setVisibility(View.VISIBLE);
        //                if (utils.netState(getActivity())){
        //                    gotoNet();
        //                }else{
        //                    Toast.makeText(getActivity(),"当前网络不可用",Toast.LENGTH_SHORT).show();
        //                }
        //            }
        //        });
    }


    public static WeatherFragment newInstance() {
        return new WeatherFragment();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x123:
                    if (dataBean != null) {
                        tv_update_time.setText("更新时间:" + dataBean.result.realtime.time);
                        tv_date.setText(dataBean.result.realtime.date);
                        tv_info.setText(dataBean.result.realtime.weather.info);
                        tv_moon.setText(dataBean.result.realtime.moon);
                        tv_wind.setText(dataBean.result.realtime.wind.direct + dataBean.result.realtime.wind.power);
                        tv_humidity.setText("湿度:" + dataBean.result.realtime.weather.humidity + "%");
                        setTemperature();
                        setBg();
                        setLowHighWeek();
                        //                        loadPb.setVisibility(View.GONE);

                    }
                    break;
                case 0x124:
                    Toast.makeText(getActivity(), "检测到网络问题，请检查网络！", Toast.LENGTH_SHORT).show();
                    //                    loadPb.setVisibility(View.GONE);
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("警告");
                    builder.setMessage("检测到网络存在问题，点击确定重新加载！");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            gotoNet();
                            //                            loadPb.setVisibility(View.VISIBLE);
                            utils.showProgressDialog(getActivity());
                        }
                    });
                    builder.show();
                    break;
            }
            utils.dismissDialog();
        }
    };

    /**
     * 设置最低温度和最高温度和星期
     */
    private void setLowHighWeek() {
        String low = dataBean.result.weather.get(0).info.night.get(2);
        String high = dataBean.result.weather.get(0).info.day.get(2);
        tv_low_high.setText(low + "/" + high + "℃");

        String week = dataBean.result.weather.get(0).week;
        tv_week.setText("星期" + week);
    }

    /**
     * 根据天气设置背景图片
     */
    private void setBg() {
        String info = dataBean.result.realtime.weather.info;
        switch (info) {
            case "晴":
                ll_bg.setBackgroundResource(R.mipmap.bg_fine_day);
                break;
            case "多云":
                ll_bg.setBackgroundResource(R.mipmap.bg_cloudy_day);
                break;
            case "小雨":
                ll_bg.setBackgroundResource(R.mipmap.bg_rain);
                break;
            case "中雨":
                ll_bg.setBackgroundResource(R.mipmap.bg_overcast);
                break;
            case "霾":
                ll_bg.setBackgroundResource(R.mipmap.bg_haze);
                break;
            case "阴":
                ll_bg.setBackgroundResource(R.mipmap.bg_fog);
                break;
        }
    }

    /**
     * 设置温度图片
     */
    private void setTemperature() {
        String temperature = dataBean.result.realtime.weather.temperature;
        String left = temperature.substring(0, 1);
        Log.e("1", left);
        String right = temperature.substring(1, 2);
        Log.e("2", right);
        switch (left) {
            case "0":
                iv_left.setImageResource(R.mipmap.org4_widget_nw0);
                break;
            case "1":
                iv_left.setImageResource(R.mipmap.org4_widget_nw1);
                break;
            case "2":
                iv_left.setImageResource(R.mipmap.org4_widget_nw2);
                break;
            case "3":
                iv_left.setImageResource(R.mipmap.org4_widget_nw3);
                break;
            case "4":
                iv_left.setImageResource(R.mipmap.org4_widget_nw4);
                break;
            case "5":
                iv_left.setImageResource(R.mipmap.org4_widget_nw5);
                break;
            case "6":
                iv_left.setImageResource(R.mipmap.org4_widget_nw6);
                break;
            case "7":
                iv_left.setImageResource(R.mipmap.org4_widget_nw7);
                break;
            case "8":
                iv_left.setImageResource(R.mipmap.org4_widget_nw8);
                break;
            case "9":
                iv_left.setImageResource(R.mipmap.org4_widget_nw9);
                break;
        }

        switch (right) {
            case "0":
                iv_right.setImageResource(R.mipmap.org4_widget_nw0);
                break;
            case "1":
                iv_right.setImageResource(R.mipmap.org4_widget_nw1);
                break;
            case "2":
                iv_right.setImageResource(R.mipmap.org4_widget_nw2);
                break;
            case "3":
                iv_right.setImageResource(R.mipmap.org4_widget_nw3);
                break;
            case "4":
                iv_right.setImageResource(R.mipmap.org4_widget_nw4);
                break;
            case "5":
                iv_right.setImageResource(R.mipmap.org4_widget_nw5);
                break;
            case "6":
                iv_right.setImageResource(R.mipmap.org4_widget_nw6);
                break;
            case "7":
                iv_right.setImageResource(R.mipmap.org4_widget_nw7);
                break;
            case "8":
                iv_right.setImageResource(R.mipmap.org4_widget_nw8);
                break;
            case "9":
                iv_right.setImageResource(R.mipmap.org4_widget_nw9);
                break;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, null);

        initView(view);

        Intent intent = getActivity().getIntent();
        if (!TextUtils.isEmpty(intent.getStringExtra("city"))) {
            cityName = intent.getStringExtra("city");
            tv_city_name.setText(cityName);
        }

        //联网获得数据
        utils = new Utils();
        utils.showProgressDialog(getActivity());


        //        loadPb.setVisibility(View.VISIBLE);
        if (utils.netState(getActivity())) {
            gotoNet();
        } else {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("警告");
            builder.setMessage("网络连接不可用，请转到设置设置网络！");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.show();
            utils.dismissDialog();
        }
        return view;
    }


    private void gotoNet() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    cityName = URLEncoder.encode(cityName, "utf-8");//转编码为utf-8
                    String result = utils.getData(WeatherUrl.URL + cityName);//从网络获取数据
                    if (result == null) {
                        handler.sendEmptyMessage(0x124);
                    } else {
                        dataBean = utils.parseJsonUseGson(result);//把JSon数据解析到Bean中
                        //发广播到第二个Fragment
                        Intent intent = new Intent();
                        intent.setAction("weather");
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("data", dataBean);
                        intent.putExtra("bundle", bundle);
                        getActivity().sendBroadcast(intent);

                        handler.sendEmptyMessage(0x123);
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    OnDataChangedListener listener;

    //定义一个接口
    public interface OnDataChangedListener {
        //定义一个回调方法
        void onDataChanged(WeatherDataBean dataBean);
    }

    public void setOnDataChangedListener(OnDataChangedListener listener) {
        if (listener != null) {
            this.listener = listener;
        }
    }
}
