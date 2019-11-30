package com.example.steven.myapplication.test;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class IntegerTypeAdapter extends TypeAdapter<Integer> {

    @Override
    public void write(JsonWriter out, Integer value) throws IOException {
        out.value(String.valueOf(value));
    }

    @Override
    public Integer read(JsonReader in) throws IOException {
        String numberStr = in.nextString();
        return toInt(numberStr);
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
