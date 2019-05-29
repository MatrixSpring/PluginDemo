package com.yty.libframe.api.cookie.store;

import android.os.Build;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.yty.libframe.base.AppReflect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

public class WebCookieHandle implements CookieJar {
    CookieManager cookieManager = CookieManager.getInstance();

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        String urlString = url.toString();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.createInstance(AppReflect.getAppContext());
        }

        for(Cookie cookie: cookies){
            cookieManager.setCookie(urlString, cookie.toString());
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            CookieManager.getInstance().flush();
        }else {
            //noinspection deprecation
            CookieSyncManager.createInstance(AppReflect.getAppContext());
            CookieSyncManager.getInstance().sync();
        }

    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        String urlString = url.toString();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.createInstance(AppReflect.getAppContext());
        }
        String cookiesString = CookieManager.getInstance().getCookie(urlString);

        if (cookiesString != null && !cookiesString.isEmpty()) {
            //We can split on the ';' char as the cookie manager only returns cookies
            //that match the url and haven't expired, so the cookie attributes aren't included
            String[] cookieHeaders = cookiesString.split(";");
            List<Cookie> cookies = new ArrayList<>(cookieHeaders.length);

            for (String header : cookieHeaders) {
                cookies.add(Cookie.parse(url, header));

            }
            return cookies;
        }
        return Collections.emptyList();
    }
}
