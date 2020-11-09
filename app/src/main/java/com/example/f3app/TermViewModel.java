package com.example.f3app;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class TermViewModel extends AndroidViewModel {

    private TermRepository termRepository;
    private LiveData<List<Term>> allTerms;
    private static final String TAG = "IgB:TermViewModel";
    public static final int QUERY_EXEC_NOT_COMPLETED = -1;

    public TermViewModel(@NonNull Application application) {
        super(application);
        Log.i(TAG, "started TermViewModel constructor");
        termRepository = new TermRepository(application);
        allTerms = termRepository.getAllTerms();
        Log.i(TAG, "finished TermViewModel constructor");

    }

    public void insert(Term term){
        termRepository.insert(term);
    }

    public void update(Term term){
        termRepository.update(term);
    }

    public int delete(Term term){
       Log.d(TAG, "delete term " + term.getTitle());
       int countCourses = QUERY_EXEC_NOT_COMPLETED ;
       countCourses = termRepository.countTermCourses(term);
       Log.d(TAG, "delete term count courses returned: " + countCourses);
       if (countCourses == 0 ){
           Log.d(TAG, "delete term " + term.getTitle() + " don't have any courses, continue");
           termRepository.delete(term);
       }
       return countCourses;
    }

    public void deleteAllTerms(){
        termRepository.deleteAllTerms();
    }

    public LiveData<List<Term>> getAllTerms(){
        Log.i(TAG, "TermViewModel.getAllTerms started");
        return allTerms;
    }

}
