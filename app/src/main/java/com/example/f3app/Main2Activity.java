package com.example.f3app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

public class Main2Activity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    private static final String TAG = "Main2Activity";

    String terms[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        terms = getResources().getStringArray(R.array.terms);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);


        //System.out.println("IGOR DEBUG: " + terms);
        for(int i = 0 ; i< terms.length; i++) {
            Log.i(TAG, " IGOR_DEBUG - " + terms[i].toString());
        }
        RecAdapter recAdapter = new RecAdapter( this, terms);

        mRecyclerView.setAdapter(recAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

}
