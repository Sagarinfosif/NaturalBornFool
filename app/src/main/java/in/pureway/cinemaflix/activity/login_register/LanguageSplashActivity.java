package in.pureway.cinemaflix.activity.login_register;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.activity.HomeActivity;
import in.pureway.cinemaflix.utils.App;
import in.pureway.cinemaflix.utils.AppConstants;

public class LanguageSplashActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    RadioButton enRadioButton, hiRadioButton, mrRadioButton;
    private String language, selectedLan;


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
    }

    private void findIds() {

        findViewById(R.id.iv_backArrow).setVisibility(View.GONE);
        findViewById(R.id.btn_next).setVisibility(View.VISIBLE);

        enRadioButton = findViewById(R.id.radioEn);
        enRadioButton.setOnCheckedChangeListener(this);
        hiRadioButton = findViewById(R.id.radioHindi);
        hiRadioButton.setOnCheckedChangeListener(this);
        mrRadioButton = findViewById(R.id.radioMarathi);
        mrRadioButton.setOnCheckedChangeListener(this);
        findViewById(R.id.btn_next).setOnClickListener(this);
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
                    selectedLan = enRadioButton.getText().toString();
                }
                break;
            case R.id.radioHindi:
                if (isChecked) {
                    language = AppConstants.LANGUAGE_HI;
                    selectedLan = hiRadioButton.getText().toString();
                }
                break;
            case R.id.radioMarathi:
                if (isChecked) {
                    language = AppConstants.LANGUAGE_MR;
                    selectedLan = mrRadioButton.getText().toString();
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
                App.getSharedpref().saveString(AppConstants.SPLASHLANGUAGESKIP, AppConstants.LANGUAGEONCECHECK);
                startActivity(new Intent(LanguageSplashActivity.this, HomeActivity.class));
                finishAffinity();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setMessage("Are you sure to use this language ?").show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                App.getSharedpref().saveBoolean(AppConstants.SELECTED_LANGUAGE, true);
                setLanguage(language);
                break;
        }
    }
}