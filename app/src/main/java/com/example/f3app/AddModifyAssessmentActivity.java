package com.example.f3app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;


import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.AlarmManager.RTC;
import static com.example.f3app.AddEditTermActivity.TERM_END_DATE;
import static com.example.f3app.AddEditTermActivity.TERM_ID;
import static com.example.f3app.AddEditTermActivity.TERM_START_DATE;
import static com.example.f3app.AddEditTermActivity.TERM_TITLE;


import static com.example.f3app.AddModifyCourseActivity.COURSE_ID;
import static com.example.f3app.AddModifyCourseActivity.COURSE_TITLE;
import static com.example.f3app.AddModifyCourseActivity.COURSE_START_DATE;
import static com.example.f3app.AddModifyCourseActivity.COURSE_END_DATE;
import static com.example.f3app.AddModifyCourseActivity.COURSE_STATUS;
import static com.example.f3app.AddModifyCourseActivity.COURSE_MENTOR;
import static com.example.f3app.AddModifyCourseActivity.COURSE_PHONE;
import static com.example.f3app.AddModifyCourseActivity.COURSE_EMAIL;



public class AddModifyAssessmentActivity extends AppCompatActivity
        implements ActivityWithDates, AdapterView.OnItemSelectedListener {
    public static final String TAG = "IgB:AddModifyAssesActi";


    private NotificationManager mNotificationManager;
    private int NOTIFICATION_ID = 3;
    private EditText editTextAssessmentTitle;
    private EditText editTextAssessmentDueDate;
    private TextView textViewAssessmentStatus;
    private Spinner spinnerAssessmentType;
    private ProgressBar progressBarAssessment;
    private Button buttonProgressBarLess;
    private Button buttonProgressBarMore;

    private int courseId;
    private int termId;
    private Intent newIntent;
    private String assessmentType = "objective";

    public static final int SHOW_ASSESSMENTS_REQUEST = 1;

    public static final String ASSESSMENT_ID = "assessmentId";
    public static final String ASSESSMENT_TITLE = "assessmentTitle";
    public static final String ASSESSMENT_DUE_DATE = "assessmentDueDate";
    public static final String ASSESSMENT_STATUS = "assessmentStatus";
    public static final String ASSESSMENT_TYPE = "assessmentType";
    public static final String ASSESSMENT_PROGRESS = "assessmentProgress";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "constructor started");
        Intent intent = getIntent();
        courseId = intent.getIntExtra(COURSE_ID,-1);
        if ( courseId == -1 ) {
            Toast.makeText(this, "missing course id", Toast.LENGTH_SHORT).show();
            return;
        }
        // should always have
        if (! intent.hasExtra(COURSE_ID) || ! intent.hasExtra(COURSE_TITLE)){
            Log.i(TAG, "OnCreate - missing course info");
            Toast.makeText(this,"missing course info",Toast.LENGTH_LONG).show();
            return;
        }

        if (! intent.hasExtra(COURSE_START_DATE)) {
            Log.e(TAG, "OnCreate - missing course start date");
            Toast.makeText(this,"missing course start date",Toast.LENGTH_LONG).show();
            return;
        }

        if (! intent.hasExtra(COURSE_END_DATE)) {
            Log.e(TAG, "OnCreate - missing course start date");
            Toast.makeText(this,"missing course end date",Toast.LENGTH_LONG).show();
            return;
        }

        Log.i(TAG, "onCreate started for course id: " + courseId);

        setContentView(R.layout.activity_add_modify_assessment);
        editTextAssessmentTitle = findViewById(R.id.assessment_title);
        editTextAssessmentDueDate = findViewById(R.id.assessment_due_date);
        textViewAssessmentStatus= findViewById(R.id.assessment_status);
        progressBarAssessment = findViewById(R.id.assessment_progress_bar);
        progressBarAssessment.setProgress(intent.getIntExtra(ASSESSMENT_PROGRESS, 0));
        if (progressBarAssessment.getProgress()==0){
                    textViewAssessmentStatus.setText("not started");
        }
        buttonProgressBarLess = findViewById(R.id.buttonProgressLess);
        buttonProgressBarMore = findViewById(R.id.buttonProgressMore);

        //messageNotification
        //alertName = assessmentTitle + " " + "Start Date Alert";
        ToggleButton dueDateToggle = findViewById(R.id.assessment_due_date_alert);
        // setDateAlertToggle("Start Date Alert", startDateAlertToggle);
        setDateAlertToggle("Assessment Alert", dueDateToggle, ASSESSMENT_DUE_DATE);

        runProgressBarLess();
        runProgressBarMore();

        spinnerAssessmentType = (Spinner) findViewById(R.id.assessment_type);
        ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(this,
                R.array.assessment_types_array, android.R.layout.simple_spinner_dropdown_item);
        spinnerAssessmentType.setAdapter(adapterSpinner);
        spinnerAssessmentType.setOnItemSelectedListener(this);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_blue_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if ( intent.hasExtra(ASSESSMENT_ID) ){
            // set assessment Id only when we want to updated existing assessment
            setTitle("Edit Assessment");
            editTextAssessmentTitle.setText(intent.getStringExtra(ASSESSMENT_TITLE));
            editTextAssessmentDueDate.setText(intent.getStringExtra(ASSESSMENT_DUE_DATE));
            textViewAssessmentStatus.setText(intent.getStringExtra(ASSESSMENT_STATUS));
            assessmentType = intent.getStringExtra(ASSESSMENT_TYPE);
            spinnerAssessmentType.setSelection(adapterSpinner.getPosition(assessmentType));
            progressBarAssessment.setProgress(intent.getIntExtra(ASSESSMENT_PROGRESS,0));
            Log.d(TAG, "onCreate, editAssessment, got type from parent : " + assessmentType);

        }else {
            setTitle("Add Assessment");
        }

    }

    public void runProgressBarMore(){
        buttonProgressBarMore.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d(TAG, "started buttonProgressBarMore onClick method");
                int progress = progressBarAssessment.getProgress();
                if (progress<100) {
                    progress += 10;
                    progressBarAssessment.setProgress(progress);
                    textViewAssessmentStatus.setText("in progress");
                }else{
                    textViewAssessmentStatus.setText("completed");
                }
            }
        });

    }

    public void runProgressBarLess(){
        buttonProgressBarLess.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d(TAG, "started buttonProgressBarLess onClick method");
                int progress = progressBarAssessment.getProgress();
                if (progress>0) {
                    progress -= 10;
                    progressBarAssessment.setProgress(progress);
                    textViewAssessmentStatus.setText("in progress");
                }else{
                    textViewAssessmentStatus.setText("not started");
                }
            }
        });

    }

    public void showDatePickerDialogForDueDate(View v) {
        DialogFragment newFragment ;
        String currentDate = null;
        try {
            if(getIntent().hasExtra(ASSESSMENT_DUE_DATE)){
                currentDate = getIntent().getStringExtra(ASSESSMENT_DUE_DATE);
            }
            newFragment = new DatePickerFragment(currentDate,
                    getIntent().getStringExtra(COURSE_START_DATE),
                    getIntent().getStringExtra(COURSE_END_DATE));
        } catch (java.text.ParseException e){
            Toast.makeText(this, "Failed to parse dates", Toast.LENGTH_LONG).show();
            Log.e(TAG, "showDatePickerDialogForDueDate - failed to parse dates");
            return;
        }
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void setDueDate(String date) {
        Log.i(TAG, "setDueDate called for date: " + date);
        this.editTextAssessmentDueDate.setText(date);
    }
    public void setStartDate(String date) {
        Log.e(TAG, "setStartDate called for date: " + date);
    }

    private void saveAssessment(){

        Log.i(TAG, "saveAssessment started");
        String assessmentTitle = editTextAssessmentTitle.getText().toString();
        String assessmentDueDate = editTextAssessmentDueDate.getText().toString();
        String assessmentStatus = textViewAssessmentStatus.getText().toString();
        int assessmentProgress = progressBarAssessment.getProgress();

        if ( assessmentTitle.trim().isEmpty() ) {
            Log.e(TAG, "saveAssessment title is missing");
            Toast.makeText(this, "Please insert title", Toast.LENGTH_SHORT).show();
            return;
        }

        if ( assessmentDueDate.trim().isEmpty() ) {
            Log.e(TAG, "saveAssessment due date is missing");
            Toast.makeText(this, "Please insert due date", Toast.LENGTH_SHORT).show();
            return;
        }

        if ( assessmentStatus.trim().isEmpty() ) {
            Log.e(TAG, "saveAssessment status is missing");
            Toast.makeText(this, "Please insert status", Toast.LENGTH_SHORT).show();
            return;
        }

        if ( assessmentType.trim().isEmpty() ) {
            Log.e(TAG, "saveAssessment type is missing");
            Toast.makeText(this, "Please insert mentor name", Toast.LENGTH_SHORT).show();
            return;
        }

        newIntent = new Intent();
        packTermIntent();
        packCourseIntent();
        newIntent.putExtra(ASSESSMENT_TITLE, assessmentTitle);
        newIntent.putExtra(ASSESSMENT_DUE_DATE, assessmentDueDate);
        newIntent.putExtra(ASSESSMENT_STATUS, assessmentStatus);
        newIntent.putExtra(ASSESSMENT_PROGRESS, assessmentProgress);
        Log.d(TAG, "saveAssessment saving type as : " + assessmentType);
        newIntent.putExtra(ASSESSMENT_TYPE, assessmentType);

        // -1 is for no id, when addAssessment
        int id = getIntent().getIntExtra(ASSESSMENT_ID, -1);
        if ( id != -1) {
            newIntent.putExtra(ASSESSMENT_ID,id);
        }

        setResult(RESULT_OK, newIntent);
        Log.i(TAG, "saveAssessment is finishing");

        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_assessment_activity_menu, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data ) {
        // request code is position of assessment
        super.onActivityResult(requestCode, resultCode, data);
        if ( requestCode == AddModifyAssessmentActivity.SHOW_ASSESSMENTS_REQUEST){
            Log.i(TAG, "onActivityResult returned for SHOW_ASSESSMENTS_REQUEST");
        }
        Log.i(TAG, "started onActivityResult with requestCode " + requestCode + " resultCode " + resultCode);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG, "AddModifyAssessmentActivity.onOptionItemSelected called for id: " + item.getItemId());
        switch (item.getItemId()) {
            case R.id.save_assessment:
                Log.i(TAG, "AddModifyAssessmentActivity.onOptionItemSelected called for SAVE");
                saveAssessment();
                return true;
            case android.R.id.home:
                Log.i(TAG, "AddModifyAssessmentActivity.onOptionItemSelected called for HOME");
                newIntent = new Intent(this, AssessmentActivity.class);
                newIntent.putExtra(ASSESSMENT_ID,getIntent().getIntExtra(ASSESSMENT_ID, -1));
                newIntent.putExtra(ASSESSMENT_DUE_DATE,getIntent().getStringExtra(ASSESSMENT_DUE_DATE));
                newIntent.putExtra(ASSESSMENT_TITLE,getIntent().getStringExtra(ASSESSMENT_TITLE));
                newIntent.putExtra(ASSESSMENT_STATUS,getIntent().getStringExtra(ASSESSMENT_STATUS));
                newIntent.putExtra(ASSESSMENT_TYPE,getIntent().getStringExtra(ASSESSMENT_TYPE));
                packTermIntent();
                packCourseIntent();
                //IGOR_DEBUG:startActivity(newIntent);
                setResult(RESULT_CANCELED, newIntent);
                finish();
                return true;
            default:
                Log.i(TAG, "AddModifyAssessmentActivity.onOptionItemSelected called for UNHANDLED");
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

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        Log.d(TAG, "onItemSelected for Spinner started");
        assessmentType = parent.getItemAtPosition(pos).toString();
        Log.d(TAG, "onItemSelected for Spinner set assessmentType to " + assessmentType);
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }


    //messageNotification
    //alertName = assessmentTitle + " " + "Start Date Alert";
    //ToggleButton startDateAlertToggle = findViewById(R.id.course_start_date_alert);
    // setDateAlertToggle("Start Date Alert", startDateAlertToggle);
    private void setDateAlertToggle(String messageNotification, ToggleButton button, String extrasDateLocation){

        String fun = "setDateAlertToggle";
        Log.i(TAG, fun + "-  started");

        Context myContext = this;
        boolean alarmUp = false;
        final Intent notifyIntent = new Intent(myContext, CourseAlarmReceiver.class);
        notifyIntent.putExtras(getIntent());

        String alertName ;
        String assessmentTitle;

        if ( getIntent().hasExtra(ASSESSMENT_TITLE) ){
            assessmentTitle = getIntent().getStringExtra(ASSESSMENT_TITLE);
            alertName = messageNotification + " " + assessmentTitle;
            Log.d(TAG, fun + " - checks for alert called : " + alertName);
            notifyIntent.setAction(alertName);
            alarmUp = (PendingIntent.getBroadcast(myContext, NOTIFICATION_ID, notifyIntent,
                    PendingIntent.FLAG_NO_CREATE) != null);
            if(! alarmUp) {
                Log.d(TAG, fun + " - checks could not find alert called : " + alertName);
            }

        }else{
            Log.i(TAG, fun + " - started with non existing assessment");
        }

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        final AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        button.setChecked(alarmUp);

        button.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton,
                                                 boolean isChecked) {

                        if ( ! getIntent().hasExtra(ASSESSMENT_TITLE) ) {
                            Log.e(TAG, fun + " - setOnCheckedChangeListener there is no ASSESSMENT_TITLE");
                            Toast.makeText(myContext, "Save Assessment to setup Alarm", Toast.LENGTH_SHORT).show();
                            button.setChecked(false);
                            return;
                        }

                        String assessmentTitle = getIntent().getStringExtra(ASSESSMENT_TITLE);
                        String alertName = messageNotification + " " + assessmentTitle;

                        Log.i(TAG, fun + " - onCheckedChanged called to set alert with name: " + alertName);
                        notifyIntent.setAction(alertName);

                        PendingIntent notifyPendingIntent = PendingIntent.getBroadcast
                                (myContext, NOTIFICATION_ID, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                        String toastMessage;
                        if(isChecked){
                            //Set the toast message for the "on" case.
                            toastMessage = "Alert On";
                            //deliverNotification(MainActivity.this);
                            if (alarmManager!=null) {

                                try {
                                    Date date = new SimpleDateFormat("yyyy-MM-dd").parse(getIntent().getStringExtra(extrasDateLocation));
                                    alarmManager.set(RTC, date.getTime(), notifyPendingIntent);
                                }catch (java.text.ParseException e){
                                    Log.e(TAG, fun + " - failed to parse seconds from date: " + getIntent().getStringExtra(extrasDateLocation)) ;
                                }
                            }
                        } else {
                            //Set the toast message for the "off" case.
                            if (alarmManager != null) {
                                alarmManager.cancel(notifyPendingIntent);
                            }
                            toastMessage = "Alert Off";
                            //mNotificationManager.cancelAll();
                            mNotificationManager.cancel(NOTIFICATION_ID);
                        }

                        //Show a toast to say the alarm is turned on or off.
                        //Toast.makeText(AddModifyCourseActivity.this, toastMessage, Toast.LENGTH_SHORT)
                        Toast.makeText(myContext, toastMessage, Toast.LENGTH_SHORT)
                                .show();
                    }
                });

    }

}