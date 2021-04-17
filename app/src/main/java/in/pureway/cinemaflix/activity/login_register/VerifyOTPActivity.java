package in.pureway.cinemaflix.activity.login_register;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.activity.HomeActivity;
import in.pureway.cinemaflix.activity.privacysettings.UpdatePhoneNumberActivity;
import in.pureway.cinemaflix.models.ModelLoginPhone;
import in.pureway.cinemaflix.models.ModelLoginRegister;
import in.pureway.cinemaflix.mvvm.LoginRegisterMvvm;
import in.pureway.cinemaflix.utils.App;
import in.pureway.cinemaflix.utils.AppConstants;
import in.pureway.cinemaflix.utils.CommonUtils;

public class VerifyOTPActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et_otp1, et_otp2, et_otp3, et_otp4;
    private String otp;
    private LoginRegisterMvvm loginRegisterMvvm;
    private LinearLayout ll_otp;
    private String phone, UpdateProfileStatus = "0", username="", email="", countryCode="", password="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.changeLanguage(VerifyOTPActivity.this);
        setContentView(R.layout.activity_verify_o_t_p);
        loginRegisterMvvm = ViewModelProviders.of(VerifyOTPActivity.this).get(LoginRegisterMvvm.class);

        phone = getIntent().getExtras().getString("phone");
        username = getIntent().getExtras().getString("username");
        email = getIntent().getExtras().getString("email");
        countryCode = getIntent().getExtras().getString("countryCode");
        password = getIntent().getExtras().getString("password");
        otp = getIntent().getExtras().getString(AppConstants.OTP_KEY);

        initIds();
        Toast.makeText(VerifyOTPActivity.this, otp, Toast.LENGTH_SHORT).show();
        textwatcher();

        UpdateProfileStatus = App.getSingleton().getUpdateNumberStatus();

        if (UpdateProfileStatus != null && UpdateProfileStatus.equalsIgnoreCase("1")) {


            UpdateProfileStatus = App.getSingleton().getUpdateNumberStatus();

        }

        if (UpdateProfileStatus != null && UpdateProfileStatus.equalsIgnoreCase("2")) {
            UpdateProfileStatus = App.getSingleton().getUpdateNumberStatus();

        }
