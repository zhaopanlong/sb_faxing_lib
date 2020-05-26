package com.anshibo.faxing_lib.api;


import android.content.Intent;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * @author 赵盼龙
 * @createtime：2018/4/2 15:07
 */
public class ReaderAppGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private Gson gson;
    private Type type;

    public ReaderAppGsonResponseBodyConverter(Gson gson, Type type) {
        this.gson = gson;
        this.type = type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        try {
            String response = value.string();
            JSONObject jsonObject = new JSONObject(response);
            if (type == String.class) {
                return (T) response;
            }
            return gson.fromJson(jsonObject.toString(), type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
