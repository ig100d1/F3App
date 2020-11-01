package com.example.f3app;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class AssessmentViewModel extends AndroidViewModel {

    private static final String TAG = "IgB:AssessmentViewModel";
    private TermRepository assessmentsRepository;
    private LiveData<List<Assessment>> allAssessments;

    public AssessmentViewModel(@NonNull Application application) {
        super(application);
        Log.i(TAG, "started AssessmentViewModel constructor");
        assessmentsRepository = new TermRepository(application);
        Log.i(TAG, "finished AssessmentViewModel constructor");
    }

    public void insert(Assessment assessment){
        assessmentsRepository.insert(assessment);
    }

    public void update(Assessment assessment){
        assessmentsRepository.update(assessment);
    }

    public void delete(Assessment assessment){
        assessmentsRepository.delete(assessment);
    }

    public void deleteAllAssessments(){
        assessmentsRepository.deleteAllAssessments();
    }

    public LiveData<List<Assessment>> getAllAssessments(int course_id){
        Log.i(TAG, "AssessmentViewModel.getAllAssessments started");
        return assessmentsRepository.getAllAssessments(course_id);
    }
}
