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

    public String getCompletionDate() {
        return completionDate;
    }

    public Assessment(String title, String completionDate, int courseId, String status) {
        ass_debug_counter = ass_debug_counter + 1;
        Log.i(TAG, "Assessment constructor started for obj # "
                + ass_debug_counter);
        this.title = title;
        this.completionDate = completionDate;
        this.courseId = courseId;
        this.status = status;
    }

    public int getCourseId() {
        return courseId;
    }

    public String getStatus() {
        return status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCompletionDate(String completionDate) {
        this.completionDate = completionDate;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String completionDate;
    private int courseId;
    private String status;
    private static int ass_debug_counter = 0;

}


