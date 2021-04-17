package in.pureway.cinemaflix.activity.privacysettings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.models.ModelLoginRegister;
import in.pureway.cinemaflix.mvvm.LoginRegisterMvvm;
import in.pureway.cinemaflix.utils.CommonUtils;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et_conf_pass, et_new_pass, et_old_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.changeLanguage(ChangePasswordActivity.this);
        setContentView(R.layout.activity_change_password);
        findIds();
    }

    private void findIds() {
        et_old_pass = findViewById(R.id.et_old_pass);
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
                changePass();
                break;
        }
    }

    private void changePass() {
        String oldPass = et_old_pass.getText().toString().trim();
        String newPass = et_new_pass.getText().toString().trim();
        String confPass = et_conf_pass.getText().toString().trim();

        if (oldPass.length() < 5) {
            et_old_pass.setError("Password cannot be less than 5 digits.");
            return;
        }
        if (newPass.length() < 5) {
            et_new_pass.setError("Password cannot be less than 5 digits.");
            return;
        }
        if (confPass.length() < 5) {
            et_conf_pass.setError("Password cannot be less than 5 digits.");
            return;
        }


        if (oldPass.isEmpty() || oldPass.equalsIgnoreCase("")) {
            Toast.makeText(this, "Old Password Cannot be empty.", Toast.LENGTH_SHORT).show();
        } else if (newPass.isEmpty() || newPass.equalsIgnoreCase("")) {
            Toast.makeText(this, "New Password Cannot be empty.", Toast.LENGTH_SHORT).show();
        } else if (confPass.isEmpty() || confPass.equalsIgnoreCase("")) {
            Toast.makeText(this, "Confirm Password Cannot be empty.", Toast.LENGTH_SHORT).show();
        } else if (!newPass.matches(confPass)) {
            Toast.makeText(this, "Passwords does not match.", Toast.LENGTH_SHORT).show();
        } else {
            String userId = CommonUtils.userId(ChangePasswordActivity.this);
            LoginRegisterMvvm loginRegisterMvvm = ViewModelProviders.of(ChangePasswordActivity.this).get(LoginRegisterMvvm.class);
            loginRegisterMvvm.changePassword(ChangePasswordActivity.this, userId, oldPass, newPass).observe(ChangePasswordActivity.this, new Observer<ModelLoginRegister>() {
                @Override
                public void onChanged(ModelLoginRegister modelLoginRegister) {
                    if (modelLoginRegister.getSuccess().equalsIgnoreCase("1")) {
                        Log.i("changePassword", modelLoginRegister.getMessage());
                        Toast.makeText(ChangePasswordActivity.this, modelLoginRegister.getMessage(), Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    } else {
                        Log.i("changePassword", modelLoginRegister.getMessage());
                        Toast.makeText(ChangePasswordActivity.this, modelLoginRegister.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
