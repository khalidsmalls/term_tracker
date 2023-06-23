package com.smalls.termtracker.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(
        tableName = "courses",
        foreignKeys = @ForeignKey(
                entity = Term.class,
                parentColumns = "id",
                childColumns = "termId")
)
public class Course {

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

    @ColumnInfo(name = "instructor_name")
    private final String mInstructorName;

    @ColumnInfo(name = "instructor_phone")
    private final String mInstructorPhone;

    @ColumnInfo(name = "instructor_email")
    private final String mInstructorEmail;

    @ColumnInfo(name = "optional_note")
    private final String mOptionalNote;

    @ColumnInfo(name = "termId")
    private final int mTermId;

    public Course(
            @NonNull String title,
            @NonNull Date start,
            @NonNull Date end,
            String instructorName,
            String instructorPhone,
            String instructorEmail,
            String optionalNote,
            int termId
    ) {
        this.mId = 0;
        this.mTitle = title;
        this.mStart = start;
        this.mEnd = end;
        this.mInstructorName = instructorName;
        this.mInstructorPhone = instructorPhone;
        this.mInstructorEmail = instructorEmail;
        this.mOptionalNote = optionalNote;
        this.mTermId = termId;
    }

    @Ignore
    public Course(
           int id,
           @NonNull String title,
           @NonNull Date start,
           @NonNull Date end,
           String instructorName,
           String instructorPhone,
           String instructorEmail,
           String optionalNote,
           int termId
    ) {
        this.mId = id;
        this.mTitle = title;
        this.mStart = start;
        this.mEnd = end;
        this.mInstructorName = instructorName;
        this.mInstructorPhone = instructorPhone;
        this.mInstructorEmail = instructorEmail;
        this.mOptionalNote = optionalNote;
        this.mTermId = termId;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
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

    public String getInstructorName() {
        return mInstructorName;
    }

    public String getInstructorPhone() {
        return mInstructorPhone;
    }

    public String getInstructorEmail() {
        return mInstructorEmail;
    }

    public String getOptionalNote() {
        return mOptionalNote;
    }

    public int getTermId() {
        return mTermId;
    }

}
