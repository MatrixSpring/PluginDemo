package com.yty.libframe.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.umeng.analytics.MobclickAgent;

public class BaseFragment extends Fragment {
    public WindowManager manager;
    public DisplayMetrics metrics;
    public int width;

    private boolean isFirst = true;
    private boolean isPrepared;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = getActivity().getWindowManager();
        metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        width = metrics.widthPixels;  //以要素为单位
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //统计fragment 进入的事件
//        MobclickAgent.onPageStart(this.getClass().getName());
    }

    @Override
    public void onPause() {
        super.onPause();
//        MobclickAgent.onPageEnd(this.getClass().getName());
    }


}
