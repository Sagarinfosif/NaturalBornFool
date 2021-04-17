package in.pureway.cinemaflix.Live_Stream;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bambuser.broadcaster.BroadcastPlayer;
import com.bambuser.broadcaster.PlayerState;
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
import in.pureway.cinemaflix.activity.privacysettings.BalanceActivity;
import in.pureway.cinemaflix.javaClasses.FollowUnfollowUser;
import in.pureway.cinemaflix.models.GiftModel;
import in.pureway.cinemaflix.models.ModelFirebaseDatabaseRead;
import in.pureway.cinemaflix.models.ModelLoginRegister;
import in.pureway.cinemaflix.mvvm.VideoMvvm;
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

import de.hdodenhof.circleimageview.CircleImageView;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

public class OtherLiveActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView recycler_comment;
    SurfaceViewWithAutoAR mVideoSurface;
    TextView mPlayerStatusTextView;
    View mPlayerContentView;
    private ImageView img_send, img_emoji;
    private EmojiconEditText edit_text;
    BroadcastPlayer mBroadcastPlayer;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference commentsRef = database.getReference();
    private List<ModelFirebaseDatabaseRead> commentsList = new ArrayList<>();
    private CommentRecyclerAdapter commentRecyclerAdapter;
    private String liveUsername = "";
    private String broadcastId = "";
    private LinearLayout relative_msg;
    DatabaseReference postBroadcastId = database.getReference("users");
    private List<GiftModel.Detail> listgift = new ArrayList<>();
    private String live_user_id,name;
    private ImageView gif_imageView, img_follow_live_user;
    Display mDefaultDisplay;
    private CircleImageView img_live_image;
    private boolean followStatus = false;
    EmojIconActions emojIcon;
    View rootView;
    private ImageView close;
    private TextView tvname;
    private int backpress = 0;

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
        setContentView(R.layout.activity_other_live);
        findIds();
        mPlayerStatusTextView.setVisibility(View.GONE);
        name = getIntent().getExtras().getString("name");
        liveUsername = getIntent().getExtras().getString("username");
        broadcastId = getIntent().getExtras().getString("resourceuri");
        live_user_id = getIntent().getExtras().getString("live_user_id");
        followStatus = getIntent().getExtras().getBoolean("follow");

        if (followStatus) {
            img_follow_live_user.setBackgroundResource(R.drawable.circle_bg_theme);
            Glide.with(OtherLiveActivity.this).load(R.drawable.ic_checked).into(img_follow_live_user);
        } else {
            img_follow_live_user.setBackgroundResource(R.drawable.circle_bg_theme);
            Glide.with(OtherLiveActivity.this).load(R.drawable.ic_baseline_add_24).into(img_follow_live_user);
        }


        tvname.setText(name);
        Glide.with(OtherLiveActivity.this).load(getIntent().getExtras().getString("userimage")).into(img_live_image);

        mDefaultDisplay = getWindowManager().getDefaultDisplay();
        if (mBroadcastPlayer != null)
            mBroadcastPlayer.close();
        mBroadcastPlayer = new BroadcastPlayer(OtherLiveActivity.this, broadcastId, AppConstants.APPLICATION_ID, mBroadcastPlayerObserver);
        mBroadcastPlayer.setSurfaceView(mVideoSurface);
        mBroadcastPlayer.load();

        postBroadcastId.child(liveUsername).child("comments").addChildEventListener(childevent);
        commentsRef.child("users").child(liveUsername).child("gifts").addChildEventListener(giftsChildEvent);


    }

    private Point getScreenSize() {
        if (mDefaultDisplay == null)
            mDefaultDisplay = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        try {
            // this is officially supported since SDK 17 and said to work down to SDK 14 through reflection,
            // so it might be everything we need.
            mDefaultDisplay.getClass().getMethod("getRealSize", Point.class).invoke(mDefaultDisplay, size);
        } catch (Exception e) {
            // fallback to approximate size.
            mDefaultDisplay.getSize(size);
        }
        return size;
    }

    BroadcastPlayer.Observer mBroadcastPlayerObserver = new BroadcastPlayer.Observer() {
        @Override
        public void onStateChange(PlayerState playerState) {
            if (mPlayerStatusTextView != null)
                mPlayerStatusTextView.setText("Status: " + playerState);
            if (playerState.equals(PlayerState.COMPLETED)) {
                onBackPressed();
                Toast.makeText(OtherLiveActivity.this, String.valueOf(playerState), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onBroadcastLoaded(boolean live, int width, int height) {

            if (!live) {
                onBackPressed();
            }

            Point size = getScreenSize();
            float screenAR = size.x / (float) size.y;
            float videoAR = width / (float) height;
            float arDiff = screenAR - videoAR;
            mVideoSurface.setCropToParent(Math.abs(arDiff) < 0.2);
        }
    };

    private void findIds() {
        tvname=findViewById(R.id.tv_name_live);
        findViewById(R.id.iv_close).setOnClickListener(this);
        img_live_image = findViewById(R.id.img_live_image);
        gif_imageView = findViewById(R.id.gif_imageView);
        relative_msg = findViewById(R.id.relative_msg);
        mPlayerContentView = findViewById(R.id.PlayerContentView);
        img_emoji = findViewById(R.id.img_emoji);
        img_send = findViewById(R.id.img_send);
        img_send.setOnClickListener(this);
        edit_text = findViewById(R.id.edit_text);
        emojIcon = new EmojIconActions(this,mPlayerContentView, edit_text, img_emoji);
        emojIcon.ShowEmojIcon();
        mPlayerStatusTextView = findViewById(R.id.mPlayerStatusTextView);
        mVideoSurface = findViewById(R.id.VideoSurfaceView);
        img_follow_live_user = findViewById(R.id.img_follow_live_user);
        img_follow_live_user.setOnClickListener(this);
        findViewById(R.id.ll_gift).setOnClickListener(this);
        findViewById(R.id.tv_leave).setOnClickListener(this);
        recycler_comment = findViewById(R.id.recycler_comment);
        commentRecyclerAdapter = new CommentRecyclerAdapter(OtherLiveActivity.this, commentsList);
        recycler_comment.setAdapter(commentRecyclerAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_follow_live_user:
                followUser();
                break;
            case R.id.ll_gift:
                giftsDialog();
                break;
            case R.id.img_send:
                comment();
                break;
            case R.id.tv_leave:
            case R.id.iv_close:
                onBackPressed();
                finish();
                break;
        }
    }

    private void followUser() {
        FollowUnfollowUser followUnfollowUser = new FollowUnfollowUser(OtherLiveActivity.this, new FollowUnfollowUser.followCallBack() {
            @Override
            public void followInterfaceCall(Map map) {
                if (followStatus) {
                    followStatus = false;
                    Glide.with(OtherLiveActivity.this).load(R.drawable.ic_baseline_add_24).into(img_follow_live_user);
                    Toast.makeText(OtherLiveActivity.this, "Unfollowed user " + liveUsername, Toast.LENGTH_SHORT).show();
                } else {
                    followStatus = true;
                    Glide.with(OtherLiveActivity.this).load(R.drawable.ic_checked).into(img_follow_live_user);
                    Toast.makeText(OtherLiveActivity.this, "Followed user " + liveUsername, Toast.LENGTH_SHORT).show();
                }

            }
        });

        followUnfollowUser.followUnfollow(live_user_id);
    }

    @Override
    public void onBackPressed() {

        backpress = (backpress + 1);
        Toast.makeText(getApplicationContext(), " Press Back again to Exit ", Toast.LENGTH_SHORT).show();

        if (backpress>1) {
            super.onBackPressed();
            mBroadcastPlayer.close();
            mBroadcastPlayer = null;
            this.finish();
        }

    }

    private void comment() {
        String comment = edit_text.getText().toString().trim();
        String image = App.getSharedpref().getModel(AppConstants.REGISTER_LOGIN_DATA, ModelLoginRegister.class).getDetails().getImage();
        String username = App.getSharedpref().getModel(AppConstants.REGISTER_LOGIN_DATA, ModelLoginRegister.class).getDetails().getUsername();
        if (!comment.equalsIgnoreCase("")) {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("userId", username);
            hashMap.put("username", username);
            hashMap.put("userImage", image);
            hashMap.put("comment", comment);

            commentsRef.child("users").child(liveUsername).child("comments").push().setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.i("databaseComment", "success");
                    edit_text.setText("");
                    hideKeyboard(OtherLiveActivity.this);

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

    ChildEventListener giftsChildEvent = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            Toast.makeText(OtherLiveActivity.this, "Somebody sent Gift", Toast.LENGTH_SHORT).show();
            Log.i("gifts_child_event", snapshot.getKey());

            gif_imageView.setVisibility(View.VISIBLE);

            String gifPath = String.valueOf(snapshot.getValue());

            Glide.with(OtherLiveActivity.this).asGif().load(gifPath)
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).listener(new RequestListener<GifDrawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                    Toast.makeText(OtherLiveActivity.this, "Gift Received", Toast.LENGTH_SHORT).show();
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

    ChildEventListener childevent = new ChildEventListener() {
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

    private void giftsDialog() {
        Dialog dialog = new Dialog(OtherLiveActivity.this);
        dialog.setContentView(R.layout.dialog_send_gifts);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        TextView tv_balance = dialog.findViewById(R.id.tv_coinBalance);
        Button getcoin = dialog.findViewById(R.id.bt_getcoin);
        getcoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OtherLiveActivity.this, BalanceActivity.class));
            }
        });
        ImageView close = dialog.findViewById(R.id.close_dialog);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        RecyclerView recyclerView = dialog.findViewById(R.id.rv_coins);
        VideoMvvm videoMvvm = ViewModelProviders.of(OtherLiveActivity.this).get(VideoMvvm.class);
        videoMvvm.sendGift(OtherLiveActivity.this, CommonUtils.userId(OtherLiveActivity.this)).observe(OtherLiveActivity.this, new Observer<GiftModel>() {
            @Override
            public void onChanged(GiftModel giftModel) {
                listgift = giftModel.getDetails();
                if (!giftModel.getCoin().equalsIgnoreCase("")) {
                    tv_balance.setText(giftModel.getCoin());
                } else {
                    tv_balance.setText("0");
                }
                GiftAdapter giftAdapter = new GiftAdapter(OtherLiveActivity.this, listgift, new GiftAdapter.Click() {
                    @Override
                    public void OnClick(int position, String balance, String price, String id) {
                        if(balance.equalsIgnoreCase("0") || balance.equalsIgnoreCase("")){
                            startActivity(new Intent(OtherLiveActivity.this, PurchaseCoinActivity.class));
                        }else {
                            int balancevalue=Integer.parseInt(balance);
                            int priceValue=Integer.parseInt(price);
                            if(balancevalue>=priceValue){
                                giftSend(videoMvvm, listgift.get(position).getPrimeAccount(), listgift.get(position).getId(), live_user_id, dialog, listgift.get(position).getImage());
                            }else {
                                startActivity(new Intent(OtherLiveActivity.this, PurchaseCoinActivity.class));
                            }
                        }

//                        if (!tv_balance.getText().toString().equalsIgnoreCase("0")) \

//                    \       int balancevalue = Integer.parseInt(balance);
//                            int priceValue = Integer.parseInt(price);
//                            if (balancevalue >= priceValue) {
//                                giftSend(videoMvvm, listgift.get(position).getPrimeAccount(), listgift.get(position).getId(), live_user_id, dialog, listgift.get(position).getImage());
//                            } else {
//                                Toast.makeText(OtherLiveActivity.this, "Not enough coins", Toast.LENGTH_SHORT).show();
//                                startActivity(new Intent(OtherLiveActivity.this, WalletActivity.class));
//                            }
//                        } else {
//                            Toast.makeText(OtherLiveActivity.this, "Not enough coins", Toast.LENGTH_SHORT).show();
//                            startActivity(new Intent(OtherLiveActivity.this, PurchaseCoinActivity.class));
//                        }
                    }
                });
                recyclerView.setAdapter(giftAdapter);
            }
        });
        dialog.show();
    }

    private void giftSend(VideoMvvm videoMvvm, String price, String id, String getOtherUserid, Dialog dialog, String gifPath) {
        videoMvvm.giftSend(OtherLiveActivity.this, CommonUtils.userId(OtherLiveActivity.this), price, getOtherUserid, id).observe(OtherLiveActivity.this, new Observer<Map>() {
            @Override
            public void onChanged(Map map) {
                if (map.get("success").toString().equalsIgnoreCase("1")) {
                    sendGift(gifPath);
                    Toast.makeText(OtherLiveActivity.this, "Gift Sent Successfully", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                    Toast.makeText(OtherLiveActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendGift(String gifPath) {
        String username = App.getSharedpref().getModel(AppConstants.REGISTER_LOGIN_DATA, ModelLoginRegister.class).getDetails().getUsername();
//        commentsRef.child("users").child(liveUsername).child("gifts").push().child(username).setValue(gifPath).addOnSuccessListener(new OnSuccessListener<Void>() {
        commentsRef.child("users").child(liveUsername).child("gifts").setValue(gifPath).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i("live_gift", "sent successfully");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("live_gift", e.getMessage());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

}