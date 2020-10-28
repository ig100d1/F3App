package com.example.f3app;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;

public class TermRepository {
    private static final String TAG = "IgB:TermRepository";
    private TermDao termDao;
    private CourseDao courseDao;
    private AssessmentDao assessmentDao;
    private LiveData<List<Term>> allTerms;
    private Term term;
    private LiveData<List<Course>> allCourses;
    private LiveData<List<Assessment>> allAssessments;

    // constructor
    public TermRepository(Application application) {
        TermDatabase termDatabase = TermDatabase.getInstance(application);
        termDao = termDatabase.termDao();
        courseDao = termDatabase.courseDao();
        assessmentDao = termDatabase.assessmentDao();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        allTerms = termDao.getAllTerms();
    }

    public LiveData<List<Term>> getAllTerms(){
        TermDatabase.databaseExecutor.execute(()->{
            allTerms=termDao.getAllTerms();
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return allTerms;
    }

    public Term getTermById(int id) {
        TermDatabase.databaseExecutor.execute(()->{
            term = termDao.getTermById(id);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return term;
    }

    public LiveData<List<Course>> getAllCourses(int term_id){
        TermDatabase.databaseExecutor.execute(()->{
            allCourses=courseDao.getAllCourses(term_id);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "got all courses executed");
        return allCourses;
    }

    public LiveData<List<Assessment>> getAllAssessments(){
        TermDatabase.databaseExecutor.execute(()->{
            allAssessments=assessmentDao.getAllAssessments();
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return allAssessments;
    }

    public void insert(Term term){
        TermDatabase.databaseExecutor.execute(()->termDao.insert(term));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void insert(Course course){
        TermDatabase.databaseExecutor.execute(()->courseDao.insert(course));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void insert(Assessment assessment){
        TermDatabase.databaseExecutor.execute(()->assessmentDao.insert(assessment));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void update(Term term){
        TermDatabase.databaseExecutor.execute(()->termDao.update(term));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void update(Course course){
        TermDatabase.databaseExecutor.execute(()->courseDao.update(course));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void update(Assessment assessment){
        TermDatabase.databaseExecutor.execute(()->assessmentDao.update(assessment));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void delete(Term term){
        TermDatabase.databaseExecutor.execute(()->termDao.delete(term));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void delete(Course course){
        TermDatabase.databaseExecutor.execute(()->courseDao.delete(course));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void delete(Assessment assessment){
        TermDatabase.databaseExecutor.execute(()->assessmentDao.delete(assessment));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void deleteAllTerms() {
        TermDatabase.databaseExecutor.execute(()->termDao.deleteAllTerms());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void deleteAllCourses(){
        TermDatabase.databaseExecutor.execute(()->courseDao.deleteAllCourses());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void deleteAllAssessments(){
        TermDatabase.databaseExecutor.execute(()->assessmentDao.deleteAllAssessments());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }




    /* not using this anymore !!! */
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
}
