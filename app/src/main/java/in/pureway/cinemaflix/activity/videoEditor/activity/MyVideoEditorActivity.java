package in.pureway.cinemaflix.activity.videoEditor.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Point;
import android.hardware.Camera;
import android.media.Image;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
//import com.google.android.exoplayer2.video.VideoListener;
//import com.seerslab.argear.exceptions.InvalidContentsException;
//import com.seerslab.argear.exceptions.NetworkException;
//import com.seerslab.argear.exceptions.SignedUrlGenerationException;
//import com.seerslab.argear.session.ARGAuth;
//import com.seerslab.argear.session.ARGContents;
//import com.seerslab.argear.session.ARGFrame;
//import com.seerslab.argear.session.ARGMedia;
//import com.seerslab.argear.session.ARGSession;
//import com.seerslab.argear.session.config.ARGCameraConfig;
//import com.seerslab.argear.session.config.ARGConfig;
//import com.seerslab.argear.session.config.ARGInferenceConfig;
import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.activity.videoEditor.api.ModelContentsResponse;
import in.pureway.cinemaflix.activity.videoEditor.camera.ReferenceCamera;
import in.pureway.cinemaflix.activity.videoEditor.camera.ReferenceCamera1;
import in.pureway.cinemaflix.activity.videoEditor.camera.ReferenceCamera2;
import in.pureway.cinemaflix.activity.videoEditor.data.BeautyItemData;
import in.pureway.cinemaflix.activity.videoEditor.fragments.BeautifyFragment;
import in.pureway.cinemaflix.activity.videoEditor.fragments.EffectsFragment;
import in.pureway.cinemaflix.activity.videoEditor.fragments.FiltersFragment;
import in.pureway.cinemaflix.activity.videoEditor.fragments.MyBulgeFragment;
import in.pureway.cinemaflix.activity.videoEditor.fragments.VirtualBackGroundFragment;
import in.pureway.cinemaflix.activity.videoEditor.network.DownloadAsyncResponse;
import in.pureway.cinemaflix.activity.videoEditor.network.DownloadAsyncTask;
import in.pureway.cinemaflix.activity.videoEditor.rendering.CameraTexture;
import in.pureway.cinemaflix.activity.videoEditor.rendering.ScreenRenderer;
import in.pureway.cinemaflix.activity.videoEditor.util.GLView;
import in.pureway.cinemaflix.activity.videoEditor.util.AppConfig;
import in.pureway.cinemaflix.activity.videoEditor.util.FileDeleteAsyncTask;
import in.pureway.cinemaflix.activity.videoEditor.util.MediaStoreUtil;
import in.pureway.cinemaflix.activity.videoEditor.util.VideoAudioFFMPEG;
import in.pureway.cinemaflix.activity.videoEditor.util.PreferenceUtil;
import in.pureway.cinemaflix.activity.videoEditor.util.Variables;
import in.pureway.cinemaflix.activity.videoEditor.viewmodel.ContentsViewModel;
import in.pureway.cinemaflix.activity.sounds.activity.SoundsActivity;
import in.pureway.cinemaflix.utils.App;
import in.pureway.cinemaflix.utils.AppConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.EnumSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyVideoEditorActivity extends AppCompatActivity {
    private static final String TAG_VIRTUAL = "tag_virtual";
    private static final String TAG_FILTERS = "tag_filters";
    private static final String TAG_BULGE = "tag_bulge";
    private static final String TAG_BEAUTIFY = "tag_beautify";
    private static final String TAG_EFFECTS = "tag_effects";
    private static final String[] ARRAY_TAGS = {TAG_VIRTUAL, TAG_FILTERS, TAG_BEAUTIFY, TAG_BULGE, TAG_EFFECTS};
    private String FRAG_TAG = "frag_tag";
    private RelativeLayout rl_bottom_video, rl_main_editor;
    private FrameLayout slot_container, camera_layout;
    private EffectsFragment effectsFragment;
    private FiltersFragment filtersFragment;
    private ToggleButton img_shoot_main;
    private TextView tv_select_sound;
    private ProgressBar progressBar;
    private static final String TAG = MyVideoEditorActivity.class.getSimpleName();
    private ReferenceCamera mCamera;
    private GLView mGlView;
    private ScreenRenderer mScreenRenderer;
    private CameraTexture mCameraTexture;
//    private ARGFrame.Ratio mScreenRatio = ARGFrame.Ratio.RATIO_FULL;
    private String mItemDownloadPath;
    private String mMediaPath;
    private String mInnerMediaPath;
    private String mVideoFilePath;
    private boolean mIsShooting = false;
    private boolean mFilterVignette = false;
    private boolean mFilterBlur = false;
    private int mFilterLevel = 100;
    private ModelContentsResponse.Category.Item mCurrentStickeritem = null;
    private boolean mHasTrigger = false;
    private ContentsViewModel mContentsViewModel;
    private int mDeviceWidth = 0;
    private int mDeviceHeight = 0;
    private TextView tv_time_lapsed, tv_30s, tv_15s;
    private int mGLViewWidth = 0;
    private int mGLViewHeight = 0;
    private VirtualBackGroundFragment virtualBackGroundFragment;
    private BeautifyFragment beautifyFragment;
    private MyBulgeFragment bulgeFragment;
    private BeautyItemData mBeautyItemData;
    private Toast mTriggerToast = null;
//    private ARGSession mARGSession;
//    private ARGMedia mARGMedia;
    String soundPath = null;
    private Spinner spinner_duration;
    private LinearLayout ll_effects_edit, ll_upload_edit, ll_sidebar_edit, ll_duration, ll_speed_side_edit;
    private long MAX_RECORDING_DURATION = 15000;
    private CountDownTimer countDownTimer;
    private ProgressBar progress_bar_video;
    private RelativeLayout rl_topbar_edit;
    private ImageView stopBtn, pauseBtn, playBtn;
    private long timeLeft, min, seconds;
    private String progressStr;
    private int REQUEST_CODE = 0;
    private String videoLengthS = "0";
    private String timeStatus = "0";
    private TextView tv_time;
    private long totoalTime = 15;
    private TextView tv_60Plus;
    private TextView tv_speed_2x, tv_speed_05, tv_reverse, tv_Normal;
    private int speed = 0;
    private boolean speedLay = false;
    private LinearLayout ll_speed_set;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_my_video_editor);
        Intent intent = getIntent();

    }}

        //
