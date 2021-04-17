package in.pureway.cinemaflix.fragments.homefrags;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.activity.HashTagVideoActivity;
import in.pureway.cinemaflix.activity.OtherUserProfileActivity;
import in.pureway.cinemaflix.activity.ReportProblemActivity;
import in.pureway.cinemaflix.activity.SoundVideoActivity;
import in.pureway.cinemaflix.activity.WebLinkOpenActivity;
import in.pureway.cinemaflix.activity.login_register.LoginActivity;
import in.pureway.cinemaflix.activity.login_register.RegisterActivity;
import in.pureway.cinemaflix.activity.videoEditor.activity.MyVideoEditorActivity;
import in.pureway.cinemaflix.adapters.AdapterVideoHome;
import in.pureway.cinemaflix.adapters.AdapterVideoHome1;
import in.pureway.cinemaflix.adapters.ModelVideoTesting;
import in.pureway.cinemaflix.javaClasses.FacebookLogin;
import in.pureway.cinemaflix.javaClasses.FollowUnfollowUser;
import in.pureway.cinemaflix.javaClasses.GoogleLogin;
import in.pureway.cinemaflix.javaClasses.ViewVideo;
import in.pureway.cinemaflix.javaClasses.commentsInterface;
import in.pureway.cinemaflix.models.ModelVideoList;
import in.pureway.cinemaflix.mvvm.VideoMvvm;
import in.pureway.cinemaflix.utils.App;
import in.pureway.cinemaflix.utils.AppConstants;
import in.pureway.cinemaflix.utils.CommonUtils;

import com.bumptech.glide.Glide;
import com.danikula.videocache.HttpProxyCacheServer;
import com.facebook.CallbackManager;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
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
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.CacheKeyFactory;
import com.google.android.exoplayer2.upstream.cache.CacheUtil;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoListener;
import com.google.android.gms.ads.MobileAds;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;

public class VideoLongFragment extends Fragment implements commentsInterface, Player.EventListener, View.OnClickListener {
    private View view;
    private RecyclerView recycler_video;
    private GoogleLogin googleLogin;
    private FacebookLogin facebookLogin;
    private CallbackManager callbackManager;
    //    private List<ModelVideoList.Detail> list = new ArrayList<>();
    private List<ModelVideoTesting.Detail> list = new ArrayList<>();
    private VideoMvvm videoMvvm;
    int currentPage = -1;
    LinearLayoutManager layoutManager;
    boolean is_visible_to_user = true;
    boolean is_user_stop_video = false;
    SpinKitView p_bar;
    private RelativeLayout rl_no_internet;
    boolean isScrolling = false;
    int currentItems, totalItem, scrollOutItems, page = 1;
    private AdapterVideoHome1 adapterVideoHome;
    private ViewVideo viewVideo;
    private TextView tv_noti_badge_count, tvCount, tv_policyLink, tvTermLink;
    private RelativeLayout rl_not_badge;

    //    private InterstitialAd interstitialAd;
    private int videoCount = 0;
    private Handler handler = new Handler();
    private int progressStatus = 0;
    private String progressStatusS = "0";
    ProgressBar progressBar;
    int a, b;
    private String stopProgrssValue;
    private CountDownTimer countDownTimer;
    private int proStatusPlayPo = 0;
    private LinearLayout ll_notLogin, ll_phoneLogin, ll_google, ll_facebook;
    private Button btn_signup, btn_SocialMail, btn_facebook;
    private String backStatus = "0";
    private ImageView img_close_Login;
    private String status = "0";
    private String typeAdmin = "1";
    CommentFragment commentFragment;
    TextView follow;
    // private List<GiftModel.Detail> listgift = new ArrayList<>();
    public VideoLongFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
// Inflate the layout for this fragment
        adapterAdd = true;
        view = inflater.inflate(R.layout.fragment_video_hot, container, false);
        videoMvvm = ViewModelProviders.of(VideoLongFragment.this).get(VideoMvvm.class);
        callbackManager = CallbackManager.Factory.create();
        viewVideo = new ViewVideo(getActivity());
//        screenOn();

                getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        initids();
        setRecycler();
        status = "0";


        App.getAppContext().setCommentsInterface(this);
        MobileAds.initialize(getActivity(), AppConstants.AD_UNIT_ID);

        setAdapter();

//        interstitialAd = new InterstitialAd(getActivity());
//        interstitialAd.setAdUnitId(AppConstants.AD_UNIT_ID);
//        interstitialAd.loadAd(new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build());

//        adsListeners();
        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                currentPage = currentPage + 1;
//                    ll_notLogin.setVisibility(View.VISIBLE);

