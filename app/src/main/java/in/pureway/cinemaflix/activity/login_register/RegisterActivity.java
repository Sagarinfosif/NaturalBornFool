package in.pureway.cinemaflix.activity.login_register;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import in.pureway.cinemaflix.activity.WebLinkOpenActivity;
import in.pureway.cinemaflix.models.ModelLoginPhone;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.models.ModelLoginRegister;
import in.pureway.cinemaflix.mvvm.LoginRegisterMvvm;
import in.pureway.cinemaflix.utils.App;
import in.pureway.cinemaflix.utils.AppConstants;
import in.pureway.cinemaflix.utils.CommonUtils;

import com.google.firebase.iid.FirebaseInstanceId;
import com.hbb20.CountryCodePicker;

import java.util.Calendar;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {
    private TextInputLayout input_email, input_phone;
    private RelativeLayout rl_email, rl_phone;
    private TextInputEditText et_phone, et_email, et_pass;
    private LoginRegisterMvvm loginRegisterMvvm;
    private String registerType = "phone";
    private String phone = "", getCode, username="", email="", password="", cPassword="", dob="", reg_id="re", device_type="", otp="";
    private CountryCodePicker ccp1, countryCodePicker;
    private Button btn_send_otp, registerBtn;
    private TextView tvTermLink,tv_policyLink;
    private EditText usernameET, emailET, phoneNumberET, passwordET, cpasswordET, dobET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.changeLanguage(RegisterActivity.this);
//        overridePendingTransition(R.anim.in_from_right, R.anim.in_from_left);
        setContentView(R.layout.activity_register);

        loginRegisterMvvm = ViewModelProviders.of(RegisterActivity.this).get(LoginRegisterMvvm.class);
        initIds();
    }

