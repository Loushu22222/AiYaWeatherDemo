package com.example.nanchen.weatherdemo.fragemnt;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nanchen.weatherdemo.R;
import com.example.nanchen.weatherdemo.bean.WeatherDataBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nanchen on 2016/6/20.
 */
public class FutureInfoFragment extends Fragment {
    private List<Furture> list;
    private ListView lv;

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

                Log.e("123456",dataBean.toString());
                if (dataBean!=null){

                    Log.e("aaa",dataBean.toString());
                    //开始填充数据
//                    fillData();

                    list = new ArrayList<>();
                    Furture furture = null;
                    for (int i = 0;i<7;i++){
                        furture = new Furture();
                        furture.setDate(dataBean.result.weather.get(i).date.substring(5));
                        furture.setWeek(dataBean.result.weather.get(i).week);
                        furture.setNongli(dataBean.result.weather.get(i).nongli);
                        furture.setDay(dataBean.result.weather.get(i).info.day.get(2));
                        furture.setNight(dataBean.result.weather.get(i).info.night.get(2));
                        list.add(furture);
                    }

                    Log.e("list",list.toString());

                    MyAdapter adapter = new MyAdapter(context, R.layout.weather_list_item,list);
                    lv.setAdapter(adapter);

                }else{
                    Toast.makeText(getActivity(),"当前数据解析出错！",Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    /**
     * 填充数据到List
     */
    private void fillData() {
        list = new ArrayList<>();
        for (int i = 0;i<7;i++){
            Furture furture = new Furture();
            furture.setDate(dataBean.result.weather.get(i).date);
            furture.setWeek(dataBean.result.weather.get(i).week);
            furture.setNongli(dataBean.result.weather.get(i).nongli);
            furture.setDay(dataBean.result.weather.get(i).info.day.get(2));
            furture.setNight(dataBean.result.weather.get(i).info.night.get(2));
            list.add(furture);
        }

        Log.e("list",list.toString());

        MyAdapter adapter = new MyAdapter(getContext(), R.layout.weather_list_item,list);
        lv.setAdapter(adapter);
    }

    public static FutureInfoFragment newInstance(){
        return new FutureInfoFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_future,null);

        //注册广播
        getActivity().registerReceiver(br,new IntentFilter("weather"));

        lv = (ListView) view.findViewById(R.id.fg3_lv);

//        dataBean = WeatherFragment.dataBean;
//        if (dataBean!=null){
//            fillData();
//        }else{
//            Toast.makeText(getActivity(),"数据解析异常，请检查网络连接！",Toast.LENGTH_SHORT).show();
//        }

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //不用的时候注销广播
        getActivity().unregisterReceiver(br);
    }

}

/**
 * 自定义适配器
 */
class MyAdapter extends ArrayAdapter<Furture>{

    public MyAdapter(Context context, int resource, List<Furture> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.weather_list_item,null);
//            convertView = View.inflate(getContext(),R.layout.weather_list_item,null);
            vh = new ViewHolder();
            vh.tv_date = (TextView) convertView.findViewById(R.id.item_tv_date);
            vh.tv_week = (TextView) convertView.findViewById(R.id.item_tv_week);
            vh.tv_nongli = (TextView) convertView.findViewById(R.id.item_tv_nongli);
            vh.tv_day = (TextView) convertView.findViewById(R.id.item_tv_day);
            vh.tv_night = (TextView) convertView.findViewById(R.id.item_tv_night);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder) convertView.getTag();
        }
        Furture furture = getItem(position);
        vh.tv_date.setText(furture.getDate());
        vh.tv_week.setText(furture.getWeek());
        vh.tv_nongli.setText(furture.getNongli());
        vh.tv_day.setText(furture.getDay());
        vh.tv_night.setText(furture.getNight());
        return convertView;
    }

    class ViewHolder{
        TextView tv_date,tv_week,tv_nongli,tv_day,tv_night;
    }
}

/**
 * 装每一行数据的model
 */
class Furture{
    private String date,week,nongli,day,night;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getNongli() {
        return nongli;
    }

    public void setNongli(String nongli) {
        this.nongli = nongli;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getNight() {
        return night;
    }

    public void setNight(String night) {
        this.night = night;
    }
}
