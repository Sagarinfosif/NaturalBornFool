package in.pureway.cinemaflix.activity.login_register;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.models.ModelLoginRegister;
import in.pureway.cinemaflix.mvvm.LoginRegisterMvvm;
import in.pureway.cinemaflix.utils.App;
import in.pureway.cinemaflix.utils.AppConstants;
import in.pureway.cinemaflix.utils.CommonUtils;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et_conf_pass, et_new_pass;
    private String emailPhone = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.changeLanguage(ForgotPasswordActivity.this);
        setContentView(R.layout.activity_forgot_password);
        findIds();
        emailPhone = getIntent().getExtras().getString(AppConstants.EMAILPHONE);
    }

    private void findIds() {
        et_new_pass = findViewById(R.id.et_new_pass);
        et_conf_pass = findViewById(R.id.et_conf_pass);
        findViewById(R.id.btn_change_pass).setOnClickListener(this);
    }

    public void back(View view) {
        onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_change_pass:
                changePassword();
                break;
        }
    }

    private void changePassword() {
        String newPass = et_new_pass.getText().toString().trim();
        String confPass = et_conf_pass.getText().toString().trim();
        if (newPass.length() < 5) {
            et_new_pass.setError("Password cannot be less than 5 digits.");
            return;
        }
        if (confPass.length() < 5) {
            et_conf_pass.setError("Password cannot be less than 5 digits.");
            return;
        }

        if (newPass.isEmpty() || confPass.isEmpty()) {
            Toast.makeText(this, "Password cannot be empty.", Toast.LENGTH_SHORT).show();
        } else if (!newPass.matches(confPass)) {
            Toast.makeText(this, "Passwords doesnt match.", Toast.LENGTH_SHORT).show();
        } else {
            LoginRegisterMvvm loginRegisterMvvm = ViewModelProviders.of(ForgotPasswordActivity.this).get(LoginRegisterMvvm.class);
            loginRegisterMvvm.forgotPass(ForgotPasswordActivity.this, App.getSingleton().getRegisterType(), emailPhone, newPass).observe(ForgotPasswordActivity.this, new Observer<ModelLoginRegister>() {
                @Override
                public void onChanged(ModelLoginRegister modelLoginRegister) {
                    if (modelLoginRegister.getSuccess().equalsIgnoreCase("1")) {
                        Toast.makeText(ForgotPasswordActivity.this, modelLoginRegister.getMessage(), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
                        finishAffinity();
                    } else {
                        Toast.makeText(ForgotPasswordActivity.this, modelLoginRegister.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}