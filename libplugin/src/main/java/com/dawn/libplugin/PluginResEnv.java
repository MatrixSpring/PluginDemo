package com.dawn.libplugin;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;

import dalvik.system.DexClassLoader;

public class PluginResEnv {
    public PackageInfo packageInfo;
    public DexClassLoader dexClassLoader;
    public AssetManager assetManager;
    public Resources resources;

    public PluginResEnv() {
    }

    public PluginResEnv(PackageInfo packageInfo, DexClassLoader dexClassLoader, AssetManager assetManager, Resources resources) {
        this.packageInfo = packageInfo;
        this.dexClassLoader = dexClassLoader;
        this.assetManager = assetManager;
        this.resources = resources;
    }
}
