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

    public TermViewModel(@NonNull Application application) {
        super(application);
        Log.i("IGOR_DEBUG", "started TermViewModel constructor");
        termRepository = new TermRepository(application);
        allTerms = termRepository.getAllTerms();
        Log.i("IGOR_DEBUG", "finished TermViewModel constructor");

    }

    public void insert(Term term){
        termRepository.insert(term);
    }

    public void update(Term term){
        termRepository.update(term);
    }

    public void delete(Term term){
        termRepository.delete(term);
    }

    public void deleteAllTerms(){
        termRepository.deleteAllTerms();
    }

    public LiveData<List<Term>> getAllTerms(){
        Log.i("IGOR_DEBUG", "TermViewModel.getAllTemrms started");
        return allTerms;
    }

}
