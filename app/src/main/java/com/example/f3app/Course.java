package com.example.f3app;

import android.util.Log;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "course")
public class Course {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String startDate;
    private String endDate;
    // enforce: in progress, completed, dropped, plan to take)
    private String status;
    private String mentorName;
    private String phone;
    private String email;
    private int term;

    public int getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setMentorName(String mentorName) {
        this.mentorName = mentorName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public String getMentorName() {
        return mentorName;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public int getTerm() {
        return term;
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

    public Course(String title,
                  String startDate,
                  String endDate,
                  String status,
                  String mentorName,
                  String phone,
                  String email,
                  int term) {
        Log.i("IGOR_DEBUG", "Course constructor started");
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.mentorName = mentorName;
        this.phone = phone;
        this.email = email;

    }
}

