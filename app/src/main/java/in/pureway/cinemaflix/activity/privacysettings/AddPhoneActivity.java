package in.pureway.cinemaflix.activity.privacysettings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;

import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.models.ModelLoginRegister;
import in.pureway.cinemaflix.mvvm.LoginRegisterMvvm;
import in.pureway.cinemaflix.utils.App;
import in.pureway.cinemaflix.utils.AppConstants;
import in.pureway.cinemaflix.utils.CommonUtils;

import java.util.Map;

public class AddPhoneActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et_phone_number;
    private Button btn_next;
    private LoginRegisterMvvm loginRegisterMvvm;
//    private CountryCodePicker ccp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.changeLanguage(AddPhoneActivity.this);
        setContentView(R.layout.activity_add_phone);

        loginRegisterMvvm = ViewModelProviders.of(AddPhoneActivity.this).get(LoginRegisterMvvm.class);
        findIds();
        ModelLoginRegister.Details details = App.getSharedpref().getModel(AppConstants.REGISTER_LOGIN_DATA, ModelLoginRegister.class).getDetails();
        String phone = details.getPhone();
        et_phone_number.setText(phone);
        et_phone_number.requestFocus();
    }

    private void findIds() {
//        ccp = findViewById(R.id.ccp);
        btn_next = findViewById(R.id.btn_next);
        et_phone_number = findViewById(R.id.et_phone_number);
        btn_next.setOnClickListener(this);
    }

    public void backPress(View view) {
        onBackPressed();
    }

    private void sendOTP(final String phone) {
        loginRegisterMvvm.checkEmailPhone(AddPhoneActivity.this, "","" ,phone).observe(AddPhoneActivity.this, new Observer<Map>() {
            @Override
            public void onChanged(Map map) {
                if (map.get("success").equals("1")) {
                    String otp = map.get("otp").toString();
                    Toast.makeText(AddPhoneActivity.this, otp, Toast.LENGTH_SHORT).show();
                    App.getSingleton().setRegisterType("phone");
                    if (getIntent().getExtras() != null) {
                        if (getIntent().hasExtra(AppConstants.PHONE_ADD_TYPE)) {
                            startActivity(new Intent(AddPhoneActivity.this, PasswordCodeActivity.class)
                                    .putExtra(AppConstants.OTP_KEY, otp)
                                    .putExtra(AppConstants.EMAILPHONE, phone)
                                    .putExtra(AppConstants.ACTIVITY_TYPE, AppConstants.ACTIVITY_TYPE_MANAGE)
                                    .putExtra(AppConstants.PHONE_ADD_TYPE, AppConstants.PHONE_TYPE_CONTACTS));
                        }
                    } else {
                        startActivity(new Intent(AddPhoneActivity.this, PasswordCodeActivity.class)
                                .putExtra(AppConstants.OTP_KEY, otp)
                                .putExtra(AppConstants.EMAILPHONE, phone)
                                .putExtra(AppConstants.ACTIVITY_TYPE, AppConstants.ACTIVITY_TYPE_MANAGE));
                    }
                    finish();
                } else {
                    Toast.makeText(AddPhoneActivity.this, map.get("message").toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                sendPhoneOtp();
                break;
        }
    }

    private void sendPhoneOtp() {
        String phone = et_phone_number.getText().toString().trim();
        if (phone.isEmpty() || phone == null) {
            Toast.makeText(this, "Phone number cannot be null.", Toast.LENGTH_SHORT).show();
        } else {
            sendOTP(phone);
        }
    }
}