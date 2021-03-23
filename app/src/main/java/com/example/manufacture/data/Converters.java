package com.example.manufacture.data;

import androidx.room.TypeConverter;

import com.google.gson.Gson;

import java.util.HashMap;

public class Converters {
    @TypeConverter
    public String mapToString(HashMap<Integer, Boolean> map) {
        return new Gson().toJson(map);
    }

    @TypeConverter
    public HashMap<Integer, Boolean> stringToMap(String stringMap) {
        return new Gson().fromJson(stringMap, HashMap.class);
    }
}