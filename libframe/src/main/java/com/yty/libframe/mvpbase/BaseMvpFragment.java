package com.yty.libframe.mvpbase;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.yty.libframe.base.BaseFragment;
import com.yty.libframe.event.EmptyMsg;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;


/**
 * Created by dawn on 2018/3/7.
 */

public abstract class BaseMvpFragment<V extends BaseView,T extends BasePresenter<V>> extends BaseFragment implements View.OnClickListener{
    public T presenter;
    public WindowManager manager;
    public DisplayMetrics metrics;
    public int width;
    public View rootView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initPresenter()是抽象方法，让view初始化自己的presenter
        presenter = initPresenter();
        //presenter和view的绑定
        presenter.attach(this.getContext(),(V)this);
        EventBus.getDefault().register(this);
        //initActivity是抽象方法，让view完成自身各种控件的初始化
        manager = getActivity().getWindowManager();
        metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        width = metrics.widthPixels;  //以要素为单位


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = initWidget(inflater,container,savedInstanceState);
        ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    @Override
    public void onResume() {
        presenter.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        presenter.detach();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EmptyMsg event) {

    }


    @Override
    public void onClick(View v) {
        widgetClick(v);
    }

    public abstract T initPresenter();
    public abstract void initFragment(Bundle savedInstanceState);

    public abstract View initWidget(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    public abstract void initView();

    public abstract void reload();

    public abstract void fetchData();


    /**
     * view点击
     * @param v
     */
    public abstract void widgetClick(View v);

}
