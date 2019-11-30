package com.example.steven.myapplication.http.retrofit;

import com.example.steven.myapplication.http.typeadapter.DoubleDefault0Adapter;
import com.example.steven.myapplication.http.typeadapter.IntegerDefault0Adapter;
import com.example.steven.myapplication.http.typeadapter.LongDefault0Adapter;
import com.example.steven.myapplication.http.typeadapter.ResponseDataTypeAdaptor;
import com.example.steven.myapplication.http.typeadapter.StringNullAdapter;
import com.example.steven.myapplication.test.DoubleTypeAdapter;
import com.example.steven.myapplication.test.IntegerTypeAdapter;
import com.example.steven.myapplication.utils.LogUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by laoshiren on 2018/8/21.
 * 解决服务器同一字段返回不同类型数据问题
 */

public class CustomGsonConverterFactory extends Converter.Factory {

    public static CustomGsonConverterFactory create() {
        return create(new GsonBuilder()
                /*.registerTypeAdapter(Integer.class, new IntegerDefault0Adapter())
                .registerTypeAdapter(int.class, new IntegerDefault0Adapter())
                .registerTypeAdapter(Double.class, new DoubleDefault0Adapter())
                .registerTypeAdapter(double.class, new DoubleDefault0Adapter())
                .registerTypeAdapter(Long.class, new LongDefault0Adapter())
                .registerTypeAdapter(long.class, new LongDefault0Adapter())
                .registerTypeAdapter(String.class, new StringNullAdapter())*/
                .registerTypeAdapter(Integer.class, new IntegerTypeAdapter())
                .registerTypeAdapter(int.class, new IntegerTypeAdapter())
                .registerTypeAdapter(List.class, new JsonDeserializer<List<?>>() {          //解决服务器接口设计时没有保证数据的一致性
                    @Override
                    public List<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

                        if (json.isJsonArray()) {
                            JsonArray array = json.getAsJsonArray();
                            Type itemType = ((ParameterizedType) typeOfT).getActualTypeArguments()[0];
                            List list = new ArrayList<>();

                            for (int i = 0; i < array.size(); i++) {
                                JsonElement element = array.get(i);
                                Object item = context.deserialize(element, itemType);
                                list.add(item);
                            }
                            return list;
                        } else {
                            //和接口类型不符，返回空List
                            return Collections.EMPTY_LIST;
                        }
                    }
                })
                .create());
    }

    public static CustomGsonConverterFactory create(Gson gson) {
        if (gson == null) {
            LogUtil.i("app出错了..");
            return null;
        }
        return new CustomGsonConverterFactory(gson);
    }

    private final Gson gson;

    private CustomGsonConverterFactory(Gson gson) {
        this.gson = gson;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new CustomGsonResponseBodyConverter<>(gson,type);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                          Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new CustomGsonRequestBodyConverter<>(gson, adapter);
    }
}
