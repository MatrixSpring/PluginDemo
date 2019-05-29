package com.yty.libframe.db;

import android.database.sqlite.SQLiteDatabase;

import java.util.List;

public interface IDaoSupport<T> {
    void init(SQLiteDatabase sqLiteDatabase, Class<T> tClass);
    //插入数据
    public long insert(T t);
    //批量插入数据
    public void insert(List<T> dataList);
    //获取专门的查询的支持类
    QuerySupport<T> querySupport();
    //
    int delete(String whereClause, String... whereArgs);

    int update(T obj, String whereClause, String... whereArgs);
}
