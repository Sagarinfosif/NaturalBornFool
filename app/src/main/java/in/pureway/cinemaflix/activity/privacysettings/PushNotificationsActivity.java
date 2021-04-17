package in.pureway.cinemaflix.activity.privacysettings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.models.PushNotificationSettingsPojo;
import in.pureway.cinemaflix.mvvm.SettingsMvvm;
import in.pureway.cinemaflix.utils.CommonUtils;

public class PushNotificationsActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private Switch likeASwitch, commenetASwitch, newFollowerASwitch, directASwitch, vidFromOtherASwitch;
    private String likeStr = "1", commentStr = "1", newFoloowerStr = "1", directMsgStr = "1", videoFromOtherFolStr = "1";
    private SettingsMvvm settingsMvvm;
    private Activity activity = PushNotificationsActivity.this;
    private Button update;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.changeLanguage(PushNotificationsActivity.this);
        setContentView(R.layout.activity_push_notifications);
        settingsMvvm = ViewModelProviders.of(PushNotificationsActivity.this).get(SettingsMvvm.class);
        findIds();
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        getSettings();


    }

    private void getSettings() {

//        progressDialog.show();

        settingsMvvm.getPushSettingsResults(activity, CommonUtils.userId(activity)).observe(PushNotificationsActivity.this, new Observer<PushNotificationSettingsPojo>() {
            @Override
            public void onChanged(PushNotificationSettingsPojo pushNotificationSettingsPojo) {
                if (pushNotificationSettingsPojo.getSuccess().equalsIgnoreCase("1")) {
//                    progressDialog.dismiss();
                    if (pushNotificationSettingsPojo.getDetails().getLikeNotifaction().equalsIgnoreCase("1")) {
                        likeASwitch.setChecked(true);
                        likeStr = "1";

                    } else {
                        likeStr = "2";
                    }
                    if (pushNotificationSettingsPojo.getDetails().getCommentNotification().equalsIgnoreCase("1")) {
                        commenetASwitch.setChecked(true);
                        commentStr = "1";
                    } else {
                        commentStr = "2";
                    }
                    if (pushNotificationSettingsPojo.getDetails().getFollowersNotification().equalsIgnoreCase("1")) {
                        newFollowerASwitch.setChecked(true);
                        newFoloowerStr = "1";
                    } else {
                        newFoloowerStr = "2";
                    }
                    if (pushNotificationSettingsPojo.getDetails().getMessageNotification().equalsIgnoreCase("1")) {
                        directASwitch.setChecked(true);
                        directMsgStr = "1";
                    } else {
                        directMsgStr = "2";
                    }
                    if (pushNotificationSettingsPojo.getDetails().getVideoNotification().equalsIgnoreCase("1")) {
                        vidFromOtherASwitch.setChecked(true);
                        videoFromOtherFolStr = "1";
                    } else {
                        videoFromOtherFolStr = "2";
                    }

                    switchListeners();

                }else {
//                    progressDialog.dismiss();
                    Toast.makeText(activity, ""+pushNotificationSettingsPojo.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void switchListeners() {
        likeASwitch.setOnCheckedChangeListener(this);
        commenetASwitch.setOnCheckedChangeListener(this);
        newFollowerASwitch.setOnCheckedChangeListener(this);
        directASwitch.setOnCheckedChangeListener(this);
        vidFromOtherASwitch.setOnCheckedChangeListener(this);
    }

    private void findIds() {
        likeASwitch = findViewById(R.id.switchLikes);
        commenetASwitch = findViewById(R.id.switchComments);
        newFollowerASwitch = findViewById(R.id.switchNewFollowers);
        directASwitch = findViewById(R.id.switchDirectMsg);
        vidFromOtherASwitch = findViewById(R.id.switchVideoFromOtherFollowAcnt);
        findViewById(R.id.update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateNotiSettings();
            }
        });


    }

    public void backPress(View view) {
        onBackPressed();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.switchLikes:
                if (isChecked) {
                    likeStr = "1";

                } else {
                    likeStr = "2";
                }
                break;
            case R.id.switchComments:
                if (isChecked) {
                    commentStr = "1";
                } else {
                    commentStr = "2";
                }
                break;
            case R.id.switchNewFollowers:
                if (isChecked) {
                    newFoloowerStr = "1";
                } else {
                    newFoloowerStr = "2";
                }
                break;
            case R.id.switchDirectMsg:
                if (isChecked) {
                    directMsgStr = "1";
                } else {
                    directMsgStr = "2";
                }
                break;
            case R.id.switchVideoFromOtherFollowAcnt:
                if (isChecked) {
                    videoFromOtherFolStr = "1";
                } else {
                    videoFromOtherFolStr = "2";
                }
                break;


        }
//        updateNotiSettings();
    }

    private void updateNotiSettings() {
        progressDialog.show();
        settingsMvvm.updatePushSettingsResults(activity, CommonUtils.userId(activity), likeStr, commentStr, newFoloowerStr, directMsgStr, videoFromOtherFolStr).observe(PushNotificationsActivity.this, new Observer<PushNotificationSettingsPojo>() {
            @Override
            public void onChanged(PushNotificationSettingsPojo pushNotificationSettingsPojo) {
                if(pushNotificationSettingsPojo.getSuccess().equalsIgnoreCase("1")) {
                    progressDialog.dismiss();
                    Toast.makeText(activity, ""+pushNotificationSettingsPojo.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(activity, pushNotificationSettingsPojo.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}