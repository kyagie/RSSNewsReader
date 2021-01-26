package com.example.rssnewsreader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class LandingActivity extends AppCompatActivity {
    private TextInputLayout filledTextField;
    private Button containedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        containedButton = (Button) findViewById(R.id.containedButton);
        containedButton.setOnClickListener(this::mainactivity);

    }
    private boolean isValidate(){
        filledTextField = (TextInputLayout) findViewById(R.id.filledTextField);
        String URL = filledTextField.getEditText().toString();
//        return  Patterns.WEB_URL.matcher(URL).matches();
        return true;
    }

    public void submitURL(){
        if (isValidate()){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }else {
            Toast.makeText(getApplicationContext(),"Invalid URL", Toast.LENGTH_LONG).show();

        }


    }

    public void mainactivity(View view){
        if(view == containedButton){
            submitURL();
        }
    }


}