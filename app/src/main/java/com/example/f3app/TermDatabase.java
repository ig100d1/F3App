package com.example.f3app;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.Date;

@Database(entities = {Term.class}, version = 1)
public abstract class TermDatabase extends RoomDatabase {
    // this is to make sure we only can have single instance of this class
    private static TermDatabase instance;

    // we dont have to create body because room will take care of the code
    // returns our TermDao interface
    public abstract TermDao termDao();

    public static synchronized TermDatabase getInstance(Context context) {
        if ( instance == null ){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    TermDatabase.class, "term_database").fallbackToDestructiveMigration().addCallback(roomCallbac).build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallbac = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db){
            super.onCreate(db);
        }
    };

    private static class PopulateDatabaseAsyncTask extends AsyncTask<Void, Void, Void> {
        private TermDao termDao;
        private PopulateDatabaseAsyncTask(TermDatabase termDatabase){
            termDao = termDatabase.termDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            termDao.insert(new Term("Fall 2009", new Date("2009-09-01"), new Date("2009-12-15")));
            termDao.insert(new Term("Winter 2010", new Date("2010-01-03"), new Date("2010-03-12")));
            termDao.insert(new Term("Spring 2010", new Date("2010-03-22"), new Date("2010-05-29")));
            termDao.insert(new Term("Summer 2010", new Date("2010-06-07"), new Date("2010-08-12")));
            termDao.insert(new Term("Fall 2010", new Date("2010-09-01"), new Date("2010-12-15")));
            termDao.insert(new Term("Winter 2011", new Date("2011-01-03"), new Date("2011-03-12")));
            termDao.insert(new Term("Spring 2011", new Date("2011-03-22"), new Date("2011-05-29")));
            termDao.insert(new Term("Summer 2011", new Date("2011-06-07"), new Date("2011-08-12")));
            termDao.insert(new Term("Fall 2011", new Date("2010-09-11"), new Date("2011-12-15")));
            termDao.insert(new Term("Winter 2012", new Date("2012-01-03"), new Date("2012-03-12")));
            termDao.insert(new Term("Spring 2012", new Date("2012-03-22"), new Date("2012-05-29")));
            termDao.insert(new Term("Summer 2012", new Date("2012-06-07"), new Date("2012-08-12")));

            return null;
        }
    }


}
