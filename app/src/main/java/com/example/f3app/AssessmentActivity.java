package com.example.f3app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import static com.example.f3app.AddEditTermActivity.TERM_END_DATE;
import static com.example.f3app.AddEditTermActivity.TERM_ID;
import static com.example.f3app.AddEditTermActivity.TERM_START_DATE;
import static com.example.f3app.AddEditTermActivity.TERM_TITLE;
import static com.example.f3app.AddEditTermActivity.CREATE_FAILED;

import static com.example.f3app.AddModifyCourseActivity.COURSE_ID;
import static com.example.f3app.AddModifyCourseActivity.COURSE_TITLE;
import static com.example.f3app.AddModifyCourseActivity.COURSE_START_DATE;
import static com.example.f3app.AddModifyCourseActivity.COURSE_END_DATE;
import static com.example.f3app.AddModifyCourseActivity.COURSE_STATUS;
import static com.example.f3app.AddModifyCourseActivity.COURSE_MENTOR;
import static com.example.f3app.AddModifyCourseActivity.COURSE_PHONE;
import static com.example.f3app.AddModifyCourseActivity.COURSE_EMAIL;

public class AssessmentActivity extends AppCompatActivity {

    private AssessmentViewModel assessmentViewModel;
    private static final int MAX_COURSE_ASSESSMENTS = 5;
    RecyclerView mRecyclerView;
    private static final String TAG = "IgB:AssessmentActivity";
    private int term_id;
    private int course_id;
    private String term_title;
    private String term_start_date;
    private String term_end_date;
    private static final int ADD_ASSESSMENT_REQUEST = 1;
    private static final int EDIT_ASSESSMENT_REQUEST = 2;
    private Intent newIntent;

    String assessments[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "started onCreate");

        Intent intent = getIntent();

        // should always have
        if (! intent.hasExtra(TERM_ID) || ! intent.hasExtra(TERM_TITLE)){
            Log.e(TAG, "OnCreate - missing term info");
            Toast.makeText(this,"missing term info",Toast.LENGTH_LONG).show();
            return;
        }

        // should always have
        if (! intent.hasExtra(COURSE_ID) || ! intent.hasExtra(COURSE_TITLE)){
            Log.e(TAG, "OnCreate - missing course info");
            Toast.makeText(this,"missing course info",Toast.LENGTH_LONG).show();
            return;
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        course_id = intent.getIntExtra(COURSE_ID,-1);
        if ( course_id == -1 ){
            Toast.makeText(this,"missing course id",Toast.LENGTH_LONG).show();
            return;
        }

        setTitle("Assessments");

        assessmentViewModel = new ViewModelProvider(this).get(AssessmentViewModel.class);
        setContentView(R.layout.activity_assessment);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);

        Log.i(TAG, " created mRecyclerView");

        AssessmentAdapter assessmentAdapter = new AssessmentAdapter();
        Log.i(TAG, "created assessmentAdapter");
        mRecyclerView.setAdapter(assessmentAdapter);

        assessmentViewModel.getAllAssessments(course_id).observe(this, new Observer<List<Assessment>>() {
            @Override
            public void onChanged(List<Assessment> assessments) {
                // update recyclerView
                Log.i(TAG, "running assessmentViewModel.getAllAssessments.observer onchanged");
                assessmentAdapter.setAssessments(assessments);
            }
        });

