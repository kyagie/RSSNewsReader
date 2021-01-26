package com.example.rssnewsreader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

public class NewsDetailsActivity extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
//        Intent intent = getIntent();
//        String url = intent.getStringExtra("HEADLINE_URL");
//
//        TextView textView = findViewById(R.id.textView);
//        textView.setText(url);



        webView = (WebView) findViewById(R.id.webView1);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("https://news.yahoo.com/biden-did-not-fact-remove-071400961.html");

    }
}