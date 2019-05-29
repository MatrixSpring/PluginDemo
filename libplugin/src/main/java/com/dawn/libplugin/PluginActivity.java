package com.dawn.libplugin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class PluginActivity extends AppCompatActivity implements IPluginLife {
    private int mFrom = FROM_INTERNAL;
    private Activity mProxyActivity;

    @Override
    public void attach(Activity proxyActivity) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if(mFrom == FROM_INTERNAL){
            super.onCreate(savedInstanceState);
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        Log.d("sdsdsdsd","sdsdsdsd setContentView "+layoutResID+" : "+mFrom);
        if(mFrom == FROM_INTERNAL){
            super.setContentView(layoutResID);
        }else{
            mProxyActivity.setContentView(layoutResID);
        }
    }

    @Override
    public void onStart() {
        if(mFrom == FROM_INTERNAL){
            super.onStart();
        }
    }

    @Override
    public void onRestart() {
        if(mFrom == FROM_INTERNAL){
            super.onRestart();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

    }

    @Override
    public void onResume() {
        if(mFrom == FROM_INTERNAL){
            super.onResume();
        }
    }

    @Override
    public void onPause() {
        if(mFrom == FROM_INTERNAL){
            super.onPause();
        }
    }

    @Override
    public void onStop() {
        if(mFrom == FROM_INTERNAL){
            super.onStop();
        }
    }

    @Override
    public void onDestroy() {
        if(mFrom == FROM_INTERNAL){
            super.onDestroy();
        }
    }
}
