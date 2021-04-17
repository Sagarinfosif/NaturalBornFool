package in.pureway.cinemaflix.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.activity.videoEditor.activity.GallerySelectedVideoActivity;
import in.pureway.cinemaflix.activity.videoEditor.activity.PlayerActivity;
import in.pureway.cinemaflix.activity.videoEditor.util.Variables;
import in.pureway.cinemaflix.fragments.homefrags.NotificationsFragment;
import in.pureway.cinemaflix.fragments.homefrags.ProfileHomeFragment;
import in.pureway.cinemaflix.fragments.homefrags.SearchHomeFragment;
import in.pureway.cinemaflix.fragments.homefrags.VideoHomeFragment;
import in.pureway.cinemaflix.javaClasses.FacebookLogin;
import in.pureway.cinemaflix.javaClasses.GoogleLogin;
import in.pureway.cinemaflix.models.ModelComments;
import in.pureway.cinemaflix.utils.App;
import in.pureway.cinemaflix.utils.AppConstants;
import in.pureway.cinemaflix.utils.CommonUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static in.pureway.cinemaflix.activity.videoEditor.util.Variables.Pick_video_from_gallery;

public class HomeActivity extends AppCompatActivity implements  View.OnClickListener {
    BottomNavigationView bottom_navi;

    private List<ModelComments.Detail> list = new ArrayList<>();
    private int visibility = FRAGMENT_HOME;
    public static final int FRAGMENT_HOME = 1;
    public static final int FRAGMENT_SEARCH = 2;
    public static final int FRAGMENT_EDIT = 3;
    public static final int FRAGMENT_NOTIFICATIONS = 4;
    public static final int FRAGMENT_PROFILE = 5;
    GoogleLogin googleLogin;
    private static final int RC_SIGN_IN = 007;
    CallbackManager callbackManager;
    FacebookLogin facebookLogin;
    private ImageView navi_home, navi_search, navi_video, navi_noti, navi_profile;
    private VideoHomeFragment videoHomeFragment;
    private SearchHomeFragment searchHomeFragment;
    private NotificationsFragment notificationsFragment;
    private ProfileHomeFragment profileHomeFragment;
    private String VIDEO_HOME_TAG = "video_home_tag";
    private String NOTIFICATIONS = "notifications";
    private String SEARCH = "search";
    private String PROFILE = "profile";
    boolean doubleBackToExitPressedOnce = false;
    private String selectedVideoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        CommonUtils.hideNavigation(HomeActivity.this);
        CommonUtils.changeLanguage(HomeActivity.this);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_home);

        visibility = FRAGMENT_HOME;
//        File file = new File(Variables.app_folder, "logo.png");
//        if (!file.exists()) {
//
//
//            CommonUtils.downloadLogo(this, AppConstants.WATERMARK);
//        }
        initIds();
        videoUploadCheck();
