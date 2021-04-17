package in.pureway.cinemaflix.activity.videoEditor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import in.pureway.cinemaflix.R;

public class UpdatePhoneOtpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_phone_otp);
    }

//    private void loginPhone() {
//        phone = getCode + phone;
//        String reg_id = FirebaseInstanceId.getInstance().getToken();
//        loginRegisterMvvm.loginWithPhone(this, phone, reg_id, "android").observe(this, new Observer<ModelLoginPhone>() {
//            @Override
//            public void onChanged(ModelLoginPhone modelLoginPhone) {
//                if (modelLoginPhone.getSuccess().equalsIgnoreCase("1")) {
//                    Toast.makeText(RegisterActivity.this, modelLoginPhone.getDetails().getLoginOtp().toString(), Toast.LENGTH_SHORT).show();
//
//                    Intent intent = new Intent(RegisterActivity.this, VerifyOTPActivity.class).putExtra("phone", phone).putExtra(AppConstants.OTP_KEY, modelLoginPhone.getDetails().getLoginOtp());
//                    startActivity(intent);
//
//                } else {
//                    Toast.makeText(RegisterActivity.this, modelLoginPhone.getMessage().toString(), Toast.LENGTH_SHORT).show();
//
//                }
//            }
//        });
//    }
}