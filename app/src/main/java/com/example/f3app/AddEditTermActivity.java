package com.example.f3app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddEditTermActivity extends AppCompatActivity {
    public static final String TAG = "IgB:AddEditTermActivity";
    private EditText editTextAddTermTitle;
    private EditText editTextAddTermStartDate;
    private EditText editTextAddTermEndDate;
    private Button buttonShowCourses;


    public static final int SHOW_COURSE_REQUEST = 1;
    public static final String TERM_ID = "termId";
    public static final String TERM_TITLE = "termTitle";
    public static final String TERM_START_DATE = "termStartDate";
    public static final String TERM_END_DATE = "termEndDate";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "constructor started");
        setContentView(R.layout.activity_add_term);
        // TODO: should we move this to Add Term section?
        editTextAddTermTitle = findViewById(R.id.add_term_title);
        editTextAddTermStartDate = findViewById(R.id.add_term_start_date);
        editTextAddTermEndDate = findViewById(R.id.add_term_end_date);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_blue_24);
        Intent intent = getIntent();
        if (getIntent().hasExtra("IGOR_DEBUG")){
            Log.i(TAG,"AddEditTermActivity.onCreate - intent came from " + getIntent().getStringExtra("IGOR_DEBUG"));
        }
        if ( getIntent().hasExtra(TERM_ID) ){
            // set term Id only when we want to updated existing term
            setTitle("Edit Term");
            buttonShowCourses = findViewById(R.id.button_show_courses);
            Log.i(TAG, "onCreate, editTerm, Title from intent: " + intent.getStringExtra(TERM_TITLE));
            editTextAddTermTitle.setText(intent.getStringExtra(TERM_TITLE));
            Log.i(TAG, "onCreate, editTerm, Start Date from intent: " + intent.getStringExtra(TERM_START_DATE));
            editTextAddTermStartDate.setText(intent.getStringExtra(TERM_START_DATE));
            editTextAddTermEndDate.setText(intent.getStringExtra(TERM_END_DATE));
            // to set number or maybe date, use value and default value
            // editTextAddTermEndDate.setValue(intent.getIntExtra(TERM_START_DATE, 1999)
            buttonShowCourses.setOnClickListener(new Button.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "started buttonShowCourses onClick method");
                    Intent intent = new Intent(AddEditTermActivity.this, CourseActivity.class );
                    intent.putExtra(TERM_ID,getIntent().getIntExtra(TERM_ID, -1));
                    intent.putExtra(TERM_START_DATE,getIntent().getStringExtra(TERM_START_DATE));
                    intent.putExtra(TERM_END_DATE,getIntent().getStringExtra(TERM_END_DATE));
                    Log.i(TAG, "onCreate, editTerm, setOnClickListner - Title from intent: "
                            + intent.getStringExtra(TERM_TITLE));
                    intent.putExtra(TERM_TITLE,getIntent().getStringExtra(TERM_TITLE));
                    startActivityForResult(intent,SHOW_COURSE_REQUEST);
                }
            });

        }else {
            setTitle("Add Term");
            buttonShowCourses = findViewById(R.id.button_show_courses);
            buttonShowCourses.setVisibility(Button.INVISIBLE);
        }


    }

    private void saveTerm(){
        String termTitle = editTextAddTermTitle.getText().toString();
        String termStartDate = editTextAddTermStartDate.getText().toString();
        String termEndDate = editTextAddTermEndDate.getText().toString();

        if ( termTitle.trim().isEmpty() ) {
            Toast.makeText(this, "Please insert title description", Toast.LENGTH_SHORT).show();
            return;
        }

        if ( termStartDate.trim().isEmpty() ) {
            Toast.makeText(this, "Please insert start date", Toast.LENGTH_SHORT).show();
            return;
        }

        if ( termEndDate.trim().isEmpty() ) {
            Toast.makeText(this, "Please insert end date", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(TERM_TITLE, termTitle);
        data.putExtra(TERM_START_DATE, termStartDate);
        data.putExtra(TERM_END_DATE, termEndDate);

        // -1 is for no id, when addTerm
        int id = getIntent().getIntExtra(TERM_ID, -1);
        if ( id != -1) {
            data.putExtra(TERM_ID,id);
        }

        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_term_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.save_term:
                saveTerm();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data ) {
        // request code is position of term
        super.onActivityResult(requestCode, resultCode, data);
        if ( requestCode == AddEditTermActivity.SHOW_COURSE_REQUEST){
            Log.i(TAG, "onActivityResult returned for SHOW_COURSE_REQUEST");
        }
        Log.i(TAG, "started onActivityResult with requestCode " + requestCode + " resultCode " + resultCode);

    }

}