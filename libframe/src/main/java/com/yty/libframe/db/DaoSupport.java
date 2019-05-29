package com.yty.libframe.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class DaoSupport<T> implements IDaoSupport<T> {
    private String TAG = "DaoSupport";
    private SQLiteDatabase mSqLiteDatabase;
    //范型类
    private Class<T> mClass;
    private static final Object[] mPutMethodArgs = new Object[2];

    private static final Map<String, Method> mPutMethods = new ArrayMap<>();

    private QuerySupport<T> mQuerySupport;

    @Override
    public void init(SQLiteDatabase sqLiteDatabase, Class<T> tClass) {
        this.mSqLiteDatabase = sqLiteDatabase;
        this.mClass = tClass;

        //创建表
//        create table if not exists Student(
//                id integer primary key autoincrement,
//                name text,
//                age integer,
//                flag boolean
//        )

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("create table if not exists ")
                .append(DaoUtils.getTableName(mClass))
                .append("(id integer primary key autoincrement, ");

        Field[] fields = mClass.getDeclaredFields();
        for(Field field: fields){
            field.setAccessible(true);
            String name = field.getName();
            String type = field.getType().getSimpleName();// int String boolean
            //  type需要进行转换 int --> integer, String text;
            stringBuffer.append(name).append(DaoUtils.getColumnType(type)).append(", ");
        }

        stringBuffer.replace(stringBuffer.length()-2, stringBuffer.length(),")");
        String createTableSql = stringBuffer.toString();

        // 创建表
        mSqLiteDatabase.execSQL(createTableSql);
    }

    @Override
    public long insert(T t) {
//        ContentValues values = new ContentValues();
//        values.put("name",person.getName());
//        values.put("age",person.getAge());
//        values.put("flag",person.getFlag());
//        db.insert("Person",null,values);

        // 使用的其实还是  原生的使用方式，只是我们是封装一下而已
        ContentValues values = contentValuesByObj(t);

        // null  速度比第三方的快一倍左右
        return mSqLiteDatabase.insert(DaoUtils.getTableName(mClass), null, values);
    }

    @Override
    public void insert(List<T> dataList) {
        //批量插入数据采用 采用事务 提高 性能
        mSqLiteDatabase.beginTransaction();
        for(T data: dataList){
            //采用单条插入
            insert(data);
        }
        mSqLiteDatabase.setTransactionSuccessful();
        mSqLiteDatabase.endTransaction();
    }

    @Override
    public QuerySupport<T> querySupport() {
        if (null == mQuerySupport){
            mQuerySupport = new QuerySupport<>(mSqLiteDatabase, mClass);
        }
        return mQuerySupport;
    }

    @Override
    public int delete(String whereClause, String... whereArgs) {
        return mSqLiteDatabase.delete(DaoUtils.getTableName(mClass), whereClause, whereArgs);
    }

    @Override
    public int update(T obj, String whereClause, String... whereArgs) {
        ContentValues values = contentValuesByObj(obj);
        return mSqLiteDatabase.update(DaoUtils.getTableName(mClass),values,whereClause,whereArgs);
    }

    private ContentValues contentValuesByObj(T obj){
        // 第三方的 使用比对一下 了解一下源码
        ContentValues values = new ContentValues();
        //封装values
        Field[] fields = mClass.getDeclaredFields();

        for(Field field: fields){
            try{
                //设置权限，私有和公有的都可以访问
                field.setAccessible(true);
                String key = field.getName();
                //获取value
                Object value = field.get(obj);

                //put第二个参数是类型 需要转化
                mPutMethodArgs[0] = key;
                mPutMethodArgs[1] = value;

                //方法使用反射，反射在一定程度上会影响性能
                //参考AppCompatViewInflater源码
                String filedTypeName = field.getType().getName();
                //还是使用反射  获取方法  put  缓存方法
                Method putMethod = mPutMethods.get(filedTypeName);
                if(putMethod == null){
                    putMethod = ContentValues.class.getDeclaredMethod("put", String.class, value.getClass());
                    mPutMethods.put(filedTypeName, putMethod);
                }
                //通过反射执行
                putMethod.invoke(values,mPutMethodArgs);
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                mPutMethodArgs[0] = null;
                mPutMethodArgs[1] = null;
            }
        }
        return values;
    }
}
