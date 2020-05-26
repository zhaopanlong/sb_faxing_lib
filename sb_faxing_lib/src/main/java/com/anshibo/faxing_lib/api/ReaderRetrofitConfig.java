package com.anshibo.faxing_lib.api;

import android.util.Log;

import okhttp3.logging.HttpLoggingInterceptor;


/**
 * @author Administrator
 * @createtime：2018/8/31 15:48
 */
public class ReaderRetrofitConfig {

    public static HttpLoggingInterceptor getHttpLogInterceptor() {
        //日志显示级别
        HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.BODY;
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.i("HttpLogInterceptor", message);
            }
        });
        interceptor.setLevel(level);
        return interceptor;
    }
}
