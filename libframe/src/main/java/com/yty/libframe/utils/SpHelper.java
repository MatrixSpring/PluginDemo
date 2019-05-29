/*
 * lwxkey.130725
 * 对SharePreference 操作的工具类
 */

package com.yty.libframe.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.yty.libframe.R;


public class SpHelper {
    private SharedPreferences sp;
    private volatile static SpHelper instance;

    // 密钥
    private final static String secretKey = "wukongshequ@gmail.com$#wk2014#$";
    // 向量
    private final static String iv = "01234567";
    // 加解密统一使用的编码方式
    private final static String encoding = "utf-8";

    /*********************************************/

    public static SpHelper getInstance(Context context, String app_name) {
        if (instance == null) {
            synchronized (SpHelper.class) {
                if (instance == null) {
                    instance = new SpHelper(context, app_name);
                }
            }
        }
        return instance;
    }

    public static SpHelper getInstance(Context context) {
        if (instance == null) {
            getInstance(context, context.getResources().getString(R.string.app_name));
        }
        return instance;
    }

    private SpHelper(Context context, String app_name) {
        super();
        sp = context.getSharedPreferences(app_name, Context.MODE_PRIVATE);
    }

    /*********************************************/
    public void setString(String keyStr, String valueStr) {
        SharedPreferences.Editor editor;
        editor = sp.edit();
        editor.putString(keyStr, valueStr);
        editor.apply();
    }

    public void setInt(String keyStr, int valueInt) {
        SharedPreferences.Editor editor;
        editor = sp.edit();
        editor.putInt(keyStr, valueInt);
        editor.apply();
    }

    public void setFloat(String keyStr, float valueFloat) {
        SharedPreferences.Editor editor;
        editor = sp.edit();
        editor.putFloat(keyStr, valueFloat);
        editor.apply();
    }

    public void setBoolean(String keyStr, Boolean valueBoolean) {
        SharedPreferences.Editor editor;
        editor = sp.edit();
        editor.putBoolean(keyStr, valueBoolean);
        editor.apply();
    }

    public void setLong(String keyStr, long valueFloat) {
        SharedPreferences.Editor editor;
        editor = sp.edit();
        editor.putLong(keyStr, valueFloat);
        editor.apply();
    }

    /*********************************************/
    public String getString(String keyStr, String defaultValue) {
        return sp.getString(keyStr, defaultValue);
    }

    public int getInt(String keyStr, int defaultValue) {
        return sp.getInt(keyStr, defaultValue);
    }

    public boolean getBoolean(String keyStr, Boolean defaultValue) {
        return sp.getBoolean(keyStr, defaultValue);
    }

    public float getFloat(String keyStr, float defaultValue) {
        return sp.getFloat(keyStr, defaultValue);
    }

    public long getLong(String keyStr, long defaultValue) {
        return sp.getLong(keyStr, defaultValue);
    }

    /*********************************************/
    public void cleanData() {
        SharedPreferences.Editor editor;
        editor = sp.edit();
        editor.clear();
        editor.apply();
    }

}
