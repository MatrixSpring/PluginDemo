package com.yty.libframe.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseResult<T> {

    @Expose
    @SerializedName("msg")
    public String msg;
    @Expose
    @SerializedName("code")
    public int code;
    @Expose
    @SerializedName("result")
    public T result;

    public boolean isSuccess() {
        return code == 0;
    }

    @Override
    public String toString() {
        return "ResponseResult{" +
                "msg='" + msg + '\'' +
                ", code=" + code +
                ", result='" + result + '\'' +
                '}';
    }
}
