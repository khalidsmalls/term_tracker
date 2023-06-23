package com.smalls.termtracker.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(
        tableName = "assessments",
        foreignKeys = @ForeignKey(
                entity = Course.class,
                parentColumns = "id",
                childColumns = "courseId")
)
public class Assessment {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int mId;

    @NonNull
    @ColumnInfo(name = "start")
    private final Date mStart;

    @NonNull
    @ColumnInfo(name = "end")
    private final Date mEnd;

    @ColumnInfo(name = "courseId")
    private final int mCourseId;

    public enum MType {
        OBJECTIVE(0),
        PERFORMANCE(1);

        public final int value;

        MType(int newValue) {
            value = newValue;
        }

        public int getValue() {
            return value;
        }
    };

    private final MType mType;

    public Assessment(
            @NonNull Date start,
            @NonNull Date end,
            int courseId,
            MType type
    ) {
        this.mStart = start;
        this.mEnd = end;
        this.mCourseId = courseId;
        this.mType = type;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public int getId() {
        return mId;
    }

    public Date getStart() {
        return mStart;
    }

    public Date getEnd() {
        return mEnd;
    }

    public int getCourseId() {
        return mCourseId;
    }

    public MType getType() {
        return mType;
    }
}
