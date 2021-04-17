package in.pureway.cinemaflix.activity.privacysettings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.models.ModelPrivacySettings;
import in.pureway.cinemaflix.mvvm.ProfileMvvm;
import in.pureway.cinemaflix.mvvm.SettingsMvvm;
import in.pureway.cinemaflix.utils.CommonUtils;

import java.util.Map;

public class PrivacyAndSafetyActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    private Switch switch_private, switch_show_profile_video, switch_show_my_following_users;
    private ProfileMvvm profileMvvm;
    private SettingsMvvm settingsMvvm;
    String userId = CommonUtils.userId(PrivacyAndSafetyActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.changeLanguage(PrivacyAndSafetyActivity.this);
        setContentView(R.layout.activity_privacy_and_safety);

        settingsMvvm = ViewModelProviders.of(PrivacyAndSafetyActivity.this).get(SettingsMvvm.class);
        profileMvvm = ViewModelProviders.of(PrivacyAndSafetyActivity.this).get(ProfileMvvm.class);
        findIds();
        getSettings();

    }

    private void getSettings() {
        settingsMvvm.privacySettings(PrivacyAndSafetyActivity.this, userId).observe(PrivacyAndSafetyActivity.this, new Observer<ModelPrivacySettings>() {
            @Override
            public void onChanged(ModelPrivacySettings modelPrivacySettings) {
                if (modelPrivacySettings.getSuccess().equalsIgnoreCase("1")) {
                    if (modelPrivacySettings.getDetails().getFollowingViewStatus()) {
                        switch_show_my_following_users.setChecked(true);
                    } else {
                        switch_show_my_following_users.setChecked(false);
                    }

                    if (modelPrivacySettings.getDetails().getPrivateAccount()) {
                        switch_private.setChecked(true);
                    } else {
                        switch_private.setChecked(false);
                    }

                    if (modelPrivacySettings.getDetails().getProfilePhotoStatus()) {
                        switch_show_profile_video.setChecked(true);
                    } else {
                        switch_show_profile_video.setChecked(false);
                    }
                    switchListeners();
                } else {

                }
            }
        });
    }

    private void switchListeners() {
        switch_private.setOnCheckedChangeListener(this);
//        switch_liked_videos.setOnCheckedChangeListener(this);
        switch_show_my_following_users.setOnCheckedChangeListener(this);
        switch_show_profile_video.setOnCheckedChangeListener(this);
    }

    private void findIds() {
        findViewById(R.id.tv_blocked_contacts).setOnClickListener(this);
        switch_show_my_following_users = findViewById(R.id.switch_show_my_following_users);
        switch_show_profile_video = findViewById(R.id.switch_show_profile_video);
        switch_private = findViewById(R.id.switch_private);
        findViewById(R.id.tv_head_block).setOnClickListener(this);
        findViewById(R.id.switch_private).setOnClickListener(this);
    }

    public void back(View view) {
        onBackPressed();
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {

            case R.id.switch_private:
                privateStatusUpdate();
                break;

//            case R.id.switch_liked_videos:
//                seeLikedVideosStatus();
//                break;

            case R.id.switch_show_profile_video:
                showProfileVideo();
                break;

            case R.id.switch_show_my_following_users:
                showMyFollowing();
                break;

        }
    }

    private void showProfileVideo() {
        settingsMvvm.showProfileVideo(PrivacyAndSafetyActivity.this, userId).observe(PrivacyAndSafetyActivity.this, new Observer<Map>() {
            @Override
            public void onChanged(Map map) {
                if (map.get("success").toString().equalsIgnoreCase("1")) {
                    Toast.makeText(PrivacyAndSafetyActivity.this, map.get("message").toString(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PrivacyAndSafetyActivity.this, map.get("message").toString(), Toast.LENGTH_SHORT).show();
                }
                Log.i("showFollowing", map.get("message").toString());
            }
        });
    }

    private void showMyFollowing() {
        settingsMvvm.showFollowing(PrivacyAndSafetyActivity.this, userId).observe(PrivacyAndSafetyActivity.this, new Observer<Map>() {
            @Override
            public void onChanged(Map map) {
                if (map.get("success").toString().equalsIgnoreCase("1")) {
                    Toast.makeText(PrivacyAndSafetyActivity.this, map.get("message").toString(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PrivacyAndSafetyActivity.this, map.get("message").toString(), Toast.LENGTH_SHORT).show();
                }
                Log.i("showFollowing", map.get("message").toString());
            }
        });
    }

    private void seeLikedVideosStatus() {
        SettingsMvvm settingsMvvm = ViewModelProviders.of(PrivacyAndSafetyActivity.this).get(SettingsMvvm.class);
        settingsMvvm.showLikedvideos(PrivacyAndSafetyActivity.this, CommonUtils.userId(PrivacyAndSafetyActivity.this)).observe(PrivacyAndSafetyActivity.this, new Observer<Map>() {
            @Override
            public void onChanged(Map map) {
                if (map.get("status").equals(true)) {
                    Toast.makeText(PrivacyAndSafetyActivity.this, map.get("status").toString(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PrivacyAndSafetyActivity.this, map.get("status").toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void privateStatusUpdate() {
        profileMvvm.privateAccount(PrivacyAndSafetyActivity.this, CommonUtils.userId(PrivacyAndSafetyActivity.this)).observe(PrivacyAndSafetyActivity.this, new Observer<Map>() {
            @Override
            public void onChanged(Map map) {
                if (map.get("status").equals(true)) {
                    Toast.makeText(PrivacyAndSafetyActivity.this, map.get("status").toString(), Toast.LENGTH_SHORT).show();
                } else {
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_head_block:
            case R.id.tv_blocked_contacts:
                startActivity(new Intent(PrivacyAndSafetyActivity.this, BlockedUserActivity.class));
                break;
        }
    }
}
