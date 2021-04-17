package in.pureway.cinemaflix.fragments.homefrags;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.downloader.PRDownloader;
import com.downloader.request.DownloadRequest;
import com.facebook.CallbackManager;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.card.MaterialCardView;
import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.activity.WebLinkOpenActivity;
import in.pureway.cinemaflix.activity.videoEditor.activity.DraftsActivity;
import in.pureway.cinemaflix.activity.FollowersFollowingActivity;
import in.pureway.cinemaflix.activity.login_register.LoginActivity;
import in.pureway.cinemaflix.activity.login_register.RegisterActivity;
import in.pureway.cinemaflix.activity.SelectedVideoActivity;
import in.pureway.cinemaflix.activity.editProfile.EditProfileActivity;
import in.pureway.cinemaflix.activity.FavoritesProfileActivity;
import in.pureway.cinemaflix.activity.findFriends.FindFriendsActivity;
import in.pureway.cinemaflix.activity.PrivacySettingsActivity;
import in.pureway.cinemaflix.activity.videoEditor.util.Variables;
import in.pureway.cinemaflix.adapters.AdapterVideoProfile;
import in.pureway.cinemaflix.javaClasses.FacebookLogin;
import in.pureway.cinemaflix.javaClasses.GoogleLogin;
import in.pureway.cinemaflix.javaClasses.ProfileData;
import in.pureway.cinemaflix.models.ModelLoginRegister;
import in.pureway.cinemaflix.models.ModelMyUploadedVideos;
import in.pureway.cinemaflix.models.OtherUserDataModel;
import in.pureway.cinemaflix.mvvm.ProfileMvvm;
import in.pureway.cinemaflix.mvvm.VideoMvvm;
import in.pureway.cinemaflix.utils.App;
import in.pureway.cinemaflix.utils.AppConstants;
import in.pureway.cinemaflix.utils.CommonUtils;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileHomeFragment extends Fragment implements View.OnClickListener {
    private View view;
    private ImageView img_profile_home, img_verified;
    private CircleImageView civ_user_profile;
    private TextView tv_name_profile, tv_bio_prof, tv_followers, tv_likes, tv_videos_count, tv_username_profile;
    private LinearLayout ll_profile_logged_in, llgift;
    private RelativeLayout rl_not_logged_in;
    private FacebookLogin facebookLogin;
    private CallbackManager callbackManager;
    private GoogleLogin googleLogin;
    private static final int RC_SIGN_IN = 007;
    private ProfileMvvm profileMvvm;
    private ShimmerFrameLayout shimmer_view_container;
    String likesCount = "0";
    private CardView card_no_videos;
    private RecyclerView recycler_videos_profile;
    private VideoMvvm videoMvvm;
    private List<ModelMyUploadedVideos.Detail> list = new ArrayList<>();
    private AdapterVideoProfile adapterVideoProfile;
    String userId = CommonUtils.userId(getActivity());
    DownloadRequest prDownloader;
    private ProfileData profileData;
    private String getCoin = "", status = "0";
    private OtherUserDataModel otherUserDataModelImage;
    private TextView tv_noti_badge_count, tvcoinbalance,tv_policyLink,tvTermLink;
    private RelativeLayout rl_not_badge;
    private TabLayout tablayout;
    private ViewPager viewpager;
    ModelLoginRegister personalData = App.getSharedpref().getModel(AppConstants.REGISTER_LOGIN_DATA, ModelLoginRegister.class);
    private Button btn_SocialMail, btn_facebook;
    RelativeLayout top_bar;
    private static final Pattern urlPattern = Pattern.compile(
            "(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)"
                    + "(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*"
                    + "[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{};']*)",
            Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
    public ProfileHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile_home, container, false);
        profileMvvm = ViewModelProviders.of(ProfileHomeFragment.this).get(ProfileMvvm.class);
        videoMvvm = ViewModelProviders.of(ProfileHomeFragment.this).get(VideoMvvm.class);
        PRDownloader.initialize(getActivity());
        findIds();
        App.getSingleton().setIdType(AppConstants.MY_ID);
        if (App.getSharedpref().isLogin(getActivity())) {
            top_bar.setVisibility(View.VISIBLE);

        }else {
            top_bar.setVisibility(View.GONE);

        }

        return view;
    }


    private void getStatus() {
        profileMvvm.getVerifyStatus(getActivity(), userId).observe(ProfileHomeFragment.this, new Observer<Map>() {
            @Override
            public void onChanged(Map map) {
                if (map.get("success").equals("1")) {
                    status = map.get("status").toString();
                    if (status.equals("1")) {
                        img_verified.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(getActivity(), map.get("message").toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void checkLogin() {
        if (App.getSharedpref().isLogin(getActivity())) {
            rl_not_logged_in.setVisibility(View.GONE);
            if (personalData.getDetails().getName() != null) {

                tv_name_profile.setText(personalData.getDetails().getName());
            } else {
                tv_name_profile.setText(personalData.getDetails().getUsername());
            }
            tv_username_profile.setText(personalData.getDetails().getUsername());
            if (personalData.getDetails().getBio().equalsIgnoreCase("")) {
                tv_bio_prof.setVisibility(View.GONE);
            } else {
                tv_bio_prof.setVisibility(View.VISIBLE);
                tv_bio_prof.setText(personalData.getDetails().getBio());
            }
            if (!personalData.getDetails().getVideo().equalsIgnoreCase("")) {
                img_profile_home.setVisibility(View.VISIBLE);
                civ_user_profile.setVisibility(View.GONE);
                Glide.with(getActivity()).load(personalData.getDetails().getVideo()).skipMemoryCache(true).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE).placeholder(getResources().getDrawable(R.drawable.ic_user1))).into(img_profile_home);
            } else {
                img_profile_home.setVisibility(View.GONE);
                civ_user_profile.setVisibility(View.VISIBLE);
                Glide.with(getActivity()).load(personalData.getDetails().getImage()).placeholder(getResources().getDrawable(R.drawable.ic_user1)).into(civ_user_profile);
            }
            rl_not_logged_in.setVisibility(View.VISIBLE);

            rl_not_logged_in.setVisibility(View.GONE);
            shimmer_view_container.startShimmer();
            getStatus();

//            String id = App.getSharedpref().getModel(AppConstants.REGISTER_LOGIN_DATA, ModelLoginRegister.class).getDetails().getId();
//            SQLiteDatabaseHandler sqLiteDatabaseHandler=SQLiteDatabaseHandler.getInstance(getActivity());
//            sqLiteDatabaseHandler.getModel(1,getActivity());
//            ModelLoginRegister.Details details = sqLiteDatabaseHandler.getModel(1,getActivity());
//            Toast.makeText(getActivity(), details.getId(), Toast.LENGTH_SHORT).show();

            profileData = new ProfileData(getActivity(), new ProfileData.followCallBack() {
                @Override
                public void followInterfaceCall(OtherUserDataModel otherUserDataModel) {
                    if (otherUserDataModel.getSuccess().equalsIgnoreCase("1")) {

                        try {

                            AdapterVP adapter = new AdapterVP(getChildFragmentManager());

                            adapter.addFrag(new ProfileVideosFragment(CommonUtils.userId(getActivity())));
                            adapter.addFrag(new ProfleLikedFragment(CommonUtils.userId(getActivity())));
                            viewpager.setAdapter(adapter);
                        } catch (Exception e) {
                            Log.i("123456789", e.toString());
                        }

                        try {
                            tablayout.setupWithViewPager(viewpager);
                            tablayout.getTabAt(0).setIcon(R.drawable.ic_box_menu);
                            tablayout.getTabAt(1).setIcon(R.drawable.ic_heart);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

//                        tablayout.setupWithViewPager(viewpager);
//                        tablayout.getTabAt(0).setIcon(R.drawable.ic_box_menu);
//                        tablayout.getTabAt(1).setIcon(R.drawable.ic_heart);

                        otherUserDataModelImage = otherUserDataModel;

                        shimmer_view_container.stopShimmer();
                        shimmer_view_container.setVisibility(View.GONE);
                        ll_profile_logged_in.setVisibility(View.VISIBLE);
                        getCoin = otherUserDataModel.getDetails().getCoin();
                        likesCount = String.valueOf(otherUserDataModel.getDetails().getLikes());

                        if (App.getSharedpref().isLogin(getActivity())) {
                            if (!otherUserDataModel.getNotificationCount().equalsIgnoreCase("0")) {
                                tv_noti_badge_count.setText(otherUserDataModel.getNotificationCount());
                                rl_not_badge.setVisibility(View.VISIBLE);
                            } else {
                                rl_not_badge.setVisibility(View.GONE);
                            }
                        } else {
                            rl_not_badge.setVisibility(View.GONE);
                        }

                        if (otherUserDataModel.getDetails().getProfilePhotoStatus()) {
                            if (!otherUserDataModel.getDetails().getVideo().equalsIgnoreCase("")) {
                                img_profile_home.setVisibility(View.VISIBLE);
                                civ_user_profile.setVisibility(View.GONE);
                                Glide.with(getActivity()).load(otherUserDataModel.getDetails().getVideo()).skipMemoryCache(true).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE).placeholder(getResources().getDrawable(R.drawable.ic_user1))).into(img_profile_home);
                            } else {
                                img_profile_home.setVisibility(View.GONE);
                                civ_user_profile.setVisibility(View.VISIBLE);
                                Glide.with(getContext()).load(otherUserDataModel.getDetails().getImage()).placeholder(getResources().getDrawable(R.drawable.ic_user1)).into(civ_user_profile);
                            }
                        } else {
                            img_profile_home.setVisibility(View.GONE);
                            civ_user_profile.setVisibility(View.VISIBLE);

                            try {
                                Glide.with(getActivity()).load(otherUserDataModel.getDetails().getImage()).placeholder(getResources().getDrawable(R.drawable.ic_user1)).into(civ_user_profile);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
//                            Glide.with(getActivity()).load(otherUserDataModel.getDetails().getImage()).placeholder(getResources().getDrawable(R.drawable.ic_user1)).into(civ_user_profile);
                        }
                        if (otherUserDataModel.getDetails().getName() != null) {
                            tv_name_profile.setText(otherUserDataModel.getDetails().getName());
                        } else {
                            tv_name_profile.setText(otherUserDataModel.getDetails().getUsername());
                        }
                        tv_username_profile.setText(otherUserDataModel.getDetails().getUsername());

                        if (otherUserDataModel.getDetails().getBio().equalsIgnoreCase("")) {
                            tv_bio_prof.setVisibility(View.GONE);
                        } else {
                            tv_bio_prof.setVisibility(View.VISIBLE);
                            tv_bio_prof.setText(otherUserDataModel.getDetails().getBio());

                            tv_bio_prof.setOnClickListener(new View.OnClickListener() {
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

                        }
//                        if (!otherUserDataModel.getDetails().getLikes().equalsIgnoreCase("")) {
//                            tv_likes.setText(otherUserDataModel.getDetails().getLikes());
//
//                        }

                        if (otherUserDataModel.getDetails().getLikes() != null) {
                            if (otherUserDataModel.getDetails().getLikes() == "") {
                                tv_likes.setText("0");
                            } else {
                                tv_likes.setText(String.valueOf(otherUserDataModel.getDetails().getLikes()));
                            }

                        }
                        tvcoinbalance.setText(otherUserDataModel.getDetails().getCoin());
                        tv_followers.setText(otherUserDataModel.getDetails().getFollowers());
                        tv_videos_count.setText(otherUserDataModel.getDetails().getFollowing());

                    } else {
                        shimmer_view_container.stopShimmer();
                        shimmer_view_container.setVisibility(View.GONE);
                        ll_profile_logged_in.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void uploadedVideosCall(ModelMyUploadedVideos modelMyUploadedVideos) {
                    if (modelMyUploadedVideos.getSuccess().equalsIgnoreCase("1")) {
                        list = modelMyUploadedVideos.getDetails();
                        adapterVideoProfile = new AdapterVideoProfile(getActivity(), list, new AdapterVideoProfile.Select() {
                            @Override
                            public void drafts(int position) {
                                if (position == 0) {
                                    //check if drafts has files in it
                                }
                            }

                            @Override
                            public void videoPlayList(int position) {
//                                Intent intent = new Intent(getActivity(), SingleVideoPlayerActivity.class);
                                Intent intent = new Intent(getActivity(), SelectedVideoActivity.class);
                                App.getSingleton().setListMyUploaded(list);
                                intent.putExtra(AppConstants.POSITION, position);
                                intent.putExtra(AppConstants.CHECK_VIDEO, 0);
                                intent.putExtra(AppConstants.SINGLE_VIDEO_TYPE, AppConstants.TYPE_UPLOADED);
                                startActivity(intent);
                            }

                            @Override
                            public void onLongClick(int position) {
                                showDeleteDialog(list.get(position).getId(), position, list.get(position).getVideoPath());
                            }

                        }, userId, userId);
                        recycler_videos_profile.setAdapter(adapterVideoProfile);
                    } else {
                        recycler_videos_profile.setVisibility(View.GONE);
                        card_no_videos.setVisibility(View.VISIBLE);
                    }
                }
            });
            getProfileData();
//            setRecycler();
        } else {
            ll_profile_logged_in.setVisibility(View.GONE);
            rl_not_logged_in.setVisibility(View.VISIBLE);
        }
    }

    private void findIds() {

        tvTermLink = view.findViewById(R.id.tvTermLink);
        tv_policyLink = view.findViewById(R.id.tv_policyLink);


        top_bar = view.findViewById(R.id.top_bar);
        btn_SocialMail = view.findViewById(R.id.btn_SocialMail);
        btn_facebook = view.findViewById(R.id.btn_facebook);


        tablayout = view.findViewById(R.id.tablayout);
        viewpager = view.findViewById(R.id.viewpager);


        tvcoinbalance = view.findViewById(R.id.tv_coinBalanceProfile);
        llgift = view.findViewById(R.id.ll_gift);
        llgift.setOnClickListener(this);

        rl_not_badge = getActivity().findViewById(R.id.rl_not_badge);
        tv_noti_badge_count = getActivity().findViewById(R.id.tv_noti_badge_count);
        civ_user_profile = view.findViewById(R.id.civ_user_profile);
        img_verified = view.findViewById(R.id.img_verified);
        shimmer_view_container = view.findViewById(R.id.shimmer_view_container);
        tv_likes = view.findViewById(R.id.tv_likes);
        tv_followers = view.findViewById(R.id.tv_followers);
        tv_videos_count = view.findViewById(R.id.tv_videos_count);
        img_profile_home = view.findViewById(R.id.img_profile_home);
        tv_bio_prof = view.findViewById(R.id.tv_bio_prof);
        tv_name_profile = view.findViewById(R.id.tv_name_profile);
        tv_username_profile = view.findViewById(R.id.tv_username_profile);
        rl_not_logged_in = view.findViewById(R.id.rl_not_logged_in);
        ll_profile_logged_in = view.findViewById(R.id.ll_profile_logged_in);
        view.findViewById(R.id.ll_followers).setOnClickListener(this);
        view.findViewById(R.id.ll_following).setOnClickListener(this);
        view.findViewById(R.id.ll_likes).setOnClickListener(this);
        view.findViewById(R.id.btn_signin).setOnClickListener(this);
        view.findViewById(R.id.btn_signup).setOnClickListener(this);

//        view.findViewById(R.id.img_google_login).setOnClickListener(this);
        view.findViewById(R.id.btn_SocialMail).setOnClickListener(this);

        view.findViewById(R.id.btn_facebook).setOnClickListener(this);
//        view.findViewById(R.id.img_fb_login).setOnClickListener(this);
        view.findViewById(R.id.img_add_friends).setOnClickListener(this);
        view.findViewById(R.id.img_settings_profile).setOnClickListener(this);
        view.findViewById(R.id.btn_edit_profile).setOnClickListener(this);
        view.findViewById(R.id.card_favorites_profile).setOnClickListener(this);
        view.findViewById(R.id.card_drafts).setOnClickListener(this);
        civ_user_profile.setOnClickListener(this);
        img_profile_home.setOnClickListener(this);

        tvTermLink.setOnClickListener(this);
        tv_policyLink.setOnClickListener(this);

        card_no_videos = view.findViewById(R.id.card_no_videos);
        recycler_videos_profile = view.findViewById(R.id.recycler_videos_profile);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_add_friends:
//                if (App.getSharedpref().isLogin(getActivity())) {
//                    startActivity(new Intent(getActivity(), FindFriendsActivity.class));
//                    getActivity().overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
//                } else {
//                    Toast.makeText(getActivity(), R.string.login_to_use_thisfeature, Toast.LENGTH_SHORT).show();
//                }

                break;

            case R.id.card_drafts:
                if (getListFiles(new File(Variables.draft_app_folder)).size() != 0) {
                    startActivity(new Intent(getActivity(), DraftsActivity.class));
                } else {
                    Toast.makeText(getActivity(), R.string.draftes_empty, Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.img_settings_profile:
                if (App.getSharedpref().isLogin(getActivity())) {
                    startActivity(new Intent(getActivity(), PrivacySettingsActivity.class).putExtra("coin", getCoin).putExtra("status", status));
                    getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                } else {

                }
                break;
            case R.id.btn_edit_profile:
                startActivity(new Intent(getActivity(), EditProfileActivity.class));
                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
            case R.id.card_favorites_profile:
                startActivity(new Intent(getActivity(), FavoritesProfileActivity.class));
                break;

            case R.id.btn_facebook:

                callbackManager = CallbackManager.Factory.create();
                facebookLogin = new FacebookLogin(getActivity(), AppConstants.LOGIN_PROFILE, getActivity().getApplication());
                facebookLogin.FBLogin();
                break;

            case R.id.btn_SocialMail:
                App.getSingleton().setLoginType(AppConstants.LOGIN_PROFILE);
                googleLogin = new GoogleLogin(getActivity(), AppConstants.LOGIN_PROFILE);
                googleLogin.signIn();
                break;

            case R.id.btn_signup:
                startActivity(new Intent(getActivity(), RegisterActivity.class));
                break;

            case R.id.btn_signin:
                startActivity(new Intent(getActivity(), LoginActivity.class));
                break;

            case R.id.civ_user_profile:
            case R.id.img_profile_home:
                openImageDialog(otherUserDataModelImage);
                break;

            case R.id.ll_likes:
                openLikeDialog();
                break;

            case R.id.ll_followers:
                startActivity(new Intent(getActivity(), FollowersFollowingActivity.class)
                        .putExtra(AppConstants.USER_ID_FOLLOW_UNFOLLOW, CommonUtils.userId(getActivity()))
                        .putExtra(AppConstants.POSITION, 1));
                break;
            case R.id.ll_following:
                startActivity(new Intent(getActivity(), FollowersFollowingActivity.class)
                        .putExtra(AppConstants.USER_ID_FOLLOW_UNFOLLOW, CommonUtils.userId(getActivity()))
                        .putExtra(AppConstants.POSITION, 0));
                break;

            case R.id.ll_gift:
//                startActivity(new Intent(getActivity(), WalletActivity.class).putExtra("coin",getCoin));
                break;

            case R.id.tv_policyLink:

                Intent intent=new Intent(getActivity(), WebLinkOpenActivity.class);
                intent.putExtra("key","1");
                intent.putExtra("title","CINEMAFLIX Privacy Policy");
                startActivity(intent);
                break;

            case R.id.tvTermLink:

                Intent intent1=new Intent(getActivity(), WebLinkOpenActivity.class);
                intent1.putExtra("key","0");
                intent1.putExtra("title","Terms and Conditions");
                startActivity(intent1);

                break;
        }
    }

    private void openLikeDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        final View confirmdailog = layoutInflater.inflate(R.layout.dialog_likes, null);
        final AlertDialog dailogbox = new AlertDialog.Builder(getActivity()).create();
        dailogbox.setCancelable(true);
        dailogbox.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dailogbox.setView(confirmdailog);

        TextView tv_total_likes = confirmdailog.findViewById(R.id.tv_total_likes);
        String totalLikes = App.getSharedpref().getModel(AppConstants.REGISTER_LOGIN_DATA, ModelLoginRegister.class).getDetails().getUsername() + " received " + likesCount + "\nlikes.";
        tv_total_likes.setText(totalLikes);
        confirmdailog.findViewById(R.id.tv_ok_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dailogbox.dismiss();
            }
        });
        dailogbox.show();
    }

    private void openImageDialog(OtherUserDataModel otherUserDataModel) {
        final Dialog dialog;
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        final View confirmdailog = layoutInflater.inflate(R.layout.dialog_show_code, null);
        dialog = new Dialog(getActivity(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setCancelable(true);
        dialog.setContentView(confirmdailog);
        ImageView img_user = confirmdailog.findViewById(R.id.img_user);
        ImageView img_gif_user = confirmdailog.findViewById(R.id.img_gif_user);

        if (otherUserDataModel.getDetails().getProfilePhotoStatus()) {
            if (!otherUserDataModel.getDetails().getVideo().equalsIgnoreCase("")) {
                img_gif_user.setVisibility(View.VISIBLE);
                img_user.setVisibility(View.GONE);
                Glide.with(getActivity()).load(otherUserDataModel.getDetails().getVideo()).skipMemoryCache(true).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE).placeholder(getResources().getDrawable(R.drawable.ic_user1))).into(img_gif_user);
            } else {
                img_gif_user.setVisibility(View.GONE);
                img_user.setVisibility(View.VISIBLE);
                Glide.with(getActivity()).load(otherUserDataModel.getDetails().getImage()).placeholder(getResources().getDrawable(R.drawable.ic_user1)).into(img_user);
            }
        } else {
            img_user.setVisibility(View.VISIBLE);
            Glide.with(getActivity()).load(otherUserDataModel.getDetails().getImage()).placeholder(getResources().getDrawable(R.drawable.ic_user1)).into(img_user);
        }
        confirmdailog.findViewById(R.id.img_back_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                CommonUtils.hideNavigation(getActivity());
            }
        });
        dialog.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            googleLogin.activityResult(requestCode, resultCode, data);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        checkLogin();
    }

    private void getProfileData() {
        profileData.getProfileData(userId);

    }

    private void setRecycler() {
        profileData.getUploadedVideo(userId, userId);
    }

    private void showDeleteDialog(final String id, final int position, final String path) {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        final View confirmdailog = layoutInflater.inflate(R.layout.dialog_delete_video, null);
        final AlertDialog dailogbox = new AlertDialog.Builder(getActivity()).create();
        dailogbox.setCancelable(true);
        dailogbox.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dailogbox.setView(confirmdailog);
        confirmdailog.findViewById(R.id.tv_delete_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteVideo(id, position, dailogbox);
            }
        });
        confirmdailog.findViewById(R.id.tv_share_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                CommonUtils.shareIntent();
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


    private void deleteVideo(String id, final int position, final Dialog dialog) {
        videoMvvm.videoDelete(getActivity(), id).observe(getActivity(), new Observer<Map>() {
            @Override
            public void onChanged(Map map) {
                if (map.get("success").equals("1")) {
                    Toast.makeText(getActivity(), "Video Deleted", Toast.LENGTH_SHORT).show();
                    list.remove(position);
                    adapterVideoProfile.notifyDataSetChanged();
                    dialog.dismiss();
                } else {
                    Toast.makeText(getActivity(), map.get("message").toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //get files from drafts folder
    public static ArrayList<File> getListFiles(File parentDir) {
        ArrayList<File> inFiles = new ArrayList<File>();
        File[] files;
        files = parentDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().endsWith(".mp4")) {
                    if (!inFiles.contains(file)) inFiles.add(file);
                    Log.i("draftsFile", file.toString());
                }
            }
        }
        return inFiles;
    }
}
