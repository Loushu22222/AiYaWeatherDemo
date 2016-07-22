package com.example.nanchen.weatherdemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.example.nanchen.weatherdemo.test.Person;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

public class SplashActivity extends AppCompatActivity {

    private ImageView image;

    public SharedPreferences sp;
    public SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);


        //这个方法一般写在工具类中
        sp = getSharedPreferences("isFirst",MODE_PRIVATE);
        boolean isFirstRun = sp.getBoolean("firstRun",true);

        if (isFirstRun){//第一次运行
            editor = sp.edit();
            editor.putBoolean("firstRun",false);
            editor.apply();
//            Toast.makeText(this,"这是你第一次运行程序!",Toast.LENGTH_SHORT).show();



            //文件存储缓存    --文件名字随便加个后缀可以让别人打不开
            try {
                //从程序输出
                FileOutputStream fos = this.openFileOutput("weather.dat",MODE_PRIVATE);//安卓特有的方法返回文件流
                //因为fos没有写入一个对象的方法，故使用封装的对象流
                ObjectOutputStream out = new ObjectOutputStream(fos);
                out.writeObject(new Person("刘世麟"));
                out.flush();
                out.close();
                fos.close();

//                Toast.makeText(this,"成功写入文件对象",Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            try {
                FileInputStream fis = this.openFileInput("weather.dat");
//                FileInputStream fis = new FileInputStream("weather.dat");
                ObjectInputStream in = new ObjectInputStream(fis);
                Person p  = (Person) in.readObject();
//                Toast.makeText(this,"读取到一个文件对象"+p.toString(),Toast.LENGTH_SHORT).show();
                in.close();
                fis.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (StreamCorruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }





        image = (ImageView) findViewById(R.id.splash_image);

        //使用android中的动画效果
        //定义一个渐变动画，透明度的值是0~1之间
        AlphaAnimation alpha = new AlphaAnimation(0.2f,1.0f);
        alpha.setDuration(3000);//定义动画的持续时间

        image.startAnimation(alpha);
        //对动画进行监听
        alpha.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //动画结束，进行页面跳转
                startActivity(new Intent(SplashActivity.this,MainActivity.class));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
