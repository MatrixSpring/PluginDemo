package com.dawn.libplugin;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;

import java.io.File;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

public class PluginManager {
    public static volatile PluginManager instance;

    private PluginManager() {
    }

    public static synchronized PluginManager getInstance() {
        if (null != instance) {
            synchronized (PluginManager.class) {
                if (null != instance) {
                    instance = new PluginManager();
                }
            }
        }
        return instance;
    }

    private PluginResEnv pluginResEnv;
    private Context mContext;

    /**
     * 初始化上下文
     *
     * @param mContext
     */
    public void init(Context mContext) {
        this.mContext = mContext.getApplicationContext();

    }

    /**
     * 加载插件
     *
     * @param apkPath 插件路径
     */
    public void loadUrlApk(String apkPath) {
        PackageInfo packageInfo = mContext.getPackageManager().getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES | PackageManager.GET_RECEIVERS);
        if (null == packageInfo) {
            return;
        }
        DexClassLoader dexClassLoader = createDexClassLoader(apkPath);
        AssetManager assetManager = createAssertManager(apkPath);
        Resources resources = createResource(assetManager);

        pluginResEnv = new PluginResEnv(packageInfo, dexClassLoader, assetManager, resources);
    }

    private DexClassLoader createDexClassLoader(String apkPath) {
        //String dexPath, String optimizedDirectory, String librarySearchPath, ClassLoader parent
        File dexFile = mContext.getDir("dex",Context.MODE_PRIVATE);
        DexClassLoader dexClassLoader = new DexClassLoader(apkPath,dexFile.getAbsolutePath(),null,mContext.getClassLoader());
        return dexClassLoader;
    }

    private AssetManager createAssertManager(String apkPath) {
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            Method method = AssetManager.class.getDeclaredMethod("addAssetPath", String.class);
            //把自己路径加进来
            method.invoke(assetManager,apkPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Resources createResource(AssetManager assetManager) {
        Resources resources = mContext.getResources();
        //AssetManager assets, DisplayMetrics metrics, Configuration config
        return new Resources(assetManager,resources.getDisplayMetrics(), resources.getConfiguration());
    }

    public PluginResEnv getPluginResEnv() {
        return pluginResEnv;
    }

    public void setPluginResEnv(PluginResEnv pluginResEnv) {
        this.pluginResEnv = pluginResEnv;
    }

}
