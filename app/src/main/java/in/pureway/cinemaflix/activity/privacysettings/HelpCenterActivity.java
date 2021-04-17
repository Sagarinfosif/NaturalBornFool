package in.pureway.cinemaflix.activity.privacysettings;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.utils.AppConstants;

public class HelpCenterActivity extends AppCompatActivity {
    private WebView webView_help_center;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_center);
        final ProgressDialog pd = ProgressDialog.show(this, "Help Center", "Loading...", true);
        webView_help_center = findViewById(R.id.webView_help_center);
        webView_help_center.getSettings().setJavaScriptEnabled(true);
        webView_help_center.getSettings().setBuiltInZoomControls(true);
        webView_help_center.getSettings().setDisplayZoomControls(false);
        webView_help_center.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                if (pd != null && pd.isShowing()) {
                    pd.dismiss();
                }
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                if (pd != null && pd.isShowing()) {
                    pd.dismiss();
                }
            }
        });
        webView_help_center.loadUrl(AppConstants.HELP_CENTER_URL);
        setTitle(getString(R.string.help_center));
    }
    
    public void backPress(View view) {
        onBackPressed();
    }
}