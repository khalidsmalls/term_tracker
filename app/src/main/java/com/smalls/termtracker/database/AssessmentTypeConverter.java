package com.smalls.termtracker.database;

import androidx.room.TypeConverter;

import com.smalls.termtracker.entity.Assessment;

public class AssessmentTypeConverter {

    @TypeConverter
    public static int fromTypeToInt(Assessment.MType value) {
        return value.ordinal();
    }

    @TypeConverter
    public static Assessment.MType fromIntToType(int value) {
        return (Assessment.MType.values()[value]);
    }
}
