package in.pureway.cinemaflix.Live_Stream;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bambuser.broadcaster.BroadcastStatus;
import com.bambuser.broadcaster.Broadcaster;
import com.bambuser.broadcaster.CameraError;
import com.bambuser.broadcaster.ConnectionError;
import com.bambuser.broadcaster.SurfaceViewWithAutoAR;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.models.ModelFirebaseDatabaseRead;
import in.pureway.cinemaflix.models.ModelLoginRegister;
import in.pureway.cinemaflix.mvvm.LiveMvvm;
import in.pureway.cinemaflix.utils.App;
import in.pureway.cinemaflix.utils.AppConstants;
import in.pureway.cinemaflix.utils.CommonUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

public class GoLiveActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView recycler_comment;
    private EmojiconEditText edit_text_comment;
    private ImageView img_send, img_rotate_camera, img_flash,img_emoji;
    SurfaceViewWithAutoAR mPreviewSurface;
    Broadcaster mBroadcaster;
    private static final String APPLICATION_ID = AppConstants.APPLICATION_ID;
    private LiveMvvm liveMvvm;
    private TextView tv_leave, tv_live;
    String broadcastId = "";
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    String username = App.getSharedpref().getModel(AppConstants.REGISTER_LOGIN_DATA, ModelLoginRegister.class).getDetails().getUsername();
    private ImageView gif_imageView;
    DatabaseReference commentsRef = database.getReference();
    DatabaseReference postBroadcastId = database.getReference("users");
    private List<ModelFirebaseDatabaseRead> commentsList = new ArrayList<>();
    private CommentRecyclerAdapter commentRecyclerAdapter;
    private boolean isLiveUser = false;
    boolean torchOn = false;
    EmojIconActions emojIcon;
    View rootView;
    boolean doubleBackToExitPressedOnce = false;


    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 1:
                    gif_imageView.setVisibility(View.GONE);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_live);
        if (!App.getSharedpref().isLogin(GoLiveActivity.this)) {
            finish();
        } else {
            liveMvvm = ViewModelProviders.of(GoLiveActivity.this).get(LiveMvvm.class);
            findIds();
            mBroadcaster.setCameraSurface(mPreviewSurface);
//            mPreviewSurface.requestFocus();
//            mPreviewSurface.setFocusable(true);
            mBroadcaster.onActivityResume();
        }
    }

    private Broadcaster.Observer mBroadcasterObserver = new Broadcaster.Observer() {
        @Override
        public void onConnectionStatusChange(BroadcastStatus broadcastStatus) {
            Log.i("Mybroadcastingapp", "Received status change: " + broadcastStatus);
            tv_leave.setText(broadcastStatus == BroadcastStatus.IDLE ? "Broadcast" : "Disconnect");

            if (broadcastStatus == BroadcastStatus.STARTING)
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            if (broadcastStatus == BroadcastStatus.IDLE)
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

        @Override
        public void onStreamHealthUpdate(int i) {

        }

        @Override
        public void onConnectionError(ConnectionError connectionError, String s) {
            Log.w("Mybroadcastingapp", "Received connection error: " + connectionError + ", " + s);
        }

        @Override
        public void onCameraError(CameraError cameraError) {
            Log.i("Mybroadcastingapp", "cameraError" + cameraError.toString());
        }

        @Override
        public void onChatMessage(String s) {
            Log.i("Mybroadcastingapp", "onChatMessage" + s);
        }

        @Override
        public void onResolutionsScanned() {
            if (mBroadcaster.getPreviewRotation() == -1 && mBroadcaster.getCaptureRotation() == -1) {
                mBroadcaster.setRotation(0);
//                mPreviewSurface.refreshDrawableState();
            }
            Log.i("Mybroadcastingapp", "onResolutionsScanned");
        }

        @Override
        public void onCameraPreviewStateChanged() {
            Log.i("Mybroadcastingapp", "onCameraPreviewStateChanged");
        }

        @Override
        public void onBroadcastInfoAvailable(String s, String s1) {
            Log.i("Mybroadcastingapp", "onBroadcastInfoAvailable" + ":" + s + ":" + s1);
        }

        @Override
        public void onBroadcastIdAvailable(String s) {
            mBroadcaster.switchCamera();
            mBroadcaster.setAuthor(CommonUtils.userId(GoLiveActivity.this));
            mBroadcaster.setTitle(username);
            Log.i("Mybroadcastingapp", "onBroadcastIdAvailable" + ":" + s);
            Log.i("rotation:Preview", String.valueOf(mBroadcaster.getPreviewRotation()));
            Log.i("rotation:Capture", String.valueOf(mBroadcaster.getCaptureRotation()));

            isLiveUser = true;
            broadcastId = s;
            sendBroadcast(s);

            HashMap<String, Object> map = new HashMap<>();
            map.put(username, s);
            postBroadcastId.child(username).setValue(s).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
//                    postBroadcastId.addValueEventListener(eventListener);
                    postBroadcastId.child(username).child("comments").addChildEventListener(childevenet);
                    Log.i("postBroadcastId", "success");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.i("postBroadcastId", e.getMessage());
                }
            });
        }
    };

    ChildEventListener giftsChildEvent = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            Log.i("gifts_child_event", snapshot.getKey());
            Toast.makeText(GoLiveActivity.this, "Received Gifted", Toast.LENGTH_SHORT).show();
            gif_imageView.setVisibility(View.VISIBLE);

            String gifPath = String.valueOf(snapshot.getValue());

            Glide.with(GoLiveActivity.this).asGif().load(gifPath)
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).listener(new RequestListener<GifDrawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                    Toast.makeText(GoLiveActivity.this, "Gift Received", Toast.LENGTH_SHORT).show();
                    Message message = new Message();
                    message.obj = 1;
                    message.what = 1;
                    message.setTarget(handler);
                    message.sendToTarget();
                    return false;
                }

                @Override
                public boolean onResourceReady(final GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                    resource.setLoopCount(1);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (true) {
                                if (!resource.isRunning()) {
                                    Message message = new Message();
                                    message.obj = 1;
                                    message.what = 1;
                                    message.setTarget(handler);
                                    message.sendToTarget();
                                    break;
                                }
                            }
                        }
                    }).start();
                    return false;
                }
            }).into(gif_imageView);
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    ChildEventListener childevenet = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            Log.i("childEvenet", snapshot.getKey());
            ModelFirebaseDatabaseRead user = snapshot.getValue(ModelFirebaseDatabaseRead.class);
            commentsList.add(user);
            commentRecyclerAdapter.notifyDataSetChanged();
            recycler_comment.smoothScrollToPosition(commentsList.size() - 1);
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    private void findIds() {
        rootView = findViewById(R.id.rootView);
        img_emoji = findViewById(R.id.img_emoji);
        img_flash = findViewById(R.id.img_flash);
        img_rotate_camera = findViewById(R.id.img_rotate_camera);
        img_rotate_camera.setOnClickListener(this);
        img_flash.setOnClickListener(this);
        gif_imageView = findViewById(R.id.gif_imageView);
        mBroadcaster = new Broadcaster(this, APPLICATION_ID, mBroadcasterObserver);
        tv_live = findViewById(R.id.tv_live);
        mPreviewSurface = findViewById(R.id.PreviewSurfaceView);
        tv_leave = findViewById(R.id.tv_leave);
        if (!hasPermission(Manifest.permission.CAMERA) && !hasPermission(Manifest.permission.RECORD_AUDIO))
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO}, 1);
        else if (!hasPermission(Manifest.permission.RECORD_AUDIO))
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
        else if (!hasPermission(Manifest.permission.CAMERA))
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);

        findViewById(R.id.img_send).setOnClickListener(this);
        edit_text_comment = findViewById(R.id.edit_text_comment);
        emojIcon = new EmojIconActions(this,rootView, edit_text_comment, img_emoji);
        emojIcon.ShowEmojIcon();
        tv_leave = findViewById(R.id.tv_leave);
        mBroadcaster.startBroadcast();
        tv_leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBroadcaster.canStartBroadcasting()) {
                    mBroadcaster.startBroadcast();
                } else {
                    mBroadcaster.stopBroadcast();
                    stopBroadcast(broadcastId);
                }
            }
        });
        recycler_comment = findViewById(R.id.recycler_comment);
        commentRecyclerAdapter = new CommentRecyclerAdapter(GoLiveActivity.this, commentsList);
        recycler_comment.setAdapter(commentRecyclerAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopBroadcast(broadcastId);
        mBroadcaster.onActivityDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        mBroadcaster.onActivityPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mBroadcaster.setCameraSurface(mPreviewSurface);
        mBroadcaster.onActivityResume();
    }

    private boolean hasPermission(String permission) {
        return ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    private void sendBroadcast(String id) {
        liveMvvm.goLive(GoLiveActivity.this, id, CommonUtils.userId(GoLiveActivity.this)).observe(GoLiveActivity.this, new Observer<Map>() {
            @Override
            public void onChanged(Map map) {
                if (map.get("success").equals("1")) {
                    img_rotate_camera.setVisibility(View.VISIBLE);
                    img_flash.setVisibility(View.VISIBLE);
                    tv_live.setVisibility(View.VISIBLE);
                    commentsRef.child("users").child(username).child("gifts").addChildEventListener(giftsChildEvent);
                    Log.i("broadcastApi", map.get("message").toString());
                }
            }
        });
    }

    private void stopBroadcast(String broadcastId) {
        liveMvvm.stopBroadcasting(GoLiveActivity.this, broadcastId).observe(GoLiveActivity.this, new Observer<Map>() {
            @Override
            public void onChanged(Map map) {
                if (map.get("success").equals("1")) {
                    Log.i("stopBroadcasting", map.get("message").toString());
                    onBackPressed();
                } else {
                    Toast.makeText(GoLiveActivity.this, map.get("message").toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            stopBroadcast(broadcastId);
            mBroadcaster.onActivityDestroy();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to end live session", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 1500);
//        super.onBackPressed();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_flash:
                if (torchOn) {
                    torchOn = false;
                    Glide.with(GoLiveActivity.this).load(R.drawable.ic_flash_off).into(img_flash);
                } else {
                    torchOn = true;
                    Glide.with(GoLiveActivity.this).load(R.drawable.ic_flash_on).into(img_flash);
                }
                mBroadcaster.toggleTorch();
                break;
            case R.id.img_rotate_camera:
                mBroadcaster.switchCamera();
                break;
            case R.id.img_send:
                if (isLiveUser) {
                    comment();
                }
                break;
        }
    }

    private void comment() {
        String comment = edit_text_comment.getText().toString().trim();
        String image = App.getSharedpref().getModel(AppConstants.REGISTER_LOGIN_DATA, ModelLoginRegister.class).getDetails().getImage();
        String username = App.getSharedpref().getModel(AppConstants.REGISTER_LOGIN_DATA, ModelLoginRegister.class).getDetails().getUsername();
        if (!comment.equalsIgnoreCase("")) {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("userId", username);
            hashMap.put("username", username);
            hashMap.put("userImage", image);
            hashMap.put("comment", comment);

            commentsRef.child("users").child(username).child("comments").push().setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid){
                    Log.i("databaseComment", "success");
                    edit_text_comment.setText("");
                    hideKeyboard(GoLiveActivity.this);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.i("databaseComment", e.getMessage());
                }
            });
        } else {
            Toast.makeText(this, "Comment cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