//        if (intent.hasExtra("video_duet")) {
//            isDuet = true;
//        } else {
//            isDuet = false;
//        }
//        if (isDuet) {
//            setupForDuet();
//            camera_layout = findViewById(R.id.camera_layout_duet);
//
//        }
//
//        Point realSize = new Point();
//        Display display = ((WindowManager) this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
//        display.getRealSize(realSize);
//        mDeviceWidth = realSize.x;
//        mDeviceHeight = realSize.y;
//        mGLViewWidth = realSize.x;
//        mGLViewHeight = realSize.y;
//        effectsFragment = new EffectsFragment();
//        filtersFragment = new FiltersFragment();
//        beautifyFragment = new BeautifyFragment();
//        bulgeFragment = new MyBulgeFragment();
//        virtualBackGroundFragment = new VirtualBackGroundFragment();
//        permissions();
//
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case 1001:
//                int count = 0;
//                if (grantResults.length > 0)
//                    for (int i = 0; i < grantResults.length; i++) {
//                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED)
//                            count++;
//                    }
//                if (count == grantResults.length) {
//                    permissions();
//                } else if ((Build.VERSION.SDK_INT > 23 && !shouldShowRequestPermissionRationale(permissions[0]))
//                        && !shouldShowRequestPermissionRationale(permissions[1])
//                        && !shouldShowRequestPermissionRationale(permissions[2])
//                        && !shouldShowRequestPermissionRationale(permissions[3])) {
//                } else {
//                    Toast.makeText(this, "Permission Not granted", Toast.LENGTH_SHORT).show();
//                    permissions();
//                }
//                break;
//        }
//    }
//
//    private void permissions() {
//        if (ActivityCompat.checkSelfPermission(MyVideoEditorActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
//                || ActivityCompat.checkSelfPermission(MyVideoEditorActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
//                || ActivityCompat.checkSelfPermission(MyVideoEditorActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
//                || ActivityCompat.checkSelfPermission(MyVideoEditorActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
//        ) {
//            ActivityCompat.requestPermissions(MyVideoEditorActivity.this, new String[]{
//                    Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1001);
//            return;
//        } else {
//            findIds();
//// spinnerlistener();
//            mBeautyItemData = new BeautyItemData();
//            mContentsViewModel = new ViewModelProvider(this).get(ContentsViewModel.class);
//            mContentsViewModel.getContents().observe(MyVideoEditorActivity.this, new Observer<ModelContentsResponse>() {
//                @Override
//                public void onChanged(ModelContentsResponse modelContentsResponse) {
//                    if (modelContentsResponse == null) return;
//                    setLastUpdateAt(MyVideoEditorActivity.this, modelContentsResponse.getLastUpdatedAt());
//                }
//            });
//            mItemDownloadPath = Variables.arGearEffectsFolder;
//            File file = new File(mItemDownloadPath);
//            if (!file.exists()) {
//                file.mkdirs();
//            }
//            mMediaPath = Variables.arGearMediaFolder;
//            File dir = new File(mMediaPath);
//            if (!dir.exists()) {
//                dir.mkdirs();
//                Log.i("mMediaPath", "mMediaPath:Created");
//            }
//            mInnerMediaPath = Variables.arGearVideoFolder;
//            clearTempMediaFiles();
//            if (getIntent().hasExtra(AppConstants.SOUND_NAME)) {
//                tv_select_sound.setText(getIntent().getExtras().getString(AppConstants.SOUND_NAME));
//
//            }
//            if (getIntent().hasExtra(AppConstants.SOUND_ID)) {
//                soundPath = Variables.mp3File;
//                PreparedAudio(soundPath);
//            }
//
//        }
//    }
//
//    LinearLayout duetLinearLayout;
//    ImageView selectSoundImage;
//
//    private void findIds() {
//        ll_speed_side_edit = findViewById(R.id.ll_speed_side_edit);
//
//        ll_speed_set = findViewById(R.id.ll_speed_set);
//        tv_Normal = findViewById(R.id.tv_Normal);
//        tv_60Plus = findViewById(R.id.tv_60Plus);
//        tv_60Plus = findViewById(R.id.tv_60Plus);
//        playBtn = findViewById(R.id.playBtn);
//        pauseBtn = findViewById(R.id.pauseBtn);
//        stopBtn = findViewById(R.id.stopBtn);
//
//        tv_speed_2x = findViewById(R.id.tv_speed_2x);
//        tv_speed_05 = findViewById(R.id.tv_speed_05);
//        tv_reverse = findViewById(R.id.tv_reverse);
//
//        ll_speed_side_edit.setOnClickListener(this);
//        tv_60Plus.setOnClickListener(this);
//        playBtn.setOnClickListener(this);
//        pauseBtn.setOnClickListener(this);
//        stopBtn.setOnClickListener(this);
//        ll_speed_side_edit.setOnClickListener(this);
//
//        ll_speed_side_edit.setOnClickListener(this);
//        tv_speed_05.setOnClickListener(this);
//        tv_speed_2x.setOnClickListener(this);
//        tv_reverse.setOnClickListener(this);
//
//
//        tv_time_lapsed = findViewById(R.id.tv_time_lapsed);
//        tv_15s = findViewById(R.id.tv_15s);
//        tv_30s = findViewById(R.id.tv_30s);
//        ll_effects_edit = findViewById(R.id.ll_effects_edit);
//        progress_bar_video = findViewById(R.id.progress_bar_video);
//        ll_duration = findViewById(R.id.ll_duration);
//        ll_effects_edit.setOnClickListener(this);
//        ll_upload_edit = findViewById(R.id.ll_upload_edit);
//        ll_sidebar_edit = findViewById(R.id.ll_sidebar_edit);
//        rl_topbar_edit = findViewById(R.id.rl_topbar_edit);
//        ll_upload_edit.setOnClickListener(this);
//        tv_15s.setOnClickListener(this);
//        tv_30s.setOnClickListener(this);
//        selectSoundImage = findViewById(R.id.image_select_sound);
//        tv_select_sound = findViewById(R.id.tv_select_sound);
//        duetLinearLayout = findViewById(R.id.duet_layout);
//        if (isDuet) {
//            ll_upload_edit.setVisibility(View.GONE);
//            tv_select_sound.setVisibility(View.GONE);
//            tv_15s.setVisibility(View.GONE);
//            selectSoundImage.setVisibility(View.GONE);
//            tv_30s.setVisibility(View.GONE);
//            camera_layout = findViewById(R.id.camera_layout_duet);
//            mGLViewHeight = camera_layout.getHeight();
//            mGLViewWidth = camera_layout.getWidth();
//            duetLinearLayout.setVisibility(View.VISIBLE);
//            findViewById(R.id.camera_layout).setVisibility(View.GONE);
//        } else {
//            tv_select_sound.setVisibility(View.VISIBLE);
//            ll_upload_edit.setVisibility(View.VISIBLE);
//            selectSoundImage.setVisibility(View.VISIBLE);
//
//            tv_15s.setVisibility(View.VISIBLE);
//            tv_30s.setVisibility(View.VISIBLE);
//            camera_layout = findViewById(R.id.camera_layout);
//            duetLinearLayout.setVisibility(View.GONE);
//
//        }
//        progressBar = findViewById(R.id.progressBar);
//        rl_main_editor = findViewById(R.id.rl_main_editor);
//        img_shoot_main = findViewById(R.id.img_shoot_main);
//        img_shoot_main.setOnClickListener(this);
//        tv_select_sound.setOnClickListener(this);
//        tv_Normal.setOnClickListener(this);
//        slot_container = findViewById(R.id.slot_container);
//        rl_bottom_video = findViewById(R.id.rl_bottom_video);
//        findViewById(R.id.ll_upload_edit).setOnClickListener(this);
//        findViewById(R.id.img_cancel_edit).setOnClickListener(this);
//        findViewById(R.id.ll_virtual_background).setOnClickListener(this);
//        findViewById(R.id.ll_bulge).setOnClickListener(this);
//        findViewById(R.id.ll_beautify).setOnClickListener(this);
//        findViewById(R.id.iv_rotate_camera).setOnClickListener(this);
//        findViewById(R.id.ll_effects_edit).setOnClickListener(this);
//        findViewById(R.id.ll_filter_side_edit).setOnClickListener(this);
//        App.getSingleton().setVideoType("0");
//
//
//    }
//
//    public void hideFragmnent() {
//        slot_container.setVisibility(View.GONE);
//        rl_bottom_video.setVisibility(View.VISIBLE);
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.tv_30s:
//                timeStatus = "1";
//                totoalTime = 30;
//                App.getSingleton().setVideoType("0");
//                tv_30s.setBackgroundResource(R.drawable.white_curve);
//                tv_15s.setBackgroundResource(R.drawable.circle_trans);
//                tv_60Plus.setBackgroundResource(R.drawable.circle_trans);
//                MAX_RECORDING_DURATION = 30000;
//                progress_bar_video.setMax(30000);
//                break;
//
//            case R.id.tv_15s:
//                timeStatus = "0";
//                totoalTime = 15;
//                App.getSingleton().setVideoType("0");
//
//                tv_30s.setBackgroundResource(R.drawable.circle_trans);
//                tv_15s.setBackgroundResource(R.drawable.white_curve);
//                tv_60Plus.setBackgroundResource(R.drawable.circle_trans);
//                MAX_RECORDING_DURATION = 15000;
//                progress_bar_video.setMax(15000);
//                break;
//
//
//            case R.id.tv_60Plus:
//                timeStatus = "1";
//                totoalTime = 60;
//                App.getSingleton().setVideoType("1");
//
//                tv_60Plus.setBackgroundResource(R.drawable.white_curve);
//                tv_15s.setBackgroundResource(R.drawable.circle_trans);
//                tv_30s.setBackgroundResource(R.drawable.circle_trans);
//                MAX_RECORDING_DURATION = 60000;
//                progress_bar_video.setMax(60000);
//                break;
//
//            case R.id.ll_upload_edit:
//                Pick_video_from_gallery();
//                break;
//            case R.id.img_cancel_edit:
//                onBackPressed();
//                finish();
//                break;
//            case R.id.ll_virtual_background:
//                changeFrag(new VirtualBackGroundFragment(), TAG_VIRTUAL);
//                rl_bottom_video.setVisibility(View.GONE);
//                break;
//            case R.id.tv_select_sound:
//                startActivityForResult(new Intent(MyVideoEditorActivity.this, SoundsActivity.class), 1);
//                overridePendingTransition(R.anim.in_from_bottom, R.anim.out_to_top);
//                break;
//            case R.id.img_shoot_main:
//                if (mARGMedia.isRecording()) {
//                    stopRecording();
//                } else {
//                    startRecording();
//                }
//                break;
//            case R.id.ll_bulge:
//                changeFrag(new MyBulgeFragment(), TAG_BULGE);
//                rl_bottom_video.setVisibility(View.GONE);
//                break;
//
//            case R.id.ll_beautify:
//                changeFrag(new BeautifyFragment(), TAG_BEAUTIFY);
//                rl_bottom_video.setVisibility(View.GONE);
//                break;
//            case R.id.iv_rotate_camera:
//                mARGSession.pause();
//                mCamera.changeCameraFacing();
//                mARGSession.resume();
//                break;
//            case R.id.ll_effects_edit:
//                changeFrag(new EffectsFragment(), TAG_EFFECTS);
//                rl_bottom_video.setVisibility(View.GONE);
//                break;
//
//            case R.id.ll_filter_side_edit:
//                changeFrag(new FiltersFragment(), TAG_FILTERS);
//                rl_bottom_video.setVisibility(View.GONE);
//                break;
//
//            case R.id.playBtn:
//                if (mARGMedia.isRecording()) {
//                    resumeRecording();
//                    resumeTimer();
//                } else {
//                    startRecording();
//                }
////                startRecording();
//                pauseBtn.setVisibility(View.VISIBLE);
//                stopBtn.setVisibility(View.GONE);
//                playBtn.setVisibility(View.GONE);
//                break;
//
//            case R.id.pauseBtn:
//                mARGMedia.pauseRecording();
//                pauseTimer();
//
//                pauseBtn.setVisibility(View.GONE);
//                if (!isDuet) {
//                    stopBtn.setVisibility(View.VISIBLE);
//                } else {
//                    if (playerDuet != null) {
//                        playerDuet.setPlayWhenReady(false);
//                    }
//                }
//
//                playBtn.setVisibility(View.VISIBLE);
//                break;
//
//            case R.id.stopBtn:
//                Toast.makeText(this, "Stop", Toast.LENGTH_SHORT).show();
//                stopRecording();
//                break;
//
//
//            case R.id.ll_speed_side_edit:
//
//
//                if (speedLay == false) {
//                    speedLay = true;
//
//                    ll_speed_set.setVisibility(View.VISIBLE);
//
//                } else {
//                    speedLay = false;
//
//                    ll_speed_set.setVisibility(View.GONE);
//
//                }
//                break;
//
//            case R.id.tv_speed_2x:
//                speed = 2;
//                tv_speed_2x.setBackgroundResource(R.drawable.bg_theme);
//
//
//                tv_Normal.setBackgroundResource(R.color.darkGrey);
//                tv_reverse.setBackgroundResource(R.color.darkGrey);
//                tv_speed_05.setBackgroundResource(R.color.darkGrey);
////                tv_speed_2x.setTextColor(getColor(R.color.purple));
//                tv_reverse.setTextColor(getColor(R.color.white));
//                tv_speed_05.setTextColor(getColor(R.color.white));
//                break;
//
//
//            case R.id.tv_speed_05:
//                speed = 1;
//                tv_speed_05.setBackgroundResource(R.drawable.bg_theme);
//
//
//                tv_Normal.setBackgroundResource(R.color.darkGrey);
//                tv_speed_2x.setBackgroundResource(R.color.darkGrey);
//                tv_reverse.setBackgroundResource(R.color.darkGrey);
////                tv_speed_05.setTextColor(getColor(R.color.purple));
//                tv_reverse.setTextColor(getColor(R.color.white));
//                tv_speed_2x.setTextColor(getColor(R.color.white));
//                ll_speed_side_edit.setVisibility(View.VISIBLE);
//                break;
//
//
//            case R.id.tv_reverse:
//                speed = 3;
//                tv_reverse.setBackgroundResource(R.drawable.bg_theme);
//
//                tv_Normal.setBackgroundResource(R.color.darkGrey);
//                tv_speed_2x.setBackgroundResource(R.color.darkGrey);
//                tv_speed_05.setBackgroundResource(R.color.darkGrey);
//
//                tv_Normal.setTextColor(getColor(R.color.white));
//                tv_speed_2x.setTextColor(getColor(R.color.white));
//                tv_speed_05.setTextColor(getColor(R.color.white));
////                tv_reverse.setTextColor(getColor(R.color.purple));
//                ll_speed_side_edit.setVisibility(View.VISIBLE);
//                break;
//            case R.id.tv_Normal:
//                speed = 0;
//                tv_Normal.setBackgroundResource(R.drawable.bg_theme);
//
//
//                tv_reverse.setBackgroundResource(R.color.darkGrey);
//                tv_speed_2x.setBackgroundResource(R.color.darkGrey);
//                tv_speed_05.setBackgroundResource(R.color.darkGrey);
//
//                tv_speed_2x.setTextColor(getColor(R.color.white));
//                tv_speed_05.setTextColor(getColor(R.color.white));
////                tv_reverse.setTextColor(getColor(R.color.purple));
//                ll_speed_side_edit.setVisibility(View.VISIBLE);
//                break;
//
//
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) {
//            if (requestCode == 1) {
//                if (data != null) {
//                    REQUEST_CODE = requestCode;
//                    if (data.getStringExtra("isSelected").equals("yes")) {
//                        tv_select_sound.setText(data.getExtras().getString("sound_name"));
//                        Variables.Selected_sound_id = data.getStringExtra("sound_id");
//                        String soundId = data.getStringExtra("sound_id");
//                        String soundName = data.getExtras().getString("sound_name");
//                        soundPath = Variables.app_folder + Variables.SelectedAudio_AAC;
//                        if (data.hasExtra("sound_path")) {
//                            soundPath = data.getExtras().getString("sound_path");
//                        }
//                        App.getSingleton().setSoundName(soundName);
//                        App.getSingleton().setSoundId(soundId);
//                        PreparedAudio(soundPath);
//                    }
//                }
//            } else if (requestCode == Variables.Pick_video_from_gallery) {
//                Uri selectedVideoUri = data.getData();
//// OI FILE Manager
//                String filemanagerstring = selectedVideoUri.toString();
//                String vidPath = filemanagerstring;
//// MEDIA GALLERY
//                String selectedVideoPath = getPath(selectedVideoUri);
//                if (selectedVideoPath != null) {
//                    vidPath = selectedVideoPath;
//                }
//                String videogallerypath = "";
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                    videogallerypath = mInnerMediaPath + "/" + System.currentTimeMillis() + ".mp4";
//                } else {
//                    videogallerypath = mMediaPath + "/" + System.currentTimeMillis() + ".mp4";
//                }
//                File videogallery = new File(vidPath);
//                File videoFile = new File(videogallerypath);
//                try {
//                    copyFile(videogallery, videoFile);
//                } catch (IOException e) {
//                    videogallerypath = vidPath;
//                    videoFile = videogallery;
////                    e.printStackTrace();
//                }
//                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//                retriever.setDataSource(MyVideoEditorActivity.this, Uri.fromFile(videoFile));
//                String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
//
//                long timeInMillisec = Long.parseLong(time);
//                long seconds = TimeUnit.MILLISECONDS.toSeconds(timeInMillisec);
//
//                retriever.release();
//                if (Integer.parseInt(String.valueOf(seconds)) > 60) {
//                    App.getSingleton().setVideoType("1");
//                } else {
//                    App.getSingleton().setVideoType("0");
//
//                }
//                if (Integer.parseInt(String.valueOf(seconds)) > Variables.GALLERY_SELECTED_VIDEO_LENGTH) {
//                    Toast.makeText(this, "Video should be less than 5 minutes", Toast.LENGTH_LONG).show();
//                } else {
//                    App.getSingleton().setGalleryVideo(true);
//                    videoLengthS = String.valueOf(seconds);
//
//                    App.getSingleton().setVideoLength(videoLengthS);
//
//                    startActivity(new Intent(MyVideoEditorActivity.this, GallerySelectedVideoActivity.class).putExtra(PlayerActivity.INTENT_URI, videogallerypath));
//                }
//            }
//        }
//    }
//
//    public static void copyFile(File sourceFile, File destFile) throws IOException {
//        if (!destFile.getParentFile().exists())
//            destFile.getParentFile().mkdirs();
//
//        if (!destFile.exists()) {
//            destFile.createNewFile();
//        }
//
//        FileChannel source = null;
//        FileChannel destination = null;
//
//        try {
//            source = new FileInputStream(sourceFile).getChannel();
//            destination = new FileOutputStream(destFile).getChannel();
//            destination.transferFrom(source, 0, source.size());
//        } finally {
//            if (source != null) {
//                source.close();
//            }
//            if (destination != null) {
//                destination.close();
//            }
//        }
//    }
//
//    //this function returns null when using IO file manager
//    public String getPath(Uri uri) {
//        String[] projection = {MediaStore.Video.Media.DATA};
//        getContentResolver();
//        Cursor cursor = getApplicationContext().getContentResolver().query(uri, projection, null, null, null);
//        if (cursor != null) {
//            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
//            cursor.moveToFirst();
//            return cursor.getString(column_index);
//        } else
//            return null;
//    }
//
//    MediaPlayer audio;
//
//    public void PreparedAudio(String audioPath) {
//        File file = new File(audioPath);
//        if (file.exists()) {
//            Log.i("file1making", String.valueOf(file.exists()) + " if");
//            audio = new MediaPlayer();
//            try {
//                audio.setDataSource(audioPath);
//                audio.prepare();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
//            mmr.setDataSource(this, Uri.fromFile(file));
//            String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
//        }
//    }
//
//    private void changeFrag(Fragment fragment, String tag) {
//        FRAG_TAG = tag;
//        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.setCustomAnimations(R.anim.in_from_bottom, R.anim.out_to_top);
//        fragmentTransaction.replace(R.id.slot_container, fragment, tag);
//        fragmentTransaction.commit();
//        if (slot_container.getVisibility() == View.GONE) {
//            slot_container.setVisibility(View.VISIBLE);
//        }
//    }
//
//    private void clearTempMediaFiles() {
//        new FileDeleteAsyncTask(new File(mInnerMediaPath), new FileDeleteAsyncTask.OnAsyncFileDeleteListener() {
//            @Override
//            public void processFinish(Object result) {
//                File dir = new File(mInnerMediaPath);
//                if (!dir.exists()) {
//                    boolean r = dir.mkdir();
//                    Log.e(TAG, "");
//                }
//            }
//        }).execute();
//    }
//
//    private void permissions2() {
//        if (ActivityCompat.checkSelfPermission(MyVideoEditorActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
//                || ActivityCompat.checkSelfPermission(MyVideoEditorActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
//                || ActivityCompat.checkSelfPermission(MyVideoEditorActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
//                || ActivityCompat.checkSelfPermission(MyVideoEditorActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        } else {
//            if (mARGSession == null) {
//                ARGConfig config = new ARGConfig(AppConfig.API_URL, AppConfig.API_KEY, AppConfig.SECRET_KEY, AppConfig.AUTH_KEY);
//                Set<ARGInferenceConfig.Feature> inferenceConfig = EnumSet.of(ARGInferenceConfig.Feature.FACE_HIGH_TRACKING);
//                mARGSession = new ARGSession(this, config, inferenceConfig);
//                mARGMedia = new ARGMedia(mARGSession);
//                mScreenRenderer = new ScreenRenderer();
//                mCameraTexture = new CameraTexture();
//                setBeauty(mBeautyItemData.getBeautyValues());
//                initGLView();
//                initCamera();
//                ll_sidebar_edit.setVisibility(View.VISIBLE);
//                rl_topbar_edit.setVisibility(View.VISIBLE);
//
//            }
//            if (audio != null) {
//                if (audio.getCurrentPosition() > 0) {
//                    audio.seekTo(soundDuration);
//                } else {
//                    if (soundPath != null) {
//                        PreparedAudio(soundPath);
//                    }
//                }
//            }
//            mCamera.startCamera();
//            mARGSession.resume();
//            ll_duration.setVisibility(View.VISIBLE);
//            tv_15s.setTextColor(getColor(R.color.purple));
//            MAX_RECORDING_DURATION = 15000;
//            setGLViewSize(mCamera.getPreviewSize());
//            playBtn.setVisibility(View.VISIBLE);
//            pauseBtn.setVisibility(View.GONE);
//            stopBtn.setVisibility(View.GONE);
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//
//        permissions2();
//        if (REQUEST_CODE != 0) {
//            if (REQUEST_CODE != 1) {
//                soundPath = "";
//                tv_select_sound = findViewById(R.id.tv_select_sound);
//                tv_select_sound.setText(getString(R.string.select_sound));
//                audio = null;
//            }
//        }
//
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (mARGSession != null) {
//            mCamera.stopCamera();
//            mARGSession.pause();
//            if (audio != null) {
//                if (audio.isPlaying()) {
//                    audio.pause();
//                    soundDuration = audio.getCurrentPosition();
//                }
//            }
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (mARGSession != null) {
//            mCamera.destroy();
//            mARGSession.destroy();
//
//        }
//    }
//
//    @Override
//    public void onBackPressed() {
//        if (rl_bottom_video.getVisibility() == View.GONE) {
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            Fragment fragment = fragmentManager.findFragmentByTag(FRAG_TAG);
//            fragmentTransaction.remove(fragment);
//            fragmentTransaction.commit();
//            slot_container.setVisibility(View.GONE);
//            rl_bottom_video.setVisibility(View.VISIBLE);
//            if (audio != null) {
//                audio.pause();
//            }
//        } else {
//            super.onBackPressed();
//            if (audio != null) {
//                audio.stop();
//            }
//            if (mARGMedia.isRecording()) {
//                mARGMedia.stopRecording();
//            }
//
//            App.getSingleton().setGalleryVideo(false);
//            mARGSession.destroy();
//            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//            finish();
//        }
//    }
//
//
//    private void initGLView() {
//        FrameLayout cameraLayout = findViewById(R.id.camera_layout);
//        if (isDuet) {
//            cameraLayout = findViewById(R.id.camera_layout_duet);
//        }
//        FrameLayout.LayoutParams params;
//        params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        if (isDuet) {
//            params.height = cameraLayout.getHeight();
//            params.width = cameraLayout.getWidth();
//        }
////        if(isDuet){
////            params = new FrameLayout.LayoutParams(camera_layout.getHeight(),camera_layout.getWidth());
////
////        }else{
////
////        }
//        mGlView = new GLView(this, glViewListener);
//        mGlView.setZOrderMediaOverlay(true);
//
//        cameraLayout.addView(mGlView, params);
//    }
//
//    private void initCamera() {
//        if (AppConfig.USE_CAMERA_API == 1) {
//            mCamera = new ReferenceCamera1(this, cameraListener);
//        } else {
//            mCamera = new ReferenceCamera2(this, cameraListener, getWindowManager().getDefaultDisplay().getRotation());
//        }
//    }
//
//
//    private void setGLViewSize(int[] cameraPreviewSize) {
//        int previewWidth = cameraPreviewSize[1];
//        int previewHeight = cameraPreviewSize[0];
//
//        if (!isDuet) {
//            if (mScreenRatio == ARGFrame.Ratio.RATIO_FULL) {
//                mGLViewHeight = mDeviceHeight;
//                mGLViewWidth = (int) ((float) mDeviceHeight * previewWidth / previewHeight);
//            } else {
//                mGLViewWidth = mDeviceWidth;
//                mGLViewHeight = (int) ((float) mDeviceWidth * previewHeight / previewWidth);
//            }
//        } else {
//            camera_layout = findViewById(R.id.camera_layout_duet);
//            mGLViewWidth = (int) (mDeviceWidth / 1.75);
//            final float scale = this.getResources().getDisplayMetrics().density;
//            mGLViewHeight = (int) (275 * scale);
////            mDeviceHeight = camera_layout.getHeight();
////            mDeviceWidth = camera_layout.getWidth();
////            if (mScreenRatio == ARGFrame.Ratio.RATIO_FULL) {
////                mGLViewHeight = mDeviceHeight;
////                mGLViewWidth = (int) ((float) mDeviceHeight * previewWidth / previewHeight);
////            } else {
////                mGLViewWidth = mDeviceWidth;
////                mGLViewHeight = (int) ((float) mDeviceWidth * previewHeight / previewWidth);
////            }
////            mGLViewHeight = camera_layout.getHeight();
////            mGLViewWidth = camera_layout.getWidth();
//        }
//        if (mGlView != null
//                && (mGLViewWidth != mGlView.getViewWidth() || mGLViewHeight != mGlView.getHeight())) {
//            camera_layout.removeView(mGlView);
//            mGlView.getHolder().setFixedSize(mGLViewWidth, mGLViewHeight);
//            camera_layout.addView(mGlView);
//        }
//    }
//
//    public void setMeasureSurfaceView(View view) {
//        if (view.getParent() instanceof FrameLayout) {
//            view.setLayoutParams(new FrameLayout.LayoutParams(camera_layout.getWidth(), camera_layout.getHeight()));
//        } else if (view.getParent() instanceof RelativeLayout) {
//            view.setLayoutParams(new RelativeLayout.LayoutParams(mGLViewWidth, mGLViewHeight));
//        }
//        if ((mScreenRatio == ARGFrame.Ratio.RATIO_FULL) && (mGLViewWidth > mDeviceWidth)) {
//            view.setX((mDeviceWidth - mGLViewWidth) / 2);
//        } else {
//            view.setX(0);
//        }
//    }
//
//    public int getGLViewWidth() {
//        return mGLViewWidth;
//    }
//
//    public int getGLViewHeight() {
//        return mGLViewHeight;
//    }
//
//    public BeautyItemData getBeautyItemData() {
//        return mBeautyItemData;
//    }
//
//    public void updateTriggerStatus(final int triggerstatus) {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                if (mCurrentStickeritem != null && mHasTrigger) {
//                    String strTrigger = null;
//                    if ((triggerstatus & 1) != 0) {
//                        strTrigger = "Open your mouth.";
//                    } else if ((triggerstatus & 2) != 0) {
//                        strTrigger = "Move your head side to side.";
//                    } else if ((triggerstatus & 8) != 0) {
//                        strTrigger = "Blink your eyes.";
//                    } else {
//                        if (mTriggerToast != null) {
//                            mTriggerToast.cancel();
//                            mTriggerToast = null;
//                        }
//                    }
//
//                    if (strTrigger != null) {
//                        mTriggerToast = Toast.makeText(MyVideoEditorActivity.this, strTrigger, Toast.LENGTH_SHORT);
//                        mTriggerToast.setGravity(Gravity.CENTER, 0, 0);
//                        mTriggerToast.show();
//                        mHasTrigger = false;
//                    }
//                }
//            }
//        });
//    }
//
//    public void clearBulge() {
//        mARGSession.contents().clear(ARGContents.Type.Bulge);
//    }
//
//    public void setItem(ARGContents.Type type, String path, ModelContentsResponse.Category.Item itemModel) {
//        mCurrentStickeritem = null;
//        mHasTrigger = false;
//
//        mARGSession.contents().setItem(type, path, itemModel.getUuid(), new ARGContents.Callback() {
//            @Override
//            public void onSuccess() {
//                if (type == ARGContents.Type.ARGItem) {
//                    mCurrentStickeritem = itemModel;
//                    mHasTrigger = itemModel.getHasTrigger();
//                }
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                mCurrentStickeritem = null;
//                mHasTrigger = false;
//                if (e instanceof InvalidContentsException) {
//                    Log.e(TAG, "InvalidContentsException");
//                }
//            }
//        });
//    }
//
//    public void setSticker(ModelContentsResponse.Category.Item item) {
//        String filePath = mItemDownloadPath + "/" + item.getUuid();
//        if (getLastUpdateAt(MyVideoEditorActivity.this) > getStickerUpdateAt(MyVideoEditorActivity.this, item.getUuid())) {
//            new FileDeleteAsyncTask(new File(filePath), new FileDeleteAsyncTask.OnAsyncFileDeleteListener() {
//                @Override
//                public void processFinish(Object result) {
//                    Log.d(TAG, "file delete success!");
//
//                    setStickerUpdateAt(MyVideoEditorActivity.this, item.getUuid(), getLastUpdateAt(MyVideoEditorActivity.this));
//                    requestSignedUrl(item, filePath, true);
//                }
//            }).execute();
//        } else {
//            if (new File(filePath).exists()) {
//                setItem(ARGContents.Type.ARGItem, filePath, item);
//            } else {
//                requestSignedUrl(item, filePath, true);
//            }
//        }
//    }
//
//    public void clearStickers() {
//        mCurrentStickeritem = null;
//        mHasTrigger = false;
//
//        mARGSession.contents().clear(ARGContents.Type.ARGItem);
//    }
//
//    public void setFilter(ModelContentsResponse.Category.Item item) {
//        String filePath = mItemDownloadPath + "/" + item.getUuid();
//        if (getLastUpdateAt(MyVideoEditorActivity.this) > getFilterUpdateAt(MyVideoEditorActivity.this, item.getUuid())) {
//            new FileDeleteAsyncTask(new File(filePath), new FileDeleteAsyncTask.OnAsyncFileDeleteListener() {
//                @Override
//                public void processFinish(Object result) {
//                    Log.d(TAG, "file delete success!");
//                    setFilterUpdateAt(MyVideoEditorActivity.this, item.getUuid(), getLastUpdateAt(MyVideoEditorActivity.this));
//                    requestSignedUrl(item, filePath, false);
//                }
//            }).execute();
//        } else {
//            if (new File(filePath).exists()) {
//                setItem(ARGContents.Type.FilterItem, filePath, item);
//            } else {
//                requestSignedUrl(item, filePath, false);
//            }
//        }
//    }
//
//    public void clearFilter() {
//        mARGSession.contents().clear(ARGContents.Type.FilterItem);
//    }
//
//    public void setFilterStrength(int strength) {
//        if ((mFilterLevel + strength) < 100 && (mFilterLevel + strength) > 0) {
//            mFilterLevel += strength;
//        }
//        Log.i("filter", String.valueOf(mFilterLevel));
//        mARGSession.contents().setFilterLevel(strength);
//    }
//
//    public int getFilterStrength() {
//        return mFilterLevel;
//    }
//
//    public void setVignette() {
//        mFilterVignette = !mFilterVignette;
//        mARGSession.contents().setFilterOption(ARGContents.FilterOption.VIGNETTING, mFilterVignette);
//    }
//
//    public void setBlurVignette() {
//        mFilterBlur = !mFilterBlur;
//        mARGSession.contents().setFilterOption(ARGContents.FilterOption.BLUR, mFilterBlur);
//    }
//
//    public void setBeauty(float[] params) {
//        mARGSession.contents().setBeauty(params);
//    }
//
//    public void setBulgeFunType(int type) {
//        ARGContents.BulgeType bulgeType = ARGContents.BulgeType.NONE;
//        switch (type) {
//            case 1:
//                bulgeType = ARGContents.BulgeType.FUN1;
//                break;
//            case 2:
//                bulgeType = ARGContents.BulgeType.FUN2;
//                break;
//            case 3:
//                bulgeType = ARGContents.BulgeType.FUN3;
//                break;
//            case 4:
//                bulgeType = ARGContents.BulgeType.FUN4;
//                break;
//            case 5:
//                bulgeType = ARGContents.BulgeType.FUN5;
//                break;
//            case 6:
//                bulgeType = ARGContents.BulgeType.FUN6;
//                break;
//        }
//        mARGSession.contents().setBulge(bulgeType);
//    }
//
//    private void setDrawLandmark(boolean landmark, boolean faceRect) {
//
//        EnumSet<ARGInferenceConfig.Debug> set = EnumSet.of(ARGInferenceConfig.Debug.NONE);
//
//        if (landmark) {
//            set.add(ARGInferenceConfig.Debug.FACE_LANDMARK);
//        }
//
//        if (faceRect) {
//            set.add(ARGInferenceConfig.Debug.FACE_RECT_HW);
//            set.add(ARGInferenceConfig.Debug.FACE_RECT_SW);
//            set.add(ARGInferenceConfig.Debug.FACE_AXIES);
//        }
//
//        mARGSession.setDebugInference(set);
//    }
//
//    private void startRecording() {
//        if (mCamera == null) {
//            return;
//        }
//        if (isDuet) {
//            if (playerDuet != null) {
//                playerDuet.setPlayWhenReady(true);
//            }
//        }
//
//        int bitrate = 6 * 1000 * 1000; // 10M
//        int[] previewSize = mCamera.getPreviewSize();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            mVideoFilePath = mInnerMediaPath + "/" + System.currentTimeMillis() + ".mp4";
//        } else {
//            mVideoFilePath = mMediaPath + "/" + System.currentTimeMillis() + ".mp4";
//        }
////        mARGMedia.initRecorder(mVideoFilePath, previewSize[0], previewSize[1], bitrate, false, false, false, ARGMedia.Ratio.RATIO_16_9);
//        mARGMedia.initRecorder(mVideoFilePath, 1280, 720, bitrate, false, false, false, ARGMedia.Ratio.RATIO_16_9);
//        mARGMedia.startRecording();
//
//        Toast.makeText(this, "start recording.", Toast.LENGTH_SHORT).show();
//        if (audio != null) {
//            audio.start();
//        }
//        startTimer(MAX_RECORDING_DURATION);
//
//
//        ll_speed_set.setVisibility(View.GONE);
//        ll_sidebar_edit.setVisibility(View.GONE);
//        rl_topbar_edit.setVisibility(View.GONE);
//        ll_effects_edit.setVisibility(View.GONE);
//        ll_upload_edit.setVisibility(View.GONE);
//        ll_duration.setVisibility(View.GONE);
//        tv_time_lapsed.setVisibility(View.VISIBLE);
////        progress_bar_video.setVisibility(View.VISIBLE);
//    }
//
//    private void resumeRecording() {
//        mARGMedia.resumeRecording();
//        if (isDuet && playerDuet != null) {
//            playerDuet.setPlayWhenReady(true);
//        }
//        Toast.makeText(this, "Resume recording.", Toast.LENGTH_SHORT).show();
//
//        ll_sidebar_edit.setVisibility(View.GONE);
//        rl_topbar_edit.setVisibility(View.GONE);
//        ll_effects_edit.setVisibility(View.GONE);
//        ll_upload_edit.setVisibility(View.GONE);
//        ll_duration.setVisibility(View.GONE);
//        tv_time_lapsed.setVisibility(View.VISIBLE);
////        progress_bar_video.setVisibility(View.VISIBLE);
//    }
//
//    private void startTimer(long maxTime) {
//        countDownTimer = new CountDownTimer(maxTime, 1000) {
//            public void onTick(long millisUntilFinished) {
//                timeLeft = millisUntilFinished;
//                min = (millisUntilFinished / (1000 * 60));
//                seconds = ((millisUntilFinished / 1000) - min * 60);
//                progressStr = String.valueOf(seconds);
//                progress_bar_video.setProgress(Integer.parseInt(progressStr), true);
//                tv_time_lapsed.setText("Time Remaining: " + String.valueOf(min + " minutes " + seconds) + " seconds");
//
//            }
//
//            public void onFinish() {
//                stopRecording();
//            }
//        }.start();
//    }
//
//
//    int soundDuration = 0;
//
//    private void pauseTimer() {
//        countDownTimer.cancel();
//        if (audio != null) {
//            audio.pause();
//            soundDuration = audio.getCurrentPosition();
//        }
//
//    }
//
//    private void resumeTimer() {
//        startTimer(timeLeft);
//        if (audio != null) {
//            audio.seekTo(soundDuration);
//            audio.start();
//
//        }
//    }
//
//    public static String video_height;
//    public static String video_width;
//    SimpleExoPlayer playerDuet;
//    PlayerView exoPlayerForDuet;
//
//    public void setupForDuet() {
//
//        exoPlayerForDuet = findViewById(R.id.exo_player_duet);
//        Intent intent = getIntent();
//        String video_duet = "";
//        if (intent.hasExtra("video_duet")) {
//            video_duet = intent.getStringExtra("video_duet");
//        }
//        duet_url = video_duet;
//
//        DefaultLoadControl loadControl = new DefaultLoadControl.Builder().setBufferDurationsMs(1 * 1024, 1 * 1024, 500, 1024).createDefaultLoadControl();
//
//        DefaultTrackSelector trackSelector = new DefaultTrackSelector();
//        playerDuet = ExoPlayerFactory.newSimpleInstance(MyVideoEditorActivity.this, trackSelector, loadControl);
//
//
//        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(MyVideoEditorActivity.this,
//                Util.getUserAgent(MyVideoEditorActivity.this, MyVideoEditorActivity.this.getResources().getString(R.string.app_name)));
//        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
//                .createMediaSource(Uri.parse(video_duet));
//        playerDuet.prepare(videoSource);
//        playerDuet.setRepeatMode(Player.REPEAT_MODE_ALL);
////        playerDuet.seekTo((long) seekToTime);
//
//        exoPlayerForDuet.setPlayer(playerDuet);
//        playerDuet.addVideoListener(new VideoListener() {
//            @Override
//            public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
//                video_height = String.valueOf(height);
//                video_width = String.valueOf(width);
//            }
//
//            @Override
//            public void onRenderedFirstFrame() {
//
//            }
//        });
////        playerDuet.addListener(new Player.EventListener() {
////            @Override
////            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
////                if (playbackState == ExoPlayer.STATE_READY && !isDurationSet) {
////
////                    maxRecordingDuration = getVideoDurationSeconds(playerDuet);
////                    recordingDuration = maxRecordingDuration;
////                    initlize_Video_progress(maxRecordingDuration * 1000);
////                    isDurationSet = true;
////                }
////
////            }
////        });
//        playerDuet.addListener(new Player.EventListener() {
//            @Override
//            public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason) {
//
//            }
//
//            @Override
//            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
//
//            }
//
//            @Override
//            public void onLoadingChanged(boolean isLoading) {
//
//            }
//
//            @Override
//            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
//                if (playbackState == ExoPlayer.STATE_READY && !isDurationSet) {
//                    int maxduration = getVideoDurationSeconds(playerDuet);
//                    MAX_RECORDING_DURATION = maxduration;
//                    isDurationSet = true;
//                }
//            }
//
//            @Override
//            public void onRepeatModeChanged(int repeatMode) {
//
//            }
//
//            @Override
//            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
//
//            }
//
//            @Override
//            public void onPlayerError(ExoPlaybackException error) {
//
//            }
//
//            @Override
//            public void onPositionDiscontinuity(int reason) {
//
//            }
//
//            @Override
//            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
//
//            }
//
//            @Override
//            public void onSeekProcessed() {
//
//            }
//        });
//
//    }
//
//    private int getVideoDurationSeconds(SimpleExoPlayer player) {
//        int timeMs = (int) player.getDuration();
//        int totalSeconds = timeMs;
//        return totalSeconds;
//    }
//
//    boolean isDurationSet = false;
//
//    public static String duet_url;
//
//    public static boolean isDuet = false;
//
//    private void stopRecording() {
//        REQUEST_CODE = 0;
//        img_shoot_main.setEnabled(false);
//        mARGMedia.stopRecording();
//        Long timePro = totoalTime - seconds;
//
//        videoLengthS = String.valueOf(timePro);
//        App.getSingleton().setVideoLength(videoLengthS);
//        Log.i("videoLength", videoLengthS);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (isDuet) {
//                    if (playerDuet != null) {
//                        playerDuet.setPlayWhenReady(false);
//
//                    }
//                }
//                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + mVideoFilePath)));
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                    MediaStoreUtil.writeVideoToMediaStoreForQ(MyVideoEditorActivity.this, mVideoFilePath, Environment.DIRECTORY_DCIM + "/ARGEAR");
//                }
//                img_shoot_main.setEnabled(true);
//                ll_sidebar_edit.setVisibility(View.VISIBLE);
//                rl_topbar_edit.setVisibility(View.VISIBLE);
//                ll_effects_edit.setVisibility(View.VISIBLE);
//                ll_upload_edit.setVisibility(View.VISIBLE);
//                progress_bar_video.setVisibility(View.GONE);
//                tv_time_lapsed.setVisibility(View.GONE);
//                countDownTimer.cancel();
//                img_shoot_main.setEnabled(true);
//                if (audio != null) {
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                        Variables.made_trimed_video = mInnerMediaPath + "/" + System.currentTimeMillis() + "trimed" + ".mp4";
//                    } else {
//                        Variables.made_trimed_video = mMediaPath + "/" + System.currentTimeMillis() + "trimed" + ".mp4";
//                    }
//                    audio.stop();
//                    merge_Audio_Video();
//                } else {
//                    if (speed != 0) {
//                        speedControls(mVideoFilePath);
//                    } else {
//                        Intent intent = new Intent(MyVideoEditorActivity.this, PlayerActivity.class);
//                        Bundle b = new Bundle();
//                        b.putString(PlayerActivity.INTENT_URI, mVideoFilePath);
//                        intent.putExtras(b);
//                        startActivity(intent);
//                        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
//                    }
//                }
//            }
//        }, 1500);
//    }
//
//    VideoAudioFFMPEG videoAudioFFMPEG;
//    Handler handler = new Handler() {
//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            super.handleMessage(msg);
//            final int what = msg.what;
//            final String[] value = (String[]) msg.obj;
//            switch (what) {
//                case 1:
//                    Log.i("myFfmpegFormatCompleted", "Merge : started");
//                    videoAudioFFMPEG.executeMergeCommand(value);
//                    break;
//            }
//        }
//    };
//
//    private void deleteExistingFiles() {
//        File audioFile = new File(Variables.trimmp3File);
//        if (audioFile.exists()) {
//            audioFile.delete();
//        }
//        File videoFile = new File(Variables.made_trimed_video);
//        if (videoFile.exists()) {
//            videoFile.delete();
//        }
//    }
//
//    private void merge_Audio_Video() {
//        deleteExistingFiles();
////get video length
//        File videoFile = new File(mVideoFilePath);
//        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//
//        retriever.setDataSource(MyVideoEditorActivity.this, Uri.fromFile(videoFile));
//
//
//        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
//        long timeInMillisec = Long.parseLong(time);
//        long seconds = TimeUnit.MILLISECONDS.toSeconds(timeInMillisec);
//        retriever.release();
//
//        Log.i("videoLength", String.valueOf(seconds));
//
//        File audioTrimFile = new File(Variables.trimmp3File);
//
//        String[] audioTrimCommand;
//
//        audioTrimCommand = new String[]{"-i", soundPath, "-ss", "0", "-t", String.valueOf(seconds), "-acodec", "copy", audioTrimFile.getAbsolutePath()};
//
////        String audioVideoMergeCommand[] = new String[]{"-i", mVideoFilePath, "-i", audioTrimFile.getAbsolutePath(), "-c:v", "copy", "-c:a", "copy", Variables.made_trimed_video};
//
//        String audioVideoMergeCommand[] = new String[]{"-i", mVideoFilePath, "-i", audioTrimFile.getAbsolutePath(), "-map", "0:v", "-map", "1:a", "-c:v", "copy", Variables.made_trimed_video};
//
//        videoAudioFFMPEG = new VideoAudioFFMPEG(MyVideoEditorActivity.this, new VideoAudioFFMPEG.AudioTrimCompleted() {
//            @Override
//            public void onMergeCompletedListener() {
//
//                if (speed != 0) {
//                    speedControls(Variables.made_trimed_video);
//                } else {
//                    Intent intent = new Intent(MyVideoEditorActivity.this, PlayerActivity.class);
//                    Bundle b = new Bundle();
//                    b.putString(PlayerActivity.INTENT_URI, Variables.made_trimed_video);
//                    intent.putExtras(b);
//                    startActivity(intent);
//                    overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
//                }
//            }
//
//            @Override
//            public void onAudioFormatCompletedListener() {
//
//                Message message = new Message();
//                message.what = 1;
//                message.obj = audioVideoMergeCommand;
//                message.setTarget(handler);
//                message.sendToTarget();
//                Log.i("myFfmpegFormatCompleted", "completed");
//            }
//        });
//
////audio format to mp3
//        videoAudioFFMPEG.executeTrimCommand(audioTrimCommand);
//    }
//
//    private void setLastUpdateAt(Context context, long updateAt) {
//        PreferenceUtil.putLongValue(context, AppConfig.USER_PREF_NAME, "ContentLastUpdateAt", updateAt);
//    }
//
//    private long getLastUpdateAt(Context context) {
//        return PreferenceUtil.getLongValue(context, AppConfig.USER_PREF_NAME, "ContentLastUpdateAt");
//    }
//
//    private void setFilterUpdateAt(Context context, String itemId, long updateAt) {
//        PreferenceUtil.putLongValue(context, AppConfig.USER_PREF_NAME_FILTER, itemId, updateAt);
//    }
//
//    private long getFilterUpdateAt(Context context, String itemId) {
//        return PreferenceUtil.getLongValue(context, AppConfig.USER_PREF_NAME_FILTER, itemId);
//    }
//
//    private void setStickerUpdateAt(Context context, String itemId, long updateAt) {
//        PreferenceUtil.putLongValue(context, AppConfig.USER_PREF_NAME_STICKER, itemId, updateAt);
//    }
//
//    private long getStickerUpdateAt(Context context, String itemId) {
//        return PreferenceUtil.getLongValue(context, AppConfig.USER_PREF_NAME_STICKER, itemId);
//    }
//
//    // region - network
//    private void requestSignedUrl(ModelContentsResponse.Category.Item item, String path, final boolean isArItem) {
//        progressBar.setVisibility(View.VISIBLE);
//        mARGSession.auth().requestSignedUrl(item.getZipFile(), item.getTitle(), item.getType(), new ARGAuth.Callback() {
//            @Override
//            public void onSuccess(String url) {
//                requestDownload(path, url, item, isArItem);
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                if (e instanceof SignedUrlGenerationException) {
//                    Log.e(TAG, "SignedUrlGenerationException !! ");
//                } else if (e instanceof NetworkException) {
//                    Log.e(TAG, "NetworkException !!");
//                }
//                progressBar.setVisibility(View.INVISIBLE);
//            }
//        });
//    }
//
//    private void requestDownload(String targetPath, String url, ModelContentsResponse.Category.Item item, boolean isSticker) {
//        new DownloadAsyncTask(targetPath, url, new DownloadAsyncResponse() {
//            @Override
//            public void processFinish(boolean result) {
//                progressBar.setVisibility(View.INVISIBLE);
//                if (result) {
//                    if (isSticker) {
//                        setItem(ARGContents.Type.ARGItem, targetPath, item);
//                    } else {
//                        setItem(ARGContents.Type.FilterItem, targetPath, item);
//                    }
//                    Log.d(TAG, "download success!");
//                } else {
//                    Log.d(TAG, "download failed!");
//                }
//            }
//        }).execute();
//    } // endregion
//
//    GLView.GLViewListener glViewListener = new GLView.GLViewListener() { // region - GLViewListener
//        @Override
//        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
//            mScreenRenderer.create(gl, config);
//            mCameraTexture.createCameraTexture();
//        }
//
//        @Override
//        public void onDrawFrame(GL10 gl, int width, int height) {
//            if (mCameraTexture.getSurfaceTexture() == null) {
//                return;
//            }
//            if (mCamera != null) {
//                mCamera.setCameraTexture(mCameraTexture.getTextureId(), mCameraTexture.getSurfaceTexture());
//            }
//            try {
//                ARGFrame frame = mARGSession.drawFrame(gl, mScreenRatio, width, height);
//                mScreenRenderer.draw(frame, width, height);
//                if (mHasTrigger) updateTriggerStatus(frame.getItemTriggerFlag());
//                if (mARGMedia != null) {
//                    if (mARGMedia.isRecording()) mARGMedia.updateFrame(frame.getTextureId());
//                }
//            } catch (Exception e) {
//                Log.i("ARGEAR", e.getMessage());
//            }
//        }
//
//    };
//// endregion
//
//
//    // region - CameraListener
//    ReferenceCamera.CameraListener cameraListener = new ReferenceCamera.CameraListener() {
//        @Override
//        public void setConfig(int previewWidth, int previewHeight, float verticalFov, float horizontalFov, int orientation, boolean isFrontFacing, float fps) {
//            mARGSession.setCameraConfig(new ARGCameraConfig(previewWidth,
//                    previewHeight,
//                    verticalFov,
//                    horizontalFov,
//                    orientation,
//                    isFrontFacing,
//                    fps));
//        }
//
//        // region - for camera api 1
//        @Override
//        public void updateFaceRects(Camera.Face[] faces) {
//            mARGSession.updateFaceRects(faces);
//        }
//
//        @Override
//        public void feedRawData(byte[] data) {
//            mARGSession.feedRawData(data);
//        }
//// endregion
//
//        // region - for camera api 2
//        @Override
//        public void updateFaceRects(int numFaces, int[][] bbox) {
//            mARGSession.updateFaceRects(numFaces, bbox);
//        }
//
//        @Override
//        public void feedRawData(Image data) {
//            mARGSession.feedRawData(data);
//        }
//// endregion
//    };
//
//    public void Pick_video_from_gallery() {
//        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
//        intent.setType("video/*");
//        startActivityForResult(Intent.createChooser(intent, "Please pick a video"), Variables.Pick_video_from_gallery);
//    }
//
//
//    private void speedControls(String inputPath) {
//
//        File speedFile = new File(Variables.speedVideoPath);
//        if (speedFile.exists()) {
//            speedFile.delete();
//        }
//
//
//        VideoAudioFFMPEG videoAudioFFMPEG = new VideoAudioFFMPEG(MyVideoEditorActivity.this, new VideoAudioFFMPEG.SpeedVideo() {
//            @Override
//            public void onSpeedCompletedListener() {
//
//                clearBulge();
//                clearFilter();
//                clearStickers();
//
//                speed = 0;
//                tv_time_lapsed.setVisibility(View.GONE);
//                progress_bar_video.setVisibility(View.GONE);
//                rl_topbar_edit.setVisibility(View.VISIBLE);
//                ll_sidebar_edit.setVisibility(View.VISIBLE);
////                MAX_RECORDING_DURATION = 15000;
//                File speedPath = new File(Variables.speedVideoPath);
//
//
//                if (!speedPath.exists()) {
//                    Log.i("speedFile", String.valueOf(!speedPath.exists()));
//                    return;
//                }
//
//
//                Intent intent = new Intent(MyVideoEditorActivity.this, PlayerActivity.class);
//                intent.putExtra(PlayerActivity.INTENT_URI, Variables.speedVideoPath);
//                startActivity(intent);
//            }
//        });
//
//        switch (speed) {
//
//            case 1: //half speed
//                Log.i("speedControls", "half Speed");
//                String[] halfSpeed = {"-y", "-i", inputPath, "-filter_complex", "[0:v]setpts=2.0*PTS[v];[0:a]atempo=0.5[a]", "-map", "[v]", "-map", "[a]", "-b:v", "2097k", "-r", "60", "-vcodec", "mpeg4", Variables.speedVideoPath};
//                videoAudioFFMPEG.speedCommand(halfSpeed, "Speed 0.5x");
//                break;
//
//            case 2: //double the speed
//                Log.i("speedControls", "double Speed");
//                String[] doubleSpeed = {"-y", "-i", inputPath, "-filter_complex", "[0:v]setpts=0.5*PTS[v];[0:a]atempo=2.0[a]", "-map", "[v]", "-map", "[a]", "-b:v", "2097k", "-r", "60", "-vcodec", "mpeg4", Variables.speedVideoPath};
//                videoAudioFFMPEG.speedCommand(doubleSpeed, "Speed 2x");
//                break;
//
//            case 3: //reverse
//                Log.i("speedControls", "reverse Speed");
//                String revrsecommand[] = {"-i", inputPath, "-vf", "reverse", "-af", "areverse", Variables.speedVideoPath};
//                videoAudioFFMPEG.speedCommand(revrsecommand, "Reversing");
//                break;
//        }
//    }


//}