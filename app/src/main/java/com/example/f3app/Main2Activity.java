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
    private static final String TAG = "IgB:Main2Activity";
    private static final int ADD_TERM_REQUEST = 1;
    private static final int EDIT_TERM_REQUEST = 2;
    String terms[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "started main activity");
        setContentView(R.layout.activity_main2);

//        terms = getResources().getStringArray(R.array.terms);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);

        Log.i(TAG, " created mRecyclerView");

        TermAdapter termAdapter = new TermAdapter();
        mRecyclerView.setAdapter(termAdapter);

        Log.i(TAG, "created termAdapter");
        termViewModel = new ViewModelProvider(this).get(TermViewModel.class);

        Log.i(TAG, "created termViewModel");

        termViewModel.getAllTerms().observe(this, new Observer<List<Term>>() {
            @Override
            public void onChanged(List<Term> terms) {
                // update recyclerView
                Log.i(TAG, "running termViewModel.getAllTerms.observer onchanged");
                termAdapter.setTerms(terms);
            }
        });

        FloatingActionButton buttonAddTerm = findViewById(R.id.button_add_term);
        buttonAddTerm.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                Log.i(TAG, "started buttonAddTerm onClick method");
                Intent intent = new Intent(Main2Activity.this, AddEditTermActivity.class );
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

        //magic
        termAdapter.setOnTermClickListener(new TermAdapter.OnTermClickListener() {
            @Override
            public void onItemClick(Term term) {
                Intent intent = new Intent(Main2Activity.this, AddEditTermActivity.class);
                intent.putExtra(AddEditTermActivity.TERM_ID, term.getId());
                intent.putExtra(AddEditTermActivity.TERM_TITLE, term.getTitle());
                intent.putExtra(AddEditTermActivity.TERM_START_DATE, term.getStartDate());
                intent.putExtra(AddEditTermActivity.TERM_END_DATE, term.getEndDate());
                startActivityForResult(intent, EDIT_TERM_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data ) {
        super.onActivityResult(requestCode,resultCode,data);
        Log.i(TAG, "started onActivityResult with requestCode " + requestCode + " resultCode " + resultCode);
        if( requestCode == ADD_TERM_REQUEST && resultCode == RESULT_OK) {
            Log.i(TAG, "onActivityResult has new term data to add");

            String termTitle = data.getStringExtra(AddEditTermActivity.TERM_TITLE);
            String termStartDate = data.getStringExtra(AddEditTermActivity.TERM_START_DATE);
            String termEndDate = data.getStringExtra(AddEditTermActivity.TERM_END_DATE);

            Term t = new Term(termTitle, termStartDate, termEndDate);
            termViewModel.insert(t);
            Toast.makeText(this, "Term Saved", Toast.LENGTH_SHORT).show();
        } else if( requestCode == EDIT_TERM_REQUEST && resultCode == RESULT_OK) {
            Log.i(TAG, "onActivityResult has new data to edit");
            int termId = data.getIntExtra(AddEditTermActivity.TERM_ID, -1);
            if (termId == -1) {
                Toast.makeText(this, "Couldn't safe term data", Toast.LENGTH_SHORT).show();
                return;
            }
            String termTitle = data.getStringExtra(AddEditTermActivity.TERM_TITLE);
            String termStartDate = data.getStringExtra(AddEditTermActivity.TERM_START_DATE);
            String termEndDate = data.getStringExtra(AddEditTermActivity.TERM_END_DATE);
            Term t = new Term(termTitle, termStartDate, termEndDate);
            t.setId(termId);
            termViewModel.update(t);
            Toast.makeText(this, "Term updated", Toast.LENGTH_SHORT).show();
        }else{
            Log.i(TAG, "onActivityResult has no data to save");
            Toast.makeText(this, "Term Not Saved", Toast.LENGTH_SHORT).show();
        }

    }
}
