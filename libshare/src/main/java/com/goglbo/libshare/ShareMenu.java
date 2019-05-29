package com.goglbo.libshare;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMWeb;


import java.util.List;


public class ShareMenu implements PopupWindow.OnDismissListener {
    public static final String SHARE_WX = "wx";
    public static final String SHARE_WXC = "wxc";
    public static final String SHARE_QQ = "qq";
    public static final String SHARE_QQZ = "qqz";
    public static final String SHARE_WB = "wb";

    private static final String TAG = "WidgetPopupPosition";
    private static final float DEFAULT_ALPHA = 0.7f;
    private Context mContext;
    private int mWidth;
    private int mHeight;
    private boolean mIsFocusable = true;
    private boolean mIsOutside = true;
    private int mResLayoutId = -1;
    private View mContentView;
    private PopupWindow mPopupWindow;
    private int mAnimationStyle = -1;

    private boolean mClippEnable = true;//default is true
    private boolean mIgnoreCheekPress = false;
    private int mInputMode = -1;
    private PopupWindow.OnDismissListener mOnDismissListener;
    private int mSoftInputMode = -1;
    private boolean mTouchable = true;//default is ture
    private View.OnTouchListener mOnTouchListener;

    private Window mWindow;//当前Activity 的窗口
    /**
     * 弹出PopWindow 背景是否变暗，默认不会变暗。
     */
    private boolean mIsBackgroundDark = false;

    private float mBackgroundDrakValue = 0;// 背景变暗的值，0 - 1
    /**
     * 设置是否允许点击 PopupWindow之外的地方，关闭PopupWindow
     */
    private boolean enableOutsideTouchDisMiss = true;// 默认点击pop之外的地方可以关闭
    ShareStatusCallback shareStatusCallback = null;

    private List<ShareItemView> mDatas;
    private ShareData shareData;

    private LinearLayout ll_share_wx;
    private LinearLayout ll_share_wxc;
    private LinearLayout ll_share_qq;
    private LinearLayout ll_share_qqz;
    private LinearLayout ll_share_weibo;


    private ShareMenu(Context context) {
        mContext = context;
    }

    /**
     * 相对于父控件的位置（通过设置Gravity.Center,Gravity.Bottom）
     *
     * @param parent
     * @param gravity
     * @param x
     * @param y
     * @return
     */
    public ShareMenu showAtLocation(View parent, int gravity, int x, int y) {
        if (mPopupWindow != null) {
            mPopupWindow.showAtLocation(parent, gravity, x, y);
        }
        return this;
    }

    public void setShareCallBack(ShareStatusCallback shareStatusCallback) {
        this.shareStatusCallback = shareStatusCallback;
    }

    /**
     * 设置属性
     *
     * @param popupWindow
     */
    private void apply(PopupWindow popupWindow) {
        popupWindow.setClippingEnabled(mClippEnable);
        if (mIgnoreCheekPress) {
            popupWindow.setIgnoreCheekPress();
        }
        if (mInputMode != -1) {
            popupWindow.setInputMethodMode(mInputMode);
        }
        if (mSoftInputMode != -1) {
            popupWindow.setSoftInputMode(mSoftInputMode);
        }
        if (mOnDismissListener != null) {
            popupWindow.setOnDismissListener(mOnDismissListener);
        }
        if (mOnTouchListener != null) {
            popupWindow.setTouchInterceptor(mOnTouchListener);
        }
        popupWindow.setTouchable(mTouchable);
    }

