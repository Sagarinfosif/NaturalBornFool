package in.pureway.cinemaflix.activity.privacysettings;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.utils.CommonUtils;

public class DataSaverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.changeLanguage(DataSaverActivity.this);
        setContentView(R.layout.activity_data_saver);
    }

    public void backPress(View view) {
        onBackPressed();
    }
}
