package com.example.f3app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
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

public class AddEditTermActivity extends AppCompatActivity implements ActivityWithDates {
    public static final String TAG = "IgB:AddEditTermActivity";
    private EditText editTextAddTermTitle;
    private EditText editTextAddTermStartDate;
    private EditText editTextAddTermEndDate;
    private Button buttonShowCourses;


    public static final int SHOW_COURSE_REQUEST = 1;
    public static final int CREATE_FAILED = 2;
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
        Log.i(TAG,"saveTerm called");
        String termTitle = editTextAddTermTitle.getText().toString();
        String termStartDate = editTextAddTermStartDate.getText().toString();
        String termEndDate = editTextAddTermEndDate.getText().toString();

        if ( termTitle.trim().isEmpty() ) {
            Log.e(TAG,"saveTerm termTitle is empty");
            Toast.makeText(this, "Please insert title description", Toast.LENGTH_SHORT).show();
            return;
        }

        if ( termStartDate.trim().isEmpty() ) {
            Log.e(TAG,"saveTerm termStartDate is empty");
            Toast.makeText(this, "Please insert start date", Toast.LENGTH_SHORT).show();
            return;
        }

        if ( termEndDate.trim().isEmpty() ) {
            Log.e(TAG,"saveTerm termEndDate is empty");
            Toast.makeText(this, "Please insert end date", Toast.LENGTH_SHORT).show();
            return;
        }

        //Intent data = new Intent();
        Intent data = new Intent(AddEditTermActivity.this, Main2Activity.class );
        data.putExtra(TERM_TITLE, termTitle);
        data.putExtra(TERM_START_DATE, termStartDate);
        data.putExtra(TERM_END_DATE, termEndDate);

        // -1 is for no id, when addTerm
        int id = getIntent().getIntExtra(TERM_ID, -1);
        if ( id != -1) {
            data.putExtra(TERM_ID,id);
        }

        Log.d(TAG,"saveTerm result_ok");
        setResult(RESULT_OK, data);
        //finish();
        startActivity(data);
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
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "started onActivityResult with requestCode " + requestCode + " resultCode " + resultCode);
        if ( requestCode == AddEditTermActivity.SHOW_COURSE_REQUEST && resultCode == RESULT_CANCELED){
            Log.i(TAG, "onActivityResult returned RESULT_CANCELED action for SHOW_COURSE_REQUEST");
        }
        else if ( requestCode == AddEditTermActivity.SHOW_COURSE_REQUEST && resultCode == CREATE_FAILED){
            Log.e(TAG, "onActivityResult returned CREATE_FAILED for SHOW_COURSE_REQUEST");
            Toast.makeText(this, "Failure to start add course function",Toast.LENGTH_LONG).show();
        }else{
            Log.e(TAG, "onActivityResult returned for unhandled request");
            Toast.makeText(this, "Unknown event happened",Toast.LENGTH_LONG).show();
        }
    }

    public void showDatePickerDialogForEndDate(View v) {
        String currentDate=null;
        DialogFragment  newFragment ;
        if(getIntent().hasExtra(AddEditTermActivity.TERM_END_DATE)){
           currentDate = getIntent().getStringExtra(AddEditTermActivity.TERM_END_DATE);
           try {
               newFragment = new DatePickerFragment().currentDate(currentDate);
           }catch (java.text.ParseException e) {
               Log.e(TAG, "showDatePickerDialogForEndDate failed to parse current date");
               return;
           }
        }else {
            newFragment = new DatePickerFragment();
        }
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void showDatePickerDialogForStartDate(View v) {
        String currentDate=null;
        DialogFragment  newFragment ;
        if(getIntent().hasExtra(AddEditTermActivity.TERM_START_DATE)){
            currentDate = getIntent().getStringExtra(AddEditTermActivity.TERM_START_DATE);
            try {
                newFragment = new DatePickerFragment().currentDate(currentDate).startPicker();
            }catch (java.text.ParseException e) {
                Log.e(TAG, "showDatePickerDialogForStartDate failed to parse current date");
                return;
            }
        }else {
            newFragment = new DatePickerFragment().startPicker();
        }
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void setDueDate(String date) {
        Log.i(TAG, "setDueDate called for date: " + date);
        this.editTextAddTermEndDate.setText(date);
    }
    public void setStartDate(String date) {
        Log.i(TAG, "setStartDate called for date: " + date);
        this.editTextAddTermStartDate.setText(date);
    }
}