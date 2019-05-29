package com.yty.libframe.api;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class RequestHead {
    private boolean mFinalFlag = false;
    private Map<String, String> mHeaders;

    private static RequestHead sDefaultRequestHeaders;

    public interface HeaderAccessor {
        void access(String key, String value);
    }

    public static RequestHead getDefaultHeaders() {
        synchronized (RequestHead.class) {
            if (sDefaultRequestHeaders == null) {
                sDefaultRequestHeaders = new RequestHead();
            }
        }
        return sDefaultRequestHeaders;
    }

    public void addHeader(String key, String value) {
        if (mHeaders == null) {
            mHeaders = new HashMap<>();
        }

        if (!mFinalFlag) mHeaders.put(key, value);
    }

    public void removeHeader(String key) {
        if (mHeaders != null && !mFinalFlag) {
            mHeaders.remove(key);
        }
    }

    public void accessAll(HeaderAccessor accessor) {
        if (accessor != null && mHeaders != null) {
            Iterator<String> keyIterator = mHeaders.keySet().iterator();
            while (keyIterator.hasNext()) {
                String key = keyIterator.next();
                String value = mHeaders.get(key);

                accessor.access(key, value);
            }
        }
    }

    public Map<String, String> allHeaders() {
        return new HashMap<>(mHeaders);
    }
}
