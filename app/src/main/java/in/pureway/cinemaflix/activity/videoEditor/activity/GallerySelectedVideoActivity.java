package in.pureway.cinemaflix.activity.videoEditor.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import in.pureway.cinemaflix.utils.App;
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
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.io.File;
import java.util.concurrent.TimeUnit;

import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.utils.AppConstants;

public class GallerySelectedVideoActivity extends AppCompatActivity implements Player.EventListener {
    public static String INTENT_URI = "player_uri";
    public static String VIDEOLEGTH = "VIDEOLEGTH";

    private PlayerView playerview;
    private PlayerControlView controls;
    boolean is_user_stop_video = false;
    String uriString = "";
    private Long videoLength;
    private String videoLengths;

    private long millis,fileSizeInMB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_selected_video);
        playerview = (PlayerView) findViewById(R.id.playerview);

        uriString = getIntent().getStringExtra(AppConstants.INTENT_URI1);
        videoLengths = App.getSingleton().getVideoLength();

        findViewById(R.id.btn_next_player_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (millis<=301 && fileSizeInMB<=100){
                    Intent intent = new Intent(GallerySelectedVideoActivity.this, PostVideoActivity.class);
                    intent.putExtra(AppConstants.VIDEO_PATH, uriString);
                    startActivity(intent);
                    Release_Privious_Player();
                }else {
                    alertDialog();
                }
//
            }
        });

        Set_Player(uriString);

        try {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(uriString);
            String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);

            int width = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
            int height = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
//        retriever.release();

            App.getSingleton().setIsWidth(String.valueOf(width));
            App.getSingleton().setIsHeight(String.valueOf(height));



            File file = new File(uriString);

// Get length of file in bytes
            long fileSizeInBytes = file.length();
// Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
            long fileSizeInKB = fileSizeInBytes / 1024;
// Convert the KB to MegaBytes (1 MB = 1024 KBytes)
            fileSizeInMB = fileSizeInKB / 1024;



            long timeInmillisec = Long.parseLong( time );
            long duration = timeInmillisec / 1000;
//        long minutes1 = TimeUnit.MILLISECONDS.toSeconds(timeInmillisec);

            long hours = duration / 3600;
            long minutes = (duration - hours * 3600) / 60;
            long seconds = duration - (hours * 3600 + minutes * 60);
//        long time1=hours*minutes*seconds;
//            Toast.makeText(this,Long.toString(seconds),Toast.LENGTH_SHORT).show();
            App.getSingleton().setVideoLength(Long.toString(seconds));

            if (minutes!=0){
                long millis = minutes * 60+seconds;

                App.getSingleton().setVideoType("1");
                App.getSingleton().setVideoLength(Long.toString(millis));

            }else {
                App.getSingleton().setVideoType("0");

            }

//            Set_Player(uriString);
        }catch (Exception e){
            Log.i("exception", "exception==="+e.getMessage());
        }


    }

    public void Set_Player(String path) {
        DefaultTrackSelector trackSelector = new DefaultTrackSelector();
        final SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(GallerySelectedVideoActivity.this, trackSelector);

        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(GallerySelectedVideoActivity.this,
                Util.getUserAgent(GallerySelectedVideoActivity.this, getString(R.string.app_name)));

        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(path));

        player.prepare(videoSource);


//
//        int duration= (int) (player.getDuration()/1000);
//        int hours = duration / 3600;
//        int minutes = (duration / 60) - (hours * 60);
//        int seconds = duration - (hours * 3600) - (minutes * 60) ;
//        String formatted = String.format("%d:%02d:%02d", hours, minutes, seconds);
//        Toast.makeText(getApplicationContext(), "duration is " + formatted ,  ToaPosst.LENGTH_LONG).show();

//        tv_timeLength.setText(formatted);


//        return totalSeconds;
//        tv_timeLength.setText(totalSeconds);


//        App.getSingleton().setVideoLength(videoLengthS);

        player.setRepeatMode(Player.REPEAT_MODE_ALL);
        player.addListener(this);


        final PlayerView playerView = findViewById(R.id.playerview);
        playerView.setPlayer(player);


        player.setPlayWhenReady(true);
        privious_player = player;

        playerView.setOnTouchListener(new View.OnTouchListener() {
            private GestureDetector gestureDetector = new GestureDetector(GallerySelectedVideoActivity.this, new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    super.onFling(e1, e2, velocityX, velocityY);
                    float deltaX = e1.getX() - e2.getX();
                    float deltaXAbs = Math.abs(deltaX);
// Only when swipe distance between minimal and maximal distance value then we treat it as effective swipe
                    if ((deltaXAbs > 100) && (deltaXAbs < 1000)) {
                        if (deltaX > 0) {
// OpenProfile(item, true);
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
        App.getSingleton().setGalleryVideo(false);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
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
//        int maxduration = getVideoDurationSeconds(privious_player);
//        tv_timeLength.setText(maxduration);


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
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private void alertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Video should be under 5 minutes or 100 MB");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }
}