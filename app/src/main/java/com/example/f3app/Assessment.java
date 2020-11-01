package com.example.f3app;

import android.util.Log;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "assessment")
public class Assessment {
    public static final String TAG = "IgB:Assessment";
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDueDate() {
        return dueDate;
    }

    public Assessment(String title, String dueDate, int courseId, String status, String type) {
        ass_debug_counter = ass_debug_counter + 1;
        Log.i(TAG, "Assessment constructor started for obj # "
                + ass_debug_counter);
        this.title = title;
        this.dueDate = dueDate;
        this.courseId = courseId;
        this.status = status;
        this.type = type;
    }

    public int getCourseId() {
        return courseId;
    }

    public String getStatus() {
        return status;
    }

    public String getType() {
        return status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setType(String status) {
        this.type = type;
    }

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String dueDate;
    private int courseId;
    private String status;
    private String type;
    private static int ass_debug_counter = 0;

}


