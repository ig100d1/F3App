package com.example.f3app;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Term.class}, version = 1)
public abstract class TermDatabase extends RoomDatabase {
    private static TermDatabase instance;
    // we dont have to create body because room will take care of the code
    public abstract TermDao termDao();

    public static synchronized TermDatabase getInstance(Context context);
    
}
