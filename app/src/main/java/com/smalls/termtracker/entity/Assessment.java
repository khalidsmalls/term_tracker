package com.smalls.termtracker.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
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
            MType type,
            int courseId
            ) {
        this.mStart = start;
        this.mEnd = end;
        this.mType = type;
        this.mCourseId = courseId;
        this.mId = 0;
    }

    @Ignore
    public Assessment(
            int id,
            @NonNull Date start,
            @NonNull Date end,
            MType type,
            int courseId
    ) {
        this.mId = id;
        this.mStart = start;
        this.mEnd = end;
        this.mType = type;
        this.mCourseId = courseId;
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
