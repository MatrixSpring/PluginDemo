package com.yty.libframe.base;

import android.app.Application;


public class BaseApplication extends Application {
    private static Application sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    /**
     * 获得当前app运行的Application
     */
    public static Application getInstance() {
        return sInstance;
    }
}
