package com.example.f3app;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TermDao {
    @Insert
    void insert(Term term);

    @Update
    void update(Term term);

    @Delete
    void delete(Term term);

    @Query("DELETE FROM term")
    void deleteAllTerms();

    @Query("SELECT * FROM term ORDER BY startDate ASC")
    LiveData<List<Term>> getAllTerms();

    @Query("SELECT * FROM term WHERE id = :term_id")
    Term getTermById(int term_id);
}