package com.example.nanchen.weatherdemo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库帮助类
 * Created by nanchen on 2016/6/20.
 */
public class DbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "city.db";
    private static final int DB_VERSION = 1;
    /**
     * @param context  所依托的上下文环境
     */
    public DbHelper(Context context) {
        super(context,DB_NAME,null,DB_VERSION);
    }

    /**
     * 创建数据库的表
     * @param db 数据库的操作类对象
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        //建表语句
        String sql = "create table city(_id integer primary key autoincrement,cityName text)";
        //执行建表语句
        db.execSQL(sql);
    }

    /**
     * 数据库更新的时候会调用这个方法
     * 当新版本大于旧版本就会自动调用这个方法
     * @param db            数据库的操作类对象
     * @param oldVersion    旧版本号
     * @param newVersion    新版本号
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
