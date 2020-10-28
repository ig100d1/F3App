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
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.List;

import static com.example.f3app.AddEditTermActivity.TERM_END_DATE;
import static com.example.f3app.AddEditTermActivity.TERM_ID;
import static com.example.f3app.AddEditTermActivity.TERM_START_DATE;
import static com.example.f3app.AddEditTermActivity.TERM_TITLE;

public class CourseActivity extends AppCompatActivity {

    private CourseViewModel courseViewModel;
    RecyclerView mRecyclerView;
    private static final String TAG = "IgB:CourseActivity";
    private int term_id;
    private String term_title;
    private String term_start_date;
    private String term_end_date;
    private static final int ADD_COURSE_REQUEST = 1;
    private static final int EDIT_COURSE_REQUEST = 2;
    private Intent newIntent;

    String courses[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "started main activity");

        Intent intent = getIntent();

        // should always have
        if (! intent.hasExtra(TERM_ID) || ! intent.hasExtra(TERM_TITLE)){
            Log.i(TAG, "OnCreate - missing term info");
            Toast.makeText(this,"missing term info",Toast.LENGTH_LONG).show();
            return;
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        term_id = intent.getIntExtra(TERM_ID,-1);
        if ( term_id == -1 ){
            Toast.makeText(this,"missing term id",Toast.LENGTH_LONG).show();
            return;
        }

        courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);
        setContentView(R.layout.activity_course);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);

        Log.i(TAG, " created mRecyclerView");

        CourseAdapter courseAdapter = new CourseAdapter();
        Log.i(TAG, "created courseAdapter");
        mRecyclerView.setAdapter(courseAdapter);

        courseViewModel.getAllCourses(term_id).observe(this, new Observer<List<Course>>() {
            @Override
            public void onChanged(List<Course> courses) {
                // update recyclerView
                Log.i(TAG, "running courseViewModel.getAllCourses.observer onchanged");
                courseAdapter.setCourses(courses);
            }
        });

        /*
            Delete Course
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
                courseViewModel.delete(courseAdapter.getCourseAt(viewHolder.getAdapterPosition()));
                Toast.makeText(CourseActivity.this, "Course Deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(mRecyclerView);

        //magic
        courseAdapter.setOnCourseClickListener(new CourseAdapter.OnCourseClickListener() {
            @Override
            public void onItemClick(Course course) {
                Log.i(TAG, "setOnCourseClickListener method started ");
                newIntent = new Intent(CourseActivity.this, AddModifyCourseActivity.class);
                newIntent.putExtra(AddModifyCourseActivity.COURSE_ID, course.getId());
                newIntent.putExtra(AddModifyCourseActivity.COURSE_TITLE, course.getTitle());
                newIntent.putExtra(AddModifyCourseActivity.COURSE_START_DATE, course.getStartDate());
                newIntent.putExtra(AddModifyCourseActivity.COURSE_END_DATE, course.getEndDate());
                newIntent.putExtra(AddModifyCourseActivity.COURSE_STATUS, course.getStatus());
                newIntent.putExtra(AddModifyCourseActivity.COURSE_MENTOR, course.getMentorName());
                newIntent.putExtra(AddModifyCourseActivity.COURSE_PHONE, course.getPhone());
                newIntent.putExtra(AddModifyCourseActivity.COURSE_EMAIL, course.getEmail());
                packTermIntent();
                startActivityForResult(newIntent, EDIT_COURSE_REQUEST);
            }
        });

        Log.i(TAG, "created courseViewModel");
    }

    /*
    Back arrow returns to empty term, because not content.
    starting AddEditTermActivity with Term data that CourseActivity started
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG, "onOptionsItemSelected method started");
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.i(TAG, "onOptionsItemSelected method started for HOME");
                newIntent = new Intent(this, AddEditTermActivity.class);
                packTermIntent();
                newIntent.putExtra("IGOR_DEBUG","Came from CourseActivity.onOptionsItemSelected.Home");
                startActivity(newIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data ) {
        super.onActivityResult(requestCode,resultCode,data);
        Log.i(TAG, "started onActivityResult with requestCode " + requestCode + " resultCode " + resultCode);
        if( requestCode == ADD_COURSE_REQUEST && resultCode == RESULT_OK) {
            Log.i(TAG, "onActivityResult has new course data to add");

            String courseTitle = data.getStringExtra(AddModifyCourseActivity.COURSE_TITLE);
            String courseStartDate = data.getStringExtra(AddModifyCourseActivity.COURSE_START_DATE);
            String courseEndDate = data.getStringExtra(AddModifyCourseActivity.COURSE_END_DATE);
            String courseStatus = data.getStringExtra(AddModifyCourseActivity.COURSE_STATUS);
            String courseMentorName = data.getStringExtra(AddModifyCourseActivity.COURSE_MENTOR);
            String courseEmail = data.getStringExtra(AddModifyCourseActivity.COURSE_EMAIL);
            String coursePhone = data.getStringExtra(AddModifyCourseActivity.COURSE_PHONE );
            int courseTermId = data.getIntExtra(TERM_ID, -1 );

            Course c = new Course(courseTitle, courseStartDate, courseEndDate, courseStatus,
                    courseMentorName, coursePhone, courseEmail, courseTermId);

            courseViewModel.insert(c);
            Toast.makeText(this, "Course Saved", Toast.LENGTH_SHORT).show();

        } else if( requestCode == EDIT_COURSE_REQUEST && resultCode == RESULT_OK) {
            Log.i(TAG, "onActivityResult has modified data to save");
            int courseId = data.getIntExtra(AddModifyCourseActivity.COURSE_ID, -1);
            if (courseId == -1) {
                Toast.makeText(this, "Couldn't safe course data", Toast.LENGTH_SHORT).show();
                return;
            }
            String courseTitle = data.getStringExtra(AddModifyCourseActivity.COURSE_TITLE);
            String courseStartDate = data.getStringExtra(AddModifyCourseActivity.COURSE_START_DATE);
            String courseEndDate = data.getStringExtra(AddModifyCourseActivity.COURSE_END_DATE);
            String courseStatus = data.getStringExtra(AddModifyCourseActivity.COURSE_STATUS);
            String courseMentorName = data.getStringExtra(AddModifyCourseActivity.COURSE_MENTOR);
            String courseEmail = data.getStringExtra(AddModifyCourseActivity.COURSE_EMAIL);
            String coursePhone = data.getStringExtra(AddModifyCourseActivity.COURSE_PHONE );
            int courseTermId = data.getIntExtra(TERM_ID, -1 );

            Course c = new Course(courseTitle, courseStartDate, courseEndDate, courseStatus,
                    courseMentorName, coursePhone, courseEmail, courseTermId);
            c.setId(courseId);
            courseViewModel.update(c);
            Toast.makeText(this, "Course updated", Toast.LENGTH_SHORT).show();
        }else{
            Log.i(TAG, "onActivityResult has no data to save");
            Toast.makeText(this, "Course Not Saved", Toast.LENGTH_SHORT).show();
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