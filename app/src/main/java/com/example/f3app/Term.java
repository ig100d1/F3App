package com.example.f3app;


import android.util.Log;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "term")
public class Term {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String startDate;
    private String endDate;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Term(String title, String startDate, String endDate) {
        Log.i("IGOR_DEBUG", "Term constructor started");
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
