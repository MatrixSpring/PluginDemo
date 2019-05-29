package com.yty.libframe.api;

/**
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */

public class ApiManager {

    private static final String TAG = "Jenly";

    private ApiHttp mApiHttp;

    private static String mBaseUrl;
    private static int mTimeout = ApiHttp.DEFAULT_TIME_OUT;
    private static ApiManager mInstance;
    private static RequestHead mRequestHead;

    public static ApiManager getInstance() {
        if (mInstance == null) {
            synchronized (ApiManager.class) {
                if (mInstance == null) {
                    mInstance = new ApiManager();
                }
            }
        }
        return mInstance;
    }

    public static void init(String baseUrl, RequestHead requestHead) {
        init(baseUrl, requestHead, ApiHttp.DEFAULT_TIME_OUT);
    }

    public static void init(String baseUrl, RequestHead requestHead, int timeout) {
        mBaseUrl = baseUrl;
        mTimeout = timeout;
        mRequestHead = requestHead;
    }

    private ApiManager() {
        mApiHttp = new ApiHttp(mBaseUrl, mRequestHead, mTimeout);
    }

    public ApiHttp getApiHttp() {
        return mApiHttp;
    }

    public void setApiHttp(ApiHttp apiHttp) {
        this.mApiHttp = apiHttp;
    }

    public <T> T getApiService(Class<T> service) {
        return mApiHttp.getRetrofit().create(service);
    }
}