        /*
            Delete Assessment
         */
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                assessmentViewModel.delete(assessmentAdapter.getAssessmentAt(viewHolder.getAdapterPosition()));
                Toast.makeText(AssessmentActivity.this, "Assessment Deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(mRecyclerView);

        Log.i(TAG, "onCreate onSwiped listeners up");

        //magic
        assessmentAdapter.setOnAssessmentClickListener(new AssessmentAdapter.OnAssessmentClickListener() {
            @Override
            public void onItemClick(Assessment assessment) {
                Log.i(TAG, "setOnAssessmentClickListener method started ");
                newIntent = new Intent(AssessmentActivity.this, AddModifyAssessmentActivity.class);
                newIntent.putExtra(AddModifyAssessmentActivity.ASSESSMENT_ID, assessment.getId());
                newIntent.putExtra(AddModifyAssessmentActivity.ASSESSMENT_TITLE, assessment.getTitle());
                newIntent.putExtra(AddModifyAssessmentActivity.ASSESSMENT_DUE_DATE, assessment.getDueDate());
                newIntent.putExtra(AddModifyAssessmentActivity.ASSESSMENT_TYPE, assessment.getType());
                newIntent.putExtra(AddModifyAssessmentActivity.ASSESSMENT_STATUS, assessment.getStatus());
                packTermIntent();
                packCourseIntent();
                startActivityForResult(newIntent, EDIT_ASSESSMENT_REQUEST);
            }
        });

        Log.i(TAG, "onCreate onClick listeners up");

        FloatingActionButton buttonAddAssessment = findViewById(R.id.button_add_note);
        if (buttonAddAssessment == null){
            Log.e(TAG, "onCreate failed to init floatingActionButton");
            newIntent = new Intent(this, AddEditTermActivity.class);
            packTermIntent();
            packCourseIntent();
            setResult(CREATE_FAILED, newIntent);
            finish();
            return;
        }
        Log.d(TAG, "addAssessment listener started");

        buttonAddAssessment.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Log.d(TAG, "buttonAddAssessment.setOnClickListneer started for course: " + course_id);
                int countCourseAssessments = assessmentViewModel.countCourseAssessments(course_id);
                if (countCourseAssessments < MAX_COURSE_ASSESSMENTS) {
                    Log.i(TAG, "started buttonAddAssessment onClick method");
                    newIntent = new Intent(AssessmentActivity.this, AddModifyAssessmentActivity.class);
                    packTermIntent();
                    packCourseIntent();
                    startActivityForResult(newIntent, ADD_ASSESSMENT_REQUEST);
                }else{
                    Toast.makeText(v.getContext(),"cant add more assessments", Toast.LENGTH_LONG).show();
                }
            }
        });

        Log.i(TAG, "created assessmentViewModel");
    }

    /*
    Back arrow returns to empty term, because not content.
    starting AddEditTermActivity with Term data that AssessmentActivity started
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG, "onOptionsItemSelected method started");
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.i(TAG, "onOptionsItemSelected method started for HOME");
                newIntent = new Intent(this, AddModifyCourseActivity.class);
                packTermIntent();
                packCourseIntent();
                setResult(RESULT_CANCELED, newIntent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data ) {
        super.onActivityResult(requestCode,resultCode,data);
        Log.i(TAG, "started onActivityResult with requestCode " + requestCode + " resultCode " + resultCode);
        if( requestCode == ADD_ASSESSMENT_REQUEST && resultCode == RESULT_OK) {
            Log.i(TAG, "onActivityResult has new assessment data to add");

            String assessmentTitle = data.getStringExtra(AddModifyAssessmentActivity.ASSESSMENT_TITLE);
            String assessmentStatus = data.getStringExtra(AddModifyAssessmentActivity.ASSESSMENT_STATUS);
            String assessmentDueDate = data.getStringExtra(AddModifyAssessmentActivity.ASSESSMENT_DUE_DATE);
            String assessmentType = data.getStringExtra(AddModifyAssessmentActivity.ASSESSMENT_TYPE);

            int assessmentCourseId = data.getIntExtra(COURSE_ID, -1 );

            Assessment c = new Assessment(assessmentTitle, assessmentDueDate, assessmentCourseId,
                    assessmentStatus, assessmentType);

            assessmentViewModel.insert(c);
            Toast.makeText(this, "Assessment Saved", Toast.LENGTH_SHORT).show();

        } else if( requestCode == EDIT_ASSESSMENT_REQUEST && resultCode == RESULT_OK) {
            Log.i(TAG, "onActivityResult has modified data to save");
            int assessmentId = data.getIntExtra(AddModifyAssessmentActivity.ASSESSMENT_ID, -1);
            if (assessmentId == -1) {
                Toast.makeText(this, "Couldn't safe assessment data", Toast.LENGTH_SHORT).show();
                return;
            }

            String assessmentTitle = data.getStringExtra(AddModifyAssessmentActivity.ASSESSMENT_TITLE);
            String assessmentStatus = data.getStringExtra(AddModifyAssessmentActivity.ASSESSMENT_STATUS);
            String assessmentDueDate = data.getStringExtra(AddModifyAssessmentActivity.ASSESSMENT_DUE_DATE);
            String assessmentType = data.getStringExtra(AddModifyAssessmentActivity.ASSESSMENT_TYPE);

            int assessmentCourseId = data.getIntExtra(COURSE_ID, -1 );

            Assessment c = new Assessment(assessmentTitle, assessmentDueDate, assessmentCourseId,
                    assessmentStatus, assessmentType);

            c.setId(assessmentId);
            assessmentViewModel.update(c);
            Toast.makeText(this, "Assessment updated", Toast.LENGTH_SHORT).show();

        }else{
            Log.i(TAG, "onActivityResult has no data to save");
            Toast.makeText(this, "Assessment Not Saved", Toast.LENGTH_SHORT).show();
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
        Log.i(TAG, "packCourseIntent called for term title: "
                + getIntent().getStringExtra(COURSE_TITLE));
        newIntent.putExtra(COURSE_ID,getIntent().getIntExtra(COURSE_ID, -1));
        newIntent.putExtra(COURSE_TITLE,getIntent().getStringExtra(COURSE_TITLE));
        newIntent.putExtra(COURSE_START_DATE,getIntent().getStringExtra(COURSE_START_DATE));
        newIntent.putExtra(COURSE_END_DATE,getIntent().getStringExtra(COURSE_END_DATE));
        newIntent.putExtra(COURSE_STATUS,getIntent().getStringExtra(COURSE_STATUS));
        newIntent.putExtra(COURSE_MENTOR,getIntent().getStringExtra(COURSE_MENTOR));
        newIntent.putExtra(COURSE_PHONE,getIntent().getStringExtra(COURSE_PHONE));
        newIntent.putExtra(COURSE_EMAIL,getIntent().getStringExtra(COURSE_EMAIL));
    }
}