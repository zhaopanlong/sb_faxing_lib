package com.anshibo.faxing_lib.api;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * @author 赵盼龙
 * @createtime：2018/3/30 16:38
 */
public class ReaderAppGsonConverterFactory extends Converter.Factory {

    public static ReaderAppGsonConverterFactory create() {
        return create(new Gson());
    }

    public static ReaderAppGsonConverterFactory create(Gson gson) {
        return new ReaderAppGsonConverterFactory(gson);
    }

    private ReaderAppGsonConverterFactory(Gson gson) {
        if (gson == null) throw new NullPointerException("gson == null");
        this.gson = gson;
    }

    private Gson gson;

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new ReaderAppGsonResponseBodyConverter<>(gson,type);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new ReaderAppGsonRequestBodyConverter<>(gson, adapter);
    }

}
