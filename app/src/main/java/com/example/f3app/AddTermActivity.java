package com.example.f3app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class AddTermActivity extends AppCompatActivity {
    private EditText editTextAddTermTitle;
    private EditText editTextAddTermStartDate;
    private EditText editTextAddTermEndDate;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_term);

        editTextAddTermTitle = findViewById(R.id.add_term_title);
        editTextAddTermStartDate = findViewById(R.id.add_term_start_date);
        editTextAddTermEndDate = findViewById(R.id.add_term_end_date);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_blue_24);
        setTitle("Add Term");
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
        data.putExtra("termTitle", termTitle);
        data.putExtra("termStartDate", termStartDate);
        data.putExtra("termEndDate", termEndDate);

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

}