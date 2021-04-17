package in.pureway.cinemaflix.activity;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import in.pureway.cinemaflix.activity.login_register.LoginActivity;
import in.pureway.cinemaflix.activity.login_register.RegisterActivity;
import in.pureway.cinemaflix.activity.videoEditor.activity.MyVideoEditorActivity;
import in.pureway.cinemaflix.javaClasses.FacebookLogin;
import in.pureway.cinemaflix.javaClasses.FollowUnfollowUser;
import in.pureway.cinemaflix.javaClasses.GoogleLogin;
import in.pureway.cinemaflix.models.NotificationVideoModel;

import com.danikula.videocache.HttpProxyCacheServer;
import com.facebook.CallbackManager;
import com.github.ybq.android.spinkit.SpinKitView;
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

import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.adapters.AdapterHashTagVideoPlayer;
import in.pureway.cinemaflix.adapters.AdapterLikedVideoPlayer;
import in.pureway.cinemaflix.adapters.AdapterOtherUserVideo;
import in.pureway.cinemaflix.adapters.AdapterSearchSoundPlayer;
import in.pureway.cinemaflix.adapters.AdapterSearchVideoPlayer;
import in.pureway.cinemaflix.adapters.AdapterUploadedVideos;
import in.pureway.cinemaflix.fragments.homefrags.CommentFragment;
import in.pureway.cinemaflix.javaClasses.ViewVideo;
import in.pureway.cinemaflix.models.GetSoundDetailsPojo;
import in.pureway.cinemaflix.models.ModelHashTagsDetails;
import in.pureway.cinemaflix.models.ModelLikedVideos;
import in.pureway.cinemaflix.models.ModelMyUploadedVideos;
import in.pureway.cinemaflix.models.SearchAllPojo;
import in.pureway.cinemaflix.mvvm.VideoMvvm;
import in.pureway.cinemaflix.utils.App;
import in.pureway.cinemaflix.utils.AppConstants;
import in.pureway.cinemaflix.utils.CommonUtils;

import com.google.android.exoplayer2.video.VideoListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.security.AccessController.getContext;

public class SelectedVideoActivity extends AppCompatActivity implements Player.EventListener, View.OnClickListener {

    private RecyclerView recycler_video_single;
    private LinearLayoutManager layoutManager;
    private Activity activity;
    private SpinKitView p_bar;
    int currentItems, totalItem, scrollOutItems, page = 0;
    int currentPage = -1;
    private ViewVideo viewVideo;
    boolean is_user_stop_video = false;
    private final int TYPE_SEARCH_VIDEO = 0;
    private final int TYPE_UPLOADED_VIDEO = 1;
    private final int TYPE_OTHER_USER_VIDEO = 2;
    private final int TYPE_SOUND_VIDEO = 3;
    private final int TYPE_HASHTAG_VIDEO = 4;
    private final int TYPE_LIKED_VIDEO = 5;
    private final int TYPE_NOTIFICATION_VIDEO = 6;
    private int TYPE = 1;
    private String userId = "0", videoId = "";
    private VideoMvvm videoMvvm;
    List<ModelMyUploadedVideos.Detail> listMyUploaded = new ArrayList<>();
    List<ModelLikedVideos.Detail> listLikedvideo = App.getSingleton().getLikedVideoList();
    List<SearchAllPojo.Details.VideoList> listSearchVideo = new ArrayList<>();
    List<GetSoundDetailsPojo.Details.SoundVideo> listSoundDetail = new ArrayList<>();
    List<ModelHashTagsDetails.Details.HashtagVideo> listHashTag = new ArrayList<>();
    List<NotificationVideoModel.Detail> listNotifiation = new ArrayList<>();
    boolean adapterAdd = true;
    private GoogleLogin googleLogin;
    private FacebookLogin facebookLogin;
    private String likeStatus = "0";
    private LinearLayout ll_notLogin;
    private TextView tv_policyLink, tvTermLink;
    private String backStatus = "0";
    private LinearLayout ll_phoneLogin, ll_google, ll_facebook;
    private ImageView img_close_Login;
    CommentFragment commentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_video);
        activity = SelectedVideoActivity.this;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        videoMvvm = ViewModelProviders.of(SelectedVideoActivity.this).get(VideoMvvm.class);
        viewVideo = new ViewVideo(SelectedVideoActivity.this);
        if (App.getSharedpref().isLogin(activity)) {
            userId = CommonUtils.userId(activity);
        }
        findIds();
        getData();


