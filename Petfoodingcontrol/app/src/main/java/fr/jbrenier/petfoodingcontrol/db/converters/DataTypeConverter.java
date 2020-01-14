package fr.jbrenier.petfoodingcontrol.db.converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataTypeConverter {

    @androidx.room.TypeConverter
    public static Date toDate(Long value) {
        return value == null ? null : new Date(value);
    }

    @androidx.room.TypeConverter
    public static Long toLong(Date value) {
        return value == null ? null : value.getTime();
    }

    @androidx.room.TypeConverter
    public static List<Integer> stringToIntegerList(String data) {
        Type listType = new TypeToken<List<Integer>>() {}.getType();
        return new Gson().fromJson(data, listType);
    }

    @androidx.room.TypeConverter
    public static String integerListToString(List<Integer> list) {
        return new Gson().toJson(list);
    }
}
