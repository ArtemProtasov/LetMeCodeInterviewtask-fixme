package ru.protasov_dev.letmecodeinterviewtask.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import ru.protasov_dev.letmecodeinterviewtask.R;
import ru.protasov_dev.letmecodeinterviewtask.WebView.WebViewClient;

public class ReviewPage extends AppCompatActivity{
    private String URL;
    private WebView webView;
    private Activity activity = this;
    private String articleTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_page);

        Intent intent = getIntent();
        URL = intent.getStringExtra("URL");
        articleTitle = intent.getStringExtra("ARTICLE_TITLE");

        //Устанавливаем наш кастомный тулбар
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.loading));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        webView = findViewById(R.id.webView);
        // включаем поддержку JavaScript
        webView.getSettings().setJavaScriptEnabled(true);
        // устанавливаем WebView клиент
        webView.setWebViewClient(new WebViewClient());
        // указываем страницу загрузки
        webView.loadUrl(URL);
        webView.setWebChromeClient(new WebChromeClient(){
            public void onProgressChanged(WebView view, int progress) {
                activity.setProgress(progress * 100);
                if(progress == 100)
                    activity.setTitle(articleTitle.replace("Read the New York Times ", ""));
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
