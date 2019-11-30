package com.example.steven.myapplication.test;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class DoubleTypeAdapter extends TypeAdapter<Double> {

    @Override
    public void write(JsonWriter out, Double value) throws IOException {
        out.value(String.valueOf(value));
    }

    @Override
    public Double read(JsonReader in) throws IOException {
        return toDouble(in.nextString());
    }

    private double toDouble(String obj) {
        try {
            return Double.parseDouble(obj);
        } catch (Exception var2) {
            return 0.0D;
        }
    }
}
