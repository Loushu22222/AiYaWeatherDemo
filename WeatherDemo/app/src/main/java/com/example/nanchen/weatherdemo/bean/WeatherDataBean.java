package com.example.nanchen.weatherdemo.bean;

import java.io.Serializable;
import java.util.List;

/**
 *
 * Created by nanchen on 2016/6/16.
 */
public class WeatherDataBean implements Serializable{

    public Result result;//返回结果
    public static class Result implements Serializable{
        public Realtime realtime;//当前时间信息
        public static class Realtime implements Serializable{
            public Wind wind;//当前日期风信息
            public static class Wind implements Serializable{
                public String direct;//风向
                public String power;//风的级别
            }
            public String time;//发布时间
            public Weather weather;
            public static class Weather implements Serializable{
                public String info;//天气情况
                public String temperature;//温度
                public String humidity;//湿度
            }
            public String date;//当前日期
            public String city_name;//城市名字
            public String week;//星期
            public String moon;//农历日期
        }

        public List<FurtureWeather> weather;
        public static class FurtureWeather implements Serializable{
            public String date;//未来几天天气的日期
            public String week;//未来几天天气的星期几
            public String nongli;//未来几天天气的农历日期
            public Info info;//未来几天天气的info
            public static class Info implements Serializable{
                public List<String> day; //白天的天气信息//可切割最高温
                public List<String> night;//晚上的天气信息//可切割最低温
            }
        }

        public Life life;//生活信息
        public static class Life implements Serializable{
            public Info info;
            public static class Info implements Serializable{
                public List<String> kongtiao;//空调
                public List<String> yundong;//运动
                public List<String> ziwaixian;//紫外线
                public List<String> xiche;//洗车
                public List<String> chuanyi;//穿衣
            }
        }
    }

}
