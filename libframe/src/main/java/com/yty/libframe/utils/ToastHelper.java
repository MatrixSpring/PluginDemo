package com.yty.libframe.utils;

import android.content.Context;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;

import com.yty.libframe.base.AppReflect;

import java.lang.ref.WeakReference;

/**
 * Created by zhaokaiqiang on 15/8/17.
 */
public class ToastHelper {

    public static void showShort(CharSequence msg) {
        Context context = AppReflect.getAppContext();
        makeAndShow(context, msg, Toast.LENGTH_SHORT);
    }

    public static void showShort(int res) {
        Context context = AppReflect.getAppContext();
        makeAndShow(context, res, Toast.LENGTH_SHORT);
    }

    public static void showLong(int res) {
        Context context = AppReflect.getAppContext();
        makeAndShow(context, res, Toast.LENGTH_LONG);
    }

    public static void showLong(CharSequence msg) {
        Context context = AppReflect.getAppContext();
        makeAndShow(context, msg, Toast.LENGTH_LONG);
    }

    private static void makeAndShow(Context context, @StringRes int res, int length){
        if (context != null) {
            makeAndShow(context, context.getText(res), length);
        }
    }

    private static WeakReference<Toast> toastWeakReference;
    private static void makeAndShow(Context context, CharSequence msg, int length){
        if (context != null && !TextUtils.isEmpty(msg)) {
            Toast toast = null != toastWeakReference ? toastWeakReference.get() : null;
            if (null == toast) {
                toast = GSToast.makeText(context, msg, length);
                toastWeakReference = new WeakReference<Toast>(toast);
                toast.setGravity(Gravity.CENTER, 0, 0);
            } else {
                toast.setText(msg);
                toast.setDuration(length);
            }
            toast.show();
        }
    }

}
