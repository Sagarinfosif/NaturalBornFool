package in.pureway.cinemaflix.activity.privacysettings;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blikoon.qrcodescanner.QrCodeActivity;
import com.bumptech.glide.Glide;
import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.activity.OtherUserProfileActivity;
import in.pureway.cinemaflix.models.ModelLoginRegister;
import in.pureway.cinemaflix.utils.App;
import in.pureway.cinemaflix.utils.AppConstants;
import in.pureway.cinemaflix.utils.CommonUtils;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class CodeActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_name_code, tv_save_qr, tv_scan_code;
    private Bitmap bitmap;
    private ImageView img_qr_code;
    private static final int REQUEST_CODE_QR_SCAN = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.changeLanguage(CodeActivity.this);
        setContentView(R.layout.activity_code);

        findIds();
        showMyQR();
    }

    private void findIds() {
        tv_scan_code = findViewById(R.id.tv_scan_code);
        tv_save_qr = findViewById(R.id.tv_save_qr);
        img_qr_code = findViewById(R.id.img_qr_code);
        tv_name_code = findViewById(R.id.tv_name_code);
        tv_save_qr.setOnClickListener(this);
        tv_scan_code.setOnClickListener(this);
    }

    public void backPress(View view) {
        onBackPressed();
    }

    @Override
    protected void onResume() {
        String name = App.getSharedpref().getModel(AppConstants.REGISTER_LOGIN_DATA, ModelLoginRegister.class).getDetails().getName();
        String username = App.getSharedpref().getModel(AppConstants.REGISTER_LOGIN_DATA, ModelLoginRegister.class).getDetails().getUsername();

        if (name.equalsIgnoreCase("")) {
            tv_name_code.setText(username);
        } else {
            tv_name_code.setText(name);
        }

        super.onResume();
    }

    private void showMyQR() {
        String userId = CommonUtils.userId(CodeActivity.this)
                + ","
                + App.getSharedpref().getModel(AppConstants.REGISTER_LOGIN_DATA,ModelLoginRegister.class).getDetails().getUsername();
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        int smallerDimension = width < height ? width : height;
        smallerDimension = smallerDimension * 3 / 4;

        QRGEncoder qrgEncoder = new QRGEncoder(userId, null, QRGContents.Type.TEXT, smallerDimension);
        try {
            // Getting QR-Code as Bitmap
            bitmap = qrgEncoder.getBitmap();
            Glide.with(CodeActivity.this).load(bitmap).into(img_qr_code);
        } catch (Exception e) {
            Log.v("Exception", e.toString());
        }
    }

    private void saveQRCode() {
        String username = App.getSharedpref().getModel(AppConstants.REGISTER_LOGIN_DATA, ModelLoginRegister.class).getDetails().getUsername();
        MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, username + "QrCode", username);
        Toast.makeText(this, "Image Saved in images", Toast.LENGTH_SHORT).show();
        Log.i("saveQr", Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "QrCode", username)).toString());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_save_qr:
                saveQRCode();
                break;

            case R.id.tv_scan_code:
                openScanner();
                break;
        }
    }

    private void openScanner() {
        Intent scannerActivity = new Intent(CodeActivity.this, QrCodeActivity.class);
        startActivityForResult(scannerActivity, REQUEST_CODE_QR_SCAN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_QR_SCAN) {
            if (data == null) {
                try {
                    Toast.makeText(this, "Error reading QR Code.", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Log.i("scannedResult", e.getMessage());
                }
            } else {
//                  Getting the passed result
                String result = data.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult");
                String[] scannedResult=TextUtils.split(result,",");
                Log.i("scannedResult",scannedResult[0]+" " + scannedResult[1]);
                startActivity(new Intent(CodeActivity.this, OtherUserProfileActivity.class).putExtra(AppConstants.OTHER_USER_ID,scannedResult[0]));
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            }
        }
    }
}





