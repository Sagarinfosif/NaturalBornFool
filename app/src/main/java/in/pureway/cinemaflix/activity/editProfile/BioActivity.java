package in.pureway.cinemaflix.activity.editProfile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.javaClasses.UpdateProfileApi;
import in.pureway.cinemaflix.models.ModelLoginRegister;
import in.pureway.cinemaflix.utils.App;
import in.pureway.cinemaflix.utils.AppConstants;
import in.pureway.cinemaflix.utils.CommonUtils;

public class BioActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et_bio;
    String bio = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.changeLanguage(BioActivity.this);
        setContentView(R.layout.activity_bio);

        findIds();
        bio();
    }

    private void bio() {
        String bio = App.getSharedpref().getModel(AppConstants.REGISTER_LOGIN_DATA, ModelLoginRegister.class).getDetails().getBio();
        et_bio.setText(bio);
    }

    private void findIds() {
        findViewById(R.id.tv_save_bio).setOnClickListener(this);
        et_bio = findViewById(R.id.et_bio);
    }

    public void backPress(View view) {
        onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_save_bio:
                bio = et_bio.getText().toString();
                if (bio.equalsIgnoreCase("") || bio.isEmpty()) {
                    et_bio.setError(getString(R.string.bio_cannot_be_empty));
                } else {
                    UpdateProfileApi updateProfileApi = new UpdateProfileApi(BioActivity.this);
                    updateProfileApi.hitUpdateProfile("", "", bio,"");
                    onBackPressed();
                }
                break;
        }
    }
}
