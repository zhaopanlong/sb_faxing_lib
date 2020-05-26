package com.anshibo.faxing_lib.api;


import com.zhaopanlong.rxcacheadapter.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * @author Administrator
 * @createtime：2018/9/3 15:26
 */
public class ReaderApiBuild {
    private OkHttpClient.Builder httpBuild;
    private Retrofit.Builder retrofit;
    private static ReaderApiBuild build;

    /**
     * 初始化参数
     */
    private ReaderApiBuild() {
        httpBuild = new OkHttpClient.Builder()
                .readTimeout(ReaderOkhttpContrans.READ_TIME_OUT, TimeUnit.MILLISECONDS)
                .connectTimeout(ReaderOkhttpContrans.CLIENT_TIME_OUT, TimeUnit.MILLISECONDS)
                .addInterceptor(ReaderRetrofitConfig.getHttpLogInterceptor());
        retrofit = new Retrofit.Builder()
                .client(httpBuild.build())
                .addConverterFactory(ReaderAppGsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
    }

    /**
     * 创建server
     *
     * @param baseUrl
     * @param apiClazz
     * @param <T>
     * @return
     */
    public static <T> T createApi(String baseUrl, Class<T> apiClazz) {
        if (build == null) {
            build = new ReaderApiBuild();
        }
        return build.retrofit.baseUrl(baseUrl).build().create(apiClazz);
    }

}
