package in.pureway.cinemaflix.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.activity.login_register.LanguageSplashActivity;
import in.pureway.cinemaflix.utils.App;
import in.pureway.cinemaflix.utils.AppConstants;
import in.pureway.cinemaflix.utils.CommonUtils;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import render.animations.Render;

public class SplashActivity extends AppCompatActivity {

    private SplashActivity activity;
    Map<String, String> map = new HashMap<>();
    private LinearLayout img_logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.hideNavigation(SplashActivity.this);
        setContentView(R.layout.activity_splash);
        activity = SplashActivity.this;
        img_logo = findViewById(R.id.img_logo);
//        printHashKey(SplashActivity.this);



        File directory = new File(Environment.getExternalStorageDirectory(), "Cinemaflix");
        if (directory.exists()) {
            Log.i("rootFolder", "exists");
            Log.i("rootFolder", directory.toString());
        } else {
            directory.mkdir();   // make directory may want to check return value
            Log.i("rootFolder", "root Created");
            Log.i("rootFolder", directory.toString());
        }
        createSubdirectory();
        createViewVideoDirectory();
        createSaveVideo();

    }

    private void createViewVideoDirectory() {
        File subDirectory = new File(Environment.getExternalStorageDirectory(), "Cinemaflix/ViewVideo");
        if (subDirectory.exists()) {
            Log.i("viewVideo", "Subdirectory exists");
            Log.i("viewVideo", subDirectory.toString());

        } else {
            subDirectory.mkdirs();
            Log.i("viewVideo", "Subdirectory created");
            Log.i("viewVideo", subDirectory.toString());
        }
    }

    private void createSaveVideo() {
        File saveVideoDir = new File(Environment.getExternalStorageDirectory(), "Cinemaflix/saveVideo");
        if (saveVideoDir.exists()) {
            Log.i("viewVideo", "Subdirectory exists");
            Log.i("viewVideo", saveVideoDir.toString());

        } else {
            saveVideoDir.mkdirs();
            Log.i("viewVideo", "Subdirectory created");
            Log.i("viewVideo", saveVideoDir.toString());
        }
    }

    private void createSubdirectory() {
        File subDirectory = new File(Environment.getExternalStorageDirectory(), "Cinemaflix/Draft");
        if (subDirectory.exists()) {
            Log.i("drafts", "Subdirectory exists");
            Log.i("drafts", subDirectory.toString());

        } else {
            subDirectory.mkdirs();
            Log.i("drafts", "Subdirectory created");
            Log.i("drafts", subDirectory.toString());
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        permissions();
    }

    public static void printHashKey(Context pContext) {
        try {
            PackageInfo info = pContext.getPackageManager().getPackageInfo(pContext.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i("hashKey", "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e("myTag", "printHashKey()", e);
        } catch (Exception e) {
            Log.e("myTag", "printHashKey()", e);
        }
    }

    private void RunnableStart() {
        final Render render = new Render(SplashActivity.this);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                img_logo.setVisibility(View.VISIBLE);
//                render.setAnimation(Zoom.In(img_logo));
//                render.setDuration(1200);
//                render.start();
//            }
//        }, 1000);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (App.getSharedpref().getString(AppConstants.LOGIN_STATUS).equalsIgnoreCase("1")) {
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                }
                else {
                    if (App.getSharedpref().getBoolean(AppConstants.SELECTED_LANGUAGE)){
                        startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                    }
                    else {
                        startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                    }
//                    App.getSharedpref().clearPreferences();
//                    if (!App.getSharedpref().getString(AppConstants.SPLASHLANGUAGESKIP).equalsIgnoreCase(AppConstants.LANGUAGEONCECHECK)) {
//
//                    } else {
//
//                    }
                }
                finish();
            }
        }, 2000);
    }

    private void permissions() {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED||
                ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED||
                ActivityCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ,Manifest.permission.CAMERA
                    ,Manifest.permission.RECORD_AUDIO,
            }, 1001);
            return;
        } else {
            RunnableStart();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1001: {
                int count = 0;
                if (grantResults.length > 0)
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED)
                            count++;
                    }

                if (count == grantResults.length) {
                    permissions();
                } else if ((Build.VERSION.SDK_INT > 23 && !shouldShowRequestPermissionRationale(permissions[0])
                        && !shouldShowRequestPermissionRationale(permissions[1])&& !shouldShowRequestPermissionRationale(permissions[2])&& !shouldShowRequestPermissionRationale(permissions[3])
                )) {
                } else {
                    Toast.makeText(this, "Permission Not granted", Toast.LENGTH_SHORT).show();
                    permissions();
                }
            }
        }
    }

//    private void permissions() {
//        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
//                ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(activity, new String[]{
//                    Manifest.permission.READ_EXTERNAL_STORAGE,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
//            }, 1001);
//            return;
//        } else {
//            RunnableStart();
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case 1001: {
//                int count = 0;
//                if (grantResults.length > 0)
//                    for (int i = 0; i < grantResults.length; i++) {
//                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED)
//                            count++;
//                    }
//
//                if (count == grantResults.length) {
//                    permissions();
//                } else if ((Build.VERSION.SDK_INT > 23 && !shouldShowRequestPermissionRationale(permissions[0])
//                        && !shouldShowRequestPermissionRationale(permissions[1])
//                )) {
//                } else {
//                    Toast.makeText(this, "Permission Not granted", Toast.LENGTH_SHORT).show();
//                    permissions();
//                }
//            }
//        }
//    }
}
