package in.pureway.cinemaflix.activity.videoEditor.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.utils.CommonUtils;

public class PostPrivacySettingsActivity extends AppCompatActivity {
    private Switch switch_download, switch_duet_react, switch_comments;
    private String allowComments = "1", allowDuet = "1", allowDownload = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.changeLanguage(PostPrivacySettingsActivity.this);
        setContentView(R.layout.activity_post_privacy_settings);

        findIds();

        if (getIntent().getExtras() != null) {
            allowComments = getIntent().getExtras().getString(PostVideoActivity.COMMENTS);
            allowDownload = getIntent().getExtras().getString(PostVideoActivity.DOWNLOADS);
            allowDuet = getIntent().getExtras().getString(PostVideoActivity.DUETS);
            if (allowComments.equalsIgnoreCase("1")) {
                switch_comments.setChecked(true);
            }
            else {
                switch_comments.setChecked(false);
            }
            if (allowDownload.equalsIgnoreCase("1")) {
                switch_download.setChecked(true);
            }
            else {
                switch_download.setChecked(false);
            }
            if (allowDuet.equalsIgnoreCase("1")) {
                switch_duet_react.setChecked(true);
            }
            else {
                switch_duet_react.setChecked(false);
            }
        }
    }

    private void findIds() {
        switch_download = findViewById(R.id.switch_download);
        switch_duet_react = findViewById(R.id.switch_duet_react);
        switch_comments = findViewById(R.id.switch_comments);
    }


    public void back(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {

        if (switch_duet_react.isChecked() || switch_comments.isChecked() || switch_download.isChecked()) {
            getValues();
            Intent intent = new Intent();
            intent.putExtra(PostVideoActivity.DOWNLOADS, allowDownload);
            intent.putExtra(PostVideoActivity.COMMENTS, allowComments);
            intent.putExtra(PostVideoActivity.DUETS, allowDuet);
            setResult(PostVideoActivity.PRIVACY_RC, intent);
            finish();
        } else {
            Intent intent = new Intent();
            setResult(PostVideoActivity.PRIVACY_RC, intent);
            super.onBackPressed();
        }
    }

    private void getValues() {

        if (switch_download.isChecked()) {
            allowDownload = "1";
        } else {
            allowDownload = "0";
        }
        if (switch_comments.isChecked()) {
            allowComments = "1";

        } else {
            allowComments = "0";
        }
        if (switch_duet_react.isChecked()) {
            allowDuet = "1";
        } else {
            allowDuet = "0";
        }
    }
}
