package com.smalls.termtracker.database;

import androidx.room.TypeConverter;

import com.smalls.termtracker.entity.Course;

public class CourseStatusTypeConverter {

    @TypeConverter
    public static int fromStatusToInt(Course.MStatus value) {
        return value.ordinal();
    }

    @TypeConverter
    public static Course.MStatus fromIntToStatus(int value) {
        return (Course.MStatus.values()[value]);
    }
}
