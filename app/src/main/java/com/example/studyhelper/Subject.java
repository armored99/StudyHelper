package com.example.studyhelper;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "subjects")
public class Subject {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "text")
    private String mText = "";

    @ColumnInfo(name = "updated")
    private long mUpdateTime;

    public Subject() {
        mUpdateTime = System.currentTimeMillis();
    }

    public Subject(String text) {
        mText = text;
        mUpdateTime = System.currentTimeMillis();
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public long getUpdateTime() {
        return mUpdateTime;
    }

    public void setUpdateTime(long updateTime) {
        mUpdateTime = updateTime;
    }
}