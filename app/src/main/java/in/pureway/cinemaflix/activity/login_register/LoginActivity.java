package in.pureway.cinemaflix.activity.login_register;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.iid.FirebaseInstanceId;
import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.activity.HomeActivity;
import in.pureway.cinemaflix.activity.privacysettings.PasswordCodeActivity;
import in.pureway.cinemaflix.javaClasses.FacebookLogin;
import in.pureway.cinemaflix.javaClasses.GoogleLogin;
import in.pureway.cinemaflix.models.ModelLoginRegister;
import in.pureway.cinemaflix.mvvm.LoginRegisterMvvm;
import in.pureway.cinemaflix.utils.App;
import in.pureway.cinemaflix.utils.AppConstants;
import in.pureway.cinemaflix.utils.CommonUtils;
import com.hbb20.CountryCodePicker;

import java.util.Map;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et_email_phone, et_pass_login, et_email_phone_dailog, usernameET, passwordET;
    private LoginRegisterMvvm loginRegisterMvvm;
    GoogleLogin googleLogin;
    private static final int RC_SIGN_IN = 007;
    public CallbackManager callbackManager;
    private String type = "email";
    private AlertDialog dailogbox;
    private Spinner spinner;
    private TextInputLayout til_email_phone;
    private TextView tv_ok, tv_forgot_password1;
    private CountryCodePicker ccp;
    private Button loginBtn;
    private ImageView img_fb_login1, img_google_login1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.changeLanguage(LoginActivity.this);
        setContentView(R.layout.activity_login);
        initIds();
        App.getSingleton().setLoginType(AppConstants.LOGIN_VIDEO);
        googleLogin = new GoogleLogin(LoginActivity.this, AppConstants.LOGIN_VIDEO);
        callbackManager = CallbackManager.Factory.create();
        loginRegisterMvvm = ViewModelProviders.of(LoginActivity.this).get(LoginRegisterMvvm.class);
    }

    private void initIds() {
        findViewById(R.id.img_google_login).setOnClickListener(this);
        findViewById(R.id.tv_forgot_password).setOnClickListener(this);
        findViewById(R.id.img_fb_login).setOnClickListener(this);
        findViewById(R.id.tv_signup_login).setOnClickListener(this);
        findViewById(R.id.btn_signin).setOnClickListener(this);
        findViewById(R.id.img_back_login).setOnClickListener(this);

        ccp = findViewById(R.id.ccp);
        et_email_phone = findViewById(R.id.et_email_phone);
        et_pass_login = findViewById(R.id.et_pass_login);
        usernameET = findViewById(R.id.usernameET);
        passwordET = findViewById(R.id.passwordET);
        tv_forgot_password1 = findViewById(R.id.tv_forgot_password1);
        loginBtn = findViewById(R.id.loginBtn);
        img_google_login1 = findViewById(R.id.img_google_login1);
        img_fb_login1 = findViewById(R.id.img_fb_login1);
        tv_forgot_password1.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
        img_google_login1.setOnClickListener(this);
        img_fb_login1.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_signup_login:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;

            case R.id.tv_forgot_password:
                openEmailPhoneDialog();
                break;

            case R.id.btn_signin:
                login();
                break;

            case R.id.img_back_login:
                onBackPressed();
                break;

            case R.id.img_fb_login:
                FacebookLogin facebookLogin = new FacebookLogin(LoginActivity.this, AppConstants.LOGIN_VIDEO, getApplication());
                facebookLogin.FBLogin();
                break;

            case R.id.img_google_login:
                googleLogin.signIn();
                break;
            case R.id.tv_forgot_password1:
                openEmailPhoneDialog();
                break;
            case R.id.loginBtn:
//                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                login();

                break;
            case R.id.img_fb_login1:
                FacebookLogin facebookLogin1 = new FacebookLogin(LoginActivity.this, AppConstants.LOGIN_VIDEO, getApplication());
                facebookLogin1.FBLogin();
                break;
            case R.id.img_google_login1:
                googleLogin.signIn();
                break;
        }
    }

    private void openEmailPhoneDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(LoginActivity.this);
        final View confirmdailog = layoutInflater.inflate(R.layout.dialog_email_phone, null);
        dailogbox = new AlertDialog.Builder(LoginActivity.this).create();
        dailogbox.setCancelable(true);
        dailogbox.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dailogbox.setView(confirmdailog);

        spinner = confirmdailog.findViewById(R.id.spinner_email_phone);
        et_email_phone_dailog = confirmdailog.findViewById(R.id.et_email_phone_dailog);
        tv_ok = confirmdailog.findViewById(R.id.tv_ok_dialog);
        til_email_phone = confirmdailog.findViewById(R.id.til_email_phone);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        type = "email";
                        et_email_phone_dailog.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                        if (!et_email_phone_dailog.getText().toString().equalsIgnoreCase("") || !et_email_phone_dailog.getText().toString().isEmpty()) {
                            et_email_phone_dailog.setText("");
                        }
                        til_email_phone.setHint("Email");
                        break;

                    case 1:
                        type = "phone";
                        et_email_phone_dailog.setInputType(InputType.TYPE_CLASS_NUMBER);
                        if (!et_email_phone_dailog.getText().toString().equalsIgnoreCase("") || !et_email_phone_dailog.getText().toString().isEmpty()) {
                            et_email_phone_dailog.setText("");
                        }
                        et_email_phone_dailog.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
                        til_email_phone.setHint("Phone");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEmailPhoneOTP(type, et_email_phone_dailog.getText().toString().trim(), dailogbox);
            }
        });
        dailogbox.show();
    }

    private void checkEmailPhoneOTP(final String type, final String emailPhone, final Dialog dialog) {
        if (type.equalsIgnoreCase("email")) {
            if (!Pattern.matches(Patterns.EMAIL_ADDRESS.pattern(), emailPhone)) {
                Toast.makeText(this, "Invalid email!", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        loginRegisterMvvm.otpforgotPass(LoginActivity.this, type, emailPhone).observe(LoginActivity.this, new Observer<Map>() {
            @Override
            public void onChanged(Map map) {
                if (map.get("success").equals("1")) {
                    App.getSingleton().setRegisterType(type);
                    startActivity(new Intent(LoginActivity.this, PasswordCodeActivity.class)
                            .putExtra(AppConstants.OTP_KEY, map.get("otp").toString())
                            .putExtra(AppConstants.EMAILPHONE, emailPhone)
                            .putExtra(AppConstants.ACTIVITY_TYPE, AppConstants.ACTIVITY_TYPE_LOGIN));
                    dialog.dismiss();
                } else {
                    Toast.makeText(LoginActivity.this, map.get("message").toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void login() {
//        String email = ccp.getSelectedCountryCodeWithPlus()+et_email_phone.getText().toString();
        String username = "@"+usernameET.getText().toString().trim();
        String pass = passwordET.getText().toString().trim();
//        String regId = FirebaseInstanceId.getInstance().getToken();
        String reg_id = "re";
        String device_type = "Android";
        if (username.isEmpty()) {
            usernameET.setError("Email Cannot Be Empty");
        } else if (pass.isEmpty()) {
            Toast.makeText(this, "Password Cannot Be Empty", Toast.LENGTH_SHORT).show();
        } else {
            loginRegisterMvvm.userLogin(LoginActivity.this, username, pass, reg_id, device_type).observe(LoginActivity.this, new Observer<ModelLoginRegister>() {
                @Override
                public void onChanged(ModelLoginRegister modelLoginRegister) {
                    if (modelLoginRegister.getSuccess().equalsIgnoreCase("1")) {
                        Toast.makeText(LoginActivity.this, modelLoginRegister.getMessage(), Toast.LENGTH_SHORT).show();

//                        SQLiteDatabaseHandler sqLiteDatabaseHandler=new SQLiteDatabaseHandler(LoginActivity.this);
//                        sqLiteDatabaseHandler.addModel(modelLoginRegister.getDetails());

                        App.getSharedpref().saveModel(AppConstants.REGISTER_LOGIN_DATA, modelLoginRegister);
                        finishAffinity();
                        App.getSharedpref().saveString(AppConstants.LOGIN_STATUS, "1");
                        App.getSharedpref().login(LoginActivity.this, true);
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    } else {
                        Toast.makeText(LoginActivity.this, modelLoginRegister.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            googleLogin.activityResult(requestCode, resultCode, data);
        } else {
            FacebookLogin.callbackManager.onActivityResult(requestCode, resultCode, data);
        }

    }

}
