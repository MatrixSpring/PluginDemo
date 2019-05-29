package com.yty.libframe.api;


import com.yty.libframe.api.cookie.CookieJarImpl;
import com.yty.libframe.api.cookie.store.PersistentCookieStore;
import com.yty.libframe.base.AppReflect;

import org.apache.http.conn.ssl.SSLSocketFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */

public class ApiHttp {

    /**
     * 默认超时时间 单位/秒
     */
    public static final int DEFAULT_TIME_OUT = 200;

    private int mTimeout = DEFAULT_TIME_OUT;

    private String mBaseUrl;

    private OkHttpClient mOkHttpClient;

    private Retrofit mRetrofit;

    private RequestHead mRequestHead;


    public ApiHttp(String baseUrl, RequestHead requestHead) {
        this(baseUrl, requestHead, DEFAULT_TIME_OUT);
    }

    /**
     * @param baseUrl
     * @param timeout 超时时间 单位/秒
     */
    public ApiHttp(String baseUrl, RequestHead requestHead, int timeout) {
        this.mBaseUrl = baseUrl;
        this.mTimeout = timeout;
        this.mRequestHead = requestHead;
    }

    public Retrofit getRetrofit() {
        if (mRetrofit == null) {
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(mBaseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(getOkHttpClient())
                    .build();
        }
        return mRetrofit;
    }

    public OkHttpClient getOkHttpClient() {
        PersistentCookieStore persistentCookieStore = new PersistentCookieStore(AppReflect.getAppContext());
        if (mOkHttpClient == null) {
            mOkHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(mTimeout, TimeUnit.SECONDS)
                    .readTimeout(mTimeout, TimeUnit.SECONDS)
                    .writeTimeout(mTimeout, TimeUnit.SECONDS)
                    .addInterceptor(new LogInterceptor())
                    .sslSocketFactory(SSLSocketFactoryUtils.createSSLSocketFactory(), SSLSocketFactoryUtils.createTrustAllManager())
                    .hostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)
                    .addNetworkInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request original = chain.request();
                            final Request.Builder builder = original.newBuilder();

                            Map<String, String> headMap = mRequestHead.allHeaders();
                            for (Map.Entry<String, String> entry : headMap.entrySet()) {
                                String resultString = entry.getValue().replaceAll("[^\\x00-\\x7F]", "");
                                builder.addHeader(entry.getKey(), resultString);
                            }

                            Request request = builder.method(original.method(), original.body()).build();
                            return chain.proceed(request);
                        }
                    })
                    .cookieJar(new CookieJarImpl(persistentCookieStore))
                    .build();
        }

        return mOkHttpClient;
    }

    public void setOkHttpClient(OkHttpClient okHttpClient) {
        this.mOkHttpClient = okHttpClient;
    }

    public void setRetrofit(Retrofit retrofit) {
        this.mRetrofit = retrofit;
    }
}
