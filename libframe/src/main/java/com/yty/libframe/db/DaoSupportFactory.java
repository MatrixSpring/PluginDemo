package com.yty.libframe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.lang.ref.WeakReference;

public class DaoSupportFactory {
    // 持有外部数据库的引用
    private SQLiteDatabase mSqLiteDatabase;
    private static final String db_name = "yty";
    private static WeakReference<Context> weakReference;

    public static class DaoSupportInstance{
        public static DaoSupportFactory daoSupportFactory=new DaoSupportFactory();
    }

    public static DaoSupportFactory getInstance(Context context){
        weakReference = new WeakReference<>(context);
        return DaoSupportInstance.daoSupportFactory;
    }

    private DaoSupportFactory(){
        // 把数据库放到内存卡里面  判断是否有存储卡 6.0要动态申请权限
        File dbRoot = new File(weakReference.get().getFilesDir().getAbsolutePath() + File.separator + db_name + File.separator + "database");
        if (!dbRoot.exists()) {
            dbRoot.mkdirs();
        }
        File dbFile = new File(dbRoot, "test.db");

        // 打开或者创建一个数据库
        mSqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
    }

    public <T> IDaoSupport<T> getDao(Class<T> clazz) {
        IDaoSupport<T> daoSupport = new DaoSupport();
        daoSupport.init(mSqLiteDatabase, clazz);
        return daoSupport;
    }
}
