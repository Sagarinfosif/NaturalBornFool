package in.pureway.cinemaflix.activity.login_register;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.activity.HomeActivity;
import in.pureway.cinemaflix.models.ModelLoginRegister;
import in.pureway.cinemaflix.mvvm.LoginRegisterMvvm;
import in.pureway.cinemaflix.utils.App;
import in.pureway.cinemaflix.utils.AppConstants;
import in.pureway.cinemaflix.utils.CommonUtils;

import java.util.Calendar;

public class WhatsBirthdayActivity extends AppCompatActivity implements View.OnClickListener {
    private DatePicker datepicker_bday;
    private LoginRegisterMvvm loginRegisterMvvm;
    private int d,m,y;
    String sMonth, mdDate;
    private String dateOfBirth = "", username="", email="", countryCode="", phone="", password="";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.changeLanguage(WhatsBirthdayActivity.this);
        setContentView(R.layout.activity_whats_birthday);

        loginRegisterMvvm = ViewModelProviders.of(WhatsBirthdayActivity.this).get(LoginRegisterMvvm.class);

        username = getIntent().getExtras().getString("username");
        email = getIntent().getExtras().getString("email");
        countryCode = getIntent().getExtras().getString("countryCode");
        phone = getIntent().getExtras().getString("phone");
        password = getIntent().getExtras().getString("password");

        initIds();
//        setDateDialog();
//        datePicker();


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void datePicker() {
        datepicker_bday.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar c = Calendar.getInstance();
//               int age=calculateAge(c.getTimeInMillis());
//                if (age < 11) {
//                    Toast
//                }else {
                    dateOfBirth = year + "-" + monthOfYear + "-" + dayOfMonth;
//                }
            }
        });
    }

    int calculateAge(long date){
        Calendar dob = Calendar.getInstance();
        dob.setTimeInMillis(date);
        Calendar today = Calendar.getInstance();
        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
        if(today.get(Calendar.DAY_OF_MONTH) < dob.get(Calendar.DAY_OF_MONTH)){
            age--;
        }
        return age;
    }

    private void initIds() {
        findViewById(R.id.img_back_bday).setOnClickListener(this);
        findViewById(R.id.btn_next_bday).setOnClickListener(this);
        datepicker_bday = findViewById(R.id.datepicker_bday);
        datepicker_bday.setMaxDate(System.currentTimeMillis() - 1000);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back_bday:
                onBackPressed();
                break;

            case R.id.btn_next_bday:
//                setDateDialog();
                dateOfBirth = datepicker_bday.getYear() + "-" + (datepicker_bday.getMonth() + 1) + "-" + datepicker_bday.getDayOfMonth();
                registerUser(dateOfBirth);
                break;
        }
    }

    private void registerUser(String dateOfBirth) {
//        String type = App.getSingleton().getRegisterType();
//        String emailPhone = App.getSingleton().getEmailPhone();
//        String password = App.getSingleton().getPassword();
        String dob = dateOfBirth;
//        String reg_id = FirebaseInstanceId.getInstance().getToken();
        String reg_id ="re";
        String deviceType = "Android";

        loginRegisterMvvm.registerUser(WhatsBirthdayActivity.this, password, dob, email, countryCode, phone, username, reg_id, deviceType).observe(WhatsBirthdayActivity.this, new Observer<ModelLoginRegister>() {
            @Override
            public void onChanged(ModelLoginRegister modelLoginRegister) {
                if (modelLoginRegister.getSuccess().equalsIgnoreCase("1")) {
                    Toast.makeText(WhatsBirthdayActivity.this, modelLoginRegister.getMessage(), Toast.LENGTH_SHORT).show();
//                    SQLiteDatabaseHandler sqLiteDatabaseHandler=new SQLiteDatabaseHandler(WhatsBirthdayActivity.this);
//                    sqLiteDatabaseHandler.addModel(modelLoginRegister.getDetails());

                    App.getSharedpref().saveModel(AppConstants.REGISTER_LOGIN_DATA, modelLoginRegister);
                    App.getSharedpref().saveString(AppConstants.LOGIN_STATUS, "1");
                    App.getSharedpref().login(WhatsBirthdayActivity.this, true);
                    startActivity(new Intent(WhatsBirthdayActivity.this, HomeActivity.class));
                    Log.i("model", modelLoginRegister.toString());
                    finishAffinity();
                } else {
                    Toast.makeText(WhatsBirthdayActivity.this, modelLoginRegister.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setDateDialog() {

//        Rect displayRectangle = new Rect();
//        Window window = getWindow();
//        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
//        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomAlertDialog);
//        ViewGroup viewGroup = findViewById(android.R.id.content);
//        View dialogView = LayoutInflater.from(this).inflate(R.layout.date_picker, viewGroup, false);
//        dialogView.setMinimumWidth((int) (displayRectangle.width() * 0.5f));
//        dialogView.setMinimumHeight((int) (displayRectangle.height() * 0.5f));
//        builder.setView(dialogView);
//        final AlertDialog alertDialog = builder.create();
//        Button btnSetDate, btnCancel;
//// picker1 = dialogView.findViewById(R.id.date_picker_actions);
//        btnSetDate = dialogView.findViewById(R.id.btnSetDate);
//        btnCancel = dialogView.findViewById(R.id.btnCancel);

        DatePicker date_picker =findViewById(R.id.date_picker_actions);

//        btnSetDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                alertDialog.dismiss();
                d = date_picker.getDayOfMonth();
                m= date_picker.getMonth() + 1;
                y = date_picker.getYear();
                if (m < 10) {
                    sMonth = "0" + (m);
                } else {
                    sMonth = String.valueOf((m));
                }

                if (d < 10) {
                    mdDate = "0" + String.valueOf(d);
                } else {
                    mdDate = String.valueOf(d);
                }
                setDOB(y + "-" + (sMonth) + "-" + mdDate);
//            }
//        });
//        btnCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                alertDialog.dismiss();
//
//            }
//        });

//        alertDialog.show();
    }

    private void setDOB(String date) {
//        dob.setText(date);
        dateOfBirth = date;
        if (!dateOfBirth.equalsIgnoreCase("")){
//            dateOfBirth = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
//            registerUser();
        }
        else {
            Toast.makeText(this, "Please select DOB first", Toast.LENGTH_SHORT).show();
        }

    }
}
