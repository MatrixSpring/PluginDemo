package com.yty.libframe.base;

import android.app.Application;

public class AppReflect {
    static Application sAppContext;

    static {
        try{
            //先通过ActivityThread来获取Application Context
            Application application = (Application) Class.forName("android.app.ActivityThread").getMethod("currentApplication").invoke(null, (Object[]) null);
            if(application != null){
                sAppContext = application;
            }
            if(sAppContext == null){
                //第二种方式初始化context
                application = (Application) Class.forName("android.app.AppGlobals").getMethod("getInitialApplication").invoke(null, (Object[]) null);
                if (application != null) {
                    sAppContext = application;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    static public Application getAppContext(){
        return sAppContext;
    }

}
