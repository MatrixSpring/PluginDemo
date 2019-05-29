package com.yty.libframe.api;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import timber.log.Timber;

/**
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */

public class LogInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();


        Timber.i(String.format("%1$s->%2$s", request.method(), request.url()));
        Log.d("ResponseBody", "ResponseBody url : " + request.url());
        if (request.headers() != null) {
            Timber.i("Headers:" + request.headers());
            Log.d("ResponseBody", "ResponseBody Headers : " + request.headers());
        }
        if (request.body() != null) {
            Timber.i("RequestBody:" + bodyToString(request.body()));
            Log.d("ResponseBody", "ResponseBody body: " + bodyToString(request.body()));
        }

        Response response = chain.proceed(chain.request());
        MediaType mediaType = response.body().contentType();
        String responseBody = response.body().string();
        Timber.d("ResponseBody:" + responseBody);
        Log.d("ResponseBody", "ResponseBody responseBody: " + responseBody);
        return response.newBuilder()
                .body(ResponseBody.create(mediaType, responseBody))
                .build();
    }

    private String bodyToString(final RequestBody request) {
        if (request != null) {
            try {
                final RequestBody copy = request;
                final Buffer buffer = new Buffer();
                copy.writeTo(buffer);
                return buffer.readUtf8();
            } catch (final IOException e) {
                Timber.e(e, "Did not work.");
            }
        }
        return null;
    }


    public static void i(String tag, String msg) {
        int max_str_length = 2001 - tag.length();
        while (msg.length() > max_str_length) {
            Log.i(tag, msg.substring(0, max_str_length));
            msg = msg.substring(max_str_length);
        }
        Log.i(tag, msg);
    }


}