//            Toast.makeText(this, otp, Toast.LENGTH_SHORT).show();
    }

    private void initIds() {
        ll_otp = findViewById(R.id.ll_otp);
        et_otp1 = findViewById(R.id.et_otp1);
        et_otp2 = findViewById(R.id.et_otp2);
        et_otp3 = findViewById(R.id.et_otp3);
        et_otp4 = findViewById(R.id.et_otp4);
        findViewById(R.id.btn_done_otp).setOnClickListener(this);
        findViewById(R.id.img_back_otp).setOnClickListener(this);
        findViewById(R.id.tv_resend_otp).setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_done_otp:
                matchOTP();
                break;

            case R.id.img_back_otp:
                onBackPressed();
                break;

            case R.id.tv_resend_otp:
                resendOTP();
                break;
        }
    }

    private void resendOTP() {
        String emailPhone = App.getSingleton().getEmailPhone();
        String type = App.getSingleton().getRegisterType();

        loginRegisterMvvm.resendOTP(this, phone).observe(this, new Observer<ModelLoginPhone>() {
            @Override
            public void onChanged(ModelLoginPhone modelLoginPhone) {
                Toast.makeText(VerifyOTPActivity.this, modelLoginPhone.getMessage(), Toast.LENGTH_SHORT).show();
                if (modelLoginPhone.getSuccess().equalsIgnoreCase("1")) {
                    otp = modelLoginPhone.getDetails().getLoginOtp();
                    Toast.makeText(VerifyOTPActivity.this, otp, Toast.LENGTH_SHORT).show();

                } else {
//                    Toast.makeText(VerifyOTPActivity.this, otp, Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void matchOTP() {
        String otp1 = et_otp1.getText().toString();
        String otp2 = et_otp2.getText().toString();
        String otp3 = et_otp3.getText().toString();
        String otp4 = et_otp4.getText().toString();

        if (otp1.isEmpty() || otp2.isEmpty() || otp3.isEmpty() || otp4.isEmpty()) {
            Toast.makeText(this, "OTP is invalid", Toast.LENGTH_SHORT).show();
        } else {
            String otpFull = otp1 + otp2 + otp3 + otp4;

            if (otpFull.equals(otp)){
                Toast.makeText(this, "Otp match successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(VerifyOTPActivity.this, WhatsBirthdayActivity.class).putExtra("username", username).putExtra("email", email)
                        .putExtra("countryCode", countryCode).putExtra("phone", phone).putExtra("password", password);
                startActivity(intent);

            } else {
                Toast.makeText(this, "Otp does not match", Toast.LENGTH_SHORT).show();
            }

//            if (UpdateProfileStatus != null && UpdateProfileStatus.equalsIgnoreCase("1")) {
//                phone = App.getSharedpref().getModel(AppConstants.REGISTER_LOGIN_DATA, ModelLoginRegister.class).getDetails().getPhone();
//
//            } else {
//
//            }

//            if (UpdateProfileStatus != null && UpdateProfileStatus.equalsIgnoreCase("2")) {
////                phone
//
//            }
//                loginRegisterMvvm.matchVerificationToken(this, phone, otpFull).observe(this, new Observer<ModelLoginPhone>() {
//                @Override
//                public void onChanged(ModelLoginPhone modelLoginPhone) {
//                    if (modelLoginPhone.getSuccess().equalsIgnoreCase("1")) {
//                        Toast.makeText(VerifyOTPActivity.this, modelLoginPhone.getMessage().toString(), Toast.LENGTH_SHORT).show();
//
//                        if (UpdateProfileStatus != null && UpdateProfileStatus.equalsIgnoreCase("1")) {
//                            finish();
//
//                            Intent intent = new Intent(VerifyOTPActivity.this, UpdatePhoneNumberActivity.class).putExtra("UpdateNumberStatus", "1");
//                            startActivity(intent);
//                        }
////                        if (UpdateProfileStatus != null && UpdateProfileStatus.equalsIgnoreCase("2")) {
////                            Intent intent = new Intent(VerifyOTPActivity.this, HomeActivity.class).putExtra("UpdateNumberStatus", "1");
////                            startActivity(intent);
////                        }
//
//                        else {
//                            startActivity(new Intent(VerifyOTPActivity.this, HomeActivity.class));
//                            App.getSharedpref().saveModel(AppConstants.REGISTER_LOGIN_DATA, modelLoginPhone);
//                            finishAffinity();
//
//                            App.getSharedpref().saveString(AppConstants.LOGIN_STATUS, "1");
//                            App.getSharedpref().login(VerifyOTPActivity.this, true);
//                        }
//
//                    } else {
//                        Toast.makeText(VerifyOTPActivity.this, modelLoginPhone.getMessage().toString(), Toast.LENGTH_SHORT).show();
//
//                    }
//                }
//            });
//            if (otpFull.matches(otp)) {
//                startActivity(new Intent(VerifyOTPActivity.this, HomeActivity.class));
//            } else {
//                Render render = new Render(VerifyOTPActivity.this);
//                render.setDuration(900);
//                render.setAnimation(Attention.Shake(ll_otp));
//                render.start();
//                Toast.makeText(this, "OTP does not match.", Toast.LENGTH_SHORT).show();
//            }
        }
    }

    private void textwatcher() {
        et_otp1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    et_otp2.requestFocus();
                }
            }
        });

        et_otp2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    et_otp3.requestFocus();
                } else if (s.length() == 0) {
                    et_otp1.requestFocus();
                }
            }
        });

        et_otp3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    et_otp4.requestFocus();
                } else if (s.length() == 0) {
                    et_otp2.requestFocus();
                }
            }
        });

        et_otp4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    et_otp3.requestFocus();
                }

                if (!et_otp1.getText().toString().isEmpty() && !et_otp2.getText().toString().isEmpty() && !et_otp3.getText().toString().isEmpty() && !et_otp4.getText().toString().isEmpty()) {
//                    matchOTP();
                }
            }
        });

    }
}
