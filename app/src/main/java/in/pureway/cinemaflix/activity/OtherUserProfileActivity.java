package in.pureway.cinemaflix.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.CallbackManager;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.card.MaterialCardView;
import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.activity.chat.MessageActivity;
import in.pureway.cinemaflix.activity.login_register.RegisterActivity;
import in.pureway.cinemaflix.adapters.AdapterVideoProfile;
import in.pureway.cinemaflix.fragments.homefrags.ReportUserBottomSheet;
import in.pureway.cinemaflix.javaClasses.FacebookLogin;
import in.pureway.cinemaflix.javaClasses.FollowUnfollowUser;
import in.pureway.cinemaflix.javaClasses.GoogleLogin;
import in.pureway.cinemaflix.javaClasses.ProfileData;
import in.pureway.cinemaflix.models.ModelMyUploadedVideos;
import in.pureway.cinemaflix.models.OtherUserDataModel;
import in.pureway.cinemaflix.mvvm.ProfileMvvm;
import in.pureway.cinemaflix.mvvm.VideoMvvm;
import in.pureway.cinemaflix.utils.App;
import in.pureway.cinemaflix.utils.AppConstants;
import in.pureway.cinemaflix.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class OtherUserProfileActivity extends AppCompatActivity implements View.OnClickListener, FollowUnfollowUser.followCallBack {
    private TextView tv_user_name, tv_bio_other_user, tv_followers_other, tv_likes_other, tv_videos_count, headername;
    private ProfileMvvm profileMvvm;
    private ImageView img_user_profile, img_verified;
    private CircleImageView civ_user_profile;
    private Button btn_follow_user;
    private String otheruserId = "";
    private LinearLayout ll_main_after_shimmer;
    private ShimmerFrameLayout shimmerFrameLayout;
    private GoogleLogin googleLogin;
    private FacebookLogin facebookLogin;
    private static final int RC_SIGN_IN = 007;
    private String userImg;
    String userName = "";
    String likesCount = "0";
    private Activity activity;
    private VideoMvvm videoMvvm;
    private MaterialCardView card_no_videos, chat_other_user, card_private_account;
    private RecyclerView recycler_videos_profile;
    private List<ModelMyUploadedVideos.Detail> list = new ArrayList<>();
    private AdapterVideoProfile adapterVideoProfile;
    private boolean showMyFollowing = false;
    private String myId = CommonUtils.userId(OtherUserProfileActivity.this);
    private ProfileData profileData;
    private OtherUserDataModel otherUserDataModelImage;
    private String name = "", mute_unmute = "", getOtheruserId;
    private boolean firstTime = true;

    private LinearLayout ll_OtherUSerProfile, ll_notLogin;
    private LinearLayout  ll_phoneLogin, ll_google, ll_facebook;
    private Button btn_signup, btn_SocialMail, btn_facebook;
    private CallbackManager callbackManager;
    private TextView tv_policyLink,tvTermLink;
    private static final Pattern urlPattern = Pattern.compile(
            "(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)"
                    + "(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*"
                    + "[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{};']*)",
            Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.changeLanguage(OtherUserProfileActivity.this);
        setContentView(R.layout.activity_other_user_profile);
        profileMvvm = ViewModelProviders.of(OtherUserProfileActivity.this).get(ProfileMvvm.class);
        videoMvvm = ViewModelProviders.of(OtherUserProfileActivity.this).get(VideoMvvm.class);
        activity = OtherUserProfileActivity.this;
        findIDs();
        getData();
        getOtheruserId = getIntent().getExtras().getString(AppConstants.OTHER_USER_ID);

        App.getSingleton().setIdType(AppConstants.OTHER_USER_ID);
    }

    private void getStatus() {

        profileMvvm.getVerifyStatus(OtherUserProfileActivity.this, getOtheruserId).observe(OtherUserProfileActivity.this, new Observer<Map>() {
            @Override
            public void onChanged(Map map) {
                if (map.get("success").equals("1")) {
                    String status = map.get("status").toString();
                    if (status.equals("1")) {
                        img_verified.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(OtherUserProfileActivity.this, map.get("message").toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getData() {
        profileData = new ProfileData(OtherUserProfileActivity.this, new ProfileData.followCallBack() {
            @Override
            public void followInterfaceCall(OtherUserDataModel otherUserDataModel) {
                if (otherUserDataModel.getSuccess().equalsIgnoreCase("1")) {
                    getStatus();
                    otherUserDataModelImage = otherUserDataModel;
                    name = otherUserDataModel.getDetails().getName();
                    userImg = otherUserDataModel.getDetails().getImage();
                    tv_bio_other_user.setText(otherUserDataModel.getDetails().getBio());

                    tv_bio_other_user.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Matcher matcher = urlPattern.matcher((otherUserDataModel.getDetails().getBio()));
                            while (matcher.find()) {


                                try {
                                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(otherUserDataModel.getDetails().getBio()));
                                    startActivity(myIntent);
                                } catch (ActivityNotFoundException e) {

                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                    tv_followers_other.setText(otherUserDataModel.getDetails().getFollowers());
                    if (otherUserDataModel.getDetails().getLikes() != null) {
                        if (otherUserDataModel.getDetails().getLikes() == "") {
                            tv_likes_other.setText("0");
                        } else {
                            tv_likes_other.setText(String.valueOf(otherUserDataModel.getDetails().getLikes()));
                        }
                    }
                    tv_videos_count.setText(otherUserDataModel.getDetails().getFollowing());
                    userName = otherUserDataModel.getDetails().getUsername();
                    likesCount = String.valueOf(otherUserDataModel.getDetails().getLikes());
                    showMyFollowing = otherUserDataModel.getDetails().getFollowingViewStatus();
                    recycler_videos_profile.setVisibility(View.VISIBLE);

                    if (otherUserDataModel.getDetails().getPrivateAccount()) {
                        card_private_account.setVisibility(View.VISIBLE);
                        recycler_videos_profile.setVisibility(View.GONE);
                        showPrivetAccountDialog();
                    } else {
                        card_private_account.setVisibility(View.GONE);
                        recycler_videos_profile.setVisibility(View.VISIBLE);
                    }
                    if (!App.getSharedpref().isLogin(OtherUserProfileActivity.this)) {

//                        if (otherUserDataModel.getDetails().getPrivateAccount()) {
//                            card_private_account.setVisibility(View.VISIBLE);
//                            recycler_videos_profile.setVisibility(View.GONE);
//                            showPrivetAccountDialog();
//                        } else {
//                            card_private_account.setVisibility(View.GONE);
//                            recycler_videos_profile.setVisibility(View.VISIBLE);
//                        }
                        chat_other_user.setVisibility(View.GONE);
                    } else {
                        if (otherUserDataModel.getDetails().getFollowStatus().equalsIgnoreCase("1")) {
                            btn_follow_user.setText(getString(R.string.following));
                            chat_other_user.setVisibility(View.VISIBLE);
                            recycler_videos_profile.setVisibility(View.VISIBLE);
                        } else {
                            if (otherUserDataModel.getDetails().getPrivateAccount()) {
                                card_private_account.setVisibility(View.GONE);
                                recycler_videos_profile.setVisibility(View.GONE);
                            }
                        }
                    }

                    if (otherUserDataModel.getDetails().getProfilePhotoStatus()) {
                        if (!otherUserDataModel.getDetails().getVideo().equalsIgnoreCase("")) {
                            Glide.with(OtherUserProfileActivity.this).load(otherUserDataModel.getDetails().getVideo()).skipMemoryCache(true).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE)).into(img_user_profile);
                            civ_user_profile.setVisibility(View.GONE);
                            img_user_profile.setVisibility(View.VISIBLE);
                        } else {
                            Glide.with(OtherUserProfileActivity.this).load(userImg).into(civ_user_profile);
                            civ_user_profile.setVisibility(View.VISIBLE);
                            img_user_profile.setVisibility(View.GONE);
                        }
                    } else {
                        Glide.with(OtherUserProfileActivity.this).load(userImg).into(civ_user_profile);
                        civ_user_profile.setVisibility(View.VISIBLE);
                        img_user_profile.setVisibility(View.GONE);
                    }

                    tv_user_name.setText(otherUserDataModel.getDetails().getUsername());
                    if (otherUserDataModel.getDetails().getName().equalsIgnoreCase("")) {
                        headername.setText(otherUserDataModel.getDetails().getUsername());
                    } else {
                        headername.setText(otherUserDataModel.getDetails().getName());
                    }


                    if (otherUserDataModel.getDetails().getBio() != null) {
                        if (otherUserDataModel.getDetails().getBio().equalsIgnoreCase("")) {
                            tv_bio_other_user.setVisibility(View.GONE);
                        }
                    }else {

                    }


                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    ll_main_after_shimmer.setVisibility(View.VISIBLE);

                } else {
                    Log.i("profile data", otherUserDataModel.getMessage());
                }
            }

            @Override
            public void uploadedVideosCall(ModelMyUploadedVideos modelMyUploadedVideos) {
                if (modelMyUploadedVideos.getSuccess().equalsIgnoreCase("1")) {
                    list = modelMyUploadedVideos.getDetails();
                    adapterVideoProfile = new AdapterVideoProfile(activity, list, new AdapterVideoProfile.Select() {
                        @Override
                        public void drafts(int position) {

                        }

                        @Override
                        public void videoPlayList(int position) {
//                            Intent intent = new Intent(activity, SingleVideoPlayerActivity.class);
                            Intent intent = new Intent(activity, SelectedVideoActivity.class);
                            App.getSingleton().setListMyUploaded(list);
                            intent.putExtra(AppConstants.POSITION, position);
//                            intent.putExtra(AppConstants.CHECK_VIDEO, 0);
                            intent.putExtra(AppConstants.SINGLE_VIDEO_TYPE, AppConstants.OTHER_USER);
                            startActivity(intent);
                        }

                        @Override
                        public void onLongClick(int position) {

                        }

//                        @Override
//                        public void onLongClick(int position) {
//
//                        }
                    }, otheruserId, CommonUtils.userId(activity));
                    recycler_videos_profile.setAdapter(adapterVideoProfile);
                } else {
                    recycler_videos_profile.setVisibility(View.GONE);
                    card_no_videos.setVisibility(View.VISIBLE);
                }
            }
        });

        shimmerFrameLayout.startShimmer();
        if (getIntent().getExtras() != null) {
            otheruserId = getIntent().getExtras().getString(AppConstants.OTHER_USER_ID);
            otherUserData(otheruserId);
            setRecycler(otheruserId);
        } else {
            Log.i("otheruser", "extras null");
            shimmerFrameLayout.stopShimmer();
        }
    }

    private void otherUserData(String otherUserId) {

        profileData.getProfileData(otherUserId);
    }

    private void findIDs() {

        tvTermLink =findViewById(R.id.tvTermLink);
        tv_policyLink =findViewById(R.id.tv_policyLink);
        ll_OtherUSerProfile = findViewById(R.id.ll_OtherUSerProfile);
        ll_notLogin = findViewById(R.id.ll_notLogin);


        findViewById(R.id.img_back_other_user).setOnClickListener(this);
        findViewById(R.id.ll_followers_other).setOnClickListener(this);
        findViewById(R.id.ll_following).setOnClickListener(this);
        findViewById(R.id.ll_likes_other).setOnClickListener(this);
        findViewById(R.id.img_settings_other_profile).setOnClickListener(OtherUserProfileActivity.this);
        civ_user_profile = findViewById(R.id.civ_user_profile);
        card_private_account = findViewById(R.id.card_private_account);
        ll_main_after_shimmer = findViewById(R.id.ll_main_after_shimmer);
        shimmerFrameLayout = findViewById(R.id.shimmer_view_container);
        chat_other_user = findViewById(R.id.chat_other_user);
        chat_other_user.setOnClickListener(this);

        card_no_videos = findViewById(R.id.card_no_videos);
        recycler_videos_profile = findViewById(R.id.recycler_videos_profile);
        btn_follow_user = findViewById(R.id.btn_follow_user);
        btn_follow_user.setOnClickListener(this);
        tv_videos_count = findViewById(R.id.tv_videos_count);
        tv_likes_other = findViewById(R.id.tv_likes_other);
        img_user_profile = findViewById(R.id.img_user_profile);
        img_verified = findViewById(R.id.img_verified);
        tv_bio_other_user = findViewById(R.id.tv_bio_other_user);
        tv_user_name = findViewById(R.id.tv_user_name);
        tv_followers_other = findViewById(R.id.tv_followers_other);
        headername = findViewById(R.id.headername);
        ll_phoneLogin = findViewById(R.id.ll_phoneLogin);
        civ_user_profile.setOnClickListener(this);
        img_user_profile.setOnClickListener(this);



        ll_google = findViewById(R.id.ll_google);
        ll_facebook =findViewById(R.id.ll_facebook);
        btn_signup = findViewById(R.id.btn_signup);
        btn_SocialMail = findViewById(R.id.btn_SocialMail);
        btn_facebook =findViewById(R.id.btn_facebook);
        ll_phoneLogin.setOnClickListener(this);
        btn_facebook.setOnClickListener(this);
        btn_SocialMail.setOnClickListener(this);
        btn_signup.setOnClickListener(this);
        ll_google.setOnClickListener(this);
        ll_facebook.setOnClickListener(this);

        tvTermLink.setOnClickListener(this);
        tv_policyLink.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_settings_other_profile:
                optionsDialog();
                break;

            case R.id.img_back_other_user:
                onBackPressed();
                break;

            case R.id.chat_other_user:
                Intent intent = new Intent(OtherUserProfileActivity.this, MessageActivity.class);
                intent.putExtra(AppConstants.OTHER_USER_ID, getIntent().getExtras().getString(AppConstants.OTHER_USER_ID));
                if (name.equalsIgnoreCase("")) {
                    intent.putExtra(AppConstants.OTHER_USER_NAME, userName);
                } else {
                    intent.putExtra(AppConstants.OTHER_USER_NAME, name);
                }

                intent.putExtra(AppConstants.OTHER_USER_IMAGE, userImg);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                break;

            case R.id.btn_follow_user:
//                if ()
                followUser();
                break;

//            case R.id.ll_followers_other:
//            case R.id.ll_video_count:
//                startActivity(new Intent(OtherUserProfileActivity.this, OtherFollowersFollowingActivity.class)
//                        .putExtra(AppConstants.OTHER_USER_ID, otheruserId)
//                        .putExtra(AppConstants.OTHER_USER_NAME, userName)
//                        .putExtra(AppConstants.SHOW_MY_FOLLOWING, showMyFollowing));
//                break;

            case R.id.civ_user_profile:
            case R.id.img_user_profile:
                openImageDialog(otherUserDataModelImage);
                break;

            case R.id.ll_likes_other:
                openLikeDialog();
                break;


            case R.id.ll_followers_other:

                startActivity(new Intent(OtherUserProfileActivity.this, OtherFollowersFollowingActivity.class)
                        .putExtra(AppConstants.OTHER_USER_ID, otheruserId)
                        .putExtra(AppConstants.POSITION, 1)
                                .putExtra(AppConstants.USER_ID_FOLLOW_UNFOLLOW, otheruserId)

                                .putExtra(AppConstants.OTHER_USER_NAME, userName)
                        );
                break;
            case R.id.ll_following:
// if(!showMyFollowing) {


                startActivity(new Intent(OtherUserProfileActivity.this, OtherFollowersFollowingActivity.class)
                        .putExtra(AppConstants.OTHER_USER_ID, otheruserId)
                        .putExtra(AppConstants.POSITION, 0)
                        .putExtra(AppConstants.USER_ID_FOLLOW_UNFOLLOW, otheruserId)

                        .putExtra(AppConstants.OTHER_USER_NAME, userName)
                );
// }
                break;


            case R.id.ll_phoneLogin:

                startActivity(new Intent(this, RegisterActivity.class));

                break;

            case R.id.btn_signup:

                startActivity(new Intent(this, RegisterActivity.class));

                break;

            case R.id.ll_google:

                App.getSingleton().setLoginType(AppConstants.LOGIN_PROFILE);
                googleLogin = new GoogleLogin(this, AppConstants.LOGIN_PROFILE);
                googleLogin.signIn();

                break;
            case R.id.btn_SocialMail:

                App.getSingleton().setLoginType(AppConstants.LOGIN_PROFILE);
                googleLogin = new GoogleLogin(this, AppConstants.LOGIN_PROFILE);
                googleLogin.signIn();

                break;

            case R.id.ll_facebook:

                callbackManager = CallbackManager.Factory.create();
                facebookLogin = new FacebookLogin(this, AppConstants.LOGIN_PROFILE, getApplication());
                facebookLogin.FBLogin();

                break;


            case R.id.btn_facebook:

                callbackManager = CallbackManager.Factory.create();
                facebookLogin = new FacebookLogin(this, AppConstants.LOGIN_PROFILE, getApplication());
                facebookLogin.FBLogin();

                break;

            case R.id.tv_policyLink:

                Intent intent11=new Intent(OtherUserProfileActivity.this, WebLinkOpenActivity.class);
                intent11.putExtra("key","1");
                intent11.putExtra("title","CINEMAFLIX Privacy Policy");
                startActivity(intent11);
                break;

            case R.id.tvTermLink:

                Intent intent1=new Intent(OtherUserProfileActivity.this, WebLinkOpenActivity.class);
                intent1.putExtra("key","0");
                intent1.putExtra("title","Terms and Conditions");
                startActivity(intent1);

                break;
        }
    }

    private void optionsDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(OtherUserProfileActivity.this);
        final View confirmdailog = layoutInflater.inflate(R.layout.dialog_menu_other_user, null);
        final AlertDialog dailogbox = new AlertDialog.Builder(OtherUserProfileActivity.this).create();
        dailogbox.setCancelable(true);
        dailogbox.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dailogbox.setView(confirmdailog);
        TextView tv_report_user = confirmdailog.findViewById(R.id.tv_report_user);
        final TextView tv_mute_notifications = confirmdailog.findViewById(R.id.tv_mute_notifications);
        if (!App.getSharedpref().isLogin(activity)) {
            tv_report_user.setVisibility(View.GONE);
            tv_mute_notifications.setVisibility(View.GONE);
        }
        if (firstTime) {
            mute_unmute = ((otherUserDataModelImage.getDetails().getMuteStatus())) ? getString(R.string.unmute_notifications) : getString(R.string.mute_notifications);
        }
        tv_mute_notifications.setText(mute_unmute);
        tv_report_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dailogbox.dismiss();
                ReportUserBottomSheet reportUserBottomSheet = new ReportUserBottomSheet(otheruserId, userName);
                reportUserBottomSheet.show(getSupportFragmentManager(), reportUserBottomSheet.getTag());
            }
        });
        tv_mute_notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                muteNotificationUser(dailogbox, tv_mute_notifications);
            }
        });

        confirmdailog.findViewById(R.id.tv_delete_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletDialog();
                dailogbox.dismiss();
            }
        });

        confirmdailog.findViewById(R.id.tv_cancel_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dailogbox.dismiss();
            }
        });
        dailogbox.show();
    }

    private void deletDialog() {
        final AlertDialog.Builder al = new AlertDialog.Builder(this, R.style.dialogStyle);
        al.setTitle("Block user ?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                blockUser(otheruserId);
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setMessage("Are you sure want to block " + otherUserDataModelImage.getDetails().getUsername() + "?").show();
    }

    private void blockUser(final String otheruserId) {
        profileMvvm.blockUser(OtherUserProfileActivity.this, myId, otheruserId).observe(OtherUserProfileActivity.this, new Observer<Map>() {
            @Override
            public void onChanged(Map map) {
                if (map.get("success").toString().equalsIgnoreCase("1")) {
                    List<String> blockedUserList = new ArrayList<>();
                    if (App.getSingleton().getBlockedUserList() != null && App.getSingleton().getBlockedUserList().size() > 0) {
                        blockedUserList = App.getSingleton().getBlockedUserList();
                    }
                    blockedUserList.add(otheruserId);
                    App.getSingleton().setBlockedUserList(blockedUserList);
                    Toast.makeText(OtherUserProfileActivity.this, map.get("message").toString(), Toast.LENGTH_SHORT).show();
                    onBackPressed();
                } else {
                    Toast.makeText(OtherUserProfileActivity.this, map.get("message").toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void openLikeDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(OtherUserProfileActivity.this);
        final View confirmdailog = layoutInflater.inflate(R.layout.dialog_likes, null);
        final AlertDialog dailogbox = new AlertDialog.Builder(OtherUserProfileActivity.this).create();
        dailogbox.setCancelable(true);
        dailogbox.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dailogbox.setView(confirmdailog);

        TextView tv_total_likes = confirmdailog.findViewById(R.id.tv_total_likes);
        String totalLikes = otherUserDataModelImage.getDetails().getUsername() + " received " + likesCount + "\nlikes.";
        tv_total_likes.setText(totalLikes);
        confirmdailog.findViewById(R.id.tv_ok_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dailogbox.dismiss();
            }
        });
        dailogbox.show();
    }

    private void followUser() {
        if (!App.getSharedpref().isLogin(OtherUserProfileActivity.this)) {
//            openDialog();

            ll_main_after_shimmer.setVisibility(View.GONE);
            ll_notLogin.setVisibility(View.VISIBLE);
        } else {
            FollowUnfollowUser followUnfollowUser = new FollowUnfollowUser(OtherUserProfileActivity.this, this);
            followUnfollowUser.followUnfollow(otheruserId);
        }
    }

    private void openDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(OtherUserProfileActivity.this);
        final View confirmdailog = layoutInflater.inflate(R.layout.dialog_signup, null);
        final AlertDialog dailogbox = new AlertDialog.Builder(OtherUserProfileActivity.this).create();
        dailogbox.setCancelable(true);
        dailogbox.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dailogbox.setView(confirmdailog);
        confirmdailog.findViewById(R.id.img_google_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                App.getSingleton().setOtherUserId(otheruserId);
                App.getSingleton().setLoginType(AppConstants.LOGIN_OTHER_USER);
                googleLogin = new GoogleLogin(OtherUserProfileActivity.this, AppConstants.LOGIN_OTHER_USER);
                googleLogin.signIn();
            }
        });

        confirmdailog.findViewById(R.id.img_fb_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                App.getSingleton().setOtherUserId(otheruserId);
                facebookLogin = new FacebookLogin(OtherUserProfileActivity.this, AppConstants.LOGIN_OTHER_USER, getApplication());
                facebookLogin.FBLogin();
                dailogbox.dismiss();
            }
        });

        confirmdailog.findViewById(R.id.btn_signup_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OtherUserProfileActivity.this, RegisterActivity.class));
            }
        });
        dailogbox.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            googleLogin.activityResult(requestCode, resultCode, data);
        } else {
            facebookLogin.callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void openImageDialog(OtherUserDataModel otherUserDataModel) {
        final Dialog dialog;
        LayoutInflater layoutInflater = LayoutInflater.from(OtherUserProfileActivity.this);
        final View confirmdailog = layoutInflater.inflate(R.layout.dialog_show_code, null);
        dialog = new Dialog(OtherUserProfileActivity.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setCancelable(true);
        dialog.setContentView(confirmdailog);
        ImageView img_user, img_back_dialog, img_gif_user;
        img_back_dialog = confirmdailog.findViewById(R.id.img_back_dialog);
        img_gif_user = confirmdailog.findViewById(R.id.img_gif_user);
        img_user = confirmdailog.findViewById(R.id.img_user);
        img_back_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        if (otherUserDataModel.getDetails().getProfilePhotoStatus()) {
            if (!otherUserDataModel.getDetails().getVideo().equalsIgnoreCase("")) {
                img_gif_user.setVisibility(View.VISIBLE);
                img_user.setVisibility(View.GONE);
                Glide.with(OtherUserProfileActivity.this).load(otherUserDataModel.getDetails().getVideo()).skipMemoryCache(true).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE)).into(img_gif_user);
            } else {
                img_gif_user.setVisibility(View.GONE);
                img_user.setVisibility(View.VISIBLE);
                Glide.with(OtherUserProfileActivity.this).load(otherUserDataModel.getDetails().getImage()).into(img_user);
            }
        } else {
            img_user.setVisibility(View.VISIBLE);
            Glide.with(OtherUserProfileActivity.this).load(otherUserDataModel.getDetails().getImage()).into(img_user);
        }
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setRecycler(otheruserId);
    }

    private void setRecycler(final String userId) {
        profileData.getUploadedVideo(userId, CommonUtils.userId(OtherUserProfileActivity.this));
    }

    private void muteNotificationUser(final Dialog dialog, final TextView tv) {
        profileMvvm.muteNotifications(OtherUserProfileActivity.this, myId, otheruserId).observe(OtherUserProfileActivity.this, new Observer<Map>() {
            @Override
            public void onChanged(Map map) {
                if (map.get("success").equals("1")) {
                    firstTime = false;
                    Log.i("muteNotifications", map.get("message").toString());
                    if (map.get("status").equals("1")) {
                        mute_unmute = getString(R.string.unmute_notifications);
                    } else {
                        mute_unmute = getString(R.string.mute_notifications);
                    }
                    dialog.dismiss();
                } else {
                    Log.i("muteNotifications", map.get("message").toString());
                }
            }
        });
    }

    @Override
    public void followInterfaceCall(Map map) {
        if (map.get("success").equals("1")) {
            if (map.get("following_status").equals(true)) {
                btn_follow_user.setText(getString(R.string.following));
                chat_other_user.setVisibility(View.VISIBLE);
                recycler_videos_profile.setVisibility(View.VISIBLE);
            } else {
                btn_follow_user.setText(getString(R.string.follow));
                chat_other_user.setVisibility(View.GONE);
//                recycler_videos_profile.setVisibility(View.GONE);
            }
            tv_followers_other.setText(map.get("following_count").toString());

        } else {
            Log.i("follow", map.get("message").toString());
        }
    }


    private void showPrivetAccountDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("This Account is Private");
        builder.setMessage("Follow this user to see their videos.");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();

    }
}
