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

public class NameActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et_name;
    String name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.changeLanguage(NameActivity.this);
        setContentView(R.layout.activity_name);

        findIds();
        checkName();
    }

    private void checkName() {
        String name=App.getSharedpref().getModel(AppConstants.REGISTER_LOGIN_DATA,ModelLoginRegister.class).getDetails().getName();
        et_name.setText(name);
    }

    private void findIds() {
        et_name = findViewById(R.id.et_name);
        findViewById(R.id.tv_save_name).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_save_name:
                name = et_name.getText().toString().trim();
                if (name.equalsIgnoreCase("") || name.isEmpty()) {
                    et_name.setError("Name cannot be empty");
                } else {
                    UpdateProfileApi updateProfileApi = new UpdateProfileApi(NameActivity.this);
                    updateProfileApi.hitUpdateProfile(name, "", "","");
                    finish();
                }
                break;
        }
    }

    public void backPress(View view) {
        onBackPressed();
    }
}
