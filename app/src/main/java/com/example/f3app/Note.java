package com.example.f3app;

import android.util.Log;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "note")
public class Note {
    public static final String TAG = "IgB:Note";
    public int getId() {
        return id;
    }

    public Note(int courseId, String title, String note) {
        note_debug_counter = note_debug_counter + 1;
        Log.i(TAG, "Note constructor started for obj # "
                + note_debug_counter);
        this.courseId = courseId;
        this.note = note;
        this.title = title;
    }

    @Ignore
    public Note(int courseId, String note) {
        note_debug_counter = note_debug_counter + 1;
        Log.i(TAG, "Note constructor started for obj # "
                + note_debug_counter);
        this.courseId = courseId;
        this.note = note;
        this.title = "";
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCourseId() {
        return courseId;
    }
    public String getNote() {
        return note;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int courseId;
    private String note;
    private String title;
    private static int note_debug_counter = 0;

}


