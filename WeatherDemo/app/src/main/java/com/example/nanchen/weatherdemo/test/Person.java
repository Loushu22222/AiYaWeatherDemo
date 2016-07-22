package com.example.nanchen.weatherdemo.test;

import java.io.Serializable;

/**
 * 用于测试文件存储的类
 * Created by nanchen on 2016/6/22.
 */
public class Person implements Serializable{
    private String name;

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                '}';
    }
}