//    @SuppressLint("ResourceAsColor")
    private void initIds() {

        btn_send_otp = findViewById(R.id.btn_send_otp);
        ccp1 = findViewById(R.id.ccp1);
        countryCodePicker = findViewById(R.id.countryCodePicker);
        findViewById(R.id.tv_policyLink).setOnClickListener(this);
        findViewById(R.id.tvTermLink).setOnClickListener(this);
        findViewById(R.id.tv_signin_reg).setOnClickListener(this);
        findViewById(R.id.btn_send_otp).setOnClickListener(this);
        findViewById(R.id.img_back_reg).setOnClickListener(this);
        et_pass = findViewById(R.id.et_pass);
        et_email = findViewById(R.id.et_email);
        et_phone = findViewById(R.id.et_phone);
        input_phone = findViewById(R.id.input_phone);
        input_email = findViewById(R.id.input_email);
        rl_phone = findViewById(R.id.rl_phone);
        rl_email = findViewById(R.id.rl_email);
        input_email = findViewById(R.id.input_email);
        input_phone = findViewById(R.id.input_phone);
        usernameET = findViewById(R.id.usernameET);
        emailET = findViewById(R.id.emailET);
        dobET = findViewById(R.id.dobET);
        dobET.setOnClickListener(this);
        phoneNumberET = findViewById(R.id.phoneNumberET);
        passwordET = findViewById(R.id.passwordET);
        cpasswordET = findViewById(R.id.cpasswordET);
        registerBtn = findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(this);

        rl_email.setOnClickListener(this);
        rl_phone.setOnClickListener(this);

//        et_phone.requestFocus();

//        phone = et_phone.getText().toString();
//        et_phone.addTextChangedListener(this);
//        if (!phone.isEmpty()) {
//            if (phone.length() == 10) {
//                btn_send_otp.setBackgroundColor(R.color.red1);
//            }
//        }


        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(et_phone, InputMethodManager.SHOW_IMPLICIT);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_signin_reg:
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                break;

            case R.id.btn_send_otp:
                validation();
//                checkEmailPhone();
                break;

            case R.id.img_back_reg:
                onBackPressed();
                break;

            case R.id.rl_phone:
                registerType = "phone";
                rl_phone.setBackgroundColor(getResources().getColor(R.color.trans));
                rl_email.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                input_phone.setVisibility(View.VISIBLE);
                input_email.setVisibility(View.GONE);
                break;

            case R.id.rl_email:
                registerType = "email";
                rl_email.setBackgroundColor(getResources().getColor(R.color.trans));
                rl_phone.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                input_email.setVisibility(View.VISIBLE);
                input_phone.setVisibility(View.GONE);
                break;

            case R.id.tvTermLink:

                Intent intent1 = new Intent(this, WebLinkOpenActivity.class);
                intent1.putExtra("key", "0");
                intent1.putExtra("title", "Terms and Conditions");
                startActivity(intent1);

                break;

            case R.id.tv_policyLink:

                Intent intent=new Intent(this, WebLinkOpenActivity.class);
                intent.putExtra("key","1");
                intent.putExtra("title","CINEMAFLIX Privacy Policy");
                startActivity(intent);
                break;
            case R.id.registerBtn:
                validation();
                break;
            case R.id.dobET:
                openCalender();
                break;
        }
    }

    private void openCalender() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                Toast.makeText(RegisterActivity.this, ""+year +"-"+month+1+"-"+dayOfMonth, Toast.LENGTH_SHORT).show();
                dobET.setText(""+year +"-"+(month+1)+"-"+dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
        dialog.show();
    }

    //    @SuppressLint("ResourceAsColor")
    private void validation() {
//        phone = et_phone.getText().toString();
        phone = phoneNumberET.getText().toString().trim();
        username = "@"+usernameET.getText().toString().trim();
        email = emailET.getText().toString().trim();
        dob = dobET.getText().toString().trim();
        password = passwordET.getText().toString().trim();
        cPassword = cpasswordET.getText().toString().trim();
        getCode = countryCodePicker.getSelectedCountryCodeWithPlus();

        if (username.isEmpty()){
            usernameET.setError("Please enter username");
        } else if (username.contains(" ")){
            usernameET.setError("Username should not contain spaces");
        } else if (email.isEmpty()){
            emailET.setError("Please enter email");
        } else if (!emailValidator(email)){
            emailET.setError("Please enter valid email");
        }
//        else if (dob.isEmpty()){
//            dobET.setError("Please Enter your birthday");
//        }
        else if (phone.isEmpty()) {
            phoneNumberET.setError("Please enter phone number");
        } else if (phone.length() < 8|| phone.length()>15) {
            phoneNumberET.setError("Please enter valid number");
        }   else if (password.isEmpty()){
            passwordET.setError("Please enter password");
        } else if (cPassword.isEmpty()){
            cpasswordET.setError("Please confirm password");
        } else if (!password.equals(cPassword)){
            passwordET.setError("Password mismatch");
        } else {
//            Toast.makeText(this, "register", Toast.LENGTH_SHORT).show();
//            loginPhone();
            checkEmailUsernamePhone();
        }

    }

    private void checkEmailUsernamePhone() {
        loginRegisterMvvm.checkEmailPhone(this, email, username, phone).observe(this, new Observer<Map>() {
            @Override
            public void onChanged(Map map) {
                if (map.get("success").equals("1")){
//                    registerUser();
                    Toast.makeText(RegisterActivity.this, map.get("message").toString(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, VerifyOTPActivity.class).putExtra("username", username).putExtra("email", email)
                            .putExtra("countryCode", getCode).putExtra("phone", phone).putExtra("password", password).putExtra(AppConstants.OTP_KEY, map.get("otp").toString());
                    startActivity(intent);
                }else {
                    Toast.makeText(RegisterActivity.this, map.get("message").toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void registerUser() {
        loginRegisterMvvm.registerUser(RegisterActivity.this, password, dob, email, getCode, phone, username, reg_id, device_type).observe(this, new Observer<ModelLoginRegister>() {
            @Override
            public void onChanged(ModelLoginRegister modelLoginRegister) {
                if (modelLoginRegister.getSuccess().equalsIgnoreCase("1")){
                    Intent intent = new Intent(RegisterActivity.this, VerifyOTPActivity.class).putExtra(AppConstants.OTP_KEY, otp).putExtra("phone", phone);
                    startActivity(intent);
                } else {
                    Toast.makeText(RegisterActivity.this, modelLoginRegister.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;

        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void loginPhone() {
        phone = getCode + phone;
        String reg_id = FirebaseInstanceId.getInstance().getToken();
        loginRegisterMvvm.loginWithPhone(this, phone, reg_id, "android").observe(this, new Observer<ModelLoginPhone>() {
            @Override
            public void onChanged(ModelLoginPhone modelLoginPhone) {
                if (modelLoginPhone.getSuccess().equalsIgnoreCase("1")) {
//                    Toast.makeText(RegisterActivity.this, modelLoginPhone.getDetails().getLoginOtp().toString(), Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(RegisterActivity.this, VerifyOTPActivity.class).putExtra("phone", phone).putExtra(AppConstants.OTP_KEY, modelLoginPhone.getDetails().getLoginOtp());
                    startActivity(intent);

                } else {
                    Toast.makeText(RegisterActivity.this, modelLoginPhone.getMessage().toString(), Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void checkEmailPhone() {

        String emailPhone = "";
        String password = et_pass.getText().toString().trim();

        if (password.length() < 5) {
            et_pass.setError("Password cannot be less than 5 digits.");
            return;
        }

        if (registerType.equalsIgnoreCase("phone")) {


            emailPhone = et_phone.getText().toString();
            if (emailPhone.isEmpty()) {
                et_phone.setError("Phone Number Cannot Be Empty!");
                return;
            }
            if (emailPhone.length() < 10) {
                et_pass.setError("Please enter valid number");
                return;
            }
        } else {
            emailPhone = et_email.getText().toString();
            if (emailPhone.isEmpty()) {
                et_email.setError("Email Cannot Be Empty!");
                return;
            } else if (!Pattern.matches(Patterns.EMAIL_ADDRESS.pattern(), emailPhone)) {
                et_email.setError("Invalid Email");
                return;
            }
        }

        if (password.isEmpty()) {
            et_pass.setError("Password Cannot Be Empty!");
            return;
        }

        App.getSingleton().setEmailPhone(emailPhone);
        App.getSingleton().setRegisterType(registerType);
        App.getSingleton().setPassword(password);

//        loginRegisterMvvm.checkEmailPhone(RegisterActivity.this, registerType, emailPhone).observe(RegisterActivity.this, new Observer<Map>() {
//            @Override
//            public void onChanged(Map map) {
//                if (map.get("success").equals("1")) {
//
//                    String otp = map.get("otp").toString();
//
//                    startActivity(new Intent(RegisterActivity.this, VerifyOTPActivity.class).putExtra(AppConstants.OTP_KEY, otp));
//                } else {
//                    Toast.makeText(RegisterActivity.this, map.get("message").toString(), Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        phone = et_phone.getText().toString();

        if (!phone.isEmpty()) {
            if (phone.length() == 10) {
                btn_send_otp.setBackgroundResource(R.drawable.btn_bg1);
            } else {
                btn_send_otp.setBackgroundResource(R.drawable.dark_bg);

            }
        }
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        phone = et_phone.getText().toString();

        if (!phone.isEmpty()) {
            if (phone.length() == 10) {
                btn_send_otp.setBackgroundResource(R.drawable.btn_bg1);
            } else {
                btn_send_otp.setBackgroundResource(R.drawable.dark_bg);

            }
        }

    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void afterTextChanged(Editable s) {
        phone = et_phone.getText().toString();

        if (!phone.isEmpty()) {
            if (phone.length() == 10) {
                btn_send_otp.setBackgroundResource(R.drawable.btn_bg1);
            } else {
                btn_send_otp.setBackgroundResource(R.drawable.dark_bg);

            }
        }
    }
}

