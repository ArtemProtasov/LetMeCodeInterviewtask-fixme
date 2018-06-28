package ru.protasov_dev.letmecodeinterviewtask.WebView;

import android.annotation.TargetApi;
import android.os.Build;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;

public class WebViewClient extends android.webkit.WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url){
        view.loadUrl(url);
        return true;
    }

    @TargetApi(Build.VERSION_CODES.N) @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        view.loadUrl(request.getUrl().toString());
        return true;
    }

}
