package in.pureway.cinemaflix.activity.privacysettings;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget. Switch;
import android.widget.TextView;

import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.models.ModelLoginRegister;
import in.pureway.cinemaflix.utils.App;
import in.pureway.cinemaflix.utils.AppConstants;
import in.pureway.cinemaflix.utils.CommonUtils;

public class ManageMyAccountActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_phone_no_manage, tv_email_manage;
    private ModelLoginRegister.Details details;
    private String phone = "";
    private String email = "";
    private RelativeLayout rl_email, rl_phone;
    private Switch switch_save_login_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.changeLanguage(ManageMyAccountActivity.this);
        setContentView(R.layout.activity_manage_my_account);

        findIds();
        setData();
        switch_save_login_info.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    App.getSharedpref().saveString(AppConstants.LOGIN_STATUS, "1");
                } else {
                    App.getSharedpref().saveString(AppConstants.LOGIN_STATUS, "0");
                }
            }
        });
    }

    private void setData() {
        details = App.getSharedpref().getModel(AppConstants.REGISTER_LOGIN_DATA, ModelLoginRegister.class).getDetails();
        phone = details.getPhone();
        email = details.getEmail();
        if (!phone.equalsIgnoreCase("")) {
            tv_phone_no_manage.setText(phone);
        } else {
            tv_phone_no_manage.setText(R.string.add_phine_number);
        }
        if (!email.equalsIgnoreCase("")) {
            tv_email_manage.setText(email);
        } else {
            tv_email_manage.setText(R.string.add_email);
        }
    }

    private void findIds() {
        findViewById(R.id.tv_password_change).setOnClickListener(this);
        findViewById(R.id.rl_phone).setOnClickListener(this);
        findViewById(R.id.rl_email).setOnClickListener(this);
        switch_save_login_info = findViewById(R.id.switch_save_login_info);
        tv_email_manage = findViewById(R.id.tv_email_manage);
        tv_email_manage.setOnClickListener(this);
        tv_phone_no_manage = findViewById(R.id.tv_phone_no_manage);
        tv_phone_no_manage.setOnClickListener(this);
    }

    public void back(View view) {
        onBackPressed();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.rl_phone:
            case R.id.tv_phone_no_manage:
//                if (!phone.equalsIgnoreCase("")) {
//                    openNumberDialog();
//                } else {
//                startActivity(new Intent(ManageMyAccountActivity.this, AddPhoneActivity.class));
//                }

                break;

            case R.id.tv_email_manage:
            case R.id.rl_email:
//                openEmailDialog();
//                startActivity(new Intent(ManageMyAccountActivity.this, AddEmailActivity.class));
                break;

            case R.id.tv_password_change:
                startActivity(new Intent(ManageMyAccountActivity.this, ChangePasswordActivity.class));
                break;
        }
    }

    private void openEmailDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(ManageMyAccountActivity.this);
        final View confirmdailog = layoutInflater.inflate(R.layout.dialog_verify_email, null);
        final AlertDialog dailogbox = new AlertDialog.Builder(ManageMyAccountActivity.this).create();
        dailogbox.setCancelable(true);
        dailogbox.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dailogbox.setView(confirmdailog);

        confirmdailog.findViewById(R.id.tv_next_email).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ManageMyAccountActivity.this, PasswordCodeActivity.class));
            }
        });

        confirmdailog.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dailogbox.dismiss();
            }
        });

        dailogbox.show();

    }

    private void openNumberDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(ManageMyAccountActivity.this);
        final View confirmdailog = layoutInflater.inflate(R.layout.dialog_verify_phone, null);
        final AlertDialog dailogbox = new AlertDialog.Builder(ManageMyAccountActivity.this).create();
        dailogbox.setCancelable(true);
        dailogbox.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dailogbox.setView(confirmdailog);

        confirmdailog.findViewById(R.id.tv_send_code).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ManageMyAccountActivity.this, PasswordCodeActivity.class));
            }
        });

        confirmdailog.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dailogbox.dismiss();
            }
        });

        dailogbox.show();
    }

    public void deleteAccount(View view) {
        startActivity(new Intent(ManageMyAccountActivity.this, DeleteAccountActivity.class));
    }

    public void switchPro(View view) {
        startActivity(new Intent(ManageMyAccountActivity.this, SwitchProAccountActiviy.class));
    }

    public void photofitCode(View view) {
        startActivity(new Intent(ManageMyAccountActivity.this, CodeActivity.class));
    }
}

