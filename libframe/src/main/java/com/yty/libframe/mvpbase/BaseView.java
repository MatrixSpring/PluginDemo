package com.yty.libframe.mvpbase;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by dawn on 2018/3/7.
 */

public interface BaseView {
    @IntDef({BaseViewType.SUCCESS, BaseViewType.FAIL, BaseViewType.ERROR, BaseViewType.GOTO, BaseViewType.CHECK})
    @Retention(RetentionPolicy.SOURCE)
    public @interface BaseViewType {
        int SUCCESS = 0x1;
        int FAIL = 0x2;
        int ERROR = 0x4;
        int GOTO = 0x6;
        int CHECK = 0x8;
    }

    public void showMessage(@BaseViewType int status, Object message, String idStr);

    void showProgress();

    void onCompleted();

    void onError(Throwable e);
}
