package com.example.f3app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    TextView showText;
    EditText enterName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showText = (TextView) findViewById(R.id.textView);
        assert showText != null;

        enterName = (EditText) findViewById(R.id.editText);
        assert enterName != null;

        Button objButton2 = (Button) findViewById(R.id.button2);
        assert objButton2 != null;
        objButton2.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v){
                        sayGoodBye(v);
                    }
                }

        );

    }

    public void sayGoodBye(View v) {
        if ( enterName.getText().toString() != null){
            showText.setText("Good Bye " + enterName.getText().toString());
        }
        System.out.println("Good BBye !!!!!!!!");

    }

    public void sayHello(View v) {
        if ( enterName.getText().toString() != null){
            showText.setText("Hello " + enterName.getText().toString());
        }
        System.out.println("Hello !!!!!!");
    }


}
