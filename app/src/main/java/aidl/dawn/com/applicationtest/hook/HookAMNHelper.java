package aidl.dawn.com.applicationtest.hook;

import android.util.Log;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

import aidl.dawn.com.applicationtest.utils.RefInvoke;

public class HookAMNHelper {

    public static void hookActivityManager(){
        try {
            //获取AMN的gDefault单例gDefault，gDefault是单例
            Object gDefault = RefInvoke.getStaticFieldOjbect(
                    "android.app.ActivityManager",
                     "gDefault");
            //gDefault是一个android.util.Singleton的对象，我们取出来这个单例里面的mInstance字段,
            // IActivityManager类型
            Object rawIActivityManager = RefInvoke.getFieldOjbect(
                    "android.util.Singleton",gDefault,"mInstance");

            //创建这个对象的代理对象iActivityManagerInterface,然后替换这个字段，
            // 让我们的代理对象帮忙干活
            Class<?> iActivityManagerInterface = Class.forName("android.app.IActivityManager");
            Object proxy = Proxy.newProxyInstance(
                    Thread.currentThread().getContextClassLoader(),
                    new Class<?>[]{iActivityManagerInterface},
                    new HookHandler(rawIActivityManager));

            //把Singleton的mInstance的替换成proxy
            RefInvoke.setFieldOjbect("android.util.Singleton", "mInstance",gDefault, proxy);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class HookHandler implements InvocationHandler{
        private static final String TAG = "HookHandler";
        private Object mBase;
        public HookHandler(Object base){
            mBase = base;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Log.d("000000000","000000000 method : "+method.getName()+" called with args: "+ Arrays.toString(args));
            Log.d(TAG,TAG+"method : "+method.getName()+" called with args: "+ Arrays.toString(args));
            return method.invoke(mBase,args);
        }
    }
}
