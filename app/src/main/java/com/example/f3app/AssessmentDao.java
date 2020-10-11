package com.example.f3app;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AssessmentDao {
    @Insert
    void insert(Assessment course);

    @Update
    void update(Assessment course);

    @Delete
    void delete(Assessment course);

    @Query("DELETE FROM course")
    void deleteAllAssessments();

    @Query("SELECT * FROM course ORDER BY startDate ASC")
    LiveData<List<Assessment>> getAllAssessments();

}