//        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
//            @Override
//            public void handleOnBackPressed() {
//
//                currentPage = currentPage + 1;
////                    ll_notLogin.setVisibility(View.VISIBLE);
//
//                if (backStatus == "0") {
////                    onBackPressed();
////                    ll_notLogin.setVisibility(View.VISIBLE);
//
//
//                } else {
//                    ll_notLogin.setVisibility(View.GONE);
//                    backStatus = "0";
//                }
////                new Handler().postDelayed(new Runnable() {
////                    //
////                    @Override
////                    public void run() {
////
////                        backStatus = "0";
////                    }
////                }, 2000);
//            }
//        };
//        getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);
    }


    boolean isResponseGot = false;
    boolean isNotiComment = false;
    public static final String TYPE_NOTI = "type_noti_";
    private String commentId = "";

    private void getData() {
        if (getIntent().getExtras() != null) {
            int position = getIntent().getExtras().getInt(AppConstants.POSITION);
            switch (getIntent().getExtras().getString(AppConstants.SINGLE_VIDEO_TYPE)) {
                case AppConstants.TYPE_LIKED:
                    TYPE = TYPE_LIKED_VIDEO;
                    listLikedvideo = App.getSingleton().getLikedVideoList();
//                    setRecycler(listMyUploaded, position, TYPE, listLikedvideo, listSearchVideo, listSoundDetail, listHashTag, listNotifiation);
                    break;

                case AppConstants.TYPE_UPLOADED:
                    TYPE = TYPE_UPLOADED_VIDEO;
                    listMyUploaded = App.getSingleton().getListMyUploaded();
//                    setRecycler(listMyUploaded, position, TYPE, listLikedvideo, listSearchVideo, listSoundDetail, listHashTag, listNotifiation);
                    break;


                case AppConstants.OTHER_USER:
                    TYPE = TYPE_OTHER_USER_VIDEO;
                    listMyUploaded = App.getSingleton().getListMyUploaded();
//                    setRecycler(listMyUploaded, position, TYPE, listLikedvideo, listSearchVideo, listSoundDetail, listHashTag, listNotifiation);
                    break;

                case AppConstants.SOUNDS:
                    TYPE = TYPE_SOUND_VIDEO;
                    listSoundDetail = App.getSingleton().getSoundDetailList();
//                    setRecycler(listMyUploaded, position, TYPE, listLikedvideo, listSearchVideo, listSoundDetail, listHashTag, listNotifiation);
                    break;

                case AppConstants.HASHTAG:
                    TYPE = TYPE_HASHTAG_VIDEO;
                    listHashTag = App.getSingleton().getHashTagDetailList();
//                    setRecycler(listMyUploaded, position, TYPE, listLikedvideo, listSearchVideo, listSoundDetail, listHashTag, listNotifiation);
                    break;

                case AppConstants.SEARCH_VIDEO:
                    TYPE = TYPE_SEARCH_VIDEO;
                    listSearchVideo = App.getSingleton().getSearchVideoList();
//                    setRecycler(listMyUploaded, position, TYPE, listLikedvideo, listSearchVideo, listSoundDetail, listHashTag, listNotifiation);
                    break;

                case AppConstants.TYPE_NOTIFICATION_VIDEO:
                    TYPE = TYPE_UPLOADED_VIDEO;
//                    videoId = getIntent().getExtras().getString("notificationVideoId");
//                    notificationVideo(videoId);

                    if (getIntent().getExtras().getString(AppConstants.VIDEOTYPENOTI) != null && getIntent().getExtras().getString(AppConstants.VIDEOTYPENOTI).equalsIgnoreCase(AppConstants.REJECTVIDEO) || getIntent().getExtras().getString(AppConstants.VIDEOTYPENOTI).equalsIgnoreCase(AppConstants.DELETEVIDEO)) {
                        adapterAdd = true;
//
//
//
                        Release_Privious_Player();
//                        App.getSingleton().getListREjDelList();

                        setRecycler(listMyUploaded, position, TYPE, listLikedvideo, listSearchVideo, listSoundDetail, listHashTag);

                        Set_Player(getIntent().getExtras().getString(AppConstants.VIDEOPATH), "", "");
//                            getData();
//                        callApiVideoList(String.valueOf(currentPage));


                    } else {
                        if (!isResponseGot) {
                            if (getIntent().hasExtra(AppConstants.NOTI_VIDEO)) {
//                            adapterAdd = true;
////                            isScrolling = true;
//                            Set_Player(getIntent().getExtras().getString(AppConstants.VIDEO), "videoId", "");

                                notificationVideo(getIntent().getExtras().getString(AppConstants.VIDEO));

                                if (getIntent().hasExtra(AppConstants.NOTI_COMMENT)) {
                                    isNotiComment = true;
                                }
                                if (getIntent().hasExtra(AppConstants.COMMENT_ID)) {
                                    commentId = getIntent().getExtras().getString(AppConstants.COMMENT_ID);
                                }


                            }
                        } else {
                            listMyUploaded = App.getSingleton().getListMyUploaded();
                        }
                    }

//                    listNotifiation = App.getSingleton().getSearchVideoList();
                    break;
            }
            setRecycler(listMyUploaded, position, TYPE, listLikedvideo, listSearchVideo, listSoundDetail, listHashTag);

//            setRecycler(listMyUploaded, position, TYPE, listLikedvideo, listSearchVideo, listSoundDetail, listHashTag, listNotifiation);
        }
    }

    private void findIds() {

        tvTermLink = findViewById(R.id.tvTermLink);
        tv_policyLink = findViewById(R.id.tv_policyLink);

        img_close_Login = findViewById(R.id.img_close_Login);
        ll_notLogin = findViewById(R.id.ll_notLogin);
        p_bar = findViewById(R.id.p_bar);
        recycler_video_single = findViewById(R.id.recycler_video_single);
        ll_notLogin.setVisibility(View.GONE);
        ll_phoneLogin = findViewById(R.id.ll_phoneLogin);
        ll_google = findViewById(R.id.ll_google);
        ll_facebook = findViewById(R.id.ll_facebook);
        ll_phoneLogin.setOnClickListener(this);
        ll_google.setOnClickListener(this);
        ll_facebook.setOnClickListener(this);
        img_close_Login.setOnClickListener(this);

        tvTermLink.setOnClickListener(this);
        tv_policyLink.setOnClickListener(this);
    }

    private void setRecycler(final List<ModelMyUploadedVideos.Detail> uploadedvideoList, int position, final int type
            , final List<ModelLikedVideos.Detail> listLikedvideo, final List<SearchAllPojo.Details.VideoList> listSearchVideo, final List<GetSoundDetailsPojo.Details.SoundVideo> listSoundDetail
            , final List<ModelHashTagsDetails.Details.HashtagVideo> listHashTag) {

        layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        SnapHelper snapHelper = new PagerSnapHelper();
        recycler_video_single.setItemAnimator(new DefaultItemAnimator());
        recycler_video_single.setLayoutManager(layoutManager);
        recycler_video_single.setOnFlingListener(null);
        snapHelper.attachToRecyclerView(recycler_video_single);
        // this is the scroll listener of recycler view which will tell the current item number
        recycler_video_single.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {

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
                    String videoPath = "", videoId = "", ownerId = "";
                    switch (type) {
                        case TYPE_HASHTAG_VIDEO:
                            ownerId = listHashTag.get(currentPage).getUserId();
                            videoId = listHashTag.get(currentPage).getId();
                            videoPath = listHashTag.get(currentPage).getVideoPath();
                            viewVideo.viewVideoAPi(videoId);


                            if (listHashTag.get(currentPage).getLikeStatus() == true) {
//                                Show_heart_on_DoubleTap(item, mainlayout, e);

                                likeStatus = "1";

                            }
                            if (listHashTag.get(currentPage).getLikeStatus() == false) {
                                likeStatus = "0";

                            }
                            break;

                        case TYPE_SEARCH_VIDEO:
                            ownerId = listSearchVideo.get(currentPage).getUserId();
                            videoId = listSearchVideo.get(currentPage).getId();
                            videoPath = listSearchVideo.get(currentPage).getVideoPath();
                            viewVideo.viewVideoAPi(videoId);

                            if (listSearchVideo.get(currentPage).getLikeStatus() == true) {
//                                Show_heart_on_DoubleTap(item, mainlayout, e);

                                likeStatus = "1";

                            }
                            if (listSearchVideo.get(currentPage).getLikeStatus() == false) {
                                likeStatus = "0";

                            }
                            break;

                        case TYPE_LIKED_VIDEO:
                            ownerId = listLikedvideo.get(currentPage).getUserId();
                            videoId = listLikedvideo.get(currentPage).getId();
                            viewVideo.viewVideoAPi(videoId);
                            videoPath = listLikedvideo.get(currentPage).getVideoPath();

                            if (listLikedvideo.get(currentPage).getLikeStatus() == true) {
//                                Show_heart_on_DoubleTap(item, mainlayout, e);

                                likeStatus = "1";

                            }
                            if (listLikedvideo.get(currentPage).getLikeStatus() == false) {
                                likeStatus = "0";

                            }
                            break;

                        case TYPE_UPLOADED_VIDEO:
                            ownerId = uploadedvideoList.get(currentPage).getUserId();
                            videoId = uploadedvideoList.get(currentPage).getId();
                            videoPath = uploadedvideoList.get(currentPage).getVideoPath();

                            if (uploadedvideoList.get(currentPage).getLikeStatus() == true) {
//                                Show_heart_on_DoubleTap(item, mainlayout, e);

                                likeStatus = "1";

                            }
                            if (uploadedvideoList.get(currentPage).getLikeStatus() == false) {
                                likeStatus = "0";

                            }
                            break;

                        case TYPE_OTHER_USER_VIDEO:
                            ownerId = uploadedvideoList.get(currentPage).getUserId();
                            videoId = uploadedvideoList.get(currentPage).getId();
                            viewVideo.viewVideoAPi(uploadedvideoList.get(currentPage).getId());
                            videoPath = uploadedvideoList.get(currentPage).getVideoPath();

                            if (uploadedvideoList.get(currentPage).getLikeStatus() == true) {
//                                Show_heart_on_DoubleTap(item, mainlayout, e);

                                likeStatus = "1";

                            }
                            if (uploadedvideoList.get(currentPage).getLikeStatus() == false) {
                                likeStatus = "0";

                            }
                            break;


                        case TYPE_SOUND_VIDEO:
                            ownerId = listSoundDetail.get(currentPage).getUserId();
                            videoId = listSoundDetail.get(currentPage).getId();
                            viewVideo.viewVideoAPi(videoId);
                            videoPath = listSoundDetail.get(currentPage).getVideoPath();

                            if (listSoundDetail.get(currentPage).getLikeStatus() == true) {
//                                Show_heart_on_DoubleTap(item, mainlayout, e);

                                likeStatus = "1";

                            }
                            if (listSoundDetail.get(currentPage).getLikeStatus() == false) {
                                likeStatus = "0";

                            }
                            break;

                        case TYPE_NOTIFICATION_VIDEO:
                            ownerId = listNotifiation.get(currentPage).getUserId();
                            videoId = listNotifiation.get(currentPage).getId();
                            viewVideo.viewVideoAPi(videoId);
                            videoPath = listNotifiation.get(currentPage).getVideoPath();
                            if (listNotifiation.get(currentPage).getLikeStatus() == true) {
//                                Show_heart_on_DoubleTap(item, mainlayout, e);

                                likeStatus = "1";

                            }
                            if (listNotifiation.get(currentPage).getLikeStatus() == false) {
                                likeStatus = "0";

                            }
                            break;

                        default:
                            videoPath = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4";
                    }
                    Release_Privious_Player();
                    Set_Player(videoPath, videoId, ownerId);
                    Log.i("scroolll", "current:" + currentPage);
                    Log.i("scroolll", "page:" + page_no);
                }
            }
        });
        mainCachingFunction(listMyUploaded, listLikedvideo, listSearchVideo, listSoundDetail, listHashTag);
        recyclerAdapters(type, position, uploadedvideoList, listLikedvideo, listSearchVideo, listSoundDetail, listHashTag);

    }

    private void recyclerAdapters(int type, int position, final List<ModelMyUploadedVideos.Detail> uploadedvideoList, final List<ModelLikedVideos.Detail> listLikedvideo, final List<SearchAllPojo.Details.VideoList> listSearchVideo, final List<GetSoundDetailsPojo.Details.SoundVideo> listSoundDetail
            , final List<ModelHashTagsDetails.Details.HashtagVideo> listHashTag) {
        switch (type) {
            case TYPE_HASHTAG_VIDEO:
                AdapterHashTagVideoPlayer adapterHashTagVideoPlayer = new AdapterHashTagVideoPlayer(activity, listHashTag, new AdapterHashTagVideoPlayer.Select() {
                    @Override
                    public void comments(int position) {
                        String allowcomments = ((listHashTag.get(position).getAllowComment()));
                        commentFragment = new CommentFragment(listHashTag.get(position).getId(), allowcomments, userId, position, new CommentFragment.BottomClick() {
                            @Override
                            public void position(String status) {

                                backStatus = "1";
                                ll_notLogin.setVisibility(View.VISIBLE);
                                commentFragment.dismiss();
                                privious_player.setPlayWhenReady(false);
                                screenOff();
//                                backStatus = "1";
//                                ll_notLogin.setVisibility(View.VISIBLE);
                            }
                        });
                        commentFragment.show(getSupportFragmentManager(), commentFragment.getTag());
                    }

                    @Override
                    public void download(int position) {
                        CommonUtils.videoDownload(SelectedVideoActivity.this, listHashTag.get(position).getVideoPath());
                    }

                    @Override
                    public void share(int position) {
                        CommonUtils.shareVideoDownload(activity, listHashTag.get(position).getDownloadPath());
                    }

                    @Override
                    public void sounds(int position) {

//                        App.getSingleton().setSoundId(listHashTag.get(position).getSoundId());
//                        startActivity(new Intent(activity, SoundVideoActivity.class));
                    }

                    @Override
                    public void duetLayout(int position) {
                        Intent intent = new Intent(SelectedVideoActivity.this, MyVideoEditorActivity.class);
                        intent.putExtra("video_duet", uploadedvideoList.get(position).getVideoPath());
                        startActivity(intent);

                    }

                    @Override
                    public void likeVideo(int position) {
                        if (App.getSharedpref().isLogin(SelectedVideoActivity.this)) {
                            likeUnlike(listHashTag.get(position).getId(), listHashTag.get(position).getUserId(), position, 0);

                        } else {
                            ll_notLogin.setVisibility(View.VISIBLE);
                            backStatus = "1";
                            privious_player.setPlayWhenReady(false);
                            screenOff();
                        }

                    }

                    @Override
                    public void hashTags(String id) {
//                        String[] hashTagId = TextUtils.split(listHashTag.get(position).getHashtag(), ",");
                        moveToHashTag(id);
                    }

                    @Override
                    public void moveToProfile(int position) {
                        moveToUserProfile(listHashTag.get(position).getUserId());
                    }

                    @Override
                    public void folowUnfollow(int position) {

                        if (App.getSharedpref().isLogin(SelectedVideoActivity.this)) {

                            followUser(listHashTag.get(position).getUserId(), position);
                        } else {
                            ll_notLogin.setVisibility(View.VISIBLE);
                            backStatus = "1";
                            privious_player.setPlayWhenReady(false);
                            screenOff();                        }
                    }

                    @Override
                    public void sideMenu(int position) {
                        if (App.getSharedpref().isLogin(SelectedVideoActivity.this)) {

                            dialogReportVideo(listHashTag.get(position).getId());
                        } else {
                            ll_notLogin.setVisibility(View.VISIBLE);
                            backStatus = "1";
                            privious_player.setPlayWhenReady(false);
                            screenOff();                        }
                    }
                });
                if (adapterAdd) {
                    adapterAdd = false;
                    recycler_video_single.setAdapter(adapterHashTagVideoPlayer);
                    recycler_video_single.scrollToPosition(position);
                }


                break;

            case TYPE_SEARCH_VIDEO:
                AdapterSearchVideoPlayer adapterSearchVideoPlayer = new AdapterSearchVideoPlayer(activity, listSearchVideo, new AdapterSearchVideoPlayer.Select() {
                    @Override
                    public void comments(int position) {
                        String allowcomments = ((listSearchVideo.get(position).getAllowComment()));
                        commentFragment = new CommentFragment(listSearchVideo.get(position).getId(), allowcomments, userId, position, new CommentFragment.BottomClick() {
                            @Override
                            public void position(String status) {
                                backStatus = "1";
                                ll_notLogin.setVisibility(View.VISIBLE);
                                commentFragment.dismiss();
                                privious_player.setPlayWhenReady(false);
                                screenOff();
                            }
                        });
                        commentFragment.show(getSupportFragmentManager(), commentFragment.getTag());
                    }

                    @Override
                    public void share(int position) {
                        CommonUtils.shareVideoDownload(activity, listSearchVideo.get(position).getDownloadPath());
                    }

                    @Override
                    public void sounds(int position) {
                        //TODO

//                        App.getSingleton().setSoundId(listHashTag.get(position).getSoundId());

//                        App.getSingleton().setSoundId(listSearchVideo.get(position).getSoundId());
//                        startActivity(new Intent(activity, SoundVideoActivity.class));
                    }

                    @Override
                    public void duetLayout(int position) {
                        //TODO

                        Intent intent = new Intent(SelectedVideoActivity.this, MyVideoEditorActivity.class);
                        intent.putExtra("video_duet", listSearchVideo.get(position).getVideoPath());
                        startActivity(intent);
                    }

                    @Override
                    public void likeVideo(int position) {

                        if (App.getSharedpref().isLogin(SelectedVideoActivity.this)) {

                            likeUnlike(listSearchVideo.get(position).getId(), listSearchVideo.get(position).getUserId(), position, 0);
                        } else {
                            ll_notLogin.setVisibility(View.VISIBLE);
                            backStatus = "1";
                            privious_player.setPlayWhenReady(false);
                            screenOff();
                        }
                    }

                    @Override
                    public void hashTags(int position) {
                        String[] hashTagId = TextUtils.split(listSearchVideo.get(position).getHashTag(), ",");
                        moveToHashTag(hashTagId[0]);
                    }

                    @Override
                    public void moveToProfile(int position) {
                        moveToUserProfile(listSearchVideo.get(position).getUserId());
                    }

                    @Override
                    public void download(int position) {
                        CommonUtils.videoDownload(SelectedVideoActivity.this, listSearchVideo.get(position).getVideoPath());

                    }

                    @Override
                    public void folowUnfollow(int position) {
                        if (App.getSharedpref().isLogin(SelectedVideoActivity.this)) {

                            followUser(listSearchVideo.get(position).getUserId(), position);
                        } else {
                            ll_notLogin.setVisibility(View.VISIBLE);
                            backStatus = "1";
                            privious_player.setPlayWhenReady(false);
                            screenOff();                        }
                    }

                    @Override
                    public void sideMenu(int position) {


                        if (App.getSharedpref().isLogin(SelectedVideoActivity.this)) {

                            dialogReportVideo(listSearchVideo.get(position).getId());
                        } else {
                            ll_notLogin.setVisibility(View.VISIBLE);
                            backStatus = "1";
                            privious_player.setPlayWhenReady(false);
                            screenOff();                        }
                    }
                });
                if (adapterAdd) {
                    adapterAdd = false;
                    recycler_video_single.setAdapter(adapterSearchVideoPlayer);
                    recycler_video_single.scrollToPosition(position);
                }
                break;


            case TYPE_SOUND_VIDEO:

                AdapterSearchSoundPlayer adapterSearchSoundPlayer = new AdapterSearchSoundPlayer(activity, listSoundDetail, new AdapterSearchSoundPlayer.Select() {
                    @Override
                    public void comments(int position) {
                        String allowcomments = ((listSoundDetail.get(position).getAllowComment()));
                        commentFragment = new CommentFragment(listSoundDetail.get(position).getId(), allowcomments, userId, position, new CommentFragment.BottomClick() {
                            @Override
                            public void position(String status) {
                                backStatus = "1";
                                ll_notLogin.setVisibility(View.VISIBLE);
                                commentFragment.dismiss();
                                privious_player.setPlayWhenReady(false);
                                screenOff();
                            }
                        });
                        commentFragment.show(getSupportFragmentManager(), commentFragment.getTag());
                    }

                    @Override
                    public void share(int position) {
                        CommonUtils.shareVideoDownload(activity, listSoundDetail.get(position).getVideoPath());
                    }

                    @Override
                    public void sounds(int position) {


//                        App.getSingleton().setSoundId(listSoundDetail.get(position).getSoundId());
//                        startActivity(new Intent(activity, SoundVideoActivity.class));
                    }

                    @Override
                    public void duetLayout(int position) {
                        Intent intent = new Intent(SelectedVideoActivity.this, MyVideoEditorActivity.class);
                        intent.putExtra("video_duet", listSoundDetail.get(position).getVideoPath());
                        startActivity(intent);
                    }

                    @Override
                    public void likeVideo(int position) {


                        if (App.getSharedpref().isLogin(SelectedVideoActivity.this)) {

                            likeUnlike(listSoundDetail.get(position).getId(), listSoundDetail.get(position).getUserId(), position, 0);
                        } else {
                            ll_notLogin.setVisibility(View.VISIBLE);
                            backStatus = "1";
                            privious_player.setPlayWhenReady(false);
                            screenOff();
                        }
                    }

                    @Override
                    public void hashTags(int position) {
                        String[] hashTagId = TextUtils.split(listSoundDetail.get(position).getHashTag(), ",");
                        moveToHashTag(hashTagId[0]);
                    }

                    @Override
                    public void moveToProfile(int position) {
                        moveToUserProfile(listSoundDetail.get(position).getUserId());
                    }

                    @Override
                    public void folowUnfollow(int position) {
                        if (App.getSharedpref().isLogin(SelectedVideoActivity.this)) {

                            followUser(listSoundDetail.get(position).getUserId(), position);
                        } else {
                            ll_notLogin.setVisibility(View.VISIBLE);
                            backStatus = "1";
                            privious_player.setPlayWhenReady(false);
                            screenOff();                        }
                    }

                    @Override
                    public void sideMenu(int position) {
                        if (App.getSharedpref().isLogin(SelectedVideoActivity.this)) {

                            dialogReportVideo(listSoundDetail.get(position).getId());
                        } else {
                            ll_notLogin.setVisibility(View.VISIBLE);
                            backStatus = "1";
                            privious_player.setPlayWhenReady(false);
                            screenOff();                        }
                    }

                    @Override
                    public void download(int position) {
                        CommonUtils.videoDownload(SelectedVideoActivity.this, listSoundDetail.get(position).getVideoPath());

                    }
                });
                if (adapterAdd) {
                    adapterAdd = false;
                    recycler_video_single.setAdapter(adapterSearchSoundPlayer);
                    recycler_video_single.scrollToPosition(position);
                }
                break;


            case TYPE_OTHER_USER_VIDEO:
                AdapterOtherUserVideo adapterOtherUserVideo = new AdapterOtherUserVideo(activity, uploadedvideoList, new AdapterOtherUserVideo.Select() {
                    @Override
                    public void comments(int position) {
                        String allowcomments = ((uploadedvideoList.get(position).getAllowComment()));
                        commentFragment = new CommentFragment(uploadedvideoList.get(position).getId(), allowcomments, userId, position, new CommentFragment.BottomClick() {
                            @Override
                            public void position(String status) {
                                backStatus = "1";
                                ll_notLogin.setVisibility(View.VISIBLE);
                                commentFragment.dismiss();
                                privious_player.setPlayWhenReady(false);
                                screenOff();
                            }
                        });
                        commentFragment.show(getSupportFragmentManager(), commentFragment.getTag());
                    }

                    @Override
                    public void share(int position) {
                        CommonUtils.shareVideoDownload(activity, uploadedvideoList.get(position).getDownloadPath());
                    }

                    @Override
                    public void sounds(int position) {
//                        App.getSingleton().setSoundId(uploadedvideoList.get(position).getSoundId());
//                        startActivity(new Intent(activity, SoundVideoActivity.class));
                    }

                    @Override
                    public void duetLayout(int position) {
                        Intent intent = new Intent(SelectedVideoActivity.this, MyVideoEditorActivity.class);
                        intent.putExtra("video_duet", uploadedvideoList.get(position).getVideoPath());
                        startActivity(intent);
                    }

                    @Override
                    public void likeVideo(int position) {


                        if (App.getSharedpref().isLogin(SelectedVideoActivity.this)) {

                            likeUnlike(uploadedvideoList.get(position).getId(), uploadedvideoList.get(position).getUserId(), position, 0);
                        } else {
                            ll_notLogin.setVisibility(View.VISIBLE);
                            backStatus = "1";
                            privious_player.setPlayWhenReady(false);
                            screenOff();
                        }
                    }

                    @Override
                    public void hashTags(String id) {
//                        String[] hashTagId = TextUtils.split(uploadedvideoList.get(position).getHashtag(), ",");
                        moveToHashTag(id);
                    }

                    @Override
                    public void moveToProfile(int position) {
                        moveToUserProfile(uploadedvideoList.get(position).getUserId());
                    }

                    @Override
                    public void folowUnfollow(int position) {
                        if (App.getSharedpref().isLogin(SelectedVideoActivity.this)) {

                            followUser(uploadedvideoList.get(position).getUserId(), position);
                        } else {
                            ll_notLogin.setVisibility(View.VISIBLE);
                            backStatus = "1";
                            privious_player.setPlayWhenReady(false);
                            screenOff();                        }
                    }

                    @Override
                    public void sideMenu(int position) {
                        if (App.getSharedpref().isLogin(SelectedVideoActivity.this)) {

                            dialogReportVideo(uploadedvideoList.get(position).getId());
                        } else {
                            ll_notLogin.setVisibility(View.VISIBLE);
                            backStatus = "1";
                            privious_player.setPlayWhenReady(false);
                            screenOff();                        }
                    }

                    @Override
                    public void download(int position) {
                        CommonUtils.videoDownload(SelectedVideoActivity.this, uploadedvideoList.get(position).getVideoPath());

                    }
                });
                if (adapterAdd) {
                    adapterAdd = false;
                    recycler_video_single.setAdapter(adapterOtherUserVideo);
                    recycler_video_single.scrollToPosition(position);
                }
                break;

            case TYPE_LIKED_VIDEO:
                AdapterLikedVideoPlayer adapterLikedVideoPlayer = new AdapterLikedVideoPlayer(activity, listLikedvideo, new AdapterLikedVideoPlayer.Select() {
                    @Override
                    public void likeVideo(int position) {


                        if (App.getSharedpref().isLogin(SelectedVideoActivity.this)) {

                            likeUnlike(listLikedvideo.get(position).getVideoId(), listLikedvideo.get(position).getOwnerId(), position, 0);
                        } else {
                            ll_notLogin.setVisibility(View.VISIBLE);
                            backStatus = "1";
                            privious_player.setPlayWhenReady(false);
                            screenOff();
                        }
                    }

                    @Override
                    public void share(int position) {
                        CommonUtils.shareVideoDownload(activity, listLikedvideo.get(position).getDownloadPath());
                    }

                    @Override
                    public void comments(int position) {
                        String allowcomments = ((listLikedvideo.get(position).getAllowComment()));
                        commentFragment = new CommentFragment(listLikedvideo.get(position).getId(), allowcomments, userId, position, new CommentFragment.BottomClick() {
                            @Override
                            public void position(String status) {
                                backStatus = "1";
                                ll_notLogin.setVisibility(View.VISIBLE);
                                commentFragment.dismiss();
                                privious_player.setPlayWhenReady(false);
                                screenOff();
                            }
                        });
                        commentFragment.show(getSupportFragmentManager(), commentFragment.getTag());
                    }

                    @Override
                    public void sounds(int position) {
//                        App.getSingleton().setSoundId(listLikedvideo.get(position).getSoundId());
//                        startActivity(new Intent(activity, SoundVideoActivity.class));
                    }

                    @Override
                    public void duetLayout(int position) {
                        Intent intent = new Intent(SelectedVideoActivity.this, MyVideoEditorActivity.class);
                        intent.putExtra("video_duet", listLikedvideo.get(position).getVideoPath());
                        startActivity(intent);
                    }

                    @Override
                    public void hashTags(int position) {
                        String[] hashTagId = TextUtils.split(listLikedvideo.get(position).getHashTag(), ",");
                        moveToHashTag(hashTagId[0]);
                    }

                    @Override
                    public void moveToProfile(int position) {
                        moveToUserProfile(listLikedvideo.get(position).getUserId());
                    }

                    @Override
                    public void folowUnfollow(int position) {
                        if (App.getSharedpref().isLogin(SelectedVideoActivity.this)) {

                            followUser(listLikedvideo.get(position).getUserId(), position);
                        } else {
                            ll_notLogin.setVisibility(View.VISIBLE);
                            backStatus = "1";
                            privious_player.setPlayWhenReady(false);
                            screenOff();                        }
                    }

                    @Override
                    public void sideMenu(int position) {
                        if (App.getSharedpref().isLogin(SelectedVideoActivity.this)) {

                            dialogReportVideo(listLikedvideo.get(position).getId());
                        } else {
                            ll_notLogin.setVisibility(View.VISIBLE);
                            backStatus = "1";
                            privious_player.setPlayWhenReady(false);
                            screenOff();                        }
                    }

                    @Override
                    public void download(int position) {
                        CommonUtils.videoDownload(SelectedVideoActivity.this, listLikedvideo.get(position).getVideoPath());

                    }
                });
                if (adapterAdd) {
                    adapterAdd = false;
                    recycler_video_single.setAdapter(adapterLikedVideoPlayer);
                    recycler_video_single.scrollToPosition(position);
                }
                break;


            case TYPE_UPLOADED_VIDEO:
                AdapterUploadedVideos adapterUploadedVideos = new AdapterUploadedVideos(activity, uploadedvideoList, new AdapterUploadedVideos.Select() {
                    @Override
                    public void comments(int position) {
                        String allowcomments = ((uploadedvideoList.get(position).getAllowComment()));
                        commentFragment = new CommentFragment(uploadedvideoList.get(position).getId(), allowcomments, userId, position, new CommentFragment.BottomClick() {
                            @Override
                            public void position(String status) {
                                backStatus = "1";
                                ll_notLogin.setVisibility(View.VISIBLE);
                                commentFragment.dismiss();
                                privious_player.setPlayWhenReady(false);
                                screenOff();
                            }
                        });
                        commentFragment.show(getSupportFragmentManager(), commentFragment.getTag());
                    }

                    @Override
                    public void share(int position) {
                        CommonUtils.shareVideoDownload(activity, uploadedvideoList.get(position).getDownloadPath());
                    }

                    @Override
                    public void sounds(int position) {
//                        App.getSingleton().setSoundId(uploadedvideoList.get(position).getSoundId());
//                        startActivity(new Intent(activity, SoundVideoActivity.class));
                    }

                    @Override
                    public void duetLayout(int position) {
                        Intent intent = new Intent(SelectedVideoActivity.this, MyVideoEditorActivity.class);
                        intent.putExtra("video_duet", uploadedvideoList.get(position).getVideoPath());
                        startActivity(intent);
                    }

                    @Override
                    public void likeVideo(int position) {

                        if (App.getSharedpref().isLogin(SelectedVideoActivity.this)) {

                            likeUnlike(uploadedvideoList.get(position).getId(), userId, position, 0);
                        } else {
                            ll_notLogin.setVisibility(View.VISIBLE);
                            backStatus = "1";
                            privious_player.setPlayWhenReady(false);
                            screenOff();
                        }
                    }

                    @Override
                    public void hashTags(String id) {
//                        String[] hashTagId = TextUtils.split(uploadedvideoList.get(position).getHashtag(), ",");
                        moveToHashTag(id);
                    }

                    @Override
                    public void moveToProfile(int position) {
                        moveToUserProfile(uploadedvideoList.get(position).getUserId());
                    }

                    @Override
                    public void folowUnfollow(int position) {
                        if (App.getSharedpref().isLogin(SelectedVideoActivity.this)) {

                            followUser(uploadedvideoList.get(position).getUserId(), position);
                        } else {
                            ll_notLogin.setVisibility(View.VISIBLE);
                            backStatus = "1";
                            privious_player.setPlayWhenReady(false);
                            screenOff();                        }
                    }

                    @Override
                    public void sideMenu(int position) {
                        if (App.getSharedpref().isLogin(SelectedVideoActivity.this)) {

                            dialogReportVideo(uploadedvideoList.get(position).getId());
                        } else {
                            ll_notLogin.setVisibility(View.VISIBLE);
                            backStatus = "1";
                            privious_player.setPlayWhenReady(false);
                            screenOff();                        }
                    }

                    @Override
                    public void download(int position) {
                        CommonUtils.videoDownload(SelectedVideoActivity.this, uploadedvideoList.get(position).getVideoPath());

                    }
                });
                if (adapterAdd) {
                    adapterAdd = false;
                    recycler_video_single.setAdapter(adapterUploadedVideos);
                    recycler_video_single.scrollToPosition(position);
                }
                if (isResponseGot) {
                    if (isNotiComment) {
                        String allowcomments = ((uploadedvideoList.get(0).getAllowComment()));
                        commentFragment = new CommentFragment(uploadedvideoList.get(0).getId(), allowcomments, userId, 0, new CommentFragment.BottomClick() {
                            @Override
                            public void position(String status) {
                                backStatus = "1";
                                ll_notLogin.setVisibility(View.VISIBLE);
                                commentFragment.dismiss();
                                privious_player.setPlayWhenReady(false);
                                screenOff();
                            }
                        });
                        commentFragment.show(getSupportFragmentManager(), commentFragment.getTag());
                    }
                } else {
                    recycler_video_single.scrollToPosition(position);
                }

                break;

        }
    }

    private void moveToUserProfile(String videoOwnerId) {
        if (videoOwnerId.equalsIgnoreCase(userId)) {
            startActivity(new Intent(SelectedVideoActivity.this, HomeActivity.class).putExtra(AppConstants.LOGIN_TYPE, AppConstants.LOGIN_PROFILE));
        } else {
            startActivity(new Intent(SelectedVideoActivity.this, OtherUserProfileActivity.class).putExtra(AppConstants.OTHER_USER_ID, videoOwnerId));
            overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        }

    }

    private void moveToHashTag(String hashTagId) {
        startActivity(new Intent(SelectedVideoActivity.this, HashTagVideoActivity.class).putExtra(AppConstants.HASHTAG_ID, hashTagId));

//        Toast.makeText(activity, "Link to page Under development.." + hashTagId, Toast.LENGTH_SHORT).show();
    }

    public void Set_Player(String videoPath, final String videoId, final String ownerId) {
        try {

            DefaultTrackSelector trackSelector = new DefaultTrackSelector();
            HttpProxyCacheServer proxy = App.getProxy(getApplicationContext());
            String proxyUrl = proxy.getProxyUrl(videoPath);
            final SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(SelectedVideoActivity.this, trackSelector);
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(SelectedVideoActivity.this,
                    Util.getUserAgent(SelectedVideoActivity.this, "Cinemaflix"));
            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(Uri.parse(proxyUrl));

            player.prepare(videoSource);
            player.setRepeatMode(Player.REPEAT_MODE_ALL);
            player.addListener(this);

            View layout = layoutManager.findViewByPosition(currentPage);

            final PlayerView playerView = layout.findViewById(R.id.playerview);
            playerView.setPlayer(player);

            player.setPlayWhenReady(true);
            privious_player = player;
            player.addVideoListener(new VideoListener() {
                @Override
                public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
//                    if (pixelWidthHeightRatio > 1.1 || height < width) {
//                        playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
//                    } else {
//                        playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
//
//                    }
                }

                @Override
                public void onRenderedFirstFrame() {

                }
            });

            final RelativeLayout mainlayout = layout.findViewById(R.id.rl_main_video);
            playerView.setOnTouchListener(new View.OnTouchListener() {
                private GestureDetector gestureDetector = new GestureDetector(SelectedVideoActivity.this, new GestureDetector.SimpleOnGestureListener() {

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
                            screenOn();
                            privious_player.setPlayWhenReady(true);
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
                            screenOn();
                            privious_player.setPlayWhenReady(true);
                        }

                        if (App.getSharedpref().isLogin(SelectedVideoActivity.this)) {
                            if (likeStatus == "0") {
//                                Show_heart_on_DoubleTap(item, mainlayout, e);
                                Show_heart_on_DoubleTap(mainlayout, e);
//                                likeStatus = "1";
                                likeUnlike(videoId, ownerId, currentPage, 1);

                            }
                        } else {
                            ll_notLogin.setVisibility(View.VISIBLE);
                            backStatus = "1";
                            screenOff();
                            privious_player.setPlayWhenReady(false);

                            screenOff();
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
//            Animation sound_animation = AnimationUtils.loadAnimation(SelectedVideoActivity.this, R.anim.d_clockwise_rotation);
//            soundimage.startAnimation(sound_animation);
        } catch (Exception e) {

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

    SimpleExoPlayer privious_player;

    public void Release_Privious_Player() {
        if (privious_player != null) {
            privious_player.removeListener(this);
            privious_player.release();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();


        Release_Privious_Player();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        Release_Privious_Player();
        if (privious_player != null) {
            privious_player.setPlayWhenReady(false);
            screenOff();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        Release_Privious_Player();
        if (privious_player != null) {
            privious_player.setPlayWhenReady(false);
            screenOff();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        Release_Privious_Player();
        if (privious_player != null) {
            privious_player.release();
        }
    }

    private void likeUnlike(String videoId, String ownerId, final int position, final int type) {
        String userid = CommonUtils.userId(activity);
        videoMvvm.likeVideo(activity, userid, videoId, ownerId).observe(SelectedVideoActivity.this, new Observer<Map>() {
            @Override
            public void onChanged(Map map) {
                if (map.get("success").equals("1")) {

                    if (map.get("like_status").equals("1")) {
                        likeStatus = "1";
                    }
                    if (map.get("like_status").equals("0")) {
                        likeStatus = "0";

                    }
                    Intent intent = new Intent(AppConstants.TYPE_LIKED);
                    intent.putExtra(AppConstants.LIKE_COUNT, map.get("like_count").toString());
                    intent.putExtra(AppConstants.POSITION, position);
                    intent.putExtra("type", type);
                    LocalBroadcastManager.getInstance(activity).sendBroadcast(intent);
                } else {
                    Toast.makeText(activity, map.get("message").toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void Show_heart_on_DoubleTap(final RelativeLayout mainlayout, MotionEvent e) {

        int x = (int) e.getX() - 100;
        int y = (int) e.getY() - 100;
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        final ImageView iv = new ImageView(getApplicationContext());
        lp.setMargins(x, y, 0, 0);
        iv.setLayoutParams(lp);
        iv.setImageDrawable(getResources().getDrawable(
                R.drawable.heart_on));

        mainlayout.addView(iv);
        Animation fadeoutani = AnimationUtils.loadAnimation(activity, R.anim.fade_out);

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

    public void mainCachingFunction(List<ModelMyUploadedVideos.Detail> listMyUploaded
            , List<ModelLikedVideos.Detail> listLikedvideo
            , List<SearchAllPojo.Details.VideoList> listSearchVideo
            , List<GetSoundDetailsPojo.Details.SoundVideo> listSoundDetail
            , List<ModelHashTagsDetails.Details.HashtagVideo> listHashTag) {
        if (canCacheData) {
            switch (getIntent().getExtras().getString(AppConstants.SINGLE_VIDEO_TYPE)) {
                case AppConstants.TYPE_LIKED:
                    for (int i = 0; i < listLikedvideo.size(); i++) {
                        String[] array = new String[]{listLikedvideo.get(i).getVideoPath(), String.valueOf(listLikedvideo.size())};
                        new CachingDataStart().execute(array);
                    }
                    break;

                case AppConstants.OTHER_USER:
                case AppConstants.TYPE_UPLOADED:
                    for (int i = 0; i < listMyUploaded.size(); i++) {
                        String[] array = new String[]{listMyUploaded.get(i).getVideoPath(), String.valueOf(listMyUploaded.size())};
                        new CachingDataStart().execute(array);
                    }
                    break;


                case AppConstants.SOUNDS:
                    for (int i = 0; i < listSoundDetail.size(); i++) {
                        String[] array = new String[]{listSoundDetail.get(i).getVideoPath(), String.valueOf(listSoundDetail.size())};
                        new CachingDataStart().execute(array);
                    }
                    break;

                case AppConstants.HASHTAG:
                    for (int i = 0; i < listHashTag.size(); i++) {
                        String[] array = new String[]{listHashTag.get(i).getVideoPath(), String.valueOf(listHashTag.size())};
                        new CachingDataStart().execute(array);
                    }
                    break;

                case AppConstants.SEARCH_VIDEO:
                    for (int i = 0; i < listSearchVideo.size(); i++) {
                        String[] array = new String[]{listSearchVideo.get(i).getVideoPath(), String.valueOf(listSearchVideo.size())};
                        new CachingDataStart().execute(array);
                    }
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.ll_phoneLogin:

                startActivity(new Intent(this, RegisterActivity.class));

                break;

            case R.id.ll_google:

                App.getSingleton().setLoginType(AppConstants.LOGIN_PROFILE);
                googleLogin = new GoogleLogin(this, AppConstants.LOGIN_PROFILE);
                googleLogin.signIn();

                break;


            case R.id.ll_facebook:

                CallbackManager callbackManager = CallbackManager.Factory.create();
                facebookLogin = new FacebookLogin(this, AppConstants.LOGIN_PROFILE, getApplication());
                facebookLogin.FBLogin();

                break;
            case R.id.img_close_Login:

                ll_notLogin.setVisibility(View.GONE);

                break;

            case R.id.tv_policyLink:

                Intent intent = new Intent(this, WebLinkOpenActivity.class);
                intent.putExtra("key", "1");
                intent.putExtra("title", "Natural Born Fool Privacy Policy");
                startActivity(intent);
                break;

            case R.id.tvTermLink:

                Intent intent1 = new Intent(this, WebLinkOpenActivity.class);
                intent1.putExtra("key", "0");
                intent1.putExtra("title", "Terms and Conditions");
                startActivity(intent1);

                break;


        }
    }

    private class CachingDataStart extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            if (Integer.parseInt(params[1]) != 0) {
                while (isCachingdata) {

                }
                if (currentCachePage + 1 < Integer.parseInt(params[1])) {
                    if (currentCachePage + 1 < currentPage) {
                        currentCachePage = currentPage;

                    } else {
                        currentCachePage += 1;

                    }
                    preCacheVideo(CachingMain(params[0]));
                }
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
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
            } catch (InterruptedException e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapterAdd = true;
        if (currentPage == 0) {
            currentPage = -1;
            if ((privious_player != null && (!is_user_stop_video))) {
                privious_player.setPlayWhenReady(true);
                screenOn();
            }
        } else {
            if ((privious_player != null && (!is_user_stop_video))) {
                privious_player.setPlayWhenReady(true);
                screenOn();
            }
        }
    }

    private void notificationVideo(String videoId) {
        videoMvvm.singleVideo(activity, userId, videoId).observe(SelectedVideoActivity.this, new Observer<ModelMyUploadedVideos>() {
            @Override
            public void onChanged(ModelMyUploadedVideos notificationVideoModel) {
//                if (notificationVideoModel.getSuccess().equalsIgnoreCase("1")) {
//                    listNotifiation = notificationVideoModel.getDetails();
////                    setRecycler(listMyUploaded, 0, TYPE, listLikedvideo, listSearchVideo, listSoundDetail, listHashTag, listNotifiation);
//                }

                if (notificationVideoModel.getSuccess().equalsIgnoreCase("1")) {
                    Log.i("singleVideo", notificationVideoModel.getMessage());
                    isResponseGot = true;
                    App.getSingleton().setListMyUploaded(notificationVideoModel.getDetails());
                    getData();
                } else {
                    Toast.makeText(activity, notificationVideoModel.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void followUser(String otheruserId, final int position) {
        FollowUnfollowUser followUnfollowUser = new FollowUnfollowUser(this, new FollowUnfollowUser.followCallBack() {
            @Override
            public void followInterfaceCall(Map map) {
                if (map.get("success").equals("1")) {
                    if (map.get("following_status").toString().equals("true")) {

                        Intent intent = new Intent(AppConstants.FOLLOW_UNFOLLOW);
                        intent.putExtra(AppConstants.FOLLOW_UNFOLLOW, AppConstants.FOLLOW);
                        intent.putExtra(AppConstants.POSITION, position);
                        Toast.makeText(SelectedVideoActivity.this, map.get("message").toString(), Toast.LENGTH_SHORT).show();
                        LocalBroadcastManager.getInstance(SelectedVideoActivity.this).sendBroadcast(intent);
                    } else {
                        Intent intent = new Intent(AppConstants.FOLLOW_UNFOLLOW);
                        intent.putExtra(AppConstants.FOLLOW_UNFOLLOW, AppConstants.UNFOLLOW);
                        intent.putExtra(AppConstants.POSITION, position);
                        LocalBroadcastManager.getInstance(SelectedVideoActivity.this).sendBroadcast(intent);
                    }
                } else {
                    Log.i("follow", map.get("message").toString());
                }
            }
        });
        followUnfollowUser.followUnfollow(otheruserId);
    }

    private void openDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        final View confirmdailog = layoutInflater.inflate(R.layout.dialog_signup, null);
        final androidx.appcompat.app.AlertDialog dailogbox = new androidx.appcompat.app.AlertDialog.Builder(this).create();
        dailogbox.setCancelable(true);
        dailogbox.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dailogbox.setView(confirmdailog);

        confirmdailog.findViewById(R.id.img_google_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                App.getSingleton().setLoginType(AppConstants.LOGIN_VIDEO);
                googleLogin = new GoogleLogin(SelectedVideoActivity.this, AppConstants.LOGIN_VIDEO);
                googleLogin.signIn();
            }
        });

        confirmdailog.findViewById(R.id.img_fb_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                facebookLogin = new FacebookLogin(SelectedVideoActivity.this, AppConstants.LOGIN_VIDEO, getApplication());
                facebookLogin.FBLogin();
            }
        });

        confirmdailog.findViewById(R.id.btn_signup_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SelectedVideoActivity.this, RegisterActivity.class));
            }
        });
        confirmdailog.findViewById(R.id.btn_signin_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SelectedVideoActivity.this, LoginActivity.class));
            }
        });

        dailogbox.show();
    }

    private void dialogReportVideo(String id) {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        final View confirmdailog = layoutInflater.inflate(R.layout.dialog_report_video, null);
        final androidx.appcompat.app.AlertDialog dailogbox = new androidx.appcompat.app.AlertDialog.Builder(this).create();
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

                Intent intent = new Intent(SelectedVideoActivity.this, ReportProblemActivity.class).putExtra("ReportVideoID", id);
                startActivity(intent);
            }
        });

        dailogbox.show();


    }


    private void screenOn() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


    }

    private void screenOff() {
        getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }
}