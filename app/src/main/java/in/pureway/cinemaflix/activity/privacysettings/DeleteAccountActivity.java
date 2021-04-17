package in.pureway.cinemaflix.activity.privacysettings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.models.ModelLoginRegister;
import in.pureway.cinemaflix.mvvm.ProfileMvvm;
import in.pureway.cinemaflix.utils.App;
import in.pureway.cinemaflix.utils.AppConstants;
import in.pureway.cinemaflix.utils.CommonUtils;

import java.util.Map;

public class DeleteAccountActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_send_phone_code, tv_send_email_code;
    private String otp = "";
    private String type = "";
    private String email_or_phone = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.changeLanguage(DeleteAccountActivity.this);
        setContentView(R.layout.activity_delete_account);
        findIds();
    }

    private void findIds() {
        findViewById(R.id.btn_delete_account).setOnClickListener(this);
        tv_send_email_code = findViewById(R.id.tv_send_email_code);
        tv_send_phone_code = findViewById(R.id.tv_send_phone_code);

        if (App.getSharedpref().getModel(AppConstants.REGISTER_LOGIN_DATA, ModelLoginRegister.class).getDetails().getPhone().equalsIgnoreCase("")) {
            tv_send_email_code.setVisibility(View.VISIBLE);
            tv_send_phone_code.setVisibility(View.GONE);
            type = "email";
            email_or_phone = App.getSharedpref().getModel(AppConstants.REGISTER_LOGIN_DATA, ModelLoginRegister.class).getDetails().getEmail();
        } else {
            tv_send_email_code.setVisibility(View.GONE);
            tv_send_phone_code.setVisibility(View.VISIBLE);
            type = "phone";
            email_or_phone = App.getSharedpref().getModel(AppConstants.REGISTER_LOGIN_DATA, ModelLoginRegister.class).getDetails().getPhone();
        }
    }

    public void back(View view) {
        onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_delete_account:
                sendOTP();
                break;
        }
    }

    private void sendOTP() {
        ProfileMvvm profileMvvm = ViewModelProviders.of(DeleteAccountActivity.this).get(ProfileMvvm.class);
        profileMvvm.otpdeleteAccount(DeleteAccountActivity.this, CommonUtils.userId(DeleteAccountActivity.this)).observe(DeleteAccountActivity.this, new Observer<Map>() {
            @Override
            public void onChanged(Map map) {
                if (map.get("success").equals("1")) {
                    Toast.makeText(DeleteAccountActivity.this, map.get("message").toString(), Toast.LENGTH_SHORT).show();
                    otp = map.get("otp").toString();
                    startActivity(new Intent(DeleteAccountActivity.this, PasswordCodeActivity.class)
                            .putExtra(AppConstants.ACTIVITY_TYPE, AppConstants.ACTIVITY_TYPE_DELETE)
                            .putExtra(AppConstants.OTP_KEY, otp)
                            .putExtra(AppConstants.EMAILPHONE, email_or_phone));
                } else {
                    Toast.makeText(DeleteAccountActivity.this, map.get("message").toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
