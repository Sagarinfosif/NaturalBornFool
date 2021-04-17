package in.pureway.cinemaflix.activity.privacysettings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;

import java.util.Map;

import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.activity.HomeActivity;
import in.pureway.cinemaflix.activity.editProfile.NameActivity;
import in.pureway.cinemaflix.activity.login_register.VerifyOTPActivity;
import in.pureway.cinemaflix.javaClasses.UpdateProfileApi;
import in.pureway.cinemaflix.models.ModelLoginPhone;
import in.pureway.cinemaflix.models.ModelLoginRegister;
import in.pureway.cinemaflix.mvvm.LoginRegisterMvvm;
import in.pureway.cinemaflix.utils.App;
import in.pureway.cinemaflix.utils.AppConstants;

public class UpdatePhoneNumberActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener {

    private Button btn_send_otp;
    private EditText et_phone;
    private String phone, UpdateNumberStatus = "0";
    private LoginRegisterMvvm loginRegisterMvvm;
    private TextView tv_TextNumber;
    private CountryCodePicker ccp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_phone_number);


        loginRegisterMvvm = ViewModelProviders.of(UpdatePhoneNumberActivity.this).get(LoginRegisterMvvm.class);

        ccp = findViewById(R.id.ccp);
        tv_TextNumber = findViewById(R.id.tv_TextNumber);
        btn_send_otp = findViewById(R.id.btn_send_otp);
        et_phone = findViewById(R.id.et_phone);
        btn_send_otp.setOnClickListener(this);
        et_phone.addTextChangedListener(this);

//        if (App.getSingleton().getUpdateNumberStatus() != null && App.getSingleton().getUpdateNumberStatus().equalsIgnoreCase("1")) {
//            if (UpdateNumberStatus.equalsIgnoreCase("1")) {
//                btn_send_otp.setText("Save");
//                tv_TextNumber.setText("Enter your new number");
//                ccp.setVisibility(View.VISIBLE);
//
//            }
//        }


//        String phone = App.getSharedpref().getModel(AppConstants.REGISTER_LOGIN_DATA, ModelLoginRegister.class).getDetails().getPhone();
//
//        if (phone != null) {
//            et_phone.setText(phone);
//
//        }


    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        phone = et_phone.getText().toString();

        if (!phone.isEmpty()) {
            if (phone.length() >= 10) {
                btn_send_otp.setBackgroundResource(R.drawable.btn_bg);
            } else {
                btn_send_otp.setBackgroundResource(R.drawable.dark_bg);

            }
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        phone = et_phone.getText().toString();

        if (!phone.isEmpty()) {
            if (phone.length() >=10) {
                btn_send_otp.setBackgroundResource(R.drawable.btn_bg);
            } else {
                btn_send_otp.setBackgroundResource(R.drawable.dark_bg);

            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        phone = et_phone.getText().toString();

        if (!phone.isEmpty()) {
            if (phone.length() >= 10) {
                btn_send_otp.setBackgroundResource(R.drawable.btn_bg);
            } else {
                btn_send_otp.setBackgroundResource(R.drawable.dark_bg);

            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send_otp:
                phone = et_phone.getText().toString();

//                if (UpdateNumberStatus.equalsIgnoreCase("1")) {
//
//
//                    if (phone.isEmpty()) {
//                        et_phone.setError("Please enter Phone number");
//                    } else {
//
//
//                        phone = ccp.getDefaultCountryCodeWithPlus() + phone;
//                        UpdateProfileApi updateProfileApi = new UpdateProfileApi(UpdatePhoneNumberActivity.this);
//                        updateProfileApi.hitUpdateProfile("", "", "", phone);
//                    }
//                } else {
                if (phone.isEmpty()) {
                    et_phone.setError("Please enter Phone number");
                } else {

//                    phone = ccp.getDefaultCountryCodeWithPlus() + phone;

                    loginRegisterMvvm.checkEmailPhone(UpdatePhoneNumberActivity.this, "", "",ccp.getSelectedCountryCodeWithPlus()+phone).observe(UpdatePhoneNumberActivity.this, new Observer<Map>() {
                        @Override
                        public void onChanged(Map map) {
                            if (map.get("success").equals("1")) {
                                String otp = map.get("otp").toString();
                                Toast.makeText(UpdatePhoneNumberActivity.this, otp, Toast.LENGTH_SHORT).show();
                                App.getSingleton().setRegisterType("phone");
                                phone = ccp.getDefaultCountryCodeWithPlus() + phone;
                                App.getSingleton().setUpdateNumber(phone);
                                App.getSingleton().setUpdateNumberStatus(phone);
                                finish();

                                startActivity(new Intent(UpdatePhoneNumberActivity.this, PasswordCodeActivity.class).putExtra(AppConstants.OTP_KEY, otp)
                                        .putExtra(AppConstants.EMAILPHONE, "phone")
                                        .putExtra(AppConstants.ACTIVITY_TYPE, AppConstants.UPDATEPHONENUMBER));


                            } else {
                                Toast.makeText(UpdatePhoneNumberActivity.this, map.get("message").toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


//                    loginRegisterMvvm.resendOTP(this, phone).observe(this, new Observer<ModelLoginPhone>() {
//                        @Override
//                        public void onChanged(ModelLoginPhone modelLoginPhone) {
//                            Toast.makeText(UpdatePhoneNumberActivity.this, modelLoginPhone.getMessage(), Toast.LENGTH_SHORT).show();
//                            if (modelLoginPhone.getSuccess().equalsIgnoreCase("1")) {
//
////                                    UpdateProfileApi updateProfileApi = new UpdateProfileApi(UpdatePhoneNumberActivity.this);
////                        updateProfileApi.hitUpdateProfile("", "", "", phone);
//
//                                App.getSingleton().setUpdateNumberStatus("2");
//
//                                App.getSingleton().setUpdateNumber(phone);
////                                    Toast.makeText(UpdatePhoneNumberActivity.this, modelLoginPhone.getDetails().getLoginOtp(), Toast.LENGTH_SHORT).show();
//                                Intent intent = new Intent(UpdatePhoneNumberActivity.this, HomeActivity.class).putExtra(AppConstants.OTP_KEY, modelLoginPhone.getDetails().getLoginOtp()).putExtra("UpdateProfileStatus", "1");
//                                startActivity(intent);
//
//                            } else {
//
//                                Toast.makeText(UpdatePhoneNumberActivity.this, modelLoginPhone.getMessage().toString(), Toast.LENGTH_SHORT).show();
//
//                            }
//                        }
//                    });
//                    }
                }


                break;
        }
    }

    public void backPress(View view) {
        onBackPressed();
    }
}