package in.pureway.cinemaflix.activity.Video_Recording;

import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daasuu.gpuv.composer.GPUMp4Composer;
import com.daasuu.gpuv.egl.filter.GlFilterGroup;
import com.daasuu.gpuv.player.GPUPlayerView;
import com.daasuu.gpuv.player.PlayerScaleType;
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
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoListener;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.activity.videoEditor.activity.PostVideoActivity;
import in.pureway.cinemaflix.activity.videoEditor.util.Functions;
import in.pureway.cinemaflix.activity.videoEditor.util.Variables;
import in.pureway.cinemaflix.activity.videoEditor.util.VideoAudioFFMPEG;
import in.pureway.cinemaflix.utils.App;
import in.pureway.cinemaflix.utils.AppConstants;

public class Preview_Video_A extends AppCompatActivity implements Player.EventListener{

    String video_url;
    GPUPlayerView gpuPlayerView;
    public static int  select_postion=0;
    final List<FilterType> filterTypes = FilterType.createFilterList();
    Filter_Adapter adapter;
    RecyclerView recylerview;
    public  String ts = String.valueOf(new Date().getTime());
    String draft_file,getThumbnail="",uriString;
    private LinearLayout duetLayout;
    PlayerView duetPlayer2;
    PlayerView duetPlayer1;
    MovieWrapperView movieWrapperView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview__video1);

        Intent intent=getIntent();
        if(intent!=null){
            draft_file=intent.getStringExtra("draft_file");
        }

        select_postion=0;

        if(App.getSingleton().getShootcheck().equalsIgnoreCase("1")){
            video_url=getIntent().getStringExtra("path");
        }else {
            video_url= Variables.outputfile2;
        }

        thumbnail(video_url);
        findViewById(R.id.Goback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
            }
        });


        findViewById(R.id.next_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(select_postion==0){
                    try {
                        if(App.getSingleton().getShootcheck().equalsIgnoreCase("1")){
                            Functions.copyFile(new File(Variables.outputfile), new File(Variables.app_folder +"/"+ts+"output-filtered.mp4"));


                            video_url= Variables.app_folder +"/"+ts+"output-filtered.mp4";
                            GotopostScreen(video_url);
                        }else {
                            Functions.copyFile(new File(Variables.outputfile2), new File(Variables.app_folder +"/"+ts+"output-filtered.mp4"));
                            video_url= Variables.app_folder +"/"+ts+"output-filtered.mp4";
                            GotopostScreen(video_url);
                        }
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                        Log.d(Variables.tag,e.toString());
                        Save_Video(video_url, Variables.app_folder +"/"+ts+"output-filtered.mp4");
                    }
                }
                else
                    Save_Video(video_url, Variables.app_folder +"/"+ts+"output-filtered.mp4");
            }
        });


        Set_Player(video_url);


        recylerview=findViewById(R.id.recylerview);

        adapter = new Filter_Adapter(this, filterTypes, new Filter_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int postion, FilterType item) {
                select_postion=postion;
                gpuPlayerView.setGlFilter(FilterType.createGlFilter(filterTypes.get(postion), getApplicationContext()));
                adapter.notifyDataSetChanged();
            }
        });
        recylerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recylerview.setAdapter(adapter);

        duetLayout = findViewById(R.id.duet_layout);
        duetPlayer1 = (PlayerView) findViewById(R.id.exo_player_duet);
        duetPlayer2 = (PlayerView) findViewById(R.id.exo_player_duet2);

        if (Video_Recoder_A.isDuet) {
            duetLayout.setVisibility(View.VISIBLE);
            movieWrapperView.setVisibility(View.GONE);
            recylerview.setVisibility(View.GONE);
            Set_Player_duet(uriString, Video_Recoder_A.duet_url);
        } else {
            recylerview.setVisibility(View.VISIBLE);
            duetLayout.setVisibility(View.GONE);
            movieWrapperView.setVisibility(View.VISIBLE);
        }

    }


    SimpleExoPlayer privious_player;
    SimpleExoPlayer player1duet;
    SimpleExoPlayer player2duet;


    public void Set_Player_duet(String path, String url) {
        DefaultTrackSelector trackSelector = new DefaultTrackSelector();
        final SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(Preview_Video_A.this, trackSelector);

        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(Preview_Video_A.this,
                Util.getUserAgent(Preview_Video_A.this, getString(R.string.app_name)));


        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(path));

        player.prepare(videoSource);

        player.setRepeatMode(Player.REPEAT_MODE_ALL);
        player.addListener(this);


