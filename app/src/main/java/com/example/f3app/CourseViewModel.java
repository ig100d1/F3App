package com.example.f3app;


import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class CourseViewModel extends AndroidViewModel {

    private TermRepository coursesRepository;
    private LiveData<List<Course>> allCourses;

    public CourseViewModel(@NonNull Application application) {
        super(application);
        Log.i("IGOR_DEBUG", "started CourseViewModel constructor");
        coursesRepository = new TermRepository(application);
        Log.i("IGOR_DEBUG", "finished CourseViewModel constructor");
    }

    public void insert(Course course){
        coursesRepository.insert(course);
    }

    public void update(Course course){
        coursesRepository.update(course);
    }

    public void delete(Course course){
        coursesRepository.delete(course);
    }

    public void deleteAllCourses(){
        coursesRepository.deleteAllCourses();
    }

    public LiveData<List<Course>> getAllCourses(int term_id){
        Log.i("IGOR_DEBUG", "CourseViewModel.getAllCourses started");
        return coursesRepository.getAllCourses(term_id);
    }

}
