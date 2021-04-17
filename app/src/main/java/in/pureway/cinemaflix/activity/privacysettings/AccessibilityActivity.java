package in.pureway.cinemaflix.activity.privacysettings;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.utils.App;
import in.pureway.cinemaflix.utils.AppConstants;
import in.pureway.cinemaflix.utils.CommonUtils;

public class AccessibilityActivity extends AppCompatActivity {


    private Switch swicthLivePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.changeLanguage(AccessibilityActivity.this);
        setContentView(R.layout.activity_accessibility);


        swicthLivePhoto=findViewById(R.id.swicthLivePhoto);


        swicthLivePhoto.setChecked(App.getSharedpref().getBoolean(AppConstants.ANIMATED_PHOTO));

        swicthLivePhoto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                App.getSharedpref().saveBoolean(AppConstants.ANIMATED_PHOTO,isChecked);
            }
        });


    }

    public void backPress(View view) {
        onBackPressed();
    }
}