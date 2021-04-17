package in.pureway.cinemaflix.activity.Video_Recording;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.coremedia.iso.boxes.Container;
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
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.AppendTrack;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.activity.videoEditor.util.Functions;
import in.pureway.cinemaflix.activity.videoEditor.util.Variables;
import in.pureway.cinemaflix.utils.App;

import static in.pureway.cinemaflix.activity.Video_Recording.Video_Recoder_A.Sounds_list_Request_code;

public class GallerySelectedVideo_A extends AppCompatActivity implements View.OnClickListener, Player.EventListener{

    String path;
    TextView add_sound_txt;
    String draft_file;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Hide_navigation();
        setContentView(R.layout.activity_gallery_selected_video_);

        Intent intent=getIntent();
        if(intent!=null){
            path=intent.getStringExtra("video_path");
            draft_file=intent.getStringExtra("draft_file");
        }

        Variables.Selected_sound_id="null";

        findViewById(R.id.Goback).setOnClickListener(this);

        add_sound_txt=findViewById(R.id.add_sound_txt);
        add_sound_txt.setOnClickListener(this);

        findViewById(R.id.next_btn).setOnClickListener(this);

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(path);
        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        long timeInmillisec = Long.parseLong( time );
        long duration = timeInmillisec / 1000;
        long minutes1 = TimeUnit.MILLISECONDS.toSeconds(timeInmillisec);

        long hours = duration / 3600;
        long minutes = (duration - hours * 3600) / 60;
        long seconds = duration - (hours * 3600 + minutes * 60);
//        long time1=hours*minutes*seconds;
        Toast.makeText(this,Long.toString(seconds),Toast.LENGTH_SHORT).show();
        App.getSingleton().setVideoLength(Long.toString(seconds));

        if (minutes!=0){
            long millis = minutes * 60+seconds;

           App.getSingleton().setVideoType("1");
           App.getSingleton().setVideoLength(Long.toString(millis));

        }else {
            App.getSingleton().setVideoType("0");

        }
//        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(GallerySelectedVideo_A.this,
//                Util.getUserAgent(GallerySelectedVideo_A.this, getString(R.string.app_name)));
//        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(path));
//
////        Uri uri=Uri.parse(path);
//        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//// There are other variations of setDataSource(), if you have a different input
//        retriever.setDataSource(this, Uri.parse(path));
//        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
//        long durationMs = Long.parseLong(time);
//
//        App.getSingleton().setVideoLength(time);

        Set_Player();

    }

    // this will call when swipe for another video and
    // this function will set the player to the current video
    SimpleExoPlayer video_player;
    public void Set_Player(){

        DefaultTrackSelector trackSelector = new DefaultTrackSelector();
        video_player = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, "TikTok"));

        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(path));

        video_player.prepare(videoSource);

        video_player.setRepeatMode(Player.REPEAT_MODE_OFF);
        video_player.addListener(this);



        final PlayerView playerView=findViewById(R.id.playerview);
        playerView.setPlayer(video_player);

        playerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });


        video_player.setPlayWhenReady(true);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.Goback:
                finish();
                overridePendingTransition(R.anim.in_from_top, R.anim.out_from_bottom);
                break;

            case R.id.add_sound_txt:
                Toast.makeText(this, "coming soon...", Toast.LENGTH_SHORT).show();
