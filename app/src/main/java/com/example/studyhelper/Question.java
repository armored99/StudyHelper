package com.example.studyhelper;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "questions",
        foreignKeys = @ForeignKey(entity = Subject.class,
                parentColumns = "text",
                childColumns = "subject"))
public class Question {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long mId;

    @ColumnInfo(name = "text")
    private String mText;

    @ColumnInfo(name = "answer")
    private String mAnswer;

    @ColumnInfo(name = "subject")
    private String mSubject;

    @ColumnInfo(name = "lastcorrecttime")
    private long mLastCorrectTime;

    public String getText() {
        return mText;
    }

    public void setId(long id) {
        mId = id;
    }

    public long getId() {
        return mId;
    }

    public void setText(String text) {
        mText = text;
    }

    public String getAnswer() {
        return mAnswer;
    }

    public void setAnswer(String answer) {
        mAnswer = answer;
    }

    public String getSubject() {
        return mSubject;
    }

    public void setSubject(String subject) {
        mSubject = subject;
    }
    public long getLastCorrectTime() {
        return mLastCorrectTime;
    }

    public void setLastCorrectTime(long updateTime) {
        mLastCorrectTime = updateTime;
    }
}