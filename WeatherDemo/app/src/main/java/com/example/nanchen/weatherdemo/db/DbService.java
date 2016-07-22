package com.example.nanchen.weatherdemo.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * 操作数据库
 * Created by nanchen on 2016/6/20.
 */
public class DbService {

    private DbHelper dbHelper;

    public DbService(Context context){
        dbHelper = new DbHelper(context);
    }

    /**
     * 插入一条数据
     */
    public void insertCity(String cityName){
        //通常都使用Readble打开，加入内存不够时，调用Readle不会影响，Writble有可能导致程序崩溃
        SQLiteDatabase db = dbHelper.getReadableDatabase();
//        String sql = "insert into city(cityName) values('?')";
//        db.execSQL(sql,new String[]{cityName});
//        db.execSQL("insert into city(cityName) values('成都')"
//        );

        ContentValues values = new ContentValues();
        values.put("cityName",cityName);
        db.insert("city",null,values);
        db.close();
    }

    /**
     * 通过城市查询返回一条数据
     */
    public List<String> findAll(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "select * from city";
        List<String> list = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql,null);
        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            String cityName = cursor.getString(1);
            Log.e("100",id+"--"+cityName);
            list.add(cityName);
        }
        db.close();
        return list;
    }

    /**
     * 删除一条城市信息
     * @param cityName
     */
    public void delete(String cityName){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "delete from city where cityName = ?";
        db.execSQL(sql,new String[]{cityName});

//        db.delete()
        db.close();
    }

}
