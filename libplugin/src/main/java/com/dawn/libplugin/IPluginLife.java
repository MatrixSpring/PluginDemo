package com.dawn.libplugin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

public interface IPluginLife {
    int FROM_INTERNAL = 0;  // 内部跳转
    int FROM_EXTERNAL = 1;  // 外部跳转

    void attach(Activity proxyActivity);

    void onCreate(Bundle savedInstanceState);

    void onStart();

    void onRestart();

    void onActivityResult(int requestCode, int resultCode, @Nullable Intent data);

    void onResume();

    void onPause();

    void onStop();

    void onDestroy();
}
