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
        Intent intent = getIntent();
        String url = intent.getStringExtra("URL");
        String urL = "https://news.yahoo.com/biden-did-not-fact-remove-071400961.html";
//
//        TextView textView = findViewById(R.id.textView);
//        textView.setText(url);


//Opens Link for URL passed in a webview
        webView = (WebView) findViewById(R.id.webView1);
        webView.getSettings().setJavaScriptEnabled(true);
//        webView.loadUrl(url);
        webView.loadUrl(urL);

    }
}