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

import static com.example.f3app.AddModifyCourseActivity.COURSE_ID;
import static com.example.f3app.AddModifyCourseActivity.COURSE_TITLE;
import static com.example.f3app.AddModifyCourseActivity.COURSE_START_DATE;
import static com.example.f3app.AddModifyCourseActivity.COURSE_END_DATE;
import static com.example.f3app.AddModifyCourseActivity.COURSE_STATUS;
import static com.example.f3app.AddModifyCourseActivity.COURSE_MENTOR;
import static com.example.f3app.AddModifyCourseActivity.COURSE_PHONE;
import static com.example.f3app.AddModifyCourseActivity.COURSE_EMAIL;

public class AddModifyNoteActivity extends AppCompatActivity {
    public static final String TAG = "IgB:AddModifyNoteActiv";

    private EditText editTextNoteNote;
    private EditText editTextNoteTitle;
    private int courseId;
    private int termId;
    private Intent newIntent;
    private boolean sendEmail = false;
    private boolean sendSms = false;

    public static final String NOTE_ID = "noteId";
    public static final String NOTE_NOTE = "noteNote";
    public static final String NOTE_TITLE = "noteTitle";

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

        Log.i(TAG, "onCreate started for course id: " + courseId);

        setContentView(R.layout.activity_add_modify_note);
        editTextNoteNote = findViewById(R.id.edit_text_note);
        editTextNoteTitle = findViewById(R.id.edit_text_title);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_blue_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if ( getIntent().hasExtra(NOTE_ID) ){
            // set note Id only when we want to updated existing note
            setTitle("Edit Note");

            editTextNoteNote.setText(intent.getStringExtra(NOTE_NOTE));
            editTextNoteTitle.setText(intent.getStringExtra(NOTE_TITLE));

        }else {
            setTitle("Add Note");
        }
    }

    private void saveNote(){

        final String fun = "saveNote";
        Log.i(TAG, fun + " - started");
        String noteNote = editTextNoteNote.getText().toString();
        String noteTitle = editTextNoteTitle.getText().toString();

        if ( noteNote.trim().isEmpty() ) {
            Log.e(TAG, fun + " - text is missing");
            Toast.makeText(this, "Please insert text", Toast.LENGTH_SHORT).show();
            return;
        }

        newIntent = new Intent();
        packTermIntent();
        packCourseIntent();
        newIntent.putExtra(NOTE_NOTE, noteNote);
        newIntent.putExtra(NOTE_TITLE, noteTitle);
        if (sendEmail){
            newIntent.putExtra("SEND_EMAIL", "true");
        }
        if (sendSms){
            newIntent.putExtra("SEND_SMS", "true");
        }

        // -1 is for no id, when addNote
        int id = getIntent().getIntExtra(NOTE_ID, -1);
        if ( id != -1) {
            newIntent.putExtra(NOTE_ID,id);
        }

        setResult(RESULT_OK, newIntent);
        Log.i(TAG, fun + " - finishing");

        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_activity_menu, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data ) {
        // request code is position of note
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "started onActivityResult with requestCode " + requestCode + " resultCode " + resultCode);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG, "AddModifyNoteActivity.onOptionItemSelected called for id: " + item.getItemId());
        switch (item.getItemId()) {
            case R.id.save_note:
                Log.i(TAG, "AddModifyNoteActivity.onOptionItemSelected called for SAVE");
                saveNote();
                return true;
            case R.id.share_note_email:
                Log.i(TAG, "AddModifyNoteActivity.onOptionItemSelected called for Email");
                sendEmail = true;
                saveNote();
                return true;
            case R.id.share_note_sms:
                Log.i(TAG, "AddModifyNoteActivity.onOptionItemSelected called for SMS");
                sendSms = true;
                saveNote();
                return true;
            case android.R.id.home:
                Log.i(TAG, "AddModifyNoteActivity.onOptionItemSelected called for HOME");
                newIntent = new Intent(this, NoteActivity.class);
                newIntent.putExtra(NOTE_ID,getIntent().getIntExtra(NOTE_ID, -1));
                newIntent.putExtra(NOTE_NOTE,getIntent().getStringExtra(NOTE_NOTE));
                packTermIntent();
                packCourseIntent();
                setResult(RESULT_CANCELED, newIntent);
                finish();
                return true;
            default:
                Log.e(TAG, "AddModifyNoteActivity.onOptionItemSelected called for UNHANDLED");
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
}