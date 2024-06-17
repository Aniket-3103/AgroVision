package com.mishraaniket.agrovision;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

public class WebActivity extends AppCompatActivity {

    // setting the TAG for debugging purposes
    private static String TAG="WebActivity";

    // declaring the webview
    private WebView myWebView;

    // declaring the url string variable
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        // assigning the views to id's
        myWebView = (WebView) findViewById(R.id.webview);

        Intent intent = getIntent();

        // checking if there is an intent
        if(intent!=null) {
            // retrieving the url in the intent
            url = intent.getStringExtra("url_key");

            // loading and displaying a
            // web page in the activity
            myWebView.loadUrl(url);
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
    }

}
