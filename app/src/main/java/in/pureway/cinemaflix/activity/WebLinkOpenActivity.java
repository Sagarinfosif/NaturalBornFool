package in.pureway.cinemaflix.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import in.pureway.cinemaflix.R;

public class WebLinkOpenActivity extends AppCompatActivity {

    private WebView webview;
    private ProgressBar progressBar;
    private String status,title;
    private ImageView back;
    private TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_link_open);
        status=getIntent().getStringExtra("key");
        title=getIntent().getStringExtra("title");

        webview = findViewById(R.id.webView);
        progressBar = findViewById(R.id.progressBar);
        tv_title = findViewById(R.id.tv_title);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        webview.setWebViewClient(new WebViewClient());

        if (status != null) {
            if (status.equalsIgnoreCase("0")){
//                webview.loadUrl("https://cinemaflix.in/terms.php");
                Toast.makeText(this, "terms", Toast.LENGTH_SHORT).show();
            }else {
//                webview.loadUrl("https://cinemaflix.in/privacy-policy.php");
                Toast.makeText(this, "privacy policy", Toast.LENGTH_SHORT).show();
            }
        }

        tv_title.setText(title);


    }
    public class WebViewClient extends android.webkit.WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
        }
    }
}