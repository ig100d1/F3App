package com.example.f3app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class TermActivity extends AppCompatActivity {

    TextView textTermName, textTermDescription;
    String termName;
    private static final String TAG = "TermActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term);
        textTermName = findViewById(R.id.textView3);
        Log.i(TAG,"retrieved field textTermName");
        getData();
        setData();
    }

    private void getData(){
        if (getIntent().hasExtra("data") ) {
            termName = getIntent().getStringExtra("data");

            Log.i(TAG, " IGOR_DEBUG - got termName: " + termName);
        }
        else{
            Toast.makeText(this, "no data", Toast.LENGTH_SHORT).show();
        }
    }

    private void setData(){
        Log.i(TAG, "IGOR_DEBUG - called with termName: " + termName);
        textTermName.setText(termName);
    }
}
