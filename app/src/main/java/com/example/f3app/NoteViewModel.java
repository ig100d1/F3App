package com.example.f3app;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {

    private static final String TAG = "IgB:NoteViewModel";
    private TermRepository notesRepository;
    private LiveData<List<Note>> allNotes;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        Log.i(TAG, "started NoteViewModel constructor");
        notesRepository = new TermRepository(application);
        Log.i(TAG, "finished NoteViewModel constructor");
    }

    public void insert(Note note){
        notesRepository.insert(note);
    }

    public void update(Note note){
        notesRepository.update(note);
    }

    public void delete(Note note){
        notesRepository.delete(note);
    }

    public void deleteAllNotes(){
        notesRepository.deleteAllNotes();
    }

    public LiveData<List<Note>> getAllNotes(int course_id){
        Log.i(TAG, "NoteViewModel.getAllNotes started");
        return notesRepository.getAllNotes(course_id);
    }
}
