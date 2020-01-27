package fr.jbrenier.petfoodingcontrol.db.converters;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.OffsetDateTime;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.ResolverStyle;
import java.util.List;

import static java.time.temporal.ChronoField.DAY_OF_MONTH;
import static java.time.temporal.ChronoField.MONTH_OF_YEAR;
import static java.time.temporal.ChronoField.YEAR;

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
