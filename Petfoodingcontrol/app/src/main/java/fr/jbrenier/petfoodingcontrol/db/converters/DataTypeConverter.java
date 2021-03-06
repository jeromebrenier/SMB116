package fr.jbrenier.petfoodingcontrol.db.converters;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Data type converters for Room.
 * @author Jérôme Brenier
 */
public class DataTypeConverter {

    private static final DateTimeFormatter dtf = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    @androidx.room.TypeConverter
    public static OffsetDateTime toOffsetDateTime(String value) {
        return value == null ? null : OffsetDateTime.parse(value,dtf);
    }

    @androidx.room.TypeConverter
    public static String fromOffsetDateTime(OffsetDateTime value) {
        return value == null ? null : value.format(dtf);
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
