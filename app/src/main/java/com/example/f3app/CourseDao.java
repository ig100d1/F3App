package com.example.f3app;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CourseDao {
   @Insert
   void insert(Course course);

   @Update
   void update(Course course);

   @Delete
   void delete(Course course);

   @Query("DELETE FROM course")
   void deleteAllCourses();

   @Query("SELECT * FROM course WHERE termId = :term_id ORDER BY startDate ASC")
   LiveData<List<Course>> getAllCourses(int term_id);

}
