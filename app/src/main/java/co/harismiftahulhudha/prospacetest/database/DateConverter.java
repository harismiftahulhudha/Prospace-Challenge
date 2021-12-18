package co.harismiftahulhudha.prospacetest.database;

import androidx.room.TypeConverter;

import java.util.Date;

public class DateConverter {
    @TypeConverter
    public long fromDate(Date value) {
        return value.getTime();
    }

    @TypeConverter
    public Date longToDate(Long value) {
        return new Date(value);
    }
}
