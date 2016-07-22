package com.example.nanchen.weatherdemo;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nanchen.weatherdemo.fragemnt.FutureInfoFragment;
import com.example.nanchen.weatherdemo.fragemnt.WeatherFragment;
import com.example.nanchen.weatherdemo.fragemnt.WeatherInfoFragment;

import java.util.ArrayList;
import java.util.List;


/**
 * android中的网络编程
 * 1)Http
 * 2)HttpUrlConnection
 * 3)HttpClient(Api6.0以后不可用)
 *
 * socket
 * 网络编程中的两点约束
 * 1、android中规定不能在主（Main、UI）线程中做耗时操作（5秒钟以上会弹出是否退出app的对话框）
 * 解决：开启子线程来进行耗时操作
 * 2、不能在子线程中更新UI
 * 解决：1）通过线程间的通信来完成更新UI的操作（Handler）
 *      2）android中提供了异步任务的类(AsyncTask)
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String APP_KEY = "6601d195ad4e4e5eb21688df3d762fa7";//和风天气APPkey



    //https://api.heweather.com/x3/weather?cityid=CN101270102&key=6601d195ad4e4e5eb21688df3d762fa7
    private static final String URL_HEAD = "https://api.heweather.com/x3/weather?cityid=";
    private static final String URL_CITY_ID = "CN101270102";//成都龙泉
    private static final String URL_KEY = "&key=6601d195ad4e4e5eb21688df3d762fa7";

    private String httpUrl;
    private TextView tv;

    private ImageView image_back;
    private ImageButton ib_add;
    private TextView tv_city;
    private ViewPager vp;

    private List<Fragment> list;
    private MyAdapter adapter;

    private Fragment weatherFragment;
    private Fragment weatherInfoFragment;

    private Fragment furtureInfoFragment;

//    Handler handler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what){
//                case 0x123:
//                    tv.setText(msg.obj.toString());
//                    break;
//            }
//        }
//    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        initView();
        list = new ArrayList<>();
        weatherFragment = WeatherFragment.newInstance();
        weatherInfoFragment = WeatherInfoFragment.newInstance();
        furtureInfoFragment = FutureInfoFragment.newInstance();

        list.add(weatherFragment);
        list.add(weatherInfoFragment);
        list.add(furtureInfoFragment);
        adapter = new MyAdapter(getSupportFragmentManager());
        vp.setAdapter(adapter);

        vp.setOffscreenPageLimit(4);



//        tv = (TextView) findViewById(R.id.info);
//
//
//        if (netState()){
//            LoadData();
//        }else{
//            Toast.makeText(this,"当前没有网络，请检查!",Toast.LENGTH_SHORT).show();
//         }
    }

    /**
     * 初始化控件
     */
    private void initView() {
        image_back = (ImageView) findViewById(R.id.menu_image_back);
        ib_add = (ImageButton) findViewById(R.id.menu_ib_add);
        tv_city = (TextView) findViewById(R.id.menu_tv_name);
        vp = (ViewPager) findViewById(R.id.main_vp);

        ib_add.setOnClickListener(this);
    }

//    private void LoadData() {
//        httpUrl = URL_HEAD + URL_CITY_ID + URL_KEY;
//        new AsynGetWeatherData(this).execute(httpUrl);
//        new MyThread().start();
//    }


    /**
     * 判断网络状态的方法
     * @return  返回是否有网
     */
    private boolean netState(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        //获取网络状态
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info.isAvailable();//返回网络是否可用
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.menu_ib_add:
                startActivity(new Intent(MainActivity.this,CityManageActivity.class));
                break;
        }
    }


    long firstTime = 0;//第一次点返回键的时间
    /**
     * 监听返回键
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        long secondTime = firstTime;
        firstTime = System.currentTimeMillis();
        if (secondTime - firstTime <= 2000){//点两次时间小于2秒，直接退出程序。
            finish();
            System.exit(0);
        }else{
            Toast.makeText(this,"再按一次退出!",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 监听所有的按键
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 自定义适配器,ViewPager里面装Fragment则继承FragmentPagerAdapter而不继承PagerAdapter
     */
    class MyAdapter extends FragmentPagerAdapter{

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }

//    public String getData(String path){
//        StringBuilder sbf = new StringBuilder();
//        try {
//            URL url = new URL(path);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            int code = conn.getResponseCode();
//            if (code == HttpURLConnection.HTTP_OK){
//                InputStream in = conn.getInputStream();
//                BufferedReader br = new BufferedReader(new InputStreamReader(in,"utf-8"));
//                String line = null;
//                while ((line = br.readLine())!=null){
//                    sbf.append(line);
//                }
//                Log.e("2",sbf.toString());
//                br.close();
//                in.close();
//            }else{
//                Log.d("1","获取失败，请检查网络");
//            }
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return sbf.toString();
//    }
//
//    /**
//     * 下载数据子线程
//     */
//    class MyThread extends Thread{
//        @Override
//        public void run() {
//            String str = getData(httpUrl);
//            Weather weather = parseJson(str);
//
//            //定义一个消息对象
//            Message msg = new Message();
//            msg.obj = weather.toString();
//            //消息的标识
//            msg.what = 0x123;
//            handler.sendMessage(msg);
//        }
//    }
//