//        final PlayerView playerView = findViewById(R.id.playerview);
        duetPlayer1.setPlayer(player);


        player.setPlayWhenReady(true);
        privious_player = player;
        final SimpleExoPlayer player1 = ExoPlayerFactory.newSimpleInstance(Preview_Video_A.this, trackSelector);
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
                if (pixelWidthHeightRatio > 1.1 || height < width) {


                    duetPlayer1.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);

                } else {
                    duetPlayer1.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);

                }
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

    //TODO thumb time stamp

    private void thumbnail(String uriString) {
        File thumbnailFile = new File(Variables.thumbnail); //delete speeded video if exists
        if (thumbnailFile.exists()) {
            thumbnailFile.delete();
        }
        String[] thumbnailCommand = new String[]{"-i", uriString, "-ss", "00:00:03.000", "-vframes", "1", thumbnailFile.getAbsolutePath()};



        VideoAudioFFMPEG videoAudioFFMPEG = new VideoAudioFFMPEG(Preview_Video_A.this, new VideoAudioFFMPEG.Thumbnail() {
            @Override
            public void onThumbnailCompleteListener() {
                getThumbnail=thumbnailFile.getAbsolutePath();
//                Toast.makeText(PlayerActivity.this, String.valueOf(thumbnailFile.getAbsolutePath()), Toast.LENGTH_SHORT).show();
            }
        });
        videoAudioFFMPEG.thumbnailCommand(thumbnailCommand);
    }




    // this function will set the player to the current video
    SimpleExoPlayer player;
    public void Set_Player(String path){
        DefaultTrackSelector trackSelector = new DefaultTrackSelector();
        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this,  getString(R.string.app_name)));

        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(path));

        player.prepare(videoSource);

        player.setRepeatMode(Player.REPEAT_MODE_ALL);
        player.addListener(this);

        player.setPlayWhenReady(true);

        gpuPlayerView = new GPUPlayerView(this);

        MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
        metaRetriever.setDataSource(path);
        String rotation=metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION);

        if(rotation!=null && rotation.equalsIgnoreCase("0")){
            gpuPlayerView.setPlayerScaleType(PlayerScaleType.RESIZE_FIT_WIDTH);
        }
        else
            gpuPlayerView.setPlayerScaleType(PlayerScaleType.RESIZE_NONE);

        gpuPlayerView.setSimpleExoPlayer(player);
        gpuPlayerView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        movieWrapperView=findViewById(R.id.layout_movie_wrapper);
        movieWrapperView.addView(gpuPlayerView);
//        ((MovieWrapperView) findViewById(R.id.layout_movie_wrapper)).addView(gpuPlayerView);

        gpuPlayerView.onResume();

    }



    // this is lifecyle of the Activity which is importent for play,pause video or relaese the player
    @Override
    protected void onStop() {
        super.onStop();
        if(player!=null){
            player.setPlayWhenReady(false);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        if(player!=null){
            player.setPlayWhenReady(true);
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(player!=null){
            player.setPlayWhenReady(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(adapter!=null)
            adapter.recycle_bitmap();

        if(player!=null){
            player.removeListener(Preview_Video_A.this);
            player.release();
            player=null;
        }
    }





    // this function will add the filter to video and save that same video for post the video in post video screen
    public void Save_Video(String srcMp4Path, final String destMp4Path){
        Functions.Show_determinent_loader(this,false,false);
        new GPUMp4Composer(srcMp4Path, destMp4Path)
                .filter(new GlFilterGroup(FilterType.createGlFilter(filterTypes.get(select_postion), getApplicationContext())))
                .listener(new GPUMp4Composer.Listener() {
                    @Override
                    public void onProgress(double progress) {
                        Log.d("resp",""+(int) (progress*100));
                        Functions.Show_loading_progress((int)(progress*100));
                    }
                    @Override
                    public void onCompleted() {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Functions.cancel_determinent_loader();

                                GotopostScreen(destMp4Path);


                            }
                        });


                    }

                    @Override
                    public void onCanceled() {
                        Log.d("resp", "onCanceled");
                    }

                    @Override
                    public void onFailed(Exception exception) {

                        Log.d("resp",exception.toString());

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {

                                    Functions.cancel_determinent_loader();

                                    Toast.makeText(Preview_Video_A.this, "Try Again", Toast.LENGTH_SHORT).show();
                                }catch (Exception e){

                                }
                            }
                        });

                    }
                })
                .start();


    }



    public void GotopostScreen(String video_url){
        Intent intent =new Intent(Preview_Video_A.this, PostVideoActivity.class);
        intent.putExtra("draft_file",draft_file);
        intent.putExtra("thumbnail", getThumbnail);
        intent.putExtra(AppConstants.VIDEO_PATH, video_url);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }



    // Bottom all the function and the Call back listener of the Expo player
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


    @Override
    public void onBackPressed() {

        finish();
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);

    }




}