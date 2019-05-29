package com.yty.libframe.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

public class ChannelManager {

    public static String getCurrentChannelName(Context context) {
        String installChannel = getMetadata(context, "InstallChannel");
        installChannel = installChannel == null || installChannel.trim().length() == 0 ? "dawn" : installChannel;
        return installChannel;
    }

    public static <T> T getMetadata(Context context, String key) {
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = info.metaData;
            if (bundle == null) return null;
            return (T) bundle.get(key);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return null;
    }
}
