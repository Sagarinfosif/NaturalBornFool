package in.pureway.cinemaflix.activity.privacysettings;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.activity.SplashActivity;
import in.pureway.cinemaflix.utils.App;
import in.pureway.cinemaflix.utils.AppConstants;

public class LanguageActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    RadioButton enRadioButton, hiRadioButton, mrRadioButton;
    private String language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        findIds();
        getLastSelection();
    }

    private void getLastSelection() {
        if (App.getSharedpref().getString(AppConstants.APP_LANGUAGE).equalsIgnoreCase(AppConstants.LANGUAGE_HI)) {
            hiRadioButton.setChecked(true);
        } else if (App.getSharedpref().getString(AppConstants.APP_LANGUAGE).equalsIgnoreCase(AppConstants.LANGUAGE_MR)) {
            mrRadioButton.setChecked(true);
        } else {
            enRadioButton.setChecked(true);
        }
        enRadioButton.setOnCheckedChangeListener(this);
        mrRadioButton.setOnCheckedChangeListener(this);
        hiRadioButton.setOnCheckedChangeListener(this);
    }

    private void findIds() {
        enRadioButton = findViewById(R.id.radioEn);
        hiRadioButton = findViewById(R.id.radioHindi);
        mrRadioButton = findViewById(R.id.radioMarathi);
    }

    public void backPress(View view) {
        onBackPressed();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.radioEn:
                if (isChecked) {
                    language = AppConstants.LANGUAGE_EN;
                    setLanguage(language);
                }
                break;
            case R.id.radioHindi:
                if (isChecked) {
                    language = AppConstants.LANGUAGE_HI;
                    setLanguage(language);
                }
                break;
            case R.id.radioMarathi:
                if (isChecked) {
                    language = AppConstants.LANGUAGE_MR;
                    setLanguage(language);
                }
                break;
        }

    }

    private void setLanguage(final String language) {
        final AlertDialog.Builder al = new AlertDialog.Builder(this, R.style.dialogStyle);
        al.setTitle("Change language").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                App.getSharedpref().saveString(AppConstants.APP_LANGUAGE, language);
                startActivity(new Intent(LanguageActivity.this, SplashActivity.class));
                finishAffinity();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setMessage("Are you sure to use this language ?").show();
    }

}