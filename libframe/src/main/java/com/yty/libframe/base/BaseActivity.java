package com.yty.libframe.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;
import io.reactivex.subjects.PublishSubject;

public class BaseActivity extends AppCompatActivity {
    public WindowManager manager;
    public DisplayMetrics metrics;
    public int width;

    public final PublishSubject<ActivityLifeCycleEvent> lifecycleSubject = PublishSubject.create();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = getWindowManager();
        metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        width = metrics.widthPixels;  //以要素为单位

        Class<? extends Activity> clazz = getClass();
        if (clazz.isAnnotationPresent(ContentView.class)) {
            setContentView(clazz.getAnnotation(ContentView.class).value());
        }
        ButterKnife.bind(this);
        lifecycleSubject.onNext(ActivityLifeCycleEvent.CREATE);
    }


    @Override
    protected void onResume() {
        super.onResume();
//        MobclickAgent.onPageStart(this.getClass().getName());
    }

    @Override
    protected void onPause() {
        lifecycleSubject.onNext(ActivityLifeCycleEvent.PAUSE);
        super.onPause();
//        MobclickAgent.onPageEnd(this.getClass().getName());
    }



    @Override
    protected void onStop() {
        lifecycleSubject.onNext(ActivityLifeCycleEvent.STOP);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        lifecycleSubject.onNext(ActivityLifeCycleEvent.DESTROY);
    }
}
