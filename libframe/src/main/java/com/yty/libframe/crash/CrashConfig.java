package com.yty.libframe.crash;

import android.app.Activity;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class CrashConfig {
    public static final int BACKGROUND_MODE_SILENT = 0;
    public static final int BACKGROUND_MODE_SHOW_CUSTOM = 1;
    public static final int BACKGROUND_MODE_CRASH = 2;

    @IntDef({BACKGROUND_MODE_CRASH, BACKGROUND_MODE_SHOW_CUSTOM, BACKGROUND_MODE_SILENT})
    @Retention(RetentionPolicy.SOURCE)
    private @interface BackgroundMode {
    }

    public int backgroundMode = BACKGROUND_MODE_SHOW_CUSTOM;
    public boolean enabled = true;
    public boolean trackActivities = false;
    public int minTimeBetweenCrashesMs = 3000;
    public Integer errorDrawable = null;
    public Class<? extends Activity> restartActivityClass = null;

    public CrashHandleNew crashHandle;

    public static class Builder {
        static CrashConfig crashConfig;

        public static Builder create() {
            Builder builder = new Builder();
            crashConfig = new CrashConfig();
            crashConfig.crashHandle = CrashHandleNew.getInstance();
            return builder;
        }

        @NonNull
        public Builder backgroundMode(@BackgroundMode int backgroundMode) {
            crashConfig.backgroundMode = backgroundMode;
            return this;
        }

        @NonNull
        public Builder errorDrawable(@Nullable @DrawableRes Integer errorDrawable) {
            crashConfig.errorDrawable = errorDrawable;
            return this;
        }


        public Builder trackActivities(boolean trackActivities) {
            crashConfig.trackActivities = trackActivities;
            return this;
        }

        /**
         * Defines the time that must pass between app crashes to determine that we are not
         * in a crash loop. If a crash has occurred less that this time ago,
         * the error activity will not be launched and the system crash screen will be invoked.
         * The default is 3000.
         */
        public Builder minTimeBetweenCrashesMs(int minTimeBetweenCrashesMs) {
            crashConfig.minTimeBetweenCrashesMs = minTimeBetweenCrashesMs;
            return this;
        }


        @NonNull
        public Builder restartActivity(@Nullable Class<? extends Activity> restartActivityClass) {
            crashConfig.restartActivityClass = restartActivityClass;
            return this;
        }

        @NonNull
        public CrashConfig get() {
            return crashConfig;
        }

        public void apply() {
            crashConfig.crashHandle.setConfig(crashConfig);
        }
    }

}
