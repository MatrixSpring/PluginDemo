package com.goglbo.libshare;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.webkit.JavascriptInterface;

import java.util.ArrayList;
import java.util.List;

public class ShareTools {
    //判断手机是否安装某款应用

    @JavascriptInterface
    public static boolean isAppInstallen(Context context, String packageName) {
//        PackageManager pm = context.getPackageManager();
//        boolean installed = false;
//        try {
//            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
//            installed = true;
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//            installed = false;
//        }
//        return installed;
        try {
            final PackageManager packageManager = context.getPackageManager();//获取packagemanager
            List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);//获取所有已安装程序的包信息
            List<String> pName = new ArrayList<String>();//用于存储所有已安装程序的包名
            //从pinfo中将包名字逐一取出，压入pName list中
            if(pinfo != null){
                for(int i = 0; i < pinfo.size(); i++){
                    String pn = pinfo.get(i).packageName;
                    pName.add(pn);
                }
            }
            return pName.contains(packageName);//判断pName中是否有目标程序的包名，有TRUE，没有FALSE
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
