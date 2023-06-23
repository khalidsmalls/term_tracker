package com.smalls.termtracker.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "terms")
public class Term {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int mId;
    @NonNull
    @ColumnInfo(name = "title")
    private final String mTitle;

    @NonNull
    @ColumnInfo(name = "start")
    private final Date mStart;

    @NonNull
    @ColumnInfo(name = "end")
    private final Date mEnd;

    public Term(
            @NonNull String title,
            @NonNull Date start,
            @NonNull Date end
    ) {
        this.mTitle = title;
        this.mStart = start;
        this.mEnd = end;
        this.mId = 0;
    }

    @Ignore
    public Term(
            int id,
            String title,
            @NonNull Date start,
            @NonNull Date end
    ) {
        this.mId = id;
        this.mTitle = title;
        this.mStart = start;
        this.mEnd = end;

    }

    public int getId() {
        return mId;
    }
    public void setId(int id) {
        mId = id;
    }
    public String getTitle() {
        return mTitle;
    }

    public Date getStart() {
        return mStart;
    }

    public Date getEnd() {
        return mEnd;
    }

}
