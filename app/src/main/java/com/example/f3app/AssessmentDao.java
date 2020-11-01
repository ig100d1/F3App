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
    void insert(Assessment assessment);

    @Update
    void update(Assessment assessment);

    @Delete
    void delete(Assessment assessment);

    @Query("DELETE FROM assessment")
    void deleteAllAssessments();

    @Query("SELECT * FROM assessment WHERE courseId = :course_id ORDER BY dueDate ASC")
    LiveData<List<Assessment>> getAllAssessments(int course_id);
}