//        App.getAppContext().setCommentsInterface(this);
        callbackManager = CallbackManager.Factory.create();
    }

    private void videoUploadCheck() {
        if (getIntent().getExtras() != null) {
            if (getIntent().hasExtra(AppConstants.LOGIN_TYPE)) {
                switch (getIntent().getExtras().getInt(AppConstants.LOGIN_TYPE)) {
                    case AppConstants.LOGIN_VIDEO:
                        changeColor(navi_home, navi_noti, navi_profile, navi_search);
                        loadfrag(getVideoHomeFragment());
                        visibility = FRAGMENT_HOME;
                        break;

                    case AppConstants.LOGIN_SEARCH:
                        changeColor(navi_search, navi_noti, navi_home, navi_profile);
                        visibility = FRAGMENT_SEARCH;
                        loadfrag(getSearchHomeFragment());
                        break;

                    case AppConstants.LOGIN_NOTI:
                        changeColor(navi_noti, navi_profile, navi_home, navi_search);
                        loadfrag(getNotificationsFragment());
                        visibility = FRAGMENT_NOTIFICATIONS;
                        break;

                    case AppConstants.LOGIN_PROFILE:
                        changeColor(navi_profile, navi_home, navi_home, navi_search);
                        loadfrag(getProfileHomeFragment());
                        visibility = FRAGMENT_PROFILE;
                        break;

                    case AppConstants.LOGIN_OTHER_USER:
                        startActivity(new Intent(HomeActivity.this, OtherUserProfileActivity.class).putExtra(AppConstants.OTHER_USER_ID, App.getSingleton().getOtherUserId()));
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;
                }
            } else if (getIntent().hasExtra("fragment")) {
                String extraString = getIntent().getExtras().getString("fragment");
                if (extraString.equalsIgnoreCase(AppConstants.VIDEO_POST)) {
                    loadfrag(getProfileHomeFragment());
                    changeColor(navi_profile, navi_home, navi_home, navi_search);
                    visibility = FRAGMENT_PROFILE;
                } else if (extraString.equals(AppConstants.NOTIFICATION)) {
                    changeColor(navi_noti, navi_profile, navi_home, navi_search);
                    loadfrag(getNotificationsFragment());
                    visibility = FRAGMENT_NOTIFICATIONS;
                } else {
                    changeColor(navi_home, navi_noti, navi_profile, navi_search);
                    loadfrag(getVideoHomeFragment());
                    visibility = FRAGMENT_HOME;
                }
            }
        } else {
            changeColor(navi_home, navi_noti, navi_profile, navi_search);
            loadfrag(getVideoHomeFragment());
            visibility = FRAGMENT_HOME;
        }
    }

    private void initIds() {
        navi_profile = findViewById(R.id.navi_profile);
        navi_noti = findViewById(R.id.navi_noti);
        navi_search = findViewById(R.id.navi_search);
        navi_home = findViewById(R.id.navi_home);
        navi_video = findViewById(R.id.navi_video);
        navi_profile.setOnClickListener(this);
        navi_noti.setOnClickListener(this);
        navi_search.setOnClickListener(this);
        navi_home.setOnClickListener(this);
        navi_video.setOnClickListener(this);
//        bottom_navi = findViewById(R.id.bottom_navi);
//        bottom_navi.setOnNavigationItemSelectedListener(this);
    }

    private void loadfrag(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frag_cont_home, fragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
    }