                if (backStatus == "0") {
//                    ll_notLogin.setVisibility(View.VISIBLE);
                    if (status == "1") {
                        getActivity().finish();
                    } else {
                        status = "1";
                        adapterAdd = true;
                        isScrolling = true;
//                    currentItems = layoutManager.getChildCount();
//                    totalItem = layoutManager.getItemCount();
//                    scrollOutItems = layoutManager.findFirstVisibleItemPosition();
                        Release_Privious_Player();

                        recycler_video.scrollToPosition(currentPage);


                        Set_Player(currentPage);
//                    setRecycler();
//                    adapterVideoHome.notifyDataSetChanged();


                        callApiVideoList(String.valueOf(currentPage));

                        Toast.makeText(getActivity(), "Please click BACK again to Home exit", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    privious_player.setPlayWhenReady(true);
                    screenOn();
                    ll_notLogin.setVisibility(View.GONE);
                    backStatus = "0";
                }
                new Handler().postDelayed(new Runnable() {
                    //
                    @Override
                    public void run() {

                        status = "0";
                        backStatus = "0";
                    }
                }, 2000);
            }
        };
        getActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), onBackPressedCallback);
        return view;
    }

    private void setAdapter() {
        adapterVideoHome = new AdapterVideoHome1(getActivity(), list, new AdapterVideoHome1.Select() {
            @Override
            public void sounds(int position) {

//
//                            App.getSingleton().setSoundId(list.get(position).getSoundId());
//                            startActivity(new Intent(getActivity(), SoundVideoActivity.class));
            }

            @Override
            public void folowUnfollow(String userId, int position) {
                if (App.getSharedpref().isLogin(getActivity())) {
                    followUser(userId, position);

                } else {
                    recycler_video.setVisibility(View.VISIBLE);
                    openLogin();
                }
            }


            @Override
            public void duetLayout(int position) {
                Intent intent = new Intent(getActivity(), MyVideoEditorActivity.class);
                intent.putExtra("video_duet", list.get(position).getVideoPath());
                startActivity(intent);


            }

            @Override
            public void download(int position) {
                Toast.makeText(getActivity(), "coming soon...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void hashTags(String hashtagId) {
                startActivity(new Intent(getActivity(), HashTagVideoActivity.class).putExtra(AppConstants.HASHTAG_ID, hashtagId));
            }

            @Override
            public void sideMenu(int position) {
                dialogReportVideo(list.get(position).getId());
            }

            @Override
            public void adMoveTOBroswer(int position) {


                if (App.getSharedpref().isLogin(getActivity())) {
                    typeAdmin = "2";
                    adminViewCount(list.get(position).getId(), position, typeAdmin);
                } else {

                    try {
                        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(list.get(position).getVideoUrl()));
                        startActivity(myIntent);
                    } catch (ActivityNotFoundException e) {

                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void comments(int position) {
                String allowcomments = ((list.get(position).getAllowComment()));
                commentFragment = new CommentFragment(list.get(position).getId(), allowcomments, list.get(position).getUserId(), position, new CommentFragment.BottomClick() {
                    @Override
                    public void position(String status) {
                        backStatus = "1";
                        ll_notLogin.setVisibility(View.VISIBLE);
                        privious_player.setPlayWhenReady(false);
                        screenOff();
                        commentFragment.dismiss();
                    }
                });
                commentFragment.show(getChildFragmentManager(), commentFragment.getTag());

            }

            @Override
            public void user(int position) {
                if (App.getSingleton().getBlockedUserList() != null && App.getSingleton().getBlockedUserList().size() > 0) {
                    List<String> blockedUserList = App.getSingleton().getBlockedUserList();
                    for (int i = 0; i < blockedUserList.size(); i++) {
                        if (blockedUserList.get(i).equalsIgnoreCase(list.get(position).getUserId())) {
                            Toast.makeText(getActivity(), "User Blocked", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }
                startActivity(new Intent(getActivity(), OtherUserProfileActivity.class).putExtra(AppConstants.OTHER_USER_ID, list.get(position).getUserId()));
                getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            }

            @Override
            public void share(int position) {
                try {
                    CommonUtils.shareVideoDownload(getActivity(), list.get(position).getDownloadPath());
                } catch (Exception e) {
                    Log.i("shareVideoHome", e.getMessage());
                }
            }

            @Override
            public void shareAddVideo(int position) {
                try {
                    CommonUtils.shareVideoDownload(getActivity(), list.get(position).getDownloadPath());
                } catch (Exception e) {
                    Log.i("shareVideoHome", e.getMessage());
                }

            }

            @Override
            public void likeVideo(int position) {
                if (App.getSharedpref().isLogin(getActivity())) {
                    likeApi(list.get(position).getId(), list.get(position).getUserId(), position, 1);
                } else {
                    openLogin();
                }
            }

            @Override
            public void unlikeVideo(int position) {
                if (App.getSharedpref().isLogin(getActivity())) {
                    likeApi(list.get(position).getId(), list.get(position).getUserId(), position, 1);

                } else {
                    openLogin();
                }
            }

            @Override
            public void addFriends(int position) {
                startActivity(new Intent(getActivity(), OtherUserProfileActivity.class));
                getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            }

        });
    }

    private void setRecycler() {
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        SnapHelper snapHelper = new PagerSnapHelper();
        recycler_video.setItemAnimator(new DefaultItemAnimator());
        recycler_video.setLayoutManager(layoutManager);
        if (status.equalsIgnoreCase("0")) {
            snapHelper.attachToRecyclerView(recycler_video);

        }
//        snapHelper.attachToRecyclerView(recycler_video);


// this is the scroll listener of recycler view which will tell the current item number
        recycler_video.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;


                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
//here we find the current item number
                currentItems = layoutManager.getChildCount();
                totalItem = layoutManager.getItemCount();
                scrollOutItems = layoutManager.findFirstVisibleItemPosition();

                final int scrollOffset = recyclerView.computeVerticalScrollOffset();
                final int height = recyclerView.getHeight();
                int page_no = scrollOffset / height;
                if (page_no != currentPage) {
                    currentPage = page_no;
                    Release_Privious_Player();
                    Set_Player(currentPage);

//                    setProgressPos(currentPage);

//                    if (list.get(currentPage).getVideolength() != null && list.get(currentPage).getVideolength() != "") {
//                        progressStatusS = list.get(currentPage).getVideolength();
//
//                        pRogressBar(progressStatusS);
//                    }
//                    progressStatusS="0";


                    Log.i("scroolll", "current:" + currentPage);
                    Log.i("scroolll", "page:" + page_no);
// Log.i("scroolll","scrolled:"+scrollOutItems);


                }

                if (page_no + 2 == list.size()) {


                    isScrolling = false;
                    page = page + 5;
                    callApiVideoList(String.valueOf(page));
                    Log.i("scroolll", "pageNewList:" + String.valueOf(page));
//data fetch
                }
            }
        });
        callApiVideoList(String.valueOf(page));
    }


    private void setProgressPos(int currentPage) {
        if (list.get(currentPage).getVideolength() != null && list.get(currentPage).getVideolength() != "") {
            progressStatusS = list.get(currentPage).getVideolength();

            pRogressBar(progressStatusS);
//                        startTimer(progressStatusS);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }


    private void pRogressBar(String progressStatusS) {
        progressStatus = Integer.parseInt(progressStatusS);
//        Toast.makeText(getActivity(), progressStatusS, Toast.LENGTH_SHORT).show();

        b = 0;
        progressBar.setProgress(0);
        if (progressStatus == 1) {
            progressBar.setMax(progressStatus);


        } else {
            progressBar.setMax(progressStatus);

        }


        new Thread(new Runnable() {
            public void run() {
                while (b < progressStatus) {

//
                    handler.post(new Runnable() {
                        public void run() {
                            stopProgrssValue = String.valueOf(b);

                            progressBar.setProgress(b);
                        }

                    });
                    b += 1;
                    try {

                        // Sleep for 200 milliseconds.
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();


    }


//    private void adsListeners() {
//        interstitialAd.setAdListener(new AdListener() {
//            @Override
//            public void onAdLoaded() {
//                // Code to be executed when an ad finishes loading.
//            }
//
//            @Override
//            public void onAdFailedToLoad(LoadAdError adError) {
//                // Code to be executed when an ad request fails.
//            }
//
//            @Override
//            public void onAdOpened() {
//                // Code to be executed when the ad is displayed.
//            }
//
//            @Override
//            public void onAdClicked() {
//                // Code to be executed when the user clicks on an ad.
//            }
//
//            @Override
//            public void onAdLeftApplication() {
//                // Code to be executed when the user has left the app.
//            }
//
//            @Override
//            public void onAdClosed() {
//                // Code to be executed when the interstitial ad is closed.
//                interstitialAd.loadAd(new AdRequest.Builder().build());
//            }
//        });
//
//        interstitialAd.setAdListener(new AdListener() {
//            @Override
//            public void onAdClosed() {
//                // Load the next interstitial.
//                interstitialAd.loadAd(new AdRequest.Builder().build());
//            }
//        });
//    }


    public void Set_Player(final int currentPage) {

//        if (list.get(currentPage).getAdvertisementStatus().equalsIgnoreCase("0")){
//
//            viewVideo.viewVideoAPi(list.get(currentPage).getId());
//
//        }

        screenOn();
        try {

//            if (list!=null) {
//                if (list.get(currentPage).getAdvertisementStatus().equalsIgnoreCase("1")) {
//
//                    typeAdmin = "1";
//                    String id = list.get(currentPage).getId();
//
//                    Log.i("videoView_1", "check view ========" + list.get(currentPage).getId() + " page :" + currentPage);
//                    adminViewCount(id, currentPage, typeAdmin);
//                }
//            }
            final ModelVideoTesting.Detail item = list.get(currentPage);
            DefaultTrackSelector trackSelector = new DefaultTrackSelector();
            HttpProxyCacheServer proxy = App.getProxy(getApplicationContext());
            String proxyUrl = proxy.getProxyUrl(item.getVideoPath());
            final SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector);
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getActivity(),
                    Util.getUserAgent(getActivity(), "Cinemaflix"));
            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(Uri.parse(proxyUrl));

            player.prepare(videoSource);
            player.setRepeatMode(Player.REPEAT_MODE_ALL);
            player.addListener(this);


            View layout = layoutManager.findViewByPosition(currentPage);

            final PlayerView playerView = layout.findViewById(R.id.playerview);
            progressBar = layout.findViewById(R.id.tv_progress_bar);
            playerView.setPlayer(player);
            playerView.getControllerShowTimeoutMs();
            player.setPlayWhenReady(is_visible_to_user);
            privious_player = player;

            playerView.setKeepScreenOn(true);

            player.addListener(new Player.EventListener() {
                @Override
                public void onTimelineChanged(Timeline timeline, int reason) {

                }

                @Override
                public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason) {

                }

                @Override
                public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

                }

                @Override
                public void onLoadingChanged(boolean isLoading) {

                }

                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                    if (playbackState == Player.STATE_BUFFERING) {
                        p_bar.setVisibility(View.VISIBLE);
                    } else if (playbackState == Player.STATE_READY) {
                        p_bar.setVisibility(View.GONE);
                    }

                }

                @Override
                public void onPlaybackSuppressionReasonChanged(int playbackSuppressionReason) {

                }

                @Override
                public void onIsPlayingChanged(boolean isPlaying) {

                }

                @Override
                public void onRepeatModeChanged(int repeatMode) {

                }

                @Override
                public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

                }

                @Override
                public void onPlayerError(ExoPlaybackException error) {

                }

                @Override
                public void onPositionDiscontinuity(int reason) {

                }

                @Override
                public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

                }

                @Override
                public void onSeekProcessed() {

                }
            });

//            playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
//            player.setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);

//            player.addVideoListener(new VideoListener() {
//                @Override
//                public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
////                    if (pixelWidthHeightRatio > 1.1 || height < width) {
////                        playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
////                    } else {
////                        playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
////                    }
//                }
//
//                @Override
//                public void onRenderedFirstFrame() {
//
//                }
//            });
            final RelativeLayout mainlayout = layout.findViewById(R.id.rl_main_video);
            mainlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressStatus = 1;
                    pRogressBar(stopProgrssValue);


                }
            });
            playerView.setOnTouchListener(new View.OnTouchListener() {
                private GestureDetector gestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {

                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                        super.onFling(e1, e2, velocityX, velocityY);
                        float deltaX = e1.getX() - e2.getX();
                        float deltaXAbs = Math.abs(deltaX);
                        if ((deltaXAbs > 100) && (deltaXAbs < 1000)) {
                            if (deltaX > 0) {
                            }
                        }
                        return true;
                    }

                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        super.onSingleTapUp(e);
                        if (!player.getPlayWhenReady()) {
                            is_user_stop_video = false;
                            privious_player.setPlayWhenReady(true);
                            screenOn();

                        } else {
                            is_user_stop_video = true;
                            privious_player.setPlayWhenReady(false);
                            screenOff();

                        }


                        return true;
                    }

                    @Override
                    public void onLongPress(MotionEvent e) {
                        super.onLongPress(e);

                    }

                    @Override
                    public boolean onDoubleTap(MotionEvent e) {
                        if (!player.getPlayWhenReady()) {
                            is_user_stop_video = false;
                            privious_player.setPlayWhenReady(true);
                            screenOn();

                        }

                        if (App.getSharedpref().isLogin(getActivity())) {
                            if (list.get(currentPage).getLikeStatus() == false) {
                                Show_heart_on_DoubleTap(item, mainlayout, e);

                                likeApi(list.get(currentPage).getId(), list.get(currentPage).getUserId(), currentPage, 1);
                            }
                        } else {

                            recycler_video.setVisibility(View.VISIBLE);
                            openLogin();
                        }
                        return super.onDoubleTap(e);
                    }
                });

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    gestureDetector.onTouchEvent(event);
                    return true;
                }
            });
            playerView.setVisibility(View.VISIBLE);

            ImageView soundimage = (ImageView) layout.findViewById(R.id.sound_image_layout);
