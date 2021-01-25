package com.example.rssnewsreader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class NewsDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
        Intent intent = getIntent();
        String url = intent.getStringExtra("HEADLINE_URL");

        TextView textView = findViewById(R.id.textView);
        textView.setText(url);


    }
}