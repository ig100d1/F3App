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

import static com.example.f3app.AddEditTermActivity.TERM_END_DATE;
import static com.example.f3app.AddEditTermActivity.TERM_ID;
import static com.example.f3app.AddEditTermActivity.TERM_START_DATE;
import static com.example.f3app.AddEditTermActivity.TERM_TITLE;

public class AddModifyCourseActivity extends AppCompatActivity implements ActivityWithDates {
    public static final String TAG = "IgB:AddModifyCourseActi";

    private EditText editTextCourseTitle;
    private EditText editTextCourseStartDate;
    private EditText editTextCourseEndDate;
    private EditText editTextCourseStatus;
    private EditText editTextCourseMentorName;
    private EditText editTextCourseEmail;
    private EditText editTextCoursePhone;
    private int termId;
    private Intent newIntent;

    private Button buttonShowAssessments;
    private Button buttonShowNotes;

    public static final int SHOW_ASSESSMENTS_REQUEST = 1;
    public static final int SHOW_NOTES_REQUEST = 1;

    public static final String COURSE_ID = "courseId";
    public static final String COURSE_TITLE = "courseTitle";
    public static final String COURSE_START_DATE = "courseStartDate";
    public static final String COURSE_END_DATE = "courseEndDate";
    public static final String COURSE_STATUS = "courseStatus";
    public static final String COURSE_MENTOR = "courseMentor";
    public static final String COURSE_PHONE = "coursePhone";
    public static final String COURSE_EMAIL = "courseEmail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "constructor started");
        Intent intent = getIntent();
        termId = intent.getIntExtra(TERM_ID,-1);
        if ( termId == -1 ) {
            Toast.makeText(this, "missing term id", Toast.LENGTH_SHORT).show();
            return;
        }
        // should always have
        if (! intent.hasExtra(TERM_ID) || ! intent.hasExtra(TERM_TITLE)){
            Log.i(TAG, "OnCreate - missing term info");
            Toast.makeText(this,"missing term info",Toast.LENGTH_LONG).show();
            return;
        }

        Log.i(TAG, "onCreate started for term id: " + termId);