//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//        switch (menuItem.getItemId()) {
//            case R.id.menu_home:
//                visibility = FRAGMENT_HOME;
//                loadfrag(getVideoHomeFragment(), VIDEO_HOME_TAG);
//                break;
//
//            case R.id.menu_search:
//                visibility = FRAGMENT_SEARCH;
//                loadfrag(getSearchHomeFragment(), SEARCH);
//                break;
//
//            case R.id.menu_Video:
//                visibility = FRAGMENT_EDIT;
//                if (App.getSharedpref().isLogin(HomeActivity.this)) {
//                    startActivity(new Intent(HomeActivity.this, VideoRecordActivity.class));
//                } else {
//                    Toast.makeText(this, "Please Login First", Toast.LENGTH_SHORT).show();
//                }
//                break;
//
//            case R.id.menu_notifi:
//                visibility = FRAGMENT_NOTIFICATIONS;
//                loadfrag(getNotificationsFragment(), NOTIFICATIONS);
//                break;
//
//            case R.id.menu_profile:
//                visibility = FRAGMENT_PROFILE;
//                loadfrag(getProfileHomeFragment(), PROFILE);
//                break;
//        }
//        return true;
//    }

    @Override
    public void onBackPressed() {
        switch (visibility) {
            case FRAGMENT_HOME:
                super.onBackPressed();
                break;

            case FRAGMENT_SEARCH:
            case FRAGMENT_EDIT:
            case FRAGMENT_NOTIFICATIONS:
            case FRAGMENT_PROFILE:
                visibility = FRAGMENT_HOME;
//                bottom_navi.setSelectedItemId(R.id.menu_home);
                changeColor(navi_home, navi_noti, navi_profile, navi_search);
                loadfrag(getVideoHomeFragment());
                break;

            default:
//                super.onBackPressed();
                break;


        }
//        if (doubleBackToExitPressedOnce) {
//            super.onBackPressed();
//            finish();
//            return;
//        }
//
//        this.doubleBackToExitPressedOnce = true;
//        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
//
//        new Handler().postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                doubleBackToExitPressedOnce=false;
//
//
////                finish();
//
//            }
//        }, 2000);

//        if (doubleBackToExitPressedOnce=true){
//            finish();
//        }
    }

    private void changeColor(ImageView selectedImage, ImageView unselectedImage1, ImageView unselectedImage2, ImageView unselectedImage3) {
        selectedImage.setColorFilter(ContextCompat.getColor(HomeActivity.this, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
        unselectedImage1.setColorFilter(ContextCompat.getColor(HomeActivity.this, R.color.darkgrey), android.graphics.PorterDuff.Mode.SRC_IN);
        unselectedImage2.setColorFilter(ContextCompat.getColor(HomeActivity.this, R.color.darkgrey), android.graphics.PorterDuff.Mode.SRC_IN);
        unselectedImage3.setColorFilter(ContextCompat.getColor(HomeActivity.this, R.color.darkgrey), android.graphics.PorterDuff.Mode.SRC_IN);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("code", "onActivityResult() called with: requestCode = [" + requestCode + "], resultCode = [" + resultCode + "], data = [" + data + "]");

        if (resultCode == RESULT_OK){
            if (requestCode == RC_SIGN_IN) {
                googleLogin = new GoogleLogin(HomeActivity.this, App.getSingleton().getLoginType());
                googleLogin.activityResult(requestCode, resultCode, data);
            }
            else if (requestCode == Pick_video_from_gallery) {
                Uri selectedVideoUri = data.getData();
                String filemanagerstring = selectedVideoUri.toString();
                String vidPath = filemanagerstring;
//                selectedVideoPath = getPath(selectedVideoUri);
                selectedVideoPath = getRealPathFromURI_BelowAPI11(HomeActivity.this,selectedVideoUri);



//                if (selectedVideoPath!=null){
//                    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//                    retriever.setDataSource(selectedVideoPath);
//                    int width = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
//                    int height = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
//                    retriever.release();

                    App.getSingleton().setGalleryStatus("1");
//                    App.getSingleton().setIsWidth(String.valueOf(width));
//                    App.getSingleton().setIsHeight(String.valueOf(height));

                    startActivity(new Intent(HomeActivity.this, GallerySelectedVideoActivity.class).putExtra(AppConstants.INTENT_URI1, selectedVideoPath));
//                }else {
//                    Toast.makeText(this, "video path null...", Toast.LENGTH_SHORT).show();
//                }


            }
            else {
                FacebookLogin.callbackManager.onActivityResult(requestCode, resultCode, data);
            }
        }

    }



//    public void dismissKeyboard() {
//        EditText fragmentSearch=null;
//        if (getSearchHomeFragment().getEt_search() != null) {
//            fragmentSearch = getSearchHomeFragment().getEt_search();
//        }
//    }
//
//    public boolean isSoftKeyboardShown(InputMethodManager imm, View v) {
//
//        IMMResult result = new IMMResult();
//        int res;
//
//        imm.showSoftInput(v, 0, result);
//
//        // if keyboard doesn't change, handle the keypress
//        res = result.getResult();
//        if (res == InputMethodManager.RESULT_UNCHANGED_SHOWN ||
//                res == InputMethodManager.RESULT_UNCHANGED_HIDDEN) {
//
//            return true;
//        }
//        else
//            return false;
//
//    }
//
//    public class IMMResult extends ResultReceiver {
//        public int result = -1;
//        public IMMResult() {
//            super(null);
//        }
//
//        @Override
//        public void onReceiveResult(int r, Bundle data) {
//            result = r;
//        }
//
//        // poll result value for up to 500 milliseconds
//        public int getResult() {
//            try {
//                int sleep = 0;
//                while (result == -1 && sleep < 500) {
//                    Thread.sleep(100);
//                    sleep += 100;
//                }
//            } catch (InterruptedException e) {
//                Log.e("IMMResult", e.getMessage());
//            }
//            return result;
//        }
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.navi_profile:

                if (App.getSharedpref().isLogin(this)) {

                    changeColor(navi_profile, navi_home, navi_noti, navi_search);
                    visibility = FRAGMENT_PROFILE;
                    loadfrag(getProfileHomeFragment());
                }
                else {
                    visibility = FRAGMENT_PROFILE;

                    changeColor(navi_profile, navi_home, navi_noti, navi_search);
                    loadfrag(getNotificationsFragment());

                }

                break;


            case R.id.navi_noti:
                changeColor(navi_noti, navi_home, navi_profile, navi_search);
                visibility = FRAGMENT_NOTIFICATIONS;
                loadfrag(getNotificationsFragment());
                break;

            case R.id.navi_video:

                visibility = FRAGMENT_EDIT;

//                startActivity(new Intent(HomeActivity.this, VideoRecordActivity.class));
//                startActivity(new Intent(HomeActivity.this, CameraActivity.class));
//                startActivity(new Intent(HomeActivity.this, MyVideoEditorActivity.class));
//                startActivity(new Intent(HomeActivity.this, Video_Recoder_A.class));
//                startActivity(new Intent(HomeActivity.this, GalleryActivity.class));
                if (App.getSharedpref().isLogin(this)) {
                    Pick_from_gallery();

                }else {
                    loadfrag(getNotificationsFragment());

                }

                break;

            case R.id.navi_search:
                changeColor(navi_search, navi_noti, navi_home, navi_profile);
                visibility = FRAGMENT_SEARCH;
                loadfrag(getSearchHomeFragment());
                break;

            case R.id.navi_home:
                changeColor(navi_home, navi_noti, navi_profile, navi_search);
                visibility = FRAGMENT_HOME;
                loadfrag(getVideoHomeFragment());
                break;
        }
    }

    private void Pick_from_gallery() {

        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        intent.setType("video/*");
        startActivityForResult(Intent.createChooser(intent, "Please pick a video"), Variables.Pick_video_from_gallery);


    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    private VideoHomeFragment getVideoHomeFragment() {
        if (videoHomeFragment == null) {
            videoHomeFragment = new VideoHomeFragment();
        }
//        else {
//            videoHomeFragment=(VideoHomeFragment) getSupportFragmentManager().findFragmentByTag(VIDEO_HOME_TAG);
//        }
        return videoHomeFragment;
    }

    private SearchHomeFragment getSearchHomeFragment() {
        if (searchHomeFragment == null) {
            searchHomeFragment = new SearchHomeFragment();
        }
//        else {
//            searchHomeFragment=(SearchHomeFragment) getSupportFragmentManager().findFragmentByTag(SEARCH);
//        }
        return searchHomeFragment;
    }

    private NotificationsFragment getNotificationsFragment() {
        if (notificationsFragment == null) {
            notificationsFragment = new NotificationsFragment();
        }
//        else {
//            notificationsFragment=(NotificationsFragment) getSupportFragmentManager().findFragmentByTag(NOTIFICATIONS);
//        }
        return notificationsFragment;
    }

    private ProfileHomeFragment getProfileHomeFragment() {
        if (profileHomeFragment == null) {
            profileHomeFragment = new ProfileHomeFragment();
        }
//        else {
//            profileHomeFragment=(ProfileHomeFragment) getSupportFragmentManager().findFragmentByTag(PROFILE);
//        }
        return profileHomeFragment;
    }


    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Video.Media.DATA};
        getContentResolver();
        Cursor cursor = getApplicationContext().getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

    public String getRealPathFromURI_BelowAPI11(Context context, Uri contentUri){
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        int column_index
                = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
}