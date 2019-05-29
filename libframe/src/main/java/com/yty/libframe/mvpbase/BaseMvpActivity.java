package com.yty.libframe.mvpbase;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.umeng.analytics.MobclickAgent;
import com.yty.libframe.base.BaseActivity;

/**
 * Created by dawn on 2018/3/7.
 */

public abstract class BaseMvpActivity<V extends BaseView,T extends BasePresenter<V>> extends BaseActivity implements View.OnClickListener  {
    public T presenter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initPresenter()是抽象方法，让view初始化自己的presenter
        presenter = initPresenter();
        //presenter和view的绑定
//        presenter.attach(BaseMvpActivity.this.getApplicationContext(),(V)this);
        presenter.attach(this,(V)this);
        //initActivity是抽象方法，让view完成自身各种控件的初始化
        initActivity(savedInstanceState);
        initWidget();

    }

    @Override
    protected void onResume() {
        presenter.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        presenter.detach();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        widgetClick(v);
    }

    public abstract T initPresenter();
    public abstract void initActivity(Bundle savedInstanceState);

    public abstract void initWidget();
    /**
     * view点击
     * @param v
     */
    public abstract void widgetClick(View v);

}
