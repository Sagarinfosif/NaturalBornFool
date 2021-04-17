
package in.pureway.cinemaflix.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.activity.privacysettings.AccessibilityActivity;
import in.pureway.cinemaflix.activity.privacysettings.BalanceActivity;
import in.pureway.cinemaflix.activity.privacysettings.CodeActivity;
import in.pureway.cinemaflix.activity.privacysettings.DataSaverActivity;
import in.pureway.cinemaflix.activity.privacysettings.HelpCenterActivity;
import in.pureway.cinemaflix.activity.privacysettings.LanguageActivity;
import in.pureway.cinemaflix.activity.privacysettings.ManageMyAccountActivity;
import in.pureway.cinemaflix.activity.privacysettings.PhotofitBadgeActivity;
import in.pureway.cinemaflix.activity.privacysettings.PrivacyAndSafetyActivity;
import in.pureway.cinemaflix.activity.privacysettings.PushNotificationsActivity;
import in.pureway.cinemaflix.activity.privacysettings.ReportProblemActivity;
import in.pureway.cinemaflix.activity.privacysettings.VerifyAccountActivity;
import in.pureway.cinemaflix.javaClasses.Logout;
import in.pureway.cinemaflix.mvvm.ProfileMvvm;
import in.pureway.cinemaflix.utils.CommonUtils;

public class PrivacySettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private String getCoin = "", status = "0";
    private ProfileMvvm profileMvvm;
    private TextView tv_about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.changeLanguage(PrivacySettingsActivity.this);
        setContentView(R.layout.activity_privacy_settings);
        profileMvvm = ViewModelProviders.of(PrivacySettingsActivity.this).get(ProfileMvvm.class);

        getCoin = getIntent().getStringExtra("coin");
        status = getIntent().getStringExtra("status");

        findViewById(R.id.img_back_privacy_settings).setOnClickListener(this);
        findViewById(R.id.tv_logout).setOnClickListener(this);
        findViewById(R.id.tv_about).setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.img_back_privacy_settings:
                onBackPressed();
                break;

            case R.id.tv_logout:
                logoutApi();
                break;

                case R.id.tv_about:
                    startActivity(new Intent(this,AboutActivity.class));
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }

    public void manageaccount(View view) {
        startActivity(new Intent(PrivacySettingsActivity.this, ManageMyAccountActivity.class));
    }

    public void privacyandsafety(View view) {
        startActivity(new Intent(PrivacySettingsActivity.this, PrivacyAndSafetyActivity.class));
    }


    public void shareProfile(View view) {
        shareData();
    }

    private void shareData() {

        Intent shareIntent =   new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
//                shareIntent.putExtra(Intent.EXTRA_SUBJECT,"Insert Subject here");
        String shareMessage = "Hello there, \n" + "I'm invited you to join CINEMAFLIX.\n\n" + "Download CINEMAFLIX from PlayStore.";
        String msg = shareMessage + "\n https://play.google.com/store/apps/details?id=in.pureway.cinemaflix";
        shareIntent.putExtra(Intent.EXTRA_TEXT,msg);
        startActivity(Intent.createChooser(shareIntent, "Share via"));

//        Intent sendIntent = new Intent();
//        sendIntent.setAction(Intent.ACTION_SEND);
//        sendIntent.putExtra(Intent.EXTRA_TEXT,
//                "Hey check out my app at: https://play.google.com/store/apps/details?id="+getApplicationContext().getPackageName());
//        sendIntent.setType("text/plain");
//        startActivity(sendIntent);
    }

    public void code(View view) {
        startActivity(new Intent(PrivacySettingsActivity.this, CodeActivity.class));
    }

    public void pushNotifications(View view) {
        startActivity(new Intent(PrivacySettingsActivity.this, PushNotificationsActivity.class));
    }

    public void language(View view) {
        startActivity(new Intent(PrivacySettingsActivity.this, LanguageActivity.class));
    }


    public void accessibilty(View view) {
        startActivity(new Intent(PrivacySettingsActivity.this, AccessibilityActivity.class));
    }

    public void dataSaver(View view) {
        startActivity(new Intent(PrivacySettingsActivity.this, DataSaverActivity.class));
    }

    public void balance(View view) {
        startActivity(new Intent(PrivacySettingsActivity.this, BalanceActivity.class));
    }

    private void logoutApi() {
        Logout logout = new Logout(PrivacySettingsActivity.this);
        logout.logoutUser();
    }

    public void reportProblem(View view) {
        startActivity(new Intent(PrivacySettingsActivity.this, ReportProblemActivity.class));
    }

    public void helpCenter(View view) {
        startActivity(new Intent(PrivacySettingsActivity.this, HelpCenterActivity.class));
    }

    public void photofit_badge(View view) {
        startActivity(new Intent(PrivacySettingsActivity.this, PhotofitBadgeActivity.class));
    }

    public void verify(View view) {
        if (status.equals("0")) {
            startActivity(new Intent(PrivacySettingsActivity.this, VerifyAccountActivity.class));
        }
        else if (status.equals("1")) {
            verifyDialog();
        }
       else if (status.equals("2")) {
            verifyDialog();
        } else {
            startActivity(new Intent(PrivacySettingsActivity.this, VerifyAccountActivity.class));
        }
    }

    public void gift(View view) {
        startActivity(new Intent(PrivacySettingsActivity.this, WalletActivity.class).putExtra("coin", getCoin));
    }

    private void verifyDialog() {
        Dialog dialog = new Dialog(PrivacySettingsActivity.this);
        dialog.setContentView(R.layout.verify_dialog);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);

        TextView dialogText = dialog.findViewById(R.id.dialogText);
         if (status.equals("1")) {
            dialogText.setText("Your are already a verified user");
        }
          if (status.equals("2")) {
            dialogText.setText("Your profile is under review, Please wait for the admin confirmation");
        }

        Button btn_ok = dialog.findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
