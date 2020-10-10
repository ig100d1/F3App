package com.example.f3app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import java.util.List;

public class Main2Activity extends AppCompatActivity {

    private TermViewModel termViewModel;
    RecyclerView mRecyclerView;
    private static final String TAG = "Main2Activity";

    String terms[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("IGOR_DEBUG", "started main activity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

//        terms = getResources().getStringArray(R.array.terms);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);

        Log.i("IGOR_DEBUG", " created mRecyclerView");

        TermAdapter termAdapter = new TermAdapter();
        mRecyclerView.setAdapter(termAdapter);

        Log.i("IGOR_DEBUG", "created termAdapter");
        termViewModel = new ViewModelProvider(this).get(TermViewModel.class);

        Log.i("IGOR_DEBUG", "created termViewModel");

        termViewModel.getAllTerms().observe(this, new Observer<List<Term>>() {
            @Override
            public void onChanged(List<Term> terms) {
                // update recyclerView
                Log.i("IGOR_DEGBUG", "running termViewModel.getAllTerms.observer onchanged");
                termAdapter.setTerms(terms);
            }
        });

    }

}
