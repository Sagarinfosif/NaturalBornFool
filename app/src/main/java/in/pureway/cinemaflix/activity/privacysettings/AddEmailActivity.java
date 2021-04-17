package in.pureway.cinemaflix.activity.privacysettings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.models.ModelLoginRegister;
import in.pureway.cinemaflix.mvvm.LoginRegisterMvvm;
import in.pureway.cinemaflix.utils.App;
import in.pureway.cinemaflix.utils.AppConstants;
import in.pureway.cinemaflix.utils.CommonUtils;

import java.util.Map;
import java.util.regex.Pattern;

public class AddEmailActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_next;
    private EditText et_email;
    private LoginRegisterMvvm loginRegisterMvvm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.changeLanguage(AddEmailActivity.this);
        setContentView(R.layout.activity_add_email);
        loginRegisterMvvm = ViewModelProviders.of(AddEmailActivity.this).get(LoginRegisterMvvm.class);
        findIds();
        ModelLoginRegister.Details details = App.getSharedpref().getModel(AppConstants.REGISTER_LOGIN_DATA, ModelLoginRegister.class).getDetails();
        String email = details.getEmail();
        et_email.setText(email);
        et_email.requestFocus();
    }

    private void findIds() {
        et_email = findViewById(R.id.et_email);
        btn_next = findViewById(R.id.btn_next);
        btn_next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                sendEmailOtp();
                break;
        }
    }

    private void sendEmailOtp() {
        String email = et_email.getText().toString().trim();
        if (email.isEmpty() || email == null) {
            Toast.makeText(this, "Email cannot be empty.", Toast.LENGTH_SHORT).show();
        } else {
            sendOTP(email);
        }
    }

    private void sendOTP(final String email) {
        if (!Pattern.matches(Patterns.EMAIL_ADDRESS.pattern(), email)) {
            Toast.makeText(this, "Invalid email", Toast.LENGTH_SHORT).show();
        } else {
            loginRegisterMvvm.checkEmailPhone(AddEmailActivity.this,  email,"","").observe(AddEmailActivity.this, new Observer<Map>() {
                @Override
                public void onChanged(Map map) {
                    if (map.get("success").equals("1")) {
                        String otp = map.get("otp").toString();
                        App.getSingleton().setRegisterType("email");
                        startActivity(new Intent(AddEmailActivity.this, PasswordCodeActivity.class)
                                .putExtra(AppConstants.OTP_KEY, otp)
                                .putExtra(AppConstants.EMAILPHONE, email)
                                .putExtra(AppConstants.ACTIVITY_TYPE, AppConstants.ACTIVITY_TYPE_MANAGE));
                        finish();
                    } else {
                        Toast.makeText(AddEmailActivity.this, map.get("message").toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void backPress(View view) {
        onBackPressed();
    }
}
