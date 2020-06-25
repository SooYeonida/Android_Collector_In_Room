package com.ajou.capstone_design_freitag;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class WebViewActivity extends AppCompatActivity {
    private WebView webView;
    private final String callbackHost = "101.101.208.224";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        webView = findViewById(R.id.webView);

        WebViewClient webViewClient = new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                try {
                    URI uri = new URI(url);
                    if(uri.getHost().equals(callbackHost)) {
                        if(uri.getPath().equals("/registerOpenBankingSuccess.html")) {
                            setResult(RESULT_OK);
                        } else {
                            setResult(RESULT_CANCELED);
                        }
                        finish();
                    }
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        };
        webView.setWebViewClient(webViewClient);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setDefaultTextEncodingName("UTF-8");

        Intent intent = getIntent();
        String url = intent.getStringExtra("URL");
        webView.loadUrl(url);
    }
}
