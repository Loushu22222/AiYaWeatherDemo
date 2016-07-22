package com.example.nanchen.weatherdemo.fragemnt;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nanchen.weatherdemo.R;
import com.example.nanchen.weatherdemo.bean.WeatherDataBean;

/**
 *
 * Created by nanchen on 2016/6/16.
 */
public class WeatherInfoFragment extends Fragment {
    private WeatherDataBean dataBean;

    //定义一个广播接收器,用于接收从WeatherFragmnet里面获取的数据
    private BroadcastReceiver br = new BroadcastReceiver() {

        //当广播接收的时候回调这个方法
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("weather")){
                //通过intent对象获得额外传递过来的值
                Bundle bundle = intent.getBundleExtra("bundle");
                dataBean = (WeatherDataBean) bundle.getSerializable("data");//反序列化
                if (dataBean!=null){
                    //开始填充数据
                    fillData();
                }else{
                    Toast.makeText(getActivity(),"当前数据解析出错！",Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    /**
     * 填充数据的方法
     */
    private void fillData() {
        tv_info.setText(dataBean.result.realtime.weather.info);
        tv_temperature.setText(dataBean.result.realtime.weather.temperature);
        tv_humidity.setText(dataBean.result.realtime.weather.humidity);
        tv_moon.setText(dataBean.result.realtime.moon);
        String week = dataBean.result.weather.get(0).week;
        tv_week.setText("星期"+week);
        tv_kongtiao.setText(dataBean.result.life.info.kongtiao.get(0));
        tv_yundong.setText(dataBean.result.life.info.yundong.get(0));
        tv_ziwaixian.setText(dataBean.result.life.info.ziwaixian.get(0));
        tv_xiche.setText(dataBean.result.life.info.xiche.get(0));
        tv_chuanyi.setText(dataBean.result.life.info.chuanyi.get(0));
    }



    private TextView tv_info,tv_temperature,tv_humidity,tv_moon,tv_week,tv_kongtiao;
    private TextView tv_yundong,tv_ziwaixian,tv_xiche,tv_chuanyi;


    public static WeatherInfoFragment newInstance(){
        return new WeatherInfoFragment();
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0x102:
                    fillData();
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather_info,null);

        //注册广播
        getActivity().registerReceiver(br,new IntentFilter("weather"));


//        dataBean = WeatherFragment.dataBean;

        initView(view);


//        if (dataBean!=null){
//            handler.sendEmptyMessage(0x102);
//        }else{
//            Toast.makeText(getActivity(),"数据解析异常，请检查网络连接！",Toast.LENGTH_SHORT).show();
//        }
        return view;
    }

    /**
     * 初始化控件
     */
    private void initView(View view) {
        tv_info = (TextView) view.findViewById(R.id.fg2_tv_info);
        tv_temperature = (TextView) view.findViewById(R.id.fg2_tv_temperature);
        tv_humidity = (TextView) view.findViewById(R.id.fg2_tv_humidity);
        tv_moon = (TextView) view.findViewById(R.id.fg2_tv_moon);
        tv_week = (TextView) view.findViewById(R.id.fg2_tv_week);
        tv_kongtiao = (TextView) view.findViewById(R.id.fg2_tv_kongtiao);
        tv_yundong = (TextView) view.findViewById(R.id.fg2_tv_yundong);
        tv_ziwaixian = (TextView) view.findViewById(R.id.fg2_tv_ziwaixian);
        tv_xiche = (TextView) view.findViewById(R.id.fg2_tv_xiche);
        tv_chuanyi = (TextView) view.findViewById(R.id.fg2_tv_chuanyi);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //不用的时候注销广播
        getActivity().unregisterReceiver(br);
    }
}
