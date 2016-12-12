package com.example.ivandimitrov.multithreading;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import java.net.MalformedURLException;
import java.net.URL;

public class SecondActivity extends AppCompatActivity {
    private WebView myWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        String node = (String) getIntent().getSerializableExtra("Object");
        myWebView = (WebView) this.findViewById(R.id.webview);
        myWebView.getSettings().setJavaScriptEnabled(true);
        try {
            myWebView.loadUrl(String.valueOf(new URL(node)));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
