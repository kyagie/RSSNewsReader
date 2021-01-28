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
//This Method is called when The Get Feeds Button is pressed.
    //This method validates the URL typed in the text field

    public void submitURL(){
        filledTextField = (TextInputLayout) findViewById(R.id.filledTextField);
        String URL = filledTextField.getEditText().getText().toString();
        if (URL.matches("((http)[s]?(://).*)")){
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("url",URL);
            startActivity(intent);
        }else {
            Toast.makeText(getApplicationContext(),"Invalid URL", Toast.LENGTH_LONG).show();
        }


    }
    //This method opens the Mainactivity.
    public void mainactivity(View view){
        if(view == containedButton){
            submitURL();
        }
    }


}