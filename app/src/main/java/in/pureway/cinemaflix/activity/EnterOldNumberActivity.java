package in.pureway.cinemaflix.activity;

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

import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.activity.login_register.VerifyOTPActivity;
import in.pureway.cinemaflix.activity.privacysettings.UpdatePhoneNumberActivity;
import in.pureway.cinemaflix.models.ModelLoginPhone;
import in.pureway.cinemaflix.models.ModelLoginRegister;
import in.pureway.cinemaflix.mvvm.LoginRegisterMvvm;
import in.pureway.cinemaflix.utils.App;
import in.pureway.cinemaflix.utils.AppConstants;

public class EnterOldNumberActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener{
    private Button btn_send_otp;
    private EditText et_phone;
    private String phone, UpdateNumberStatus = "0";
    private LoginRegisterMvvm loginRegisterMvvm;
    private TextView tv_TextNumber;
    private CountryCodePicker ccp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_old_number);
        loginRegisterMvvm = ViewModelProviders.of(EnterOldNumberActivity.this).get(LoginRegisterMvvm.class);

        ccp = findViewById(R.id.ccp);
        tv_TextNumber = findViewById(R.id.tv_TextNumber);
        btn_send_otp = findViewById(R.id.btn_send_otp);
        et_phone = findViewById(R.id.et_phone);
        btn_send_otp.setOnClickListener(this);
//        et_phone.addTextChangedListener(this);
        tv_TextNumber.setText("Enter your Old number");



        String phone = App.getSharedpref().getModel(AppConstants.REGISTER_LOGIN_DATA, ModelLoginRegister.class).getDetails().getPhone();

        if (phone != null) {
            et_phone.setText(phone);

        }


    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        phone = et_phone.getText().toString();

        if (!phone.isEmpty()) {
            if (phone.length() == 10) {
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
            if (phone.length() == 10) {
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
            if (phone.length() == 10) {
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

                if (phone.isEmpty()) {
                    et_phone.setError("Please enter Phone number");
                } else {

                    loginRegisterMvvm.resendOTP(this, phone).observe(this, new Observer<ModelLoginPhone>() {
                        @Override
                        public void onChanged(ModelLoginPhone modelLoginPhone) {
                            Toast.makeText(EnterOldNumberActivity.this, modelLoginPhone.getMessage(), Toast.LENGTH_SHORT).show();
                            if (modelLoginPhone.getSuccess().equalsIgnoreCase("1")) {

                                App.getSingleton().setUpdateNumberStatus("1");
                                finish();

//                                    Toast.makeText(UpdatePhoneNumberActivity.this, modelLoginPhone.getDetails().getLoginOtp(), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(EnterOldNumberActivity.this, VerifyOTPActivity.class).putExtra(AppConstants.OTP_KEY, modelLoginPhone.getDetails().getLoginOtp()).putExtra("UpdateProfileStatus", "1");
                                startActivity(intent);

                            } else {

                                Toast.makeText(EnterOldNumberActivity.this, modelLoginPhone.getMessage().toString(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
//                    }
                }


                break;
        }
    }

    public void backPress(View view) {
        onBackPressed();
    }
}