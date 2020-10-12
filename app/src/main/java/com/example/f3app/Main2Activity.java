package com.example.f3app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class Main2Activity extends AppCompatActivity {

    private TermViewModel termViewModel;
    RecyclerView mRecyclerView;
    private static final String TAG = "Main2Activity";
    private static final int ADD_TERM_REQUEST = 1;
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

        FloatingActionButton buttonAddTerm = findViewById(R.id.button_add_term);
        buttonAddTerm.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                Log.i("IGOR_DEBUG", "started buttonAddTerm onClick method");
                Intent intent = new Intent(Main2Activity.this, AddTermActivity.class );
                startActivityForResult(intent,ADD_TERM_REQUEST);
            }
        });


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                termViewModel.delete(termAdapter.getTermAt(viewHolder.getAdapterPosition()));
                Toast.makeText(Main2Activity.this, "Term Deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(mRecyclerView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data ) {
        super.onActivityResult(requestCode,resultCode,data);
        Log.i("IGOR_DEBUG", "started onActivityResult with requestCode " + requestCode + " resultCode " + resultCode);
        if( requestCode == ADD_TERM_REQUEST && resultCode == RESULT_OK) {
            Log.i("IGOR_DEBUG", "onActivityResult has data to save");
            String termTitle = data.getStringExtra("termTitle");
            String termStartDate = data.getStringExtra("termStartDate");
            String termEndDate = data.getStringExtra("termEndDate");
            Term t = new Term(termTitle, termStartDate, termEndDate);
            termViewModel.insert(t);
            Toast.makeText(this, "Term Saved", Toast.LENGTH_SHORT).show();
        }else{
            Log.i("IGOR_DEBUG", "onActivityResult has no data to save");
            Toast.makeText(this, "Term Not Saved", Toast.LENGTH_SHORT).show();
        }

    }
}