//            Animation sound_animation = AnimationUtils.loadAnimation(getActivity(), R.anim.d_clockwise_rotation);
//            soundimage.startAnimation(sound_animation);
        } catch (Exception e) {

        }

    }

    private void openLogin() {
        backStatus = "1";
        ll_notLogin.setVisibility(View.VISIBLE);
        privious_player.setPlayWhenReady(false);

        screenOff();

    }

    private void callApiVideoList(final String count) {
        String userId = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.i("deviceId", userId);
        if (App.getSharedpref().isLogin(getActivity())) {
            userId = CommonUtils.userId(getActivity());
        }


        videoMvvm.videoTesting(getActivity(), userId, count, "trending", 1, rl_no_internet, recycler_video, p_bar).observe(getActivity(), new Observer<ModelVideoTesting>() {
            @Override
            public void onChanged(ModelVideoTesting modelVideoList) {
                if (modelVideoList.getSuccess().equalsIgnoreCase("1")) {
                    if (rl_no_internet.getVisibility() == View.VISIBLE) {
                        rl_no_internet.setVisibility(View.GONE);
                        recycler_video.setVisibility(View.VISIBLE);
                        p_bar.setVisibility(View.VISIBLE);
                    }

                    if (App.getSharedpref().isLogin(getActivity())) {
                        if (!modelVideoList.getNotificationCount().equalsIgnoreCase("0")) {
                            rl_not_badge.setVisibility(View.VISIBLE);
                            tvCount.setText(modelVideoList.getNotificationCount());

                        } else {
                            rl_not_badge.setVisibility(View.GONE);
                        }
                    } else {
                        rl_not_badge.setVisibility(View.GONE);
                    }
                    list.addAll(list.size(), modelVideoList.getDetails());

                    Log.i("scroolll", "list.size:" + list.size());


                    int heightRecycler = recycler_video.getHeight();
                    if (adapterAdd) {
                        recycler_video.setAdapter(adapterVideoHome);
                        adapterAdd = false;
                        if (currentPage > 0) {
                            recycler_video.scrollToPosition(currentPage - 1);
                            recycler_video.smoothScrollBy(0, heightRecycler);


//                            progressStatusS = "0";
                        } else {
                            recycler_video.smoothScrollToPosition(0);
                        }
                    }
                    adapterVideoHome.notifyDataSetChanged();
                    mainCachingFunction();
                } else {
                    Log.i("myvideoList", modelVideoList.getMessage());
                }
            }
        });

//        videoMvvm.videoList(getActivity(), userId, count, "trending", 1, rl_no_internet, recycler_video, p_bar).observe(getActivity(), new Observer<ModelVideoList>() {
//            @Override
//            public void onChanged(ModelVideoList modelVideoList) {
//                if (modelVideoList.getSuccess().equalsIgnoreCase("1")) {
//                    if (rl_no_internet.getVisibility() == View.VISIBLE) {
//                        rl_no_internet.setVisibility(View.GONE);
//                        recycler_video.setVisibility(View.VISIBLE);
//                        p_bar.setVisibility(View.VISIBLE);
//                    }
//
//                    if (App.getSharedpref().isLogin(getActivity())) {
//                        if (!modelVideoList.getNotificationCount().equalsIgnoreCase("0")) {
//                            rl_not_badge.setVisibility(View.VISIBLE);
//                            tvCount.setText(modelVideoList.getNotificationCount());
//
//                        } else {
//                            rl_not_badge.setVisibility(View.GONE);
//                        }
//                    } else {
//                        rl_not_badge.setVisibility(View.GONE);
//                    }
//                    list.addAll(list.size(), modelVideoList.getDetails());
//
//                    Log.i("scroolll", "list.size:" + list.size());
//
//                    adapterVideoHome = new AdapterVideoHome(getActivity(), list, new AdapterVideoHome.Select() {
//                        @Override
//                        public void sounds(int position) {
//
//
//                            App.getSingleton().setSoundId(list.get(position).getSoundId());
//                            startActivity(new Intent(getActivity(), SoundVideoActivity.class));
//                        }
//
//                        @Override
//                        public void folowUnfollow(int position) {
//                            if (App.getSharedpref().isLogin(getActivity())) {
//
//                                followUser(list.get(position).getUserId(), position);
//                            } else {
//                                openLogin();
//                            }
//                        }
//
//                        @Override
//                        public void duetLayout(int position) {
//                            Intent intent = new Intent(getActivity(), MyVideoEditorActivity.class);
//                            intent.putExtra("video_duet", list.get(position).getVideoPath());
//                            startActivity(intent);
//
//
//                        }
//
//                        @Override
//                        public void hashTags(String hashtagId) {
//                            startActivity(new Intent(getActivity(), HashTagVideoActivity.class).putExtra(AppConstants.HASHTAG_ID, hashtagId));
//                        }
//
//                        @Override
//                        public void sideMenu(int position) {
//                            dialogReportVideo(list.get(position).getId());
//                        }
//
//                        @Override
//                        public void comments(int position) {
//                            String allowcomments = ((list.get(position).getAllowComment()));
//                            CommentFragment commentFragment = new CommentFragment(list.get(position).getId(), allowcomments, list.get(position).getUserId(), position);
//                            commentFragment.show(getChildFragmentManager(), commentFragment.getTag());
//
//                        }
//
//                        @Override
//                        public void user(int position) {
//                            if (App.getSingleton().getBlockedUserList() != null && App.getSingleton().getBlockedUserList().size() > 0) {
//                                List<String> blockedUserList = App.getSingleton().getBlockedUserList();
//                                for (int i = 0; i < blockedUserList.size(); i++) {
//                                    if (blockedUserList.get(i).equalsIgnoreCase(list.get(position).getUserId())) {
//                                        Toast.makeText(getActivity(), "User Blocked", Toast.LENGTH_SHORT).show();
//                                        return;
//                                    }
//                                }
//                            }
//                            startActivity(new Intent(getActivity(), OtherUserProfileActivity.class).putExtra(AppConstants.OTHER_USER_ID, list.get(position).getUserId()));
//                            getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
//                        }
//
//                        @Override
//                        public void share(int position) {
//                            try {
//                                CommonUtils.shareVideoDownload(getActivity(), list.get(position).getDownloadPath());
//                            } catch (Exception e) {
//                                Log.i("shareVideoHome", e.getMessage());
//                            }
//                        }
//
//                        @Override
//                        public void likeVideo(int position) {
//                            if (App.getSharedpref().isLogin(getActivity())) {
//                                likeApi(list.get(position).getId(), list.get(position).getUserId(), position, 1);
//                            } else {
//                                openLogin();
//                            }
//                        }
//
//                        @Override
//                        public void unlikeVideo(int position) {
//                            if (App.getSharedpref().isLogin(getActivity())) {
//                                likeApi(list.get(position).getId(), list.get(position).getUserId(), position, 1);
//
//                            } else {
//                                openLogin();
//                            }
//                        }
//
//                        @Override
//                        public void addFriends(int position) {
//                            startActivity(new Intent(getActivity(), OtherUserProfileActivity.class));
//                            getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
//                        }
//
//                    });
//                    int heightRecycler = recycler_video.getHeight();
//                    if (adapterAdd) {
//                        recycler_video.setAdapter(adapterVideoHome);
//                        adapterAdd = false;
//                        if (currentPage > 0) {
//                            recycler_video.scrollToPosition(currentPage - 1);
//                            recycler_video.smoothScrollBy(0, heightRecycler);
//
//
////                            progressStatusS = "0";
//                        } else {
//                            recycler_video.smoothScrollToPosition(0);
//                        }
//                    }
//                    adapterVideoHome.notifyDataSetChanged();
//                    mainCachingFunction();
//                } else {
//                    Log.i("myvideoList", modelVideoList.getMessage());
//                }
//            }
//        });


    }

    public void mainCachingFunction() {
        if (canCacheData) {
            if (list.size() >= 5) {
                for (int i = 0; i < 5; i++) {
                    new CachingDataStart().execute();
                }
            }
        }
    }


    private class CachingDataStart extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            if (list.size() != 0) {
                while (isCachingdata) {
                }
                if (currentCachePage + 1 < list.size()) {
                    if (currentCachePage + 1 < currentPage) {
                        currentCachePage = currentPage;
                    } else {
                        currentCachePage += 1;
                    }
                    preCacheVideo(CachingMain(list.get(currentCachePage).getVideoPath()));
                }
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {


//            Toast.makeText(context, "Cached succesfully", Toast.LENGTH_SHORT).show();
            super.onPostExecute(aVoid);
        }
    }

    public boolean canCacheData = true;

    public void preCacheVideo(String videoUrl) {
        Uri videoUri = Uri.parse(videoUrl);
        DataSpec dataSpec = new DataSpec(videoUri);
        CacheKeyFactory defaultCacheKeyFactory = CacheUtil.DEFAULT_CACHE_KEY_FACTORY;
        CacheUtil.ProgressListener progressListener = new CacheUtil.ProgressListener() {
            @Override
            public void onProgress(long requestLength, long bytesCached, long newBytesCached) {
                final Double downloadPercentage = bytesCached * 100.0 / requestLength;

                isCachingdata = downloadPercentage < 99;

            }
        };
        if (getApplicationContext() != null && getContext() != null) {
            DataSource dataSource = new DefaultDataSourceFactory(getApplicationContext(), Util.getUserAgent(getApplicationContext(), getString(R.string.app_name))).createDataSource();
            new CachingDataBack().execute(dataSpec, defaultCacheKeyFactory, dataSource, progressListener);
        }
    }

    public String CachingMain(String video_url) {
        HttpProxyCacheServer proxy = App.getProxy(getApplicationContext());
        String videoproxy = proxy.getProxyUrl(video_url);
        return videoproxy;
    }

    private int currentCachePage = -1;
    private boolean isCachingdata;

    private class CachingDataBack extends AsyncTask<Object, Void, Void> {

        @Override
        protected Void doInBackground(Object... params) {
            DataSpec dataSpec = (DataSpec) params[0];
            CacheKeyFactory defaultCacheKeyFactory = (CacheKeyFactory) params[1];
            DataSource dataSource = (DataSource) params[2];

            CacheUtil.ProgressListener progressListener = (CacheUtil.ProgressListener) params[3];
            try {
                CacheUtil.cache(dataSpec, App.simpleCache, defaultCacheKeyFactory, dataSource, progressListener, null);
            } catch (IOException e) {
                //Log.e("cacheerror","1 = "+ e.getMessage());
            } catch (InterruptedException e) {
                //Log.e("cacheerror","2 = "+ e.getMessage());

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {


//            Toast.makeText(context, "Cached succesfully", Toast.LENGTH_SHORT).show();
            super.onPostExecute(aVoid);
        }

    }

    private void likeApi(String videoId, String ownerId, final int position, final int type) {
        String userId = CommonUtils.userId(getActivity());
        videoMvvm.likeVideo(getActivity(), userId, videoId, ownerId).observe(getActivity(), new Observer<Map>() {
            @Override
            public void onChanged(Map map) {
                if (map.get("success").equals("1")) {
                    boolean isLike = (map.get("like_status").toString().equalsIgnoreCase("1")) ? true : false;
                    Intent intent = new Intent("like");
                    if (map.get("like_status").toString().equalsIgnoreCase("1")) {
//                        if(AdapterVideoHome.likeButton != null){
//                            AdapterVideoHome.likeButton.setLiked(true);
//                        }
                        list.get(position).setLikeStatus(true);

                    } else {
                        list.get(position).setLikeStatus(false);

                    }
                    String likeCOunt = map.get("like_count").toString();
                    list.get(position).setLikeCount(likeCOunt);
                    intent.putExtra("likeCount", likeCOunt);
                    intent.putExtra("isLike", isLike);
                    intent.putExtra("position", position);
                    intent.putExtra("type", type);
                    LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
                } else {
                    Toast.makeText(getActivity(), map.get("message").toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initids() {

        tvTermLink = view.findViewById(R.id.tvTermLink);
        tv_policyLink = view.findViewById(R.id.tv_policyLink);


        tvCount = getActivity().findViewById(R.id.tv_noti_badge_count);
        rl_not_badge = getActivity().findViewById(R.id.rl_not_badge);
        tv_noti_badge_count = getActivity().findViewById(R.id.tv_noti_badge_count);
        view.findViewById(R.id.btn_refresh_no_internet).setOnClickListener(this);
        rl_no_internet = view.findViewById(R.id.rl_no_internet);
        p_bar = view.findViewById(R.id.p_bar);
        recycler_video = view.findViewById(R.id.recycler_video);
        img_close_Login = view.findViewById(R.id.img_close_Login);


        ll_notLogin = view.findViewById(R.id.ll_notLogin);
        ll_phoneLogin = view.findViewById(R.id.ll_phoneLogin);
        ll_google = view.findViewById(R.id.ll_google);
        ll_facebook = view.findViewById(R.id.ll_facebook);
        btn_signup = view.findViewById(R.id.btn_signup);
        btn_SocialMail = view.findViewById(R.id.btn_SocialMail);
        btn_facebook = view.findViewById(R.id.btn_facebook);
        ll_phoneLogin.setOnClickListener(this);
        btn_facebook.setOnClickListener(this);
        btn_SocialMail.setOnClickListener(this);
        btn_signup.setOnClickListener(this);
        ll_google.setOnClickListener(this);
        ll_facebook.setOnClickListener(this);
        img_close_Login.setOnClickListener(this);
        recycler_video.setVisibility(View.VISIBLE);
        ll_notLogin.setVisibility(View.GONE);

        tvTermLink.setOnClickListener(this);
        tv_policyLink.setOnClickListener(this);
    }

//    private void openDialog() {
//        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
//        final View confirmdailog = layoutInflater.inflate(R.layout.dialog_signup, null);
//        final AlertDialog dailogbox = new AlertDialog.Builder(getActivity()).create();
//        dailogbox.setCancelable(true);
//        dailogbox.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        dailogbox.setView(confirmdailog);
//
//        confirmdailog.findViewById(R.id.img_google_login).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                App.getSingleton().setLoginType(AppConstants.LOGIN_VIDEO);
//                googleLogin = new GoogleLogin(getActivity(), AppConstants.LOGIN_VIDEO);
//                googleLogin.signIn();
//            }
//        });
//
//        confirmdailog.findViewById(R.id.img_fb_login).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                facebookLogin = new FacebookLogin(getActivity(), AppConstants.LOGIN_VIDEO, getActivity().getApplication());
//                facebookLogin.FBLogin();
//            }
//        });
//
//        confirmdailog.findViewById(R.id.btn_signup_dialog).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getActivity(), RegisterActivity.class));
//            }
//        });
//        confirmdailog.findViewById(R.id.btn_signin_dialog).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getActivity(), LoginActivity.class));
//            }
//        });
//
//        dailogbox.show();
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_refresh_no_internet:
                callApiVideoList(String.valueOf(page));
                break;
            case R.id.ll_phoneLogin:

                startActivity(new Intent(getActivity(), RegisterActivity.class));

                break;

            case R.id.btn_signup:

                startActivity(new Intent(getActivity(), RegisterActivity.class));

                break;

            case R.id.ll_google:

                App.getSingleton().setLoginType(AppConstants.LOGIN_PROFILE);
                googleLogin = new GoogleLogin(getActivity(), AppConstants.LOGIN_PROFILE);
                googleLogin.signIn();

                break;
            case R.id.btn_SocialMail:

                App.getSingleton().setLoginType(AppConstants.LOGIN_PROFILE);
                googleLogin = new GoogleLogin(getActivity(), AppConstants.LOGIN_PROFILE);
                googleLogin.signIn();

                break;

            case R.id.ll_facebook:

                callbackManager = CallbackManager.Factory.create();
                facebookLogin = new FacebookLogin(getActivity(), AppConstants.LOGIN_PROFILE, getActivity().getApplication());
                facebookLogin.FBLogin();

                break;


            case R.id.btn_facebook:

                callbackManager = CallbackManager.Factory.create();
                facebookLogin = new FacebookLogin(getActivity(), AppConstants.LOGIN_PROFILE, getActivity().getApplication());
                facebookLogin.FBLogin();

                break;


            case R.id.img_close_Login:
                ll_notLogin.setVisibility(View.GONE);
                backStatus = "0";
                privious_player.setPlayWhenReady(true);
                screenOn();


                break;

            case R.id.tv_policyLink:

                Intent intent = new Intent(getActivity(), WebLinkOpenActivity.class);
                intent.putExtra("key", "1");
                intent.putExtra("title", "CINEMAFLIX Privacy Policy");
                startActivity(intent);
                break;

            case R.id.tvTermLink:

                Intent intent1 = new Intent(getActivity(), WebLinkOpenActivity.class);
                intent1.putExtra("key", "0");
                intent1.putExtra("title", "Terms and Conditions");
                startActivity(intent1);

                break;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    SimpleExoPlayer privious_player;

    public void Release_Privious_Player() {
//        setProgressPos(currentPage-1);

        if (privious_player != null) {
            privious_player.removeListener(this);
            privious_player.getCurrentTimeline();
            privious_player.release();
        }
    }


    @Override
    public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == Player.STATE_BUFFERING) {
            p_bar.setVisibility(View.VISIBLE);
        } else if (playbackState == Player.STATE_READY) {
            p_bar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        is_visible_to_user = isVisibleToUser;
        if (privious_player != null && (isVisibleToUser && !is_user_stop_video)) {
            privious_player.setPlayWhenReady(true);
            screenOn();

        } else if (privious_player != null && !isVisibleToUser) {
            privious_player.setPlayWhenReady(false);
            screenOff();

        }
    }

    @Override
    public void onResume() {
        adapterAdd = true;
        super.onResume();
        if ((privious_player != null && (is_visible_to_user && !is_user_stop_video)) && !is_fragment_exits()) {
            privious_player.setPlayWhenReady(true);
            screenOn();

        }
        adapterAdd = true;
        if (currentPage == 0) {
            currentPage = -1;
            if ((privious_player != null && (is_visible_to_user && !is_user_stop_video)) && !is_fragment_exits()) {
                screenOn();

                privious_player.setPlayWhenReady(true);
            }
        } else {
            if ((privious_player != null && (is_visible_to_user && !is_user_stop_video)) && !is_fragment_exits()) {
                screenOn();

                privious_player.setPlayWhenReady(true);
            }
        }
    }

    boolean adapterAdd = true;

    public boolean is_fragment_exits() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        if (fm.getBackStackEntryCount() == 0) {
            return false;
        } else {
            return true;
        }

    }


    @Override
    public void onPause() {
        super.onPause();
//        progressBar.setEnabled(false);
//        privious_player.setPlayWhenReady(false);
//        privious_player.getPlaybackState();

        if (stopProgrssValue != null) {
            progressStatus = 1;
            pRogressBar(stopProgrssValue);
        }
        if (privious_player != null) {
            screenOff();

            privious_player.setPlayWhenReady(false);
            progressBar.setVisibility(View.GONE);

        }

    }

    @Override
    public void onStop() {
        super.onStop();
//        privious_player.setPlayWhenReady(false);
//        privious_player.getPlaybackState();

        if (privious_player != null) {
            privious_player.setPlayWhenReady(false);
            screenOff();

            progressBar.setProgress(0);

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Release_Privious_Player();
        if (privious_player != null) {
            screenOn();

            privious_player.setPlayWhenReady(true);
            privious_player.getPlaybackState();
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (privious_player != null) {
            privious_player.release();
        }
    }

    public void Show_heart_on_DoubleTap(ModelVideoTesting.Detail item, final RelativeLayout mainlayout, MotionEvent e) {

        int x = (int) e.getX() - 100;
        int y = (int) e.getY() - 100;
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        final ImageView iv = new ImageView(getApplicationContext());
        lp.setMargins(x, y, 0, 0);
        iv.setLayoutParams(lp);
        if (item.getLikeStatus().equals("1")) {
            iv.setImageDrawable(getResources().getDrawable(
                    R.drawable.ic_unliked));
        } else {
            iv.setImageDrawable(getResources().getDrawable(
                    R.drawable.ic_heart_gradient));
        }


        mainlayout.addView(iv);
        Animation fadeoutani = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);

        fadeoutani.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                iv.setVisibility(View.GONE);
                mainlayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mainlayout.removeView(iv);
                    }
                }, 500);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        iv.startAnimation(fadeoutani);

    }

    @Override
    public void commentsCallBack(String videoId, String comments, String ownerId, int position) {
        commentFragment = new CommentFragment(videoId, comments, ownerId, position, new CommentFragment.BottomClick() {
            @Override
            public void position(String status) {
                commentFragment.dismiss();
                backStatus = "1";
                ll_notLogin.setVisibility(View.VISIBLE);
            }
        });
        commentFragment.show(getChildFragmentManager(), commentFragment.getTag());
    }


    private void followUser(String otherId, final int position) {
        FollowUnfollowUser followUnfollowUser = new FollowUnfollowUser(getActivity(), new FollowUnfollowUser.followCallBack() {
            @Override
            public void followInterfaceCall(Map map) {
                if (map.get("success").equals("1")) {

                    String status = "";
                    View layout = recycler_video.getLayoutManager().findViewByPosition(position);
try {
     follow = layout.findViewById(R.id.followVideo);
} catch (Exception e) {
    e.printStackTrace();
}

                    if (map.get("following_status").equals(true)) {
                        Log.i("follow", String.valueOf(true));
                        status = "1";
                        follow.setText("Following");

//                        Glide.with(getActivity()).load(R.drawable.ic_checked).into(follow);
                    } else if (map.get("following_status").equals(false)) {
                        Log.i("follow", String.valueOf(false));
                        status = "0";
                        follow.setText("Follow");

//                        Glide.with(getActivity()).load(R.drawable.ic_baseline_add_24).into(follow);
                    }

                    list.get(position).setFollowStatus(status);

                    for (int i = 0; i < list.size() - 1; i++) {
                        if (list.get(i).getUserId() != null) {
                            if (list.get(i).getUserId().equalsIgnoreCase(otherId)) {
                                Log.i("like123", i + "pos,  userId" + otherId + " " + status);
                                if (status.equalsIgnoreCase("1")) {
                                    follow.setText("Following");

//                                Glide.with(getActivity()).load(R.drawable.ic_checked).into(follow);
                                } else {
//                                Glide.with(getActivity()).load(R.drawable.ic_baseline_add_24).into(follow);
                                    follow.setText("Follow");

                                }
                                list.get(i).setFollowStatus(status);
                                adapterVideoHome.notifyDataSetChanged();
                            }
                        }
                    }
                } else {
                    Log.i("follow", map.get("message").toString());
                }
            }
        });
        followUnfollowUser.followUnfollow(otherId);
    }

//    private void followUser(String otheruserId, final int position) {
//        FollowUnfollowUser followUnfollowUser = new FollowUnfollowUser(getActivity(), new FollowUnfollowUser.followCallBack() {
//            @Override
//            public void followInterfaceCall(Map map) {
//                if (map.get("success").equals("1")) {
//                    if (map.get("following_status").equals("true")) {
//
//                        Intent intent = new Intent(AppConstants.FOLLOW_UNFOLLOW);
//                        intent.putExtra(AppConstants.FOLLOW_UNFOLLOW, AppConstants.FOLLOW);
//                        intent.putExtra(AppConstants.POSITION, position);
//                        Toast.makeText(getActivity(), map.get("message").toString(), Toast.LENGTH_SHORT).show();
//                        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
//                    } else {
//                        Intent intent = new Intent(AppConstants.FOLLOW_UNFOLLOW);
//                        intent.putExtra(AppConstants.FOLLOW_UNFOLLOW, AppConstants.UNFOLLOW);
//                        intent.putExtra(AppConstants.POSITION, position);
//                        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
//                    }
//                } else {
//                    Log.i("follow", map.get("message").toString());
//                }
//            }
//        });
//        followUnfollowUser.followUnfollow(otheruserId);
//    }

    private void openDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        final View confirmdailog = layoutInflater.inflate(R.layout.dialog_signup, null);
        final androidx.appcompat.app.AlertDialog dailogbox = new androidx.appcompat.app.AlertDialog.Builder(getActivity()).create();
        dailogbox.setCancelable(true);
        dailogbox.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dailogbox.setView(confirmdailog);

        confirmdailog.findViewById(R.id.img_google_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                App.getSingleton().setLoginType(AppConstants.LOGIN_VIDEO);
                googleLogin = new GoogleLogin(getActivity(), AppConstants.LOGIN_VIDEO);
                googleLogin.signIn();
            }
        });

        confirmdailog.findViewById(R.id.img_fb_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                facebookLogin = new FacebookLogin(getActivity(), AppConstants.LOGIN_VIDEO, getActivity().getApplication());
                facebookLogin.FBLogin();
            }
        });

        confirmdailog.findViewById(R.id.btn_signup_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), RegisterActivity.class));
            }
        });
        confirmdailog.findViewById(R.id.btn_signin_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });

        dailogbox.show();
    }

    private void dialogReportVideo(String id) {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        final View confirmdailog = layoutInflater.inflate(R.layout.dialog_report_video, null);
        final androidx.appcompat.app.AlertDialog dailogbox = new androidx.appcompat.app.AlertDialog.Builder(getActivity()).create();
        dailogbox.setCancelable(true);
        dailogbox.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dailogbox.setView(confirmdailog);

        confirmdailog.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dailogbox.dismiss();

            }
        });

        confirmdailog.findViewById(R.id.tv_reportVideo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dailogbox.dismiss();

                Intent intent = new Intent(getActivity(), ReportProblemActivity.class).putExtra("ReportVideoID", id);
                startActivity(intent);


            }
        });

        dailogbox.show();


    }

    private void adminViewCount(String id, int position, String typeAdmin) {

        String userId = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.i("deviceId", userId);
        if (App.getSharedpref().isLogin(getActivity())) {
            userId = CommonUtils.userId(getActivity());
        }

        videoMvvm.adminVideo(getActivity(), userId, id, typeAdmin).observe(requireActivity(), new Observer<Map>() {
            @Override
            public void onChanged(Map map) {
                if (map.get("success").equals("1")) {
                    Log.i("type", typeAdmin);

                    if (typeAdmin.equalsIgnoreCase("0")) {
                        try {
                            Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(list.get(position).getVideoUrl()));
                            startActivity(myIntent);
                        } catch (ActivityNotFoundException e) {

                            e.printStackTrace();
                        }

                    }
//                    Toast.makeText(getContext(), map.get("message").toString(), Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getContext(), map.get("message").toString(), Toast.LENGTH_SHORT).show();

                }

            }
        });
    }

    private void screenOn() {
//        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


    }

    private void screenOff() {
//        getActivity().getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }
}