        setContentView(R.layout.activity_add_modify_course);
        editTextCourseTitle = findViewById(R.id.course_title);
        editTextCourseStartDate = findViewById(R.id.course_start_date);
        editTextCourseEndDate = findViewById(R.id.course_end_date);
        editTextCourseStatus= findViewById(R.id.course_status);
        editTextCourseMentorName = findViewById(R.id.course_mentor_name);
        editTextCourseEmail = findViewById(R.id.course_email);
        editTextCoursePhone = findViewById(R.id.course_phone);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_blue_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if ( getIntent().hasExtra(COURSE_ID) ){
            // set course Id only when we want to updated existing course
            setTitle("Edit Course");
            buttonShowAssessments = findViewById(R.id.button_show_assessments);
            buttonShowNotes = findViewById(R.id.button_show_notes);

            editTextCourseTitle.setText(intent.getStringExtra(COURSE_TITLE));
            editTextCourseStartDate.setText(intent.getStringExtra(COURSE_START_DATE));
            editTextCourseEndDate.setText(intent.getStringExtra(COURSE_END_DATE));
            editTextCourseStatus.setText(intent.getStringExtra(COURSE_STATUS));
            editTextCourseMentorName.setText(intent.getStringExtra(COURSE_MENTOR));
            editTextCoursePhone.setText(intent.getStringExtra(COURSE_PHONE));
            editTextCourseEmail.setText(intent.getStringExtra(COURSE_EMAIL));

            buttonShowAssessments.setOnClickListener(new Button.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "started buttonShowAssessments onClick method");
                    newIntent = new Intent(AddModifyCourseActivity.this, AssessmentActivity.class );
                    packTermIntent();
                    packCourseIntent();
                    startActivityForResult(newIntent,SHOW_ASSESSMENTS_REQUEST);
                }
            });

            buttonShowNotes.setOnClickListener(new Button.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "started buttonShowNotes onClick method");
                    newIntent = new Intent(AddModifyCourseActivity.this, NoteActivity.class );
                    packTermIntent();
                    packCourseIntent();
                    startActivityForResult(newIntent,SHOW_NOTES_REQUEST);
                }
            });


        }else {
            setTitle("Add Course");
            buttonShowAssessments = findViewById(R.id.button_show_assessments);
            buttonShowAssessments.setVisibility(Button.INVISIBLE);

            buttonShowNotes = findViewById(R.id.button_show_notes);
            buttonShowNotes.setVisibility(Button.INVISIBLE);
        }
    }

    private void saveCourse(){

        Log.i(TAG, "saveCourse started");
        String courseTitle = editTextCourseTitle.getText().toString();
        String courseStartDate = editTextCourseStartDate.getText().toString();
        String courseEndDate = editTextCourseEndDate.getText().toString();
        String courseStatus = editTextCourseStatus.getText().toString();
        String courseMentorName = editTextCourseMentorName.getText().toString();
        String courseEmail = editTextCourseEmail.getText().toString();
        String coursePhone = editTextCoursePhone.getText().toString();

        if ( courseTitle.trim().isEmpty() ) {
            Log.e(TAG, "saveCourse title is missing");
            Toast.makeText(this, "Please insert title", Toast.LENGTH_SHORT).show();
            return;
        }

        if ( courseStartDate.trim().isEmpty() ) {
            Log.e(TAG, "saveCourse start date is missing");
            Toast.makeText(this, "Please insert start date", Toast.LENGTH_SHORT).show();
            return;
        }

        if ( courseEndDate.trim().isEmpty() ) {
            Log.e(TAG, "saveCourse end date is missing");
            Toast.makeText(this, "Please insert end date", Toast.LENGTH_SHORT).show();
            return;
        }

        if ( courseStatus.trim().isEmpty() ) {
            Log.e(TAG, "saveCourse status is missing");
            Toast.makeText(this, "Please insert status", Toast.LENGTH_SHORT).show();
            return;
        }

        if ( courseMentorName.trim().isEmpty() ) {
            Log.e(TAG, "saveCourse mentor is missing");
            Toast.makeText(this, "Please insert mentor name", Toast.LENGTH_SHORT).show();
            return;
        }

        if ( coursePhone.trim().isEmpty() ) {
            Log.e(TAG, "saveCourse phone is missing");
            Toast.makeText(this, "Please insert phone", Toast.LENGTH_SHORT).show();
            return;
        }

        if ( courseEmail.trim().isEmpty() ) {
            Log.e(TAG, "saveCourse email is missing");
            Toast.makeText(this, "Please insert email", Toast.LENGTH_SHORT).show();
            return;
        }

        newIntent = new Intent();
        packTermIntent();
        newIntent.putExtra(COURSE_TITLE, courseTitle);
        newIntent.putExtra(COURSE_START_DATE, courseStartDate);
        newIntent.putExtra(COURSE_END_DATE, courseEndDate);
        newIntent.putExtra(COURSE_STATUS, courseStatus);
        newIntent.putExtra(COURSE_MENTOR, courseMentorName);
        newIntent.putExtra(COURSE_PHONE, coursePhone);
        newIntent.putExtra(COURSE_EMAIL, courseEmail);
        newIntent.putExtra(TERM_ID, termId);
        newIntent.putExtra(TERM_END_DATE, getIntent().getStringExtra(TERM_END_DATE));
        newIntent.putExtra(TERM_START_DATE, getIntent().getStringExtra(TERM_START_DATE));

        // -1 is for no id, when addCourse
        int id = getIntent().getIntExtra(COURSE_ID, -1);
        if ( id != -1) {
            newIntent.putExtra(COURSE_ID,id);
        }

        setResult(RESULT_OK, newIntent);
        Log.i(TAG, "saveCourse is finishing");

        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_course_activity_menu, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data ) {
        // request code is position of course
        super.onActivityResult(requestCode, resultCode, data);
        if ( requestCode == AddModifyCourseActivity.SHOW_ASSESSMENTS_REQUEST){
            Log.i(TAG, "onActivityResult returned for SHOW_ASSESSMENTS_REQUEST");
        }
        if ( requestCode == AddModifyCourseActivity.SHOW_NOTES_REQUEST){
            Log.i(TAG, "onActivityResult returned for SHOW_NOTES_REQUEST");
        }

        Log.i(TAG, "started onActivityResult with requestCode " + requestCode + " resultCode " + resultCode);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG, "AddModifyCourseActivity.onOptionItemSelected called for id: " + item.getItemId());
        switch (item.getItemId()) {
            case R.id.save_course:
                Log.i(TAG, "AddModifyCourseActivity.onOptionItemSelected called for SAVE");
                saveCourse();
                return true;
            case android.R.id.home:
                Log.i(TAG, "AddModifyCourseActivity.onOptionItemSelected called for HOME");
                newIntent = new Intent(this, CourseActivity.class);
                newIntent.putExtra(COURSE_ID,getIntent().getIntExtra(COURSE_ID, -1));
                newIntent.putExtra(COURSE_START_DATE,getIntent().getStringExtra(COURSE_START_DATE));
                newIntent.putExtra(COURSE_END_DATE,getIntent().getStringExtra(COURSE_END_DATE));
                newIntent.putExtra(COURSE_TITLE,getIntent().getStringExtra(COURSE_TITLE));
                packTermIntent();
                //IGOR_DEBUG:startActivity(newIntent);
                setResult(RESULT_CANCELED, newIntent);
                finish();
                return true;
            default:
                Log.i(TAG, "AddModifyCourseActivity.onOptionItemSelected called for UNHANDLED");
                return super.onOptionsItemSelected(item);
        }
    }

    private void packTermIntent(){
        Log.i(TAG, "packTermIntent called for term title: "
                + getIntent().getStringExtra(TERM_TITLE));
        newIntent.putExtra(TERM_END_DATE, getIntent().getStringExtra(TERM_END_DATE));
        newIntent.putExtra(TERM_TITLE, getIntent().getStringExtra(TERM_TITLE));
        newIntent.putExtra(TERM_START_DATE, getIntent().getStringExtra(TERM_START_DATE));
        newIntent.putExtra(TERM_ID,getIntent().getIntExtra(TERM_ID,-1));
    }

    private void packCourseIntent(){
        Log.i(TAG, "packCourseIntent called for course title: "
                + getIntent().getStringExtra(COURSE_TITLE));
        newIntent.putExtra(COURSE_ID,getIntent().getIntExtra(COURSE_ID, -1));
        newIntent.putExtra(COURSE_TITLE,getIntent().getStringExtra(COURSE_TITLE));
        newIntent.putExtra(COURSE_START_DATE,getIntent().getStringExtra(COURSE_START_DATE));
        newIntent.putExtra(COURSE_END_DATE,getIntent().getStringExtra(COURSE_END_DATE));
        newIntent.putExtra(COURSE_STATUS,getIntent().getStringExtra(COURSE_STATUS));
        newIntent.putExtra(COURSE_MENTOR,getIntent().getStringExtra(COURSE_MENTOR));
        newIntent.putExtra(COURSE_PHONE,getIntent().getStringExtra(COURSE_PHONE));
        newIntent.putExtra(COURSE_EMAIL,getIntent().getStringExtra(COURSE_EMAIL));
        Log.i(TAG, "packCourseIntent finished for course title: "
                + getIntent().getStringExtra(COURSE_TITLE));
    }


    public void showDatePickerDialogForEndDate(View v) {
        DialogFragment newFragment ;
        String currentDate = null;
        try {
            if(getIntent().hasExtra(AddModifyCourseActivity.COURSE_END_DATE)){
                currentDate = getIntent().getStringExtra(AddModifyCourseActivity.COURSE_END_DATE);
            }
            newFragment = new DatePickerFragment(currentDate,
                    getIntent().getStringExtra(TERM_START_DATE),
                    getIntent().getStringExtra(TERM_END_DATE));
        } catch (java.text.ParseException e){
            Toast.makeText(this, "Failed to parse dates", Toast.LENGTH_LONG).show();
            Log.e(TAG, "showDatePickerDialogForEndDate - failed to parse dates");
            return;
        }
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void showDatePickerDialogForStartDate(View v) {
        DialogFragment newFragment ;
        String currentDate = null;

        try {
            if(getIntent().hasExtra(AddModifyCourseActivity.COURSE_START_DATE)){
                currentDate = getIntent().getStringExtra(AddModifyCourseActivity.COURSE_START_DATE);
            }
            newFragment = new DatePickerFragment(currentDate,
                    getIntent().getStringExtra(TERM_START_DATE),
                    getIntent().getStringExtra(TERM_END_DATE)).startPicker();

        } catch (java.text.ParseException e){
            Toast.makeText(this, "Failed to parse dates", Toast.LENGTH_LONG).show();
            Log.e(TAG, "showDatePickerDialogForDueDate - failed to parse dates");
            return;
        }
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void setDueDate(String date) {
        Log.i(TAG, "setDueDate called for date: " + date);
        this.editTextCourseEndDate.setText(date);
    }
    public void setStartDate(String date) {
        Log.i(TAG, "setStartDate called for date: " + date);
        this.editTextCourseStartDate.setText(date);
    }

}