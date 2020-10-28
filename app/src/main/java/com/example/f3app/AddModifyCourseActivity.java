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

import static com.example.f3app.AddEditTermActivity.TERM_END_DATE;
import static com.example.f3app.AddEditTermActivity.TERM_ID;
import static com.example.f3app.AddEditTermActivity.TERM_START_DATE;
import static com.example.f3app.AddEditTermActivity.TERM_TITLE;

public class AddModifyCourseActivity extends AppCompatActivity {
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

    public static final int SHOW_ASSESSMENTS_REQUEST = 1;

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

            editTextCourseTitle.setText(intent.getStringExtra(COURSE_TITLE));
            editTextCourseStartDate.setText(intent.getStringExtra(COURSE_START_DATE));
            editTextCourseEndDate.setText(intent.getStringExtra(COURSE_END_DATE));
            editTextCourseStatus.setText(intent.getStringExtra(COURSE_STATUS));
            editTextCourseMentorName.setText(intent.getStringExtra(COURSE_MENTOR));
            editTextCoursePhone.setText(intent.getStringExtra(COURSE_PHONE));
            editTextCourseEmail.setText(intent.getStringExtra(COURSE_EMAIL));

            // TODO: redo for Assessments
            // to set number or maybe date, use value and default value
            // editTextCourseEndDate.setValue(intent.getIntExtra(COURSE_START_DATE, 1999)
            buttonShowAssessments.setOnClickListener(new Button.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "started buttonShowAssessments onClick method");
                    newIntent = new Intent(AddModifyCourseActivity.this, CourseActivity.class );
                    newIntent.putExtra(COURSE_ID,getIntent().getIntExtra(COURSE_ID, -1));
                    newIntent.putExtra(COURSE_TITLE,getIntent().getStringExtra(COURSE_TITLE));
                    newIntent.putExtra(COURSE_START_DATE,getIntent().getStringExtra(COURSE_START_DATE));
                    newIntent.putExtra(COURSE_END_DATE,getIntent().getStringExtra(COURSE_END_DATE));
                    newIntent.putExtra(COURSE_STATUS,getIntent().getStringExtra(COURSE_STATUS));
                    newIntent.putExtra(COURSE_MENTOR,getIntent().getStringExtra(COURSE_MENTOR));
                    newIntent.putExtra(COURSE_PHONE,getIntent().getStringExtra(COURSE_PHONE));
                    newIntent.putExtra(COURSE_EMAIL,getIntent().getStringExtra(COURSE_EMAIL));
                    packTermIntent();
                    startActivityForResult(newIntent,SHOW_ASSESSMENTS_REQUEST);
                }
            });

        }else {
            setTitle("Add Course");
            buttonShowAssessments = findViewById(R.id.button_show_assessments);
            buttonShowAssessments.setVisibility(Button.INVISIBLE);
            packTermIntent();
        }
    }

    private void saveCourse(){

        String courseTitle = editTextCourseTitle.getText().toString();
        String courseStartDate = editTextCourseStartDate.getText().toString();
        String courseEndDate = editTextCourseEndDate.getText().toString();
        String courseStatus = editTextCourseStatus.getText().toString();
        String courseMentorName = editTextCourseMentorName.getText().toString();
        String courseEmail = editTextCourseEmail.getText().toString();
        String coursePhone = editTextCoursePhone.getText().toString();

        if ( courseTitle.trim().isEmpty() ) {
            Toast.makeText(this, "Please insert title description", Toast.LENGTH_SHORT).show();
            return;
        }

        if ( courseStartDate.trim().isEmpty() ) {
            Toast.makeText(this, "Please insert start date", Toast.LENGTH_SHORT).show();
            return;
        }

        if ( courseEndDate.trim().isEmpty() ) {
            Toast.makeText(this, "Please insert end date", Toast.LENGTH_SHORT).show();
            return;
        }

        if ( courseStatus.trim().isEmpty() ) {
            Toast.makeText(this, "Please insert status", Toast.LENGTH_SHORT).show();
            return;
        }

        if ( courseMentorName.trim().isEmpty() ) {
            Toast.makeText(this, "Please insert mentor name", Toast.LENGTH_SHORT).show();
            return;
        }

        if ( coursePhone.trim().isEmpty() ) {
            Toast.makeText(this, "Please insert phone", Toast.LENGTH_SHORT).show();
            return;
        }

        if ( courseEmail.trim().isEmpty() ) {
            Toast.makeText(this, "Please insert email", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(COURSE_TITLE, courseTitle);
        data.putExtra(COURSE_START_DATE, courseStartDate);
        data.putExtra(COURSE_END_DATE, courseEndDate);
        data.putExtra(COURSE_STATUS, courseStatus);
        data.putExtra(COURSE_MENTOR, courseMentorName);
        data.putExtra(COURSE_PHONE, coursePhone);
        data.putExtra(COURSE_EMAIL, courseEmail);
        data.putExtra(TERM_ID, termId);
        data.putExtra(TERM_END_DATE, getIntent().getStringExtra(TERM_END_DATE));
        data.putExtra(TERM_START_DATE, getIntent().getStringExtra(TERM_START_DATE));

        // -1 is for no id, when addCourse
        int id = getIntent().getIntExtra(COURSE_ID, -1);
        if ( id != -1) {
            data.putExtra(COURSE_ID,id);
        }

        setResult(RESULT_OK, data);
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
                startActivity(newIntent);
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


}