//
//
//    String city;
//
//    public Weather parseJson(String json){
//        Weather weather = null;
//        try {
//            weather = new Weather();
//            JSONObject obj = new JSONObject(json);
//            Log.e("12",obj.toString());
//            JSONArray array = obj.getJSONArray("HeWeather data service 3.0");
//            JSONObject obj1 = array.getJSONObject(0);
//            JSONObject basic = obj1.getJSONObject("basic");
//            city = basic.getString("city");//城市
//            Log.e("123",city);
//            weather.setCity(city);
//            JSONObject update = basic.getJSONObject("update");
//            String update_time = update.getString("loc");//更新时间
//            weather.setUpdate_time(update_time);
//            JSONArray daily_forecast = obj1.getJSONArray("daily_forecast");
//            JSONObject obj2 = daily_forecast.getJSONObject(0);
//            JSONObject cond = obj2.getJSONObject("cond");
//            String txt_d = cond.getString("txt_d");//白天天气
//            weather.setTxt_d(txt_d);
//            String txt_n = cond.getString("txt_n");//晚上天气
//            weather.setTxt_n(txt_n);
//            String date = obj2.getString("date");//日期
//            weather.setDate(date);
//            JSONObject tmp = obj2.getJSONObject("tmp");
//            String tmp_max = tmp.getString("max");//最高温
//            weather.setTmp_max(tmp_max);
//            String tmp_min = tmp.getString("min");//最低温
//            weather.setTem_min(tmp_min);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return weather;
//    }

}


/**
 * 获取天气数据
 */
//class AsynGetWeatherData extends AsyncTask<String,Void,String>{
//
//    private ArrayList<Weather> list = new ArrayList<>();
//
//    private MainActivity mainActivity;
//
//    public AsynGetWeatherData(MainActivity mainActivity){
//        this.mainActivity = mainActivity;
//    }
//
//
//    @Override
//    protected String doInBackground(String... params) {
//        StringBuilder sbf = new StringBuilder();
//        try {
//            URL url = new URL(params[0]);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            int code = conn.getResponseCode();
//            if (code == HttpURLConnection.HTTP_OK){
//                InputStream in = conn.getInputStream();
//                BufferedReader br = new BufferedReader(new InputStreamReader(in,"utf-8"));
//                String line = null;
//                while ((line = br.readLine())!=null){
//                    sbf.append(line);
//                }
//                Log.e("2",sbf.toString());
//                br.close();
//                in.close();
//            }else{
//                Log.d("1","获取失败，请检查网络");
//            }
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return sbf.toString();
//    }
//
//
//    @Override
//    protected void onPostExecute(String s) {
//        if (s!=null){
//            list.add(parseJson(s));
//
//            Weather weather = list.get(0);
//            //定义一个消息对象
//            Message msg = new Message();
//            msg.obj = weather.toString();
//            //消息的标识
//            msg.what = 0x123;
//            mainActivity.handler.sendMessage(msg);
//        }
//    }
//
//    private Weather parseJson(String s) {
//        Weather weather = null;
//        try {
//            weather = new Weather();
//            JSONObject obj = new JSONObject(s);
//            Log.e("12",obj.toString());
//            JSONArray array = obj.getJSONArray("HeWeather data service 3.0");
//            JSONObject obj1 = array.getJSONObject(0);
//            JSONObject basic = obj1.getJSONObject("basic");
//            String city = basic.getString("city");//城市
//            Log.e("123",city);
//            weather.setCity(city);
//            JSONObject update = basic.getJSONObject("update");
//            String update_time = update.getString("loc");//更新时间
//            weather.setUpdate_time(update_time);
//            JSONArray daily_forecast = obj1.getJSONArray("daily_forecast");
//            JSONObject obj2 = daily_forecast.getJSONObject(0);
//            JSONObject cond = obj2.getJSONObject("cond");
//            String txt_d = cond.getString("txt_d");//白天天气
//            weather.setTxt_d(txt_d);
//            String txt_n = cond.getString("txt_n");//晚上天气
//            weather.setTxt_n(txt_n);
//            String date = obj2.getString("date");//日期
//            weather.setDate(date);
//            JSONObject tmp = obj2.getJSONObject("tmp");
//            String tmp_max = tmp.getString("max");//最高温
//            weather.setTmp_max(tmp_max);
//            String tmp_min = tmp.getString("min");//最低温
//            weather.setTem_min(tmp_min);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return weather;
//    }
//}
//
//class Weather{
//    private String city;
//    private String update_time;
//    private String txt_d;
//    private String txt_n,date,tmp_max,tem_min;
//
//    public String getCity() {
//        return city;
//    }
//
//    public void setCity(String city) {
//        this.city = city;
//    }
//
//    public String getUpdate_time() {
//        return update_time;
//    }
//
//    public void setUpdate_time(String update_time) {
//        this.update_time = update_time;
//    }
//
//    public String getTxt_d() {
//        return txt_d;
//    }
//
//    public void setTxt_d(String txt_d) {
//        this.txt_d = txt_d;
//    }
//
//    public String getTxt_n() {
//        return txt_n;
//    }
//
//    public void setTxt_n(String txt_n) {
//        this.txt_n = txt_n;
//    }
//
//    public String getDate() {
//        return date;
//    }
//
//    public void setDate(String date) {
//        this.date = date;
//    }
//
//    public String getTmp_max() {
//        return tmp_max;
//    }
//
//    public void setTmp_max(String tmp_max) {
//        this.tmp_max = tmp_max;
//    }
//
//    public String getTem_min() {
//        return tem_min;
//    }
//
//    public void setTem_min(String tem_min) {
//        this.tem_min = tem_min;
//    }
//
//    @Override
//    public String toString() {
//        return "Weather{" +
//                "city='" + city + '\'' +
//                ", update_time='" + update_time + '\'' +
//                ", txt_d='" + txt_d + '\'' +
//                ", txt_n='" + txt_n + '\'' +
//                ", date='" + date + '\'' +
//                ", tmp_max='" + tmp_max + '\'' +
//                ", tem_min='" + tem_min + '\'' +
//                '}';
//    }
//}