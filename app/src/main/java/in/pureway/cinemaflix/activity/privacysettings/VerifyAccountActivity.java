package in.pureway.cinemaflix.activity.privacysettings;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.activity.HomeActivity;
import in.pureway.cinemaflix.mvvm.ProfileMvvm;
import in.pureway.cinemaflix.utils.CommonUtils;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class VerifyAccountActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et_aadharPan;
    private ImageView aadharImage;
    private Button btn_verify;
    private String imagepath = "", aadharPanNumber, type = "0";
    private ProfileMvvm profileMvvm;
    private RadioButton panRadio, aadharRadio;
    private int length = 12;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_account);
        profileMvvm = ViewModelProviders.of(VerifyAccountActivity.this).get(ProfileMvvm.class);

        findIds();
    }

    private void findIds() {
        et_aadharPan = findViewById(R.id.et_aadharPan);
        aadharImage = findViewById(R.id.aadharImage);
        btn_verify = findViewById(R.id.btn_verify);
        aadharRadio = findViewById(R.id.aadharRadio);
        panRadio = findViewById(R.id.panRadio);
        aadharImage.setOnClickListener(this);
        btn_verify.setOnClickListener(this);
        aadharRadio.setOnClickListener(this);
        panRadio.setOnClickListener(this);
        aadharRadio.setChecked(true);
    }

    public void backPress(View view) {
        onBackPressed();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.aadharImage:
                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(this);
                break;

            case R.id.btn_verify:
                validate();
                break;

            case R.id.aadharRadio:
                et_aadharPan.setText("");
                length = 12;
                setEditTextMaxLength(et_aadharPan, length);
                type = "0";
                break;

            case R.id.panRadio:
                et_aadharPan.setText("");
                length = 10;
                setEditTextMaxLength(et_aadharPan, length);
                type = "1";
                break;
        }
    }

    private void validate() {
        aadharPanNumber = et_aadharPan.getText().toString().trim();

        if (aadharPanNumber.length() == length) {
//            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
            if (!imagepath.equalsIgnoreCase("")) {
                verifyApi();
            } else {
                if (aadharRadio.isChecked()) {
                    Toast.makeText(this, "Please Upload Aadhar Image", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Please Upload PAN Image", Toast.LENGTH_SHORT).show();
                }
            }

        } else {
            if (aadharRadio.isChecked()) {
                Toast.makeText(this, "Please Enter Valid Aadhar Number", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Please Enter Valid PAN Number", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void setEditTextMaxLength(EditText et, int maxLength) {
        et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
    }

    private void verifyApi() {
        RequestBody rb_userId = RequestBody.create(MediaType.parse("text/plain"), CommonUtils.userId(VerifyAccountActivity.this));
        RequestBody rb_type = RequestBody.create(MediaType.parse("text/plain"), type);
        RequestBody rb_aadharNumber = RequestBody.create(MediaType.parse("text/plain"), aadharPanNumber);
        MultipartBody.Part rb_image;
        // Toast.makeText(this, imagepath, Toast.LENGTH_SHORT).show();
        if (!imagepath.isEmpty()) {
            File file = new File(imagepath);
            final RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            rb_image = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        } else {
            final RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), "");
            rb_image = MultipartBody.Part.createFormData("image", "", requestFile);
        }

        profileMvvm.aadharVerify(this, rb_userId, rb_type, rb_aadharNumber, rb_image).observe(this, new Observer<Map>() {
            @Override
            public void onChanged(Map map) {
                if (map.get("success").equals("")){
                    verifyDialog();
                }
                else{
                    Toast.makeText(VerifyAccountActivity.this, map.get("message").toString(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    Uri resultUri = result.getUri();
                    imagepath = resultUri.getPath();
                    if (!imagepath.equalsIgnoreCase("")) {
                        Glide.with(this).load(imagepath).into(aadharImage);
                    }
                } else {
                    Toast.makeText(this, "Try Again", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    private void verifyDialog() {
        Dialog dialog = new Dialog(VerifyAccountActivity.this);
        dialog.setContentView(R.layout.verify_dialog);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);

        Button btn_ok = dialog.findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VerifyAccountActivity.this, HomeActivity.class));
                finishAffinity();
            }
        });
        dialog.show();
    }
}