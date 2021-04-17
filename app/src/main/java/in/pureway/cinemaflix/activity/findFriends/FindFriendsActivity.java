package in.pureway.cinemaflix.activity.findFriends;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.activity.privacysettings.AddPhoneActivity;
import in.pureway.cinemaflix.models.ModelLoginRegister;
import in.pureway.cinemaflix.utils.App;
import in.pureway.cinemaflix.utils.AppConstants;
import in.pureway.cinemaflix.utils.CommonUtils;

public class FindFriendsActivity extends AppCompatActivity {

    private Activity activity;
    private int type = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.changeLanguage(FindFriendsActivity.this);
        setContentView(R.layout.activity_find_friends);
        activity = FindFriendsActivity.this;
//        findIds();
//    }
//
//    private void findIds() {
//        findViewById(R.id.rl_find_contact).setOnClickListener(this);
//        findViewById(R.id.rl_invite_friends).setOnClickListener(this);
//        findViewById(R.id.et_search_friends).setOnClickListener(this);
//        findViewById(R.id.img_back_find).setOnClickListener(this);
//        findViewById(R.id.rl_fb_friend).setOnClickListener(this);
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.rl_fb_friend:
//                startActivity(new Intent(FindFriendsActivity.this, FindFacebookFriendsActivity.class));
//                break;
//
//            case R.id.rl_find_contact:
//                type = 1;
//                permissions();
//                break;
//
//            case R.id.img_back_find:
//                onBackPressed();
//                break;
//
//            case R.id.rl_invite_friends:
//                type = 2;
//                permissions();
//                break;
//
//            case R.id.et_search_friends:
//                startActivity(new Intent(FindFriendsActivity.this, SearchFriendsActivity.class));
//                break;
//        }
//    }
//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
//    }
//
//    private void permissions() {
//        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(activity, new String[]{
//                    Manifest.permission.READ_CONTACTS}, 1001);
//            return;
//        } else {
//            if (!App.getSharedpref().getModel(AppConstants.REGISTER_LOGIN_DATA, ModelLoginRegister.class).getDetails().getPhone().equalsIgnoreCase("")) {
//                Intent intent = null;
//                if (type == 1) {
//                    intent = new Intent(FindFriendsActivity.this, FindContactActivity.class);
//                } else {
//                    intent = new Intent(FindFriendsActivity.this, InviteFriendsActivity.class);
//                }
//                startActivity(intent);
//            } else {
//                addPhoneDialog();
//                Toast.makeText(activity, "Please add phone number in settings.", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//    private void addPhoneDialog() {
//        final AlertDialog.Builder al = new AlertDialog.Builder(this, R.style.dialogStyle);
//        al.setTitle("Add Phone Number").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                startActivity(new Intent(FindFriendsActivity.this, AddPhoneActivity.class).putExtra(AppConstants.PHONE_ADD_TYPE, AppConstants.PHONE_TYPE_CONTACTS));
//            }
//        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        }).setMessage("Please add your phone number");
//        al.show();
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
//                } else if ((Build.VERSION.SDK_INT > 23 && !shouldShowRequestPermissionRationale(permissions[0]))) {
////                    rationale();
//                } else {
//                    Toast.makeText(this, "Permission Not granted", Toast.LENGTH_SHORT).show();
//                    permissions();
//                }
//            }
//        }
    }
}
