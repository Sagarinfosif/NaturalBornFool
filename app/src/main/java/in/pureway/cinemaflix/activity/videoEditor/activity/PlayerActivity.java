package in.pureway.cinemaflix.activity.videoEditor.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

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
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.utils.App;
import in.pureway.cinemaflix.utils.AppConstants;
import com.google.android.exoplayer2.video.VideoListener;

import java.io.File;


public class PlayerActivity extends Activity implements Player.EventListener {

    public static String INTENT_URI = "player_uri";
    public static String VIDEOLEGTH = "VIDEOLEGTH";

    private PlayerView playerview;
    private PlayerControlView controls;
    boolean is_user_stop_video = false;
    private String uriString;
    PlayerView duetPlayer1;
    PlayerView duetPlayer2;
    LinearLayout duetLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        uriString = getIntent().getStringExtra(INTENT_URI);
        setContentView(R.layout.activity_player);
        duetLayout = findViewById(R.id.duet_layout);
        playerview = (PlayerView) findViewById(R.id.playerview);
        duetPlayer1 = (PlayerView) findViewById(R.id.exo_player_duet);
        duetPlayer2 = (PlayerView) findViewById(R.id.exo_player_duet2);
        findViewById(R.id.btn_next_player_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlayerActivity.this, PostVideoActivity.class);
                intent.putExtra(AppConstants.VIDEO_PATH, uriString);
                startActivity(intent);
                Release_Privious_Player();
            }
        });
//        if(MyVideoEditorActivity.isDuet){
//            duetLayout.setVisibility(View.VISIBLE);
//            playerview.setVisibility(View.GONE);
//            Set_Player_duet(uriString,MyVideoEditorActivity.duet_url);
//        }else{
//            duetLayout.setVisibility(View.GONE);
//            playerview.setVisibility(View.VISIBLE);
//        }

        Set_Player(uriString);
    }
    public void Set_Player(String path) {
        DefaultTrackSelector trackSelector = new DefaultTrackSelector();
        final SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(PlayerActivity.this, trackSelector);

        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(PlayerActivity.this,
                Util.getUserAgent(PlayerActivity.this, getString(R.string.app_name)));

        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(path));

        player.prepare(videoSource);

        player.setRepeatMode(Player.REPEAT_MODE_ALL);
        player.addListener(this);


        final PlayerView playerView = findViewById(R.id.playerview);
        playerView.setPlayer(player);


        player.setPlayWhenReady(true);
        privious_player = player;

        playerView.setOnTouchListener(new View.OnTouchListener() {
            private GestureDetector gestureDetector = new GestureDetector(PlayerActivity.this, new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    super.onFling(e1, e2, velocityX, velocityY);
                    float deltaX = e1.getX() - e2.getX();
                    float deltaXAbs = Math.abs(deltaX);
                    // Only when swipe distance between minimal and maximal distance value then we treat it as effective swipe
                    if ((deltaXAbs > 100) && (deltaXAbs < 1000)) {
                        if (deltaX > 0) {
//                            OpenProfile(item, true);
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
                    } else {
                        is_user_stop_video = true;
                        privious_player.setPlayWhenReady(false);
                    }


                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    super.onLongPress(e);

                }

                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    return super.onDoubleTap(e);
                }
            });

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(player2duet != null){
            player2duet.setPlayWhenReady(false);
        }
        if(player1duet != null){
            player1duet.setPlayWhenReady(false);

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(player2duet != null){
            player2duet.setPlayWhenReady(false);
        }
        if(player1duet != null){
            player1duet.setPlayWhenReady(false);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(player2duet != null){
            player2duet.setPlayWhenReady(true);
        }
        if(player1duet != null){
            player1duet.setPlayWhenReady(true);

        }
    }

    SimpleExoPlayer player1duet;
    SimpleExoPlayer player2duet;
    public void Set_Player_duet(String path,String url) {
        DefaultTrackSelector trackSelector = new DefaultTrackSelector();
        final SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(PlayerActivity.this, trackSelector);

        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(PlayerActivity.this,
                Util.getUserAgent(PlayerActivity.this, getString(R.string.app_name)));


        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(path));

        player.prepare(videoSource);

        player.setRepeatMode(Player.REPEAT_MODE_ALL);
        player.addListener(this);


//        final PlayerView playerView = findViewById(R.id.playerview);
        duetPlayer1.setPlayer(player);


        player.setPlayWhenReady(true);
        privious_player = player;
        final SimpleExoPlayer player1 = ExoPlayerFactory.newSimpleInstance(PlayerActivity.this, trackSelector);
        videoSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(url));

        player1.prepare(videoSource);

        player1.setRepeatMode(Player.REPEAT_MODE_ALL);
        player1.addListener(this);


//        final PlayerView playerView = findViewById(R.id.playerview);
        duetPlayer2.setPlayer(player1);


        player1.setPlayWhenReady(true);
        player.addVideoListener(new VideoListener() {
            @Override
            public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
//                if (pixelWidthHeightRatio > 1.1 || height < width) {
//                    duetPlayer1.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
//
//                } else {
//                    duetPlayer1.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);
//
//                }
            }

            @Override
            public void onRenderedFirstFrame() {

            }
        });
        player1.addVideoListener(new VideoListener() {
            @Override
            public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
//                if (pixelWidthHeightRatio > 1.1 || height < width) {
//
//
//
//                    duetPlayer2.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
//
//                } else {
//                    duetPlayer2.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);
//
//                }
            }

            @Override
            public void onRenderedFirstFrame() {

            }
        });
        player2duet = player1;
        player1duet = player;
    }
    SimpleExoPlayer privious_player;

    public void Release_Privious_Player() {
        if (privious_player != null) {
            privious_player.removeListener(this);
            privious_player.release();
        }
        if(player2duet != null){
            player2duet.removeListener(this);
            player2duet.release();
        }
        if(player1duet != null){
            player1duet.removeListener(this);
            player1duet.release();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        App.getSingleton().setGalleryVideo(false);
        File file = new File(uriString);
        if (file.exists()) {
            file.delete();
        }
        Release_Privious_Player();
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
}