//                Intent intent =new Intent(this, SoundList_Main_A.class);
//                startActivityForResult(intent,Sounds_list_Request_code);
//                overridePendingTransition(R.anim.in_from_bottom, R.anim.out_to_top);
                break;

            case R.id.next_btn:

                if(video_player!=null) {
                    video_player.setPlayWhenReady(false);
                }
                if(audio!=null) {
                    audio.pause();
                }

                if(audio!=null)
                    Merge_withAudio();
                else {
                    Go_To_preview_Activity();
                }

                //append();
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Sounds_list_Request_code){
            if(data!=null){

                if(data.getStringExtra("isSelected").equals("yes")){
                    add_sound_txt.setText(data.getStringExtra("sound_name"));
                    Variables.Selected_sound_id=data.getStringExtra("sound_id");
                    PreparedAudio();
                }

            }

        }
    }


    // this will play the sound with the video when we select the audio
    MediaPlayer audio;
    public  void PreparedAudio(){
        video_player.setVolume(0);

        File file=new File(Variables.app_hided_folder + Variables.SelectedAudio_AAC);
        if(file.exists()) {
            audio = new MediaPlayer();
            try {
                audio.setDataSource(Variables.app_hided_folder + Variables.SelectedAudio_AAC);
                audio.prepare();
                audio.setLooping(true);


                video_player.seekTo(0);
                video_player.setPlayWhenReady(true);
                audio.start();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }




    // this will apped all the videos parts in one  fullvideo
    private boolean append() {
        final ProgressDialog progressDialog=new ProgressDialog(GallerySelectedVideo_A.this);
        new Thread(new Runnable() {
            @Override
            public void run() {


                runOnUiThread(new Runnable() {
                    public void run() {

                        progressDialog.setMessage("Please wait..");
                        progressDialog.show();
                    }
                });

                try {

                    ArrayList<String> video_list=new ArrayList<>();

                    File file=new File(path);

                    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                    retriever.setDataSource(GallerySelectedVideo_A.this, Uri.fromFile(file));
                    String hasVideo = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_VIDEO);
                    boolean isVideo = "yes".equals(hasVideo);

                    if(isVideo && file.length()>3000){
                        video_list.add(path);
                    }


                    Movie[] inMovies = new Movie[video_list.size()];

                    for (int i=0;i<video_list.size();i++){

                        inMovies[i]= MovieCreator.build(video_list.get(i));
                    }


                    List<Track> videoTracks = new LinkedList<Track>();
                    List<Track> audioTracks = new LinkedList<Track>();
                    for (Movie m : inMovies) {
                        for (Track t : m.getTracks()) {
                            if (t.getHandler().equals("soun")) {
                                audioTracks.add(t);
                            }
                            if (t.getHandler().equals("vide")) {
                                videoTracks.add(t);
                            }
                        }
                    }
                    Movie result = new Movie();
                    if (audioTracks.size() > 0) {
                        result.addTrack(new AppendTrack(audioTracks.toArray(new Track[audioTracks.size()])));
                    }
                    if (videoTracks.size() > 0) {
                        result.addTrack(new AppendTrack(videoTracks.toArray(new Track[videoTracks.size()])));
                    }

                    Container out = new DefaultMp4Builder().build(result);

                    String outputFilePath=null;
                    if(audio!=null){
                        outputFilePath= Variables.outputfile;
                    }else {
                        outputFilePath= Variables.outputfile2;
                    }

                    FileOutputStream fos = new FileOutputStream(new File(outputFilePath));
                    out.writeContainer(fos.getChannel());
                    fos.close();

                    runOnUiThread(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();

                            if(audio!=null)
                                Merge_withAudio();
                            else {
                                Go_To_preview_Activity();
                            }

                        }
                    });



                } catch (Exception e) {
                    Log.d(Variables.tag,e.toString());
                    progressDialog.dismiss();

                }
            }
        }).start();



        return true;
    }



    // this will add the select audio with the video
    public void Merge_withAudio(){

        String audio_file;
        audio_file = Variables.app_hided_folder + Variables.SelectedAudio_AAC;


        Merge_Video_Audio merge_video_audio=new Merge_Video_Audio(GallerySelectedVideo_A.this);
        merge_video_audio.doInBackground(audio_file, Variables.outputfile, Variables.outputfile2,draft_file);

    }



    public void Go_To_preview_Activity(){

        try {
            Functions.copyFile(new File(path),
                    new File(Variables.outputfile2));
        } catch (IOException e) {
            e.printStackTrace();
        }


        Intent intent =new Intent(this, Preview_Video_A.class);
        intent.putExtra("draft_file",draft_file);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        finish();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(video_player!=null){
            video_player.setPlayWhenReady(true);
        }
    }



    @Override
    public void onStop() {
        super.onStop();
        try{
            if(video_player!=null){
                video_player.setPlayWhenReady(false);
            }
            if(audio!=null){
                audio.pause();
            }
        }catch (Exception e){

        }
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        if(video_player!=null){
            video_player.release();
        }

        if(audio!=null){
            audio.pause();
            audio.release();
        }
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

        if(playbackState== Player.STATE_ENDED){

            video_player.seekTo(0);
            video_player.setPlayWhenReady(true);

            if(audio!=null){
                audio.seekTo(0);
                audio.start();
            }
        }

    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

        Toast.makeText(this, "Repeat mode change", Toast.LENGTH_SHORT).show();

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

        Log.d("resp","smmdsmd");
    }



    // this will hide the bottom mobile navigation controll
    public void Hide_navigation(){

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        // This work only for android 4.4+
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {

            getWindow().getDecorView().setSystemUiVisibility(flags);

            // Code below is to handle presses of Volume up or Volume down.
            // Without this, after pressing volume buttons, the navigation bar will
            // show up and won't hide
            final View decorView = getWindow().getDecorView();
            decorView
                    .setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener()
                    {

                        @Override
                        public void onSystemUiVisibilityChange(int visibility)
                        {
                            if((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0)
                            {
                                decorView.setSystemUiVisibility(flags);
                            }
                        }
                    });
        }

    }


    @SuppressLint("NewApi")
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && hasFocus)
        {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }



    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Functions.deleteCache(this);
    }


}