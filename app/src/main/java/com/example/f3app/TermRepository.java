package com.example.f3app;


import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class TermRepository {
    private TermDao termDao;
    private LiveData<List<Term>> allTerms;

    public TermRepository(Application application) {
        TermDatabase termDatabase = TermDatabase.getInstance(application);
        termDao = termDatabase.termDao();
//        Term t1 = new Term("Term Test", "2020-01-01","2020-02-01");
//        igor: debugging by inserting
//        this.insert(t1);
        allTerms = termDao.getAllTerms();

    }

    public void insert(Term term){
        new InsertTermAsyncTask(termDao).execute(term);
    }

    public void update(Term term){
        new UpdateTermAsyncTask(termDao).execute(term);
    }

    public void delete(Term term){
        new DeleteTermAsyncTask(termDao).execute(term);
    }

    public void deleteAllTerms(){
        new DeleteAllTermsAsyncTask(termDao).execute();
    }

    public LiveData<List<Term>> getAllTerms(){
        return allTerms;
    }

    /* starting the most confusing part */
    /* the most confusing part */
    /* it si the most confusing part */
    private static class InsertTermAsyncTask extends AsyncTask<Term , Void, Void > {
        private TermDao termDao;
        private InsertTermAsyncTask(TermDao termDao_){
            this.termDao = termDao_;
        }

        protected Void doInBackground(Term... terms){
            termDao.insert(terms[0]);
            return null;
        }
    }

    private static class UpdateTermAsyncTask extends AsyncTask<Term , Void, Void > {
        private TermDao termDao;
        private UpdateTermAsyncTask(TermDao termDao_){
            this.termDao = termDao_;
        }
        @Override
        protected Void doInBackground(Term... terms){
            termDao.update(terms[0]);
            return null;
        }
    }

    private static class DeleteTermAsyncTask extends AsyncTask<Term , Void, Void > {
        private TermDao termDao;
        private DeleteTermAsyncTask(TermDao termDao_){
            this.termDao = termDao_;
        }
        protected Void doInBackground(Term... terms){
            termDao.delete(terms[0]);
            return null;
        }
    }

    private static class DeleteAllTermsAsyncTask extends AsyncTask<Void , Void, Void > {
        private TermDao termDao;
        private DeleteAllTermsAsyncTask(TermDao termDao_){
            this.termDao = termDao_;
        }
        protected Void doInBackground(Void... voids){
            termDao.deleteAllTerms();
            return null;
        }
    }
    /*
    TODO: End Of Very Confusing AsyncTask Part that need to be looked up later
     */


}
