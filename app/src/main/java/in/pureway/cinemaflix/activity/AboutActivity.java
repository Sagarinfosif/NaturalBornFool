package in.pureway.cinemaflix.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import in.pureway.cinemaflix.R;

public class AboutActivity extends AppCompatActivity {
    private TextView tv_policyLink,tvTermLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);


        findViewById(R.id.tv_policyLink).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AboutActivity.this, WebLinkOpenActivity.class);
                intent.putExtra("key","1");
                intent.putExtra("title","CINEMAFLIX Privacy Policy");
                startActivity(intent);
            }
        });
        findViewById(R.id.tvTermLink).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent(AboutActivity.this, WebLinkOpenActivity.class);
                intent1.putExtra("key","0");
                intent1.putExtra("title","Terms and Conditions");
                startActivity(intent1);
            }
        });


    }

    public void onBack(View view) {
        onBackPressed();
    }
}