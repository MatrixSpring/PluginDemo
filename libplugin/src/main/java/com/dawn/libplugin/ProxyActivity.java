package com.dawn.libplugin;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.lang.reflect.Constructor;

public class ProxyActivity extends AppCompatActivity {
    private String mClassName;
    private PluginResEnv pluginResEnv;
    private IPluginLife pluginLife;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mClassName = getIntent().getStringExtra("className");
        pluginResEnv = PluginManager.getInstance().getPluginResEnv();
        launchPluginActivity();
    }

    /**
     * 通过这个方法跳转到插件的activity中，本app是在manifest中注册，可以绕过系统检测的activity
     */
    private void launchPluginActivity(){
        if(pluginResEnv == null){
            throw new RuntimeException("请先加载插件apk");
        }
        try {
            Class mClass = pluginResEnv.dexClassLoader.loadClass(mClassName);
            Constructor constructor = mClass.getConstructor();
            Object object = constructor.newInstance();
            //插件apk的activity， 注意没有生命周期 也没有上下文Context服务
            if(object instanceof IPluginLife){
                pluginLife = (IPluginLife) object;
                pluginLife.attach(this);
                Bundle bundle = new Bundle();
                bundle.putInt("FROM", IPluginLife.FROM_INTERNAL);
                pluginLife.onCreate(bundle);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        if (null != pluginLife) {
            pluginLife.onStart();
        }
        super.onStart();
    }

    @Override
    public Resources getResources() {
        return pluginResEnv != null ? pluginResEnv.resources : super.getResources();
    }

    @Override
    public AssetManager getAssets() {
        return pluginResEnv != null ? pluginResEnv.assetManager : super.getAssets();
    }

    @Override
    public ClassLoader getClassLoader() {
        return pluginResEnv != null ? pluginResEnv.dexClassLoader : super.getClassLoader();
    }
}
