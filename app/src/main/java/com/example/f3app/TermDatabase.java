package com.example.f3app;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.Date;

// we need to change version and implement fallbackToDestructiveMigration to re-seed database
@Database(entities = {Term.class, Course.class, Assessment.class}, version = 2)
public abstract class TermDatabase extends RoomDatabase {
    // this is to make sure we only can have single instance of this class
    private static TermDatabase instance;
    // never gets increment
    // TODO: test this counter
    private static int on_create_counter = 0;

    // we dont have to create body because room will take care of the code
    // returns our TermDao interface
    // termDao only used to seed database, does not need for TermData
    public abstract TermDao termDao();
    public abstract CourseDao courseDao();
    public abstract AssessmentDao assessmentDao();

    public static synchronized TermDatabase getInstance(Context context) {
        Log.i("IGOR_DEBUG","TermDatabase.getInstance started");
        if ( instance == null ){
            Log.i("IGOR_DEBUG", "TermDatabase.getInstance new instance init started");
            // there is a way how to push all queries into main thread
            // now it is required to use AsyncTasks (deprecated)
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    TermDatabase.class, "term_database")
                    .fallbackToDestructiveMigration()
                    //.addMigrations(MIGRATION_2_3)
                    .addCallback(roomCallback)
                    .addCallback((roomCallbackRecreate))
                    .build();
        }
        Log.i("IGOR_DEBUG", "TermDatabase on_create_counter = " + on_create_counter);
        return instance;
    }

    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            //database.execSQL("ALTER TABLE Book "
            //        + " ADD COLUMN pub_year INTEGER");
            Log.i("IGOR_DEBUG","TermDatabase.migration 2 to 3 started");
        }
    };

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db){
            super.onCreate(db);
            on_create_counter +=1 ;
            Log.i("IGOR_DEBUG", "TermDatabase.roomCallback started  !!!!!!!!!!!!! ");
            new ReloadDatabase(instance).execute();
        }

    };

    private static RoomDatabase.Callback roomCallbackRecreate = new RoomDatabase.Callback() {
        @Override
        public void onDestructiveMigration(@NonNull SupportSQLiteDatabase db) {
            super.onDestructiveMigration(db);
            on_create_counter += 1;
            Log.i("IGOR_DEBUG", "TermDatabase.roomCallbackRecreate started  !!!!!!!!!!!!! ");
            new ReloadDatabase(instance).execute();
        }
    };

    private static class ReloadDatabase extends AsyncTask<Void, Void, Void> {
        private TermDao termDao;
        private CourseDao courseDao;
        private AssessmentDao assessmentDao;
        private ReloadDatabase(TermDatabase termDatabase){
            termDao = termDatabase.termDao();
            courseDao = termDatabase.courseDao();
            assessmentDao = termDatabase.assessmentDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Log.i("IGOR_DEBUG", "TermDatabase.reloadDatabaseAsync started");
            termDao.insert(new Term("Fall 2009", new String("2009-09-01"), new String("2009-12-15")));
            termDao.insert(new Term("Winter 2010", new String("2010-01-03"), new String("2010-03-12")));
            termDao.insert(new Term("Spring 2010", new String("2010-03-22"), new String("2010-05-29")));
            termDao.insert(new Term("Summer 2010", new String("2010-06-07"), new String("2010-08-12")));
            termDao.insert(new Term("Fall 2010", new String("2010-09-01"), new String("2010-12-15")));
            termDao.insert(new Term("Winter 2011", new String("2011-01-03"), new String("2011-03-12")));
            termDao.insert(new Term("Spring 2011", new String("2011-03-22"), new String("2011-05-29")));
            termDao.insert(new Term("Summer 2011", new String("2011-06-07"), new String("2011-08-12")));
            termDao.insert(new Term("Fall 2011", new String("2010-09-11"), new String("2011-12-15")));
            termDao.insert(new Term("Winter 2012", new String("2012-01-03"), new String("2012-03-12")));
            termDao.insert(new Term("Spring 2012", new String("2012-03-22"), new String("2012-05-29")));
            termDao.insert(new Term("Summer 2012", new String("2012-06-07"), new String("2012-08-12")));

            // Term 1
            courseDao.insert(new Course("mech1",
                    "2009-09-01", "2009-12-15", "completed",
                    "Roger Twister", "123-1234-12345", "profes1@school.edu",1 ));

            courseDao.insert(new Course("math1",
                    "2009-09-01", "2009-12-15", "failed",
                    "Anna Counter", "123-1234-12345", "pro1@school.edu",1 ));

            courseDao.insert(new Course("lang1",
                    "2009-09-01", "2009-12-15", "enjoyed",
                    "Ala Grama", "123-1234-12345", "rocks1@school.edu",1 ));


            // Term 2
            courseDao.insert(new Course("mech2",
                    "2010-01-09", "2010-03-12", "completed",
                    "Roger Twister", "123-1234-12345", "profes1@school.edu",1 ));

            courseDao.insert(new Course("math2",
                    "2010-01-10", "2010-03-01", "failed",
                    "Anna Counter", "123-1234-12345", "pro1@school.edu",1 ));

            courseDao.insert(new Course("lang2",
                    "2010-01-09", "2010-03-02", "enjoyed",
                    "Ala Grama", "123-1234-12345", "rocks1@school.edu",1 ));

            // Term 3
            courseDao.insert(new Course("mech3",
                    "2010-03-27", "2010-05-01", "completed",
                    "Roger Twister", "123-1234-12345", "profes1@school.edu",1 ));

            courseDao.insert(new Course("math3",
                    "2010-03-28", "2010-05-03", "failed",
                    "Anna Counter", "123-1234-12345", "pro1@school.edu",1 ));

            courseDao.insert(new Course("lang3",
                    "2010-03-29", "2010-05-02", "enjoyed",
                    "Ala Grama", "123-1234-12345", "rocks1@school.edu",1 ));

            return null;
        }
    }


}
