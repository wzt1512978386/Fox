package com.example.fox;

import android.app.Application;
import android.os.Handler;


import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author: lenovo
 * @date: 2020/11/14
 */
public class MyApp extends Application {
    //全局调用

    //页面
    public static MainActivity mainActivity;


    //线程
    public static final Handler handler = new Handler();
    public static final Executor EXECUTOR = Executors.newCachedThreadPool();


    public static BTServer btServer;

    private static  MyApp mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }
    public static MyApp get(){
        return mInstance;
    }

}
