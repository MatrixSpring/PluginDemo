package com.yty.libframe.crash;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;


public class CrashHandleNew{
    public static final String Tag = CrashHandleNew.class.getSimpleName();
    private static final String DEFAULT_HANDLER_PACKAGE_NAME = "com.android.internal.os";
    //CrashHandle实例
    private static CrashHandleNew INSTANCE = new CrashHandleNew();
    //程序的context对象 一般是application 所以不会内存泄漏
    private Context mContext;
    //系统默认的UncaughtException处理类
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    //用来存储设备信息和异常信息
    private Map<String, String> infos = new HashMap<>();
    private Map<String, String> regexMap = new HashMap<String, String>();
    //用于格式化日期,作为日志文件名的一部分
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.CHINA);
    //配置信息
    CrashConfig crashConfig;

    public void setConfig(@NonNull CrashConfig config) {
        crashConfig = config;
    }

    //单例模式私有构造方法
    private CrashHandleNew() {
    }

    public static CrashHandleNew getInstance() {
        return INSTANCE;
    }

    public void init(Context context) {
        mContext = context;
        //获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置该CrashHandler为程序的默认处理器

        //收集设备参数信息
        collectDeviceInfo(mContext);

//        final Thread.UncaughtExceptionHandler oldHandler = Thread.getDefaultUncaughtExceptionHandler();
//        if (oldHandler != null && !oldHandler.getClass().getName().startsWith(DEFAULT_HANDLER_PACKAGE_NAME)) {
//            Log.e(TAG, "IMPORTANT WARNING! You already have an UncaughtExceptionHandler, are you sure this is correct? If you use a custom UncaughtExceptionHandler, you must initialize it AFTER CustomActivityOnCrash! Installing anyway, but your original handler will not be called.");
//        }

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, final Throwable throwable) {
                Log.d("2222222222", "2222222222 sds " + saveCrashInfo2File(throwable));
            }
        });
    }


    public void uncaughtException(Thread thread, Throwable ex) {
        Log.d("2222222222", "2222222222 sds " + saveCrashInfo2File(ex));

//        if (null != mDefaultHandler && !handleException(ex)) {
//            // 如果用户没有处理则让系统默认的异常处理器来处理
//            mDefaultHandler.uncaughtException(thread, ex);
//        }

        Log.d("2222222222", "2222222222 11111 " + crashConfig.restartActivityClass);

        new Thread() {
            @Override
            public void run() {
                Intent intent = new Intent(mContext,crashConfig.restartActivityClass);
                @SuppressLint("WrongConstant") PendingIntent restartIntent = PendingIntent.getActivity(mContext, 0, intent, FLAG_ACTIVITY_NEW_TASK);
                AlarmManager mgr = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
                mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, restartIntent);
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        }.start();

//        if (null != mDefaultHandler && !handleException(ex)) {
//            // 未经过人为处理,则调用系统默认处理异常,弹出系统强制关闭的对话框
//            mDefaultHandler.uncaughtException(thread, ex);
//
//        } else {
//            // 已经人为处理,系统自己退出
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e1) {
//                e1.printStackTrace();
//            }
//            System.exit(1);
//        }
    }

    /**
     * 自定义错误处理，收集错误信息，发送错误报告等操作均在此完成
     *
     * @param ex
     * @return true：如果处理了该异常信息；否则返回 false
     */
    private boolean handleException(final Throwable ex) {
        if (null != ex) {
            return false;
        }
        Log.d("2222222222", "2222222222 222 " + saveCrashInfo2File(ex));
        //收集错误栈信息
//        Log.d("2222222222","2222222222 sds "+saveCrashInfo2File(ex));;
//        new Thread() {
//            @Override
//            public void run() {
//                Looper.prepare();
//                Looper.loop();
//                startIntent(saveCrashInfo2File(ex));
//            }
//        }.start();

        new Thread() {
            @Override
            public void run() {
                Intent intent = new Intent(mContext, crashConfig.restartActivityClass);
                @SuppressLint("WrongConstant") PendingIntent restartIntent = PendingIntent.getActivity(mContext, 0, intent, FLAG_ACTIVITY_NEW_TASK);
                AlarmManager mgr = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
                mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, restartIntent);
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        }.start();


//        startIntent(saveCrashInfo2File(ex));
        return true;
    }

    private String saveCrashInfo2File(Throwable ex) {
        //StringBuffer sb = getTraceInfo(ex);
        StringBuffer sb = new StringBuffer();
        sb.append(infos.toString()).append("\n");
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (null != cause) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        return sb.toString();
    }

    private void collectDeviceInfo(Context ctx) {
        try {
            PackageManager packageManager = ctx.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (null != packageInfo) {
                infos.put("versionName", packageInfo.versionName);
                infos.put("versionCode", "" + packageInfo.versionCode);
            }
            Field[] fields = Build.class.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
            }
        } catch (Exception e) {
            Log.e(Tag, e.getMessage());
        }
    }


    private void startIntent(String error) {
        if (null != crashConfig.restartActivityClass) {
            //跳转到app的activity界面 一般是主界面
            Intent intent = new Intent(mContext, crashConfig.restartActivityClass);
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            mContext.startActivity(intent);
        } else {
            //跳转到错误的默认界面
            Intent intent = new Intent(mContext, crashConfig.restartActivityClass);
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("ErrorInfo", error);
            mContext.startActivity(intent);
        }

    }

}
