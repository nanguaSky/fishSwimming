package com.example.steven.myapplication.http.typeadapter;

import com.example.steven.myapplication.utils.LogUtil;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;

/**
 * File descripition:double=>
 *
 * @author Administrator    对返回值为空处理
 * @date 2018/5/21
 */

public class IntegerDefault0Adapter implements JsonSerializer<Integer>, JsonDeserializer<Integer> {
    @Override
    public Integer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        return toInt(json.getAsString());
    }


    @Override
    public JsonElement serialize(Integer src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src);
    }


    private int toInt(Object obj) {
        return obj == null ? 0 : toInt(obj.toString(), 0);
    }

    private int toInt(String str, int defValue) {
        //将其作为一个字符串读取出来
        //返回的numberStr不会为null
        if (str.contains(".") || str.contains("e")
                || str.contains("E")) {

            try {
                return (int) Double.parseDouble(str);

            } catch (Exception var2) {
                return defValue;
            }
        }

        try {
            return Integer.valueOf(str);

        } catch (Exception e){

            try {
                return (int) Long.parseLong(str);

            } catch (Exception var2) {
                return defValue;
            }
        }
    }
}