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
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import static android.content.Intent.EXTRA_EMAIL;
import static android.content.Intent.EXTRA_SUBJECT;
import static android.content.Intent.EXTRA_TEXT;
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

public class NoteActivity extends AppCompatActivity {

    private NoteViewModel noteViewModel;
    RecyclerView mRecyclerView;
    private static final String TAG = "IgB:NoteActivity";
    private int term_id;
    private int course_id;
    private String term_title;
    private String term_start_date;
    private String term_end_date;
    private static final int ADD_NOTE_REQUEST = 1;
    private static final int EDIT_NOTE_REQUEST = 2;
    private Intent newIntent;

    String notes[];

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

        setTitle("Notes");

        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        setContentView(R.layout.activity_note);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);

        Log.i(TAG, " created mRecyclerView");

        NoteAdapter noteAdapter = new NoteAdapter();
        Log.i(TAG, "created noteAdapter");
        mRecyclerView.setAdapter(noteAdapter);

        noteViewModel.getAllNotes(course_id).observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                // update recyclerView
                Log.i(TAG, "running noteViewModel.getAllNotes.observer onchanged");
                noteAdapter.setNotes(notes);
            }
        });

        /*
            Delete Note
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
                noteViewModel.delete(noteAdapter.getNoteAt(viewHolder.getAdapterPosition()));
                Toast.makeText(NoteActivity.this, "Note Deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(mRecyclerView);

        Log.i(TAG, "onCreate onSwiped listeners up");

        //magic
        noteAdapter.setOnNoteClickListener(new NoteAdapter.OnNoteClickListener() {
            @Override
            public void onItemClick(Note note) {
                Log.i(TAG, "setOnNoteClickListener method started ");
                newIntent = new Intent(NoteActivity.this, AddModifyNoteActivity.class);
                newIntent.putExtra(AddModifyNoteActivity.NOTE_ID, note.getId());
                newIntent.putExtra(AddModifyNoteActivity.NOTE_NOTE, note.getNote());
                newIntent.putExtra(AddModifyNoteActivity.NOTE_TITLE, note.getTitle());
                packTermIntent();
                packCourseIntent();
                startActivityForResult(newIntent, EDIT_NOTE_REQUEST);
            }
        });

        Log.i(TAG, "onCreate onClick listeners up");

        FloatingActionButton buttonAddNote = findViewById(R.id.button_add_note);
        if (buttonAddNote== null){
            Log.e(TAG, "onCreate failed to init floatingActionButton");
            newIntent = new Intent(this, AddEditTermActivity.class);
            packTermIntent();
            packCourseIntent();
            setResult(CREATE_FAILED, newIntent);
            finish();
            return;
        }
        Log.d(TAG, "addNote listener started");

        buttonAddNote.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Log.i(TAG, "started buttonAddNote onClick method");
                newIntent= new Intent(NoteActivity.this, AddModifyNoteActivity.class );
                packTermIntent();
                packCourseIntent();
                startActivityForResult(newIntent,ADD_NOTE_REQUEST);
            }
        });

        Log.i(TAG, "created noteViewModel");
    }

    /*
    Back arrow returns to empty term, because not content.
    starting AddEditTermActivity with Term data that NoteActivity started
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
        if( requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK) {
            Log.i(TAG, "onActivityResult has new note data to add");

            String noteNote = data.getStringExtra(AddModifyNoteActivity.NOTE_NOTE);
            String noteTitle = data.getStringExtra(AddModifyNoteActivity.NOTE_TITLE);

            int noteCourseId = data.getIntExtra(COURSE_ID, -1 );

            Note c = new Note(noteCourseId, noteTitle, noteNote);

            noteViewModel.insert(c);
            Toast.makeText(this, "Note Saved", Toast.LENGTH_SHORT).show();

            // implementing sharing with email and sms
            // instead of menu option need to use checkbox
            if (data.hasExtra("SEND_EMAIL")) {
                sendEmail(noteTitle, noteNote);
            }
            if (data.hasExtra("SEND_SMS")) {
                sendSms(noteNote);
            }

        } else if( requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK) {
            Log.i(TAG, "onActivityResult has modified data to save");
            int noteId = data.getIntExtra(AddModifyNoteActivity.NOTE_ID, -1);
            if (noteId == -1) {
                Toast.makeText(this, "Couldn't safe note data", Toast.LENGTH_SHORT).show();
                return;
            }

            String noteNote = data.getStringExtra(AddModifyNoteActivity.NOTE_NOTE);
            String noteTitle = data.getStringExtra(AddModifyNoteActivity.NOTE_TITLE);
            int noteCourseId = data.getIntExtra(COURSE_ID, -1 );
            Note n = new Note(noteCourseId, noteTitle, noteNote);

            n.setId(noteId);
            noteViewModel.update(n);
            Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT).show();

            // implementing sharing with email and sms
            // instead of menu option need to use checkbox
            if (data.hasExtra("SEND_EMAIL")) {
                sendEmail(noteTitle, noteNote);
            }
            if (data.hasExtra("SEND_SMS")) {
                sendSms(noteNote);
            }


        }else{
            Log.e(TAG, "onActivityResult has no data to save");
            Toast.makeText(this, "Note Not Saved", Toast.LENGTH_SHORT).show();
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

    private void sendEmail(String title, String note){
        final String fun = "sendEmail";
        Log.i(TAG, fun + " started");
        Intent sendEmail = new Intent(Intent.ACTION_SENDTO);
        sendEmail.setType("text/plain");
        sendEmail.putExtra(EXTRA_EMAIL, new String[]{getIntent().getStringExtra(COURSE_EMAIL)});
        sendEmail.putExtra(EXTRA_SUBJECT, title);
        sendEmail.putExtra(EXTRA_TEXT, note);
        //sendEmail.setData(Uri.parse("mailto:"));
        startActivity(Intent.createChooser(sendEmail, "Send Email"));
    }

    private void sendSms(String note){
        final String fun = "sendSms";
        Log.i(TAG, fun + " started");
        SmsManager smgr = SmsManager.getDefault();
        smgr.sendTextMessage(getIntent().getStringExtra(COURSE_PHONE),null,note,null,null);
        Log.i(TAG, fun + " finished");
    }
}