    private PopupWindow build() {
        if (mContentView == null) {
            mContentView = LayoutInflater.from(mContext).inflate(R.layout.widget_share_menu, null);
        }
        Activity activity = (Activity) mContentView.getContext();
        if (activity != null && mIsBackgroundDark) {
            //如果设置的值在0 - 1的范围内，则用设置的值，否则用默认值
            final float alpha = (mBackgroundDrakValue > 0 && mBackgroundDrakValue < 1) ? mBackgroundDrakValue : DEFAULT_ALPHA;
            mWindow = activity.getWindow();
            WindowManager.LayoutParams params = mWindow.getAttributes();
            params.alpha = alpha;
            mWindow.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            mWindow.setAttributes(params);
        }

        if (mWidth != 0 && mHeight != 0) {
            mPopupWindow = new PopupWindow(mContentView, mWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        } else {
            mPopupWindow = new PopupWindow(mContentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        if (mAnimationStyle != -1) {
            mPopupWindow.setAnimationStyle(mAnimationStyle);
        }

        if (mWidth == 0 || mHeight == 0) {
            mPopupWindow.getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            //如果外面没有设置宽高的情况下，计算宽高并赋值
            mWidth = mPopupWindow.getContentView().getMeasuredWidth();
            mHeight = mPopupWindow.getContentView().getMeasuredHeight();
        }

        if (!enableOutsideTouchDisMiss) {
            //注意这三个属性必须同时设置，不然不能disMiss，以下三行代码在Android 4.4 上是可以，然后在Android 6.0以上，下面的三行代码就不起作用了，就得用下面的方法
            mPopupWindow.setFocusable(true);
            mPopupWindow.setOutsideTouchable(false);
            mPopupWindow.setBackgroundDrawable(null);
            //注意下面这三个是contentView 不是PopupWindow
            mPopupWindow.getContentView().setFocusable(true);
            mPopupWindow.getContentView().setFocusableInTouchMode(true);
            mPopupWindow.getContentView().setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        mPopupWindow.dismiss();
                        return true;
                    }
                    return false;
                }
            });
            //在Android 6.0以上 ，只能通过拦截事件来解决
            mPopupWindow.setTouchInterceptor(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    final int x = (int) event.getX();
                    final int y = (int) event.getY();

                    if ((event.getAction() == MotionEvent.ACTION_DOWN)
                            && ((x < 0) || (x >= mWidth) || (y < 0) || (y >= mHeight))) {
                        return true;
                    } else if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                        return true;
                    }
                    return false;
                }
            });
        } else {
            mPopupWindow.setFocusable(mIsFocusable);
            mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mPopupWindow.setOutsideTouchable(mIsOutside);
        }

        // 添加dissmiss 监听
        mPopupWindow.setOnDismissListener(this);
        apply(mPopupWindow);//设置一些属性
        // update
        mPopupWindow.update();

        if (null != mContentView) {
            ll_share_wx = mContentView.findViewById(R.id.ll_share_wx);
            ll_share_wxc = mContentView.findViewById(R.id.ll_share_wxc);
            ll_share_qq = mContentView.findViewById(R.id.ll_share_qq);
            ll_share_qqz = mContentView.findViewById(R.id.ll_share_qqz);
            ll_share_weibo = mContentView.findViewById(R.id.ll_share_weibo);

            ll_share_wx.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareFunction(mContext, SHARE_MEDIA.WEIXIN, shareData);
                    if (null != shareStatusCallback) {
                        shareStatusCallback.statusCallback(SHARE_MEDIA.WEIXIN, 1, "");
                    }
                }
            });
            ll_share_wxc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareFunction(mContext, SHARE_MEDIA.WEIXIN_CIRCLE, shareData);
                    if (null != shareStatusCallback) {
                        shareStatusCallback.statusCallback(SHARE_MEDIA.WEIXIN_CIRCLE, 1, "");
                    }
                }
            });
            ll_share_qq.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareFunction(mContext, SHARE_MEDIA.QQ, shareData);
                    if (null != shareStatusCallback) {
                        shareStatusCallback.statusCallback(SHARE_MEDIA.QQ, 1, "");
                    }
                }
            });
            ll_share_qqz.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareFunction(mContext, SHARE_MEDIA.QZONE, shareData);
                    if (null != shareStatusCallback) {
                        shareStatusCallback.statusCallback(SHARE_MEDIA.QZONE, 1, "");
                    }
                }
            });

            ll_share_weibo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareFunction(mContext, SHARE_MEDIA.SINA, shareData);
                    if (null != shareStatusCallback) {
                        shareStatusCallback.statusCallback(SHARE_MEDIA.SINA, 1, "");
                    }
                }
            });
        }
        return mPopupWindow;
    }

    @Override
    public void onDismiss() {
        dissmiss();
    }

    /**
     * 关闭popWindow
     */
    public void dissmiss() {
        if (mOnDismissListener != null) {
            mOnDismissListener.onDismiss();
        }

        //如果设置了背景变暗，那么在dissmiss的时候需要还原
        if (mWindow != null) {
            WindowManager.LayoutParams params = mWindow.getAttributes();
            params.alpha = 1.0f;
            mWindow.setAttributes(params);
        }
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }

    public PopupWindow getPopupWindow() {
        return mPopupWindow;
    }


    public static class ViewBuilder {
        private ShareMenu mCustomPopWindow;

        public ViewBuilder(Context context) {
            mCustomPopWindow = new ShareMenu(context);
        }

        public ViewBuilder size(int width, int height) {
            mCustomPopWindow.mWidth = width;
            mCustomPopWindow.mHeight = height;
            return this;
        }


        public ViewBuilder setFocusable(boolean focusable) {
            mCustomPopWindow.mIsFocusable = focusable;
            return this;
        }

        public ViewBuilder setView(int resLayoutId) {
            mCustomPopWindow.mResLayoutId = R.layout.widget_share_menu;
            mCustomPopWindow.mContentView = null;
            return this;
        }

        public ViewBuilder setOutsideTouchable(boolean outsideTouchable) {
            mCustomPopWindow.mIsOutside = outsideTouchable;
            return this;
        }

        /**
         * 设置弹窗动画
         *
         * @param animationStyle
         * @return
         */
        public ViewBuilder setAnimationStyle(int animationStyle) {
            mCustomPopWindow.mAnimationStyle = animationStyle;
            return this;
        }

        public ViewBuilder setOnDissmissListener(PopupWindow.OnDismissListener onDissmissListener) {
            mCustomPopWindow.mOnDismissListener = onDissmissListener;
            return this;
        }

        public ViewBuilder setSoftInputMode(int softInputMode) {
            mCustomPopWindow.mSoftInputMode = softInputMode;
            return this;
        }

        public ViewBuilder setTouchable(boolean touchable) {
            mCustomPopWindow.mTouchable = touchable;
            return this;
        }

        /**
         * 设置背景变暗是否可用
         *
         * @param isDark
         * @return
         */
        public ViewBuilder enableBackgroundDark(boolean isDark) {
            mCustomPopWindow.mIsBackgroundDark = isDark;
            return this;
        }

        /**
         * 设置背景变暗的值
         *
         * @param darkValue
         * @return
         */
        public ViewBuilder setBgDarkAlpha(float darkValue) {
            mCustomPopWindow.mBackgroundDrakValue = darkValue;
            return this;
        }

        /**
         * 设置是否允许点击 PopupWindow之外的地方，关闭PopupWindow
         *
         * @param disMiss
         * @return
         */
        public ViewBuilder enableOutsideTouchableDissmiss(boolean disMiss) {
            mCustomPopWindow.enableOutsideTouchDisMiss = disMiss;
            return this;
        }

        public ViewBuilder setListData(List<ShareItemView> mDatas) {
            mCustomPopWindow.mDatas = mDatas;
            return this;
        }

        public ViewBuilder setShareData(ShareData shareData) {
            mCustomPopWindow.shareData = shareData;
            return this;
        }

        public ShareMenu create() {
            //构建PopWindow
            mCustomPopWindow.build();
            return mCustomPopWindow;
        }
    }

    private void shareFunction(final Context context, final SHARE_MEDIA platform, final ShareData shareData) {
        if (null == shareData) {
            return;
        }
        String packName = "";
        if (platform == SHARE_MEDIA.WEIXIN || platform == SHARE_MEDIA.WEIXIN_CIRCLE) {
            packName = "com.tencent.mm";
        } else if (platform == SHARE_MEDIA.QQ || platform == SHARE_MEDIA.QZONE) {
            packName = "com.tencent.mobileqq";
        } else if (platform == SHARE_MEDIA.SINA) {
            packName = "com.sina.weibo";
        }


        //判断是否安装
        if (!ShareTools.isAppInstallen(mContext, packName)) {
            if (null != shareStatusCallback) {
                shareStatusCallback.statusCallback(platform, -2, "");
            }
            return;
        }

        final UMShareListener umShareListener = new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
            }

            @Override
            public void onResult(SHARE_MEDIA share_media) {
                if (null != shareStatusCallback) {
                    shareStatusCallback.statusCallback(share_media, 0, "");
                }
            }

            @Override
            public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                if (null != shareStatusCallback) {
                    shareStatusCallback.statusCallback(share_media, -1, throwable.getMessage());
                }
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {
                if (null != shareStatusCallback) {
                    shareStatusCallback.statusCallback(share_media, -3, mContext.getString(R.string.canneldown));
                }
            }
        };

        if (platform == SHARE_MEDIA.WEIXIN) {
            UMWeb umWeb = new UMWeb(shareData.shareUrl.replace("?",SHARE_WX));
            umWeb.setTitle(shareData.title);
            umWeb.setDescription(shareData.description);
            new ShareAction((Activity) context).withMedia(umWeb)
                    .setPlatform(platform)
                    .setCallback(umShareListener)
                    .share();
        } else if (platform == SHARE_MEDIA.WEIXIN_CIRCLE) {
            UMWeb umWeb = new UMWeb(shareData.shareUrl.replace("?",SHARE_WXC));
            umWeb.setTitle(shareData.title);
            umWeb.setDescription(shareData.description);
            new ShareAction((Activity) context).withMedia(umWeb)
                    .setPlatform(platform)
                    .setCallback(umShareListener)
                    .share();
        } else if (platform == SHARE_MEDIA.QQ) {
            UMWeb umWeb = new UMWeb(shareData.shareUrl.replace("?",SHARE_QQ));
            umWeb.setTitle(shareData.title);
            umWeb.setDescription(shareData.description);
            new ShareAction((Activity) context).withMedia(umWeb)
                    .setPlatform(platform)
                    .setCallback(umShareListener)
                    .share();
        } else if (platform == SHARE_MEDIA.QZONE) {
            UMWeb umWeb = new UMWeb(shareData.shareUrl.replace("?",SHARE_QQZ));
            umWeb.setTitle(shareData.title);
            umWeb.setDescription(shareData.description);
            new ShareAction((Activity) context).withMedia(umWeb)
                    .setPlatform(platform)
                    .setCallback(umShareListener)
                    .share();
        } else if (platform == SHARE_MEDIA.SINA) {
            UMWeb umWeb = new UMWeb(shareData.shareUrl.replace("?",SHARE_WB));
            umWeb.setTitle(shareData.title);
            umWeb.setDescription(shareData.description);
            new ShareAction((Activity) context).withMedia(umWeb)
                    .setPlatform(platform)
                    .setCallback(umShareListener)
                    .share();
        }
    }

    public interface ShareStatusCallback {
        void statusCallback(SHARE_MEDIA share_media, int type, String message);
    }

    private String getCurrentTime() {
        return System.currentTimeMillis() + "";
    }


}
