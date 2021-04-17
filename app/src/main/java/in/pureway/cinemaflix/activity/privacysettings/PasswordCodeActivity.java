package in.pureway.cinemaflix.activity.privacysettings;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.activity.HomeActivity;
import in.pureway.cinemaflix.activity.PrivacySettingsActivity;
import in.pureway.cinemaflix.activity.editProfile.EditProfileActivity;
import in.pureway.cinemaflix.activity.editProfile.UsernameActivity;
import in.pureway.cinemaflix.activity.login_register.ForgotPasswordActivity;
import in.pureway.cinemaflix.activity.findFriends.FindFriendsActivity;
import in.pureway.cinemaflix.javaClasses.Logout;
import in.pureway.cinemaflix.javaClasses.UpdateProfileApi;
import in.pureway.cinemaflix.models.ModelLoginRegister;
import in.pureway.cinemaflix.mvvm.LoginRegisterMvvm;
import in.pureway.cinemaflix.mvvm.ProfileMvvm;
import in.pureway.cinemaflix.utils.App;
import in.pureway.cinemaflix.utils.AppConstants;
import in.pureway.cinemaflix.utils.CommonUtils;

import java.util.Map;

public class PasswordCodeActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et_otp1, et_otp2, et_otp3, et_otp4;
    private String otp = "", emailPhone = "";
    private ProfileMvvm profileMvvm;
    private TextView tv_resend_otp, tv_email_phone;
    private String activityType = "";
    private String userId = CommonUtils.userId(PasswordCodeActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.changeLanguage(PasswordCodeActivity.this);
        setContentView(R.layout.activity_password_code);
        findiDs();
        profileMvvm = ViewModelProviders.of(PasswordCodeActivity.this).get(ProfileMvvm.class);
        otp = getIntent().getExtras().getString(AppConstants.OTP_KEY);
        emailPhone = getIntent().getExtras().getString(AppConstants.EMAILPHONE);
        activityType = getIntent().getExtras().getString(AppConstants.ACTIVITY_TYPE);
        if (activityType.equalsIgnoreCase(AppConstants.ACTIVITY_TYPE_LOGIN)) {
            tv_resend_otp.setVisibility(View.GONE);
        }
        tv_email_phone.setText("To set your password, enter 4-digit code send\n" + "to " + emailPhone);
//        Toast.makeText(this, otp, Toast.LENGTH_SHORT).show();
        textwatcher();
    }

    private void findiDs() {
        findViewById(R.id.btn_done_verify_otp).setOnClickListener(this);
        tv_email_phone = findViewById(R.id.tv_email_phone);
        tv_resend_otp = findViewById(R.id.tv_resend_otp);
        tv_resend_otp.setOnClickListener(this);
        et_otp1 = findViewById(R.id.et_otp1);
        et_otp2 = findViewById(R.id.et_otp2);
        et_otp3 = findViewById(R.id.et_otp3);
        et_otp4 = findViewById(R.id.et_otp4);
    }

    public void backPress(View view) {
        onBackPressed();
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
                    matchOTP();
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
            if (otpFull.matches(otp)) {
                if (activityType.equalsIgnoreCase(AppConstants.ACTIVITY_TYPE_LOGIN)) {
                    startActivity(new Intent(PasswordCodeActivity.this, ForgotPasswordActivity.class)
                            .putExtra(AppConstants.EMAILPHONE, emailPhone));
                } else if (activityType.equalsIgnoreCase(AppConstants.ACTIVITY_TYPE_DELETE)) {
                    deletDialog();
                } else if (activityType.equalsIgnoreCase(AppConstants.UPDATEPHONENUMBER)) {

                    String phone = App.getSingleton().getUpdateNumber();
                    UpdateProfileApi updateProfileApi = new UpdateProfileApi(PasswordCodeActivity.this);
                    updateProfileApi.hitUpdateProfile("", "", "", phone);

                    onBackPressed();
//                    startActivity(new Intent(PasswordCodeActivity.this, EditProfileActivity.class)
//                            .putExtra(AppConstants.EMAILPHONE, emailPhone));


                } else {
                    updateValueBackend();
                }
            } else {
                Toast.makeText(this, "OTP does not match.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateValueBackend() {
        profileMvvm.updateEmailPhone(PasswordCodeActivity.this, userId, App.getSingleton().getRegisterType(), emailPhone)
                .observe(PasswordCodeActivity.this, new Observer<ModelLoginRegister>() {
                    @Override
                    public void onChanged(ModelLoginRegister modelLoginRegister) {
                        if (modelLoginRegister.getSuccess().equalsIgnoreCase("1")) {
                            Log.i("updateEmailPhone", modelLoginRegister.getMessage());
                            App.getSharedpref().saveModel(AppConstants.REGISTER_LOGIN_DATA, modelLoginRegister);
                            if (getIntent().hasExtra(AppConstants.PHONE_ADD_TYPE)) {
                                startActivity(new Intent(PasswordCodeActivity.this, FindFriendsActivity.class));
                            } else {
                                startActivity(new Intent(PasswordCodeActivity.this, ManageMyAccountActivity.class));
                            }
                            finish();
                        } else {
                            Log.i("updateEmailPhone", modelLoginRegister.getMessage());
                            Toast.makeText(PasswordCodeActivity.this, modelLoginRegister.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void resendOTP(final String type, String emailPhone) {
        LoginRegisterMvvm loginRegisterMvvm = ViewModelProviders.of(PasswordCodeActivity.this).get(LoginRegisterMvvm.class);
        loginRegisterMvvm.checkEmailPhone(PasswordCodeActivity.this, "", "",emailPhone).observe(PasswordCodeActivity.this, new Observer<Map>() {
            @Override
            public void onChanged(Map map) {
                if (map.get("success").equals("1")) {
                    otp = map.get("otp").toString();
//                    Toast.makeText(PasswordCodeActivity.this, otp, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PasswordCodeActivity.this, map.get("message").toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_resend_otp:
                resendOTP(App.getSingleton().getRegisterType(), emailPhone);
                break;

            case R.id.btn_done_verify_otp:
                matchOTP();
                break;
        }
    }

    private void deletDialog() {
        final AlertDialog.Builder al = new AlertDialog.Builder(this, R.style.dialogStyle);
        al.setTitle("Delete Account ?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteAccount();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setMessage("Do you really want to delete the account ?").show();
    }

    private void deleteAccount() {
        profileMvvm.deleteAccount(PasswordCodeActivity.this, userId).observe(PasswordCodeActivity.this, new Observer<Map>() {
            @Override
            public void onChanged(Map map) {
                if (map.get("success").equals("1")) {
                    Toast.makeText(PasswordCodeActivity.this, map.get("message").toString(), Toast.LENGTH_SHORT).show();
                    Logout logout = new Logout(PasswordCodeActivity.this);  //logout user
                    logout.logoutUser();
                } else {
                    Toast.makeText(PasswordCodeActivity.this, map.get("message").toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

