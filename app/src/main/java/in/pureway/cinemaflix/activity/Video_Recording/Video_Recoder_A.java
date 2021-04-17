package in.pureway.cinemaflix.activity.Video_Recording;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.coremedia.iso.boxes.Container;
import com.coremedia.iso.boxes.MovieHeaderBox;
import com.daasuu.gpuv.composer.GPUMp4Composer;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
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
import com.google.android.exoplayer2.video.VideoListener;
import com.googlecode.mp4parser.FileDataSourceImpl;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.AppendTrack;
import com.googlecode.mp4parser.authoring.tracks.CroppedTrack;
import com.googlecode.mp4parser.util.Matrix;
import com.googlecode.mp4parser.util.Path;
import com.gowtham.library.ui.ActVideoTrimmer;
import com.gowtham.library.utils.TrimmerConstants;
import com.wonderkiln.camerakit.CameraKit;
import com.wonderkiln.camerakit.CameraProperties;
import com.wonderkiln.camerakit.CameraView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.activity.sounds.activity.SoundsActivity;
import in.pureway.cinemaflix.activity.videoEditor.util.Functions;
import in.pureway.cinemaflix.activity.videoEditor.util.ProgressBarListener;
import in.pureway.cinemaflix.activity.videoEditor.util.Variables;
import in.pureway.cinemaflix.utils.App;

//import com.gowtham.library.utils.TrimmerConstants;

public class Video_Recoder_A extends AppCompatActivity implements View.OnClickListener{

    CameraView cameraView,camera_layout_duet;
    int number = 0;
    ArrayList<String> videopaths = new ArrayList<>();
    ImageButton record_image;
    ImageButton done_btn;
    boolean is_recording = false;
    boolean is_flash_on = false;
    private String videoLengthS = "0";

    ImageButton flash_btn;
    SegmentedProgressBar video_progress;
    LinearLayout camera_options;
    ImageButton rotate_camera, cut_video_btn;

    public  Long tsLong = System.currentTimeMillis() / 1000;
    public  String ts = tsLong.toString();
    String path = "";
    String soundPath = "";

    public static String duet_url;
    public static boolean isDuet = false;

    public static int Sounds_list_Request_code = 1;
    TextView add_sound_txt,sec;

    int sec_passed = 0;
    long time_in_milis = 0;

    TextView countdown_timer_txt;
    boolean is_recording_timer_enable;
    int recording_time = 3;
    FrameLayout camera_layout;
    PlayerView exoPlayerForDuet;
    SimpleExoPlayer playerDuet;
    public static String video_height;
    public static String video_width;
    boolean isDurationSet = false;
    private long MAX_RECORDING_DURATION = 15000;
    private LinearLayout upload_layout,duetLinearLayout;
    TextView short_video_time_txt, long_video_time_txt;
    private RelativeLayout time_layout;
    private ImageView img_sound;

    private int mGLViewWidth = 0;
    private int mGLViewHeight = 0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video__recoder_);

        Point realSize = new Point();
        Display display = ((WindowManager)this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        display.getRealSize(realSize);

        mGLViewWidth = realSize.x;
        mGLViewHeight = realSize.y;

        Variables.Selected_sound_id = "null";
        Variables.recording_duration = Variables.max_recording_duration;

        cameraView = findViewById(R.id.camera);
        camera_layout_duet = findViewById(R.id.camera_layout_duet);


        camera_options = findViewById(R.id.camera_options);
        sec = findViewById(R.id.sec);

        record_image = findViewById(R.id.record_image);

//        findViewById(R.id.upload_layout).setOnClickListener(this);
        upload_layout=findViewById(R.id.upload_layout);
        upload_layout.setOnClickListener(this);

        cut_video_btn = findViewById(R.id.cut_video_btn);
        cut_video_btn.setVisibility(View.GONE);
        cut_video_btn.setOnClickListener(this);

        done_btn = findViewById(R.id.done);
        done_btn.setEnabled(false);
        done_btn.setOnClickListener(this);

        duetLinearLayout = findViewById(R.id.duet_layout);
        img_sound = findViewById(R.id.img_sound);
        rotate_camera = findViewById(R.id.rotate_camera);
        rotate_camera.setOnClickListener(this);
        flash_btn = findViewById(R.id.flash_camera);
        flash_btn.setOnClickListener(this);

        findViewById(R.id.Goback).setOnClickListener(this);
        time_layout=findViewById(R.id.time_layout);
        add_sound_txt = findViewById(R.id.add_sound_txt);
        add_sound_txt.setOnClickListener(this);

        findViewById(R.id.time_btn).setOnClickListener(this);
        Intent intent = getIntent();
        if (intent.hasExtra("sound_name")) {
            add_sound_txt.setText(intent.getStringExtra("sound_name"));
            Variables.Selected_sound_id = intent.getStringExtra("sound_id");

            time_layout=findViewById(R.id.time_layout);
            time_layout.setVisibility(View.INVISIBLE);
//            findViewById(R.id.time_layout).setVisibility(View.INVISIBLE);
            soundPath = Variables.mp3File;
            PreparedAudio(soundPath);
        }
        record_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                App.getSingleton().setVideoType("0");

                Start_or_Stop_Recording();
            }
        });
        countdown_timer_txt = findViewById(R.id.countdown_timer_txt);
        short_video_time_txt = findViewById(R.id.short_video_time_txt);
        long_video_time_txt = findViewById(R.id.long_video_time_txt);
        short_video_time_txt.setOnClickListener(this);
        long_video_time_txt.setOnClickListener(this);

        if (getIntent().hasExtra("duet_video")) {
            isDuet = true;
        } else {
            isDuet = false;
        }
        if (isDuet) {
            setupForDuet();
            camera_layout_duet = findViewById(R.id.camera_layout_duet);
        }


        if (isDuet) {
//            ll_speed_bottom.setVisibility(View.INVISIBLE);
            upload_layout.setVisibility(View.GONE);
            add_sound_txt.setVisibility(View.GONE);
            time_layout.setVisibility(View.GONE);
            img_sound.setVisibility(View.GONE);
            camera_layout_duet = findViewById(R.id.camera_layout_duet);
            mGLViewHeight = camera_layout_duet.getHeight();
            mGLViewWidth = camera_layout_duet.getWidth();
            duetLinearLayout.setVisibility(View.VISIBLE);
            cameraView.setVisibility(View.GONE);
            camera_layout_duet.setVisibility(View.VISIBLE);
            cut_video_btn.setVisibility(View.GONE);
//            findViewById(R.id.camera_layout).setVisibility(View.GONE);
        } else {
            cut_video_btn.setVisibility(View.VISIBLE);
            add_sound_txt.setVisibility(View.VISIBLE);
            upload_layout.setVisibility(View.VISIBLE);
            img_sound.setVisibility(View.VISIBLE);
            time_layout.setVisibility(View.VISIBLE);
            camera_layout = findViewById(R.id.camera_layout);
            cameraView.setVisibility(View.VISIBLE);
            duetLinearLayout.setVisibility(View.GONE);
        }

        initlize_Video_progress();

    }

    private int getVideoDurationSeconds(SimpleExoPlayer player) {
        int timeMs = (int) player.getDuration();
        int totalSeconds = timeMs;
        return totalSeconds;
    }

    public void setupForDuet() {

//        mCamera.setCameraSize(camera_layout.getHeight(),camera_layout.getWidth());
//        isDurationSet = false;
        exoPlayerForDuet = findViewById(R.id.exo_player_duet);
        Intent intent = getIntent();
//        String video_duet = "https://jazzyvideos.b-cdn.net/videos/1596110069_1524025352_main.mp4";
        String video_duet = "";
        if (intent.hasExtra("duet_video")) {
            video_duet = intent.getStringExtra("duet_video");
        }
        duet_url = video_duet;

        DefaultLoadControl loadControl = new DefaultLoadControl.Builder().setBufferDurationsMs(1 * 1024, 1 * 1024, 500, 1024).createDefaultLoadControl();

        DefaultTrackSelector trackSelector = new DefaultTrackSelector();
        playerDuet = ExoPlayerFactory.newSimpleInstance(Video_Recoder_A.this, trackSelector, loadControl);

        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(Video_Recoder_A.this, Util.getUserAgent(Video_Recoder_A.this, Video_Recoder_A.this.getResources().getString(R.string.app_name)));
        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(video_duet));
        playerDuet.prepare(videoSource);
        playerDuet.setRepeatMode(Player.REPEAT_MODE_ALL);
        exoPlayerForDuet.setPlayer(playerDuet);


        playerDuet.addVideoListener(new VideoListener() {
            @Override
            public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
//                video_height = String.valueOf(height);
//                video_width = String.valueOf(width);
            }

            @Override
            public void onRenderedFirstFrame() {

            }
        });
        playerDuet.addListener(new Player.EventListener() {
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
                if (playbackState == ExoPlayer.STATE_READY && !isDurationSet) {
                    int maxduration = getVideoDurationSeconds(playerDuet);
                    MAX_RECORDING_DURATION = maxduration;
                    initlize_Video_progress();
                    Log.i("MAX_DURATION_DUET", MAX_RECORDING_DURATION + " : " + maxduration);
                    isDurationSet = true;
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
        });

    }



    public void initlize_Video_progress() {
        sec_passed = 0;
        video_progress = findViewById(R.id.video_progress);
        video_progress.enableAutoProgressView(Variables.recording_duration);
        video_progress.setDividerColor(Color.WHITE);
        video_progress.setDividerEnabled(true);
        video_progress.setDividerWidth(4);
        video_progress.setShader(new int[]{Color.CYAN, Color.CYAN, Color.CYAN});


        video_progress.SetListener(new ProgressBarListener() {
            @Override
            public void TimeinMill(long mills) {
                time_in_milis = mills;
                sec_passed = (int) (mills / 1000);



                if (sec_passed > (Variables.recording_duration / 1000) - 1) {
                    Start_or_Stop_Recording();
                }

                if (is_recording_timer_enable && sec_passed >= recording_time) {
                    is_recording_timer_enable = false;
                    Start_or_Stop_Recording();
                }

                if (isDuet) {
                    if (mills == MAX_RECORDING_DURATION) {
                        Log.i("recording_Completed", "MAX_DURATION REACHED : " + MAX_RECORDING_DURATION);
                        if (isDuet) {
                            playerDuet.release();
                        }
                        Start_or_Stop_Recording();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                append();
                            }
                        }, 500);
                    }
                }
//                else {
//                    if (mills == MAX_RECORDING_DURATION || mills >= MAX_RECORDING_DURATION) {
//                        Log.i("holdListener:recording Completed", "MAX_DURATION REACHED : " + MAX_RECORDING_DURATION);
//                        Start_or_Stop_Recording();
//                        final Handler handler = new Handler();
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                append();
//                            }
//                        }, 500);
//                    }
//                }

            }
        });
    }

    // if the Recording is stop then it we start the recording
    // and if the mobile is recording the video then it will stop the recording
    public void Start_or_Stop_Recording() {


        if (isDuet) {
            if (playerDuet != null) {
                playerDuet.setPlayWhenReady(true);
            }
        }

        if (!is_recording && sec_passed < (Variables.recording_duration / 1000) - 1) {

            number = number + 1;

            is_recording = true;

            File file = new File(Variables.app_hided_folder + "myvideo" + (number) + ".mp4");
            videopaths.add(Variables.app_hided_folder + "myvideo" + (number) + ".mp4");
            cameraView.captureVideo(file);

            if (audio != null)
                audio.start();

            done_btn.setImageDrawable(getResources().getDrawable(R.drawable.ic_not_done));
            done_btn.setEnabled(false);
            video_progress.resume();


            record_image.setImageDrawable(getResources().getDrawable(R.drawable.ic_recoding_yes));

            cut_video_btn.setVisibility(View.GONE);

            time_layout.setVisibility(View.INVISIBLE);
//            findViewById(R.id.time_layout).setVisibility(View.INVISIBLE);
//            findViewById(R.id.upload_layout).setEnabled(false);
            upload_layout.setEnabled(false);
            camera_options.setVisibility(View.GONE);
            add_sound_txt.setClickable(false);
            rotate_camera.setVisibility(View.GONE);

        } else if (is_recording) {


            if (isDuet) {
                if (playerDuet != null) {
                    playerDuet.setPlayWhenReady(false);

                }
            }

            is_recording = false;

            video_progress.pause();
            video_progress.addDivider();

            if (audio != null && audio.isPlaying())
                audio.pause();

            cameraView.stopVideo();


            Check_done_btn_enable();

            if(isDuet){
                cut_video_btn.setVisibility(View.GONE);
            }else {
                cut_video_btn.setVisibility(View.VISIBLE);
            }


//            findViewById(R.id.upload_layout).setEnabled(true);
            upload_layout.setEnabled(false);

            record_image.setImageDrawable(getResources().getDrawable(R.drawable.ic_recoding_no));
            camera_options.setVisibility(View.VISIBLE);

        } else if (sec_passed > (Variables.recording_duration / 1000)) {
            Functions.Show_Alert(this, "Alert", "Video only can be a " + (int) Variables.recording_duration / 1000 + " S");
        }

    }


    public void Check_done_btn_enable() {
        if (sec_passed > (Variables.min_time_recording / 1000)) {
            sec_passed=sec_passed-3;
            String str = Integer.toString(sec_passed);

            App.getSingleton().setVideoLength(str);

            done_btn.setImageDrawable(getResources().getDrawable(R.drawable.ic_done_red));
            done_btn.setEnabled(true);
        } else {
            String str = Integer.toString(sec_passed);
            sec_passed=sec_passed-3;

            App.getSingleton().setVideoLength(str);
            done_btn.setImageDrawable(getResources().getDrawable(R.drawable.ic_not_done));
            done_btn.setEnabled(false);
        }
    }


    // this will apped all the videos parts in one  fullvideo
    private boolean append() {
        final ProgressDialog progressDialog = new ProgressDialog(Video_Recoder_A.this);
        new Thread(new Runnable() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    public void run() {

                        progressDialog.setMessage("Please wait..");
                        progressDialog.show();
                    }
                });

                ArrayList<String> video_list = new ArrayList<>();
                for (int i = 0; i < videopaths.size(); i++) {

                    File file = new File(videopaths.get(i));
                    if (file.exists()) {
                        try {
                            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                            retriever.setDataSource(Video_Recoder_A.this, Uri.fromFile(file));
                            String hasVideo = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_VIDEO);
                            boolean isVideo = "yes".equals(hasVideo);

                            if (isVideo && file.length() > 3000) {
                                Log.d("resp", videopaths.get(i));
                                video_list.add(videopaths.get(i));
                                String videoLength;
                                videoLength= String.valueOf(file.length());


                                App.getSingleton().setVideoLength(videoLength);
                            }

                            else {
                                String videoLength;
                                videoLength= String.valueOf(file.length());


                                App.getSingleton().setVideoLength(videoLength);
                            }




//                            long timeInMillisec = Long.parseLong(hasVideo);
//                            long seconds = TimeUnit.MILLISECONDS.toSeconds(timeInMillisec);
//
//                            retriever.release();
//                            if (Integer.parseInt(String.valueOf(seconds)) > 60) {
//                                App.getSingleton().setVideoType("1");
//                            } else {
//                                App.getSingleton().setVideoType("0");
//
//                            }
//                            if (Integer.parseInt(String.valueOf(seconds)) > Variables.GALLERY_SELECTED_VIDEO_LENGTH) {
//                                Toast.makeText(Video_Recoder_A.this, "Video should be less than 5 minutes", Toast.LENGTH_LONG).show();
//                            } else {
//                                App.getSingleton().setGalleryVideo(true);
//                                videoLengthS = String.valueOf(seconds);
//
////                                App.getSingleton().setVideoLength(videoLengthS);
//
////                                startActivity(new Intent(Video_Recoder_A.this, GallerySelectedVideo_A.class).putExtra(PlayerActivity.INTENT_URI, videogallerypath));
//                            }

                        } catch (Exception e) {
                            Log.d(Variables.tag, e.toString());
                        }
                    }
                }

                try {

                    Movie[] inMovies = new Movie[video_list.size()];

                    for (int i = 0; i < video_list.size(); i++) {
                        inMovies[i] = MovieCreator.build(video_list.get(i));
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

                    String outputFilePath = null;
                    if (cameraView.isFacingFront()) {
                        outputFilePath = Variables.output_frontcamera;
                    } else {
                        if (audio != null) {
//                            outputFilePath = Variables.app_folder+"/"+ts+"output.mp4";
                            outputFilePath = Variables.outputfile;
                        } else {
//                            outputFilePath = Variables.app_folder+"/"+ts+"output2.mp4";
                            outputFilePath = Variables.outputfile2;
                        }
                    }
                    FileOutputStream fos = new FileOutputStream(new File(outputFilePath));
                    out.writeContainer(fos.getChannel());
                    fos.close();

                    runOnUiThread(new Runnable() {
                        public void run() {

                            progressDialog.dismiss();

                            if (cameraView.isFacingFront()) {
                                if (audio != null)
                                    Change_fliped_video(Variables.output_frontcamera, Variables.outputfile);
                                else
                                    Change_fliped_video(Variables.output_frontcamera, Variables.outputfile2);
                            } else {
                                if (audio != null)
                                    Merge_withAudio();
                                else {
                                    Go_To_preview_Activity();
                                }
                            }

                        }
                    });


                } catch (Exception e) {
                }
            }
        }).start();


        return true;
    }


    public void Change_fliped_video(String srcMp4Path, final String destMp4Path) {

        Functions.Show_determinent_loader(this, false, false);
        new GPUMp4Composer(srcMp4Path, destMp4Path)
                .flipHorizontal(true)
                .listener(new GPUMp4Composer.Listener() {
                    @Override
                    public void onProgress(double progress) {
                        Log.d("resp", "" + (int) (progress * 100));
                        Functions.Show_loading_progress((int) (progress * 100));
                    }

                    @Override
                    public void onCompleted() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Functions.cancel_determinent_loader();
                                if (audio != null)
                                    Merge_withAudio();
                                else {
                                    Go_To_preview_Activity();
                                }


                            }
                        });
                    }

                    @Override
                    public void onCanceled() {
                        Log.d("resp", "onCanceled");
                    }

                    @Override
                    public void onFailed(Exception exception) {

                        Log.d("resp", exception.toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {

                                    Functions.cancel_determinent_loader();

                                    Toast.makeText(Video_Recoder_A.this, "Try Again", Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                }
                            }
                        });

                    }
                })
                .start();


    }


    // this will add the select audio with the video
    public void Merge_withAudio() {
        String audio_file;
//        audio_file =Variables.app_hided_folder +Variables.SelectedAudio_AAC;
        audio_file = Variables.mp3File;
        Merge_Video_Audio merge_video_audio = new Merge_Video_Audio(Video_Recoder_A.this);
        merge_video_audio.doInBackground(audio_file, Variables.outputfile, Variables.outputfile2);
    }


    public void RotateCamera() {
        cameraView.toggleFacing();

        if (cameraView.getFacing() == CameraKit.Constants.FACING_FRONT)
            cameraView.setScaleX(1);

        CameraProperties properties = cameraView.getCameraProperties();
        Log.d(Variables.tag, properties.verticalViewingAngle + "--" + properties.horizontalViewingAngle);

    }


    public void Remove_Last_Section() {

        if (videopaths.size() > 0) {
            File file = new File(videopaths.get(videopaths.size() - 1));
            if (file.exists()) {

                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(Video_Recoder_A.this, Uri.fromFile(file));
                String hasVideo = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_VIDEO);
                String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                long timeInMillisec = Long.parseLong(time);
                boolean isVideo = "yes".equals(hasVideo);
                if (isVideo) {
                    time_in_milis = time_in_milis - timeInMillisec;
                    video_progress.removeDivider();
                    videopaths.remove(videopaths.size() - 1);
                    video_progress.updateProgress(time_in_milis);
                    video_progress.back_countdown(timeInMillisec);
                    if (audio != null) {
                        int audio_backtime = (int) (audio.getCurrentPosition() - timeInMillisec);
                        audio.seekTo(audio_backtime);
                    }

                    sec_passed = (int) (time_in_milis / 1000);

                    Check_done_btn_enable();

                }
            }

            if (videopaths.isEmpty()) {
                time_layout.setVisibility(View.VISIBLE);
//                findViewById(R.id.time_layout).setVisibility(View.VISIBLE);
                cut_video_btn.setVisibility(View.GONE);
                add_sound_txt.setClickable(true);
                rotate_camera.setVisibility(View.VISIBLE);

                initlize_Video_progress();

                if (audio != null)
                    soundPath = Variables.mp3File;
                PreparedAudio(soundPath);

            }

            file.delete();
        }
    }


    @SuppressLint("WrongConstant")
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.rotate_camera:
                RotateCamera();
                break;

            case R.id.upload_layout:
                Pick_video_from_gallery();
//                Toast.makeText(this, "Under Development", Toast.LENGTH_SHORT).show();
                break;

            case R.id.done:
                append();
                break;

            case R.id.cut_video_btn:
                Functions.Show_Alert1(this, null, "Discard the last clip?", "DELETE", "CANCEL", new Callback() {
                    @Override
                    public void Responce(String resp) {
                        if (resp.equalsIgnoreCase("yes")) {
                            Remove_Last_Section();
                        }
                    }
                });
                break;

            case R.id.flash_camera:
                if (is_flash_on) {
                    is_flash_on = false;
                    cameraView.setFlash(0);
                    flash_btn.setImageDrawable(getResources().getDrawable(R.drawable.ic_flash_on));

                } else {
                    is_flash_on = true;
                    cameraView.setFlash(CameraKit.Constants.FLASH_TORCH);
                    flash_btn.setImageDrawable(getResources().getDrawable(R.drawable.ic_flash_off));
                }

                break;

            case R.id.Goback:
                onBackPressed();
                break;

            case R.id.add_sound_txt:
//                Intent intent =new Intent(this,SoundList_Main_A.class);
//                startActivityForResult(intent,Sounds_list_Request_code);
//                overridePendingTransition(R.anim.in_from_bottom, R.anim.out_to_top);
                Intent intent = new Intent(this, SoundsActivity.class);
                startActivityForResult(intent, Sounds_list_Request_code);
                overridePendingTransition(R.anim.in_from_bottom, R.anim.out_to_top);
                break;

            case R.id.time_btn:
                if (sec_passed + 1 < Variables.recording_duration / 1000) {
                    RecordingTimeRang_F recordingTimeRang_f = new RecordingTimeRang_F(new Fragment_Callback() {
                        @Override
                        public void Responce(Bundle bundle) {
                            if (bundle != null) {
                                is_recording_timer_enable = true;
                                recording_time = bundle.getInt("end_time");
                                countdown_timer_txt.setText("3");
                                countdown_timer_txt.setVisibility(View.VISIBLE);
                                record_image.setClickable(false);
                                final Animation scaleAnimation = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f,
                                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                                new CountDownTimer(4000, 1000) {
                                    @Override
                                    public void onTick(long millisUntilFinished) {

                                        countdown_timer_txt.setText("" + (millisUntilFinished / 1000));
                                        countdown_timer_txt.setAnimation(scaleAnimation);

                                    }

                                    @Override
                                    public void onFinish() {
                                        record_image.setClickable(true);
                                        countdown_timer_txt.setVisibility(View.GONE);
                                        Start_or_Stop_Recording();
                                    }
                                }.start();

                            }
                        }
                    });
                    Bundle bundle = new Bundle();
                    if (sec_passed < (Variables.recording_duration / 1000) - 3)
                        bundle.putInt("end_time", (sec_passed + 3));
                    else
                        bundle.putInt("end_time", (sec_passed + 1));

                    bundle.putInt("total_time", (Variables.recording_duration / 1000));
                    recordingTimeRang_f.setArguments(bundle);
                    recordingTimeRang_f.show(getSupportFragmentManager(), "");
                }
                break;


            case R.id.short_video_time_txt:
                RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                param.addRule(RelativeLayout.CENTER_HORIZONTAL);
                short_video_time_txt.setLayoutParams(param);

                RelativeLayout.LayoutParams param4 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                param4.addRule(RelativeLayout.START_OF, R.id.short_video_time_txt);
                long_video_time_txt.setLayoutParams(param4);

                short_video_time_txt.setTextColor(getResources().getColor(R.color.white));
                long_video_time_txt.setTextColor(getResources().getColor(R.color.grey));

                Variables.recording_duration = 60000;

                initlize_Video_progress();
                break;


            case R.id.long_video_time_txt:
                RelativeLayout.LayoutParams param2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                param2.addRule(RelativeLayout.CENTER_HORIZONTAL);
                long_video_time_txt.setLayoutParams(param2);

                RelativeLayout.LayoutParams param3 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                param3.addRule(RelativeLayout.END_OF, R.id.long_video_time_txt);
                short_video_time_txt.setLayoutParams(param3);

                short_video_time_txt.setTextColor(getResources().getColor(R.color.grey));
                long_video_time_txt.setTextColor(getResources().getColor(R.color.white));

                Variables.recording_duration = 60000;

                initlize_Video_progress();
                break;

        }


    }


    public void Pick_video_from_gallery() {
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        intent.setType("video/*");
        startActivityForResult(intent, Variables.Pick_video_from_gallery);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);




        if (resultCode == RESULT_OK) {

//            if (requestCode == Sounds_list_Request_code) {
//                if (data != null) {
//
//                    if (data.getStringExtra("isSelected").equals("yes")) {
//                        add_sound_txt.setText(data.getStringExtra("sound_name"));
//                        Variables.Selected_sound_id = data.getStringExtra("sound_id");
//                        PreparedAudio();
//                    }
//                }
//            }

            if (requestCode == Sounds_list_Request_code) {
                if (data != null) {
                    if (data.getStringExtra("isSelected").equals("yes")) {
                        add_sound_txt.setText(data.getStringExtra("sound_name"));
                        Variables.Selected_sound_id = data.getStringExtra("sound_id");
                        String soundId = data.getStringExtra("sound_id");
                        String soundName = data.getExtras().getString("sound_name");
                        soundPath = Variables.mp3File;
                        if (data.hasExtra("sound_path")) {
                            soundPath = data.getExtras().getString("sound_path");
                        }
                        App.getSingleton().setSoundName(soundName);
                        App.getSingleton().setSoundId(soundId);
                        PreparedAudio(soundPath);
                    }
                }
            }

            else if (requestCode == Variables.Pick_video_from_gallery) {
                Uri uri = data.getData();

//                String videoLength;
//                videoLength= String.valueOf(file.length());


//                App.getSingleton().setVideoLength(videoLength);
                try {
                    Intent intent = new Intent(this, ActVideoTrimmer.class);;
                    intent.putExtra(TrimmerConstants.TRIM_VIDEO_URI, String.valueOf(uri));
                    intent.putExtra(TrimmerConstants.DESTINATION, Variables.app_hided_folder);
                    intent.putExtra(TrimmerConstants.TRIM_TYPE, 1);
//                    intent.putExtra(TrimmerConstants.FIXED_GAP_DURATION, 60L); //in secs
                    startActivityForResult(intent, TrimmerConstants.REQ_CODE_VIDEO_TRIMMER);




                    /*
                    File video_file = FileUtils.getFileFromUri(this, uri);

                    if (Functions.getfileduration(this,uri) < Variables.max_recording_duration) {
                        Chnage_Video_size(video_file.getAbsolutePath(), Variables.gallery_resize_video);

                    } else {
                        Intent intent=new Intent(Video_Recoder_A.this,Trim_video_A.class);
                        intent.putExtra("path",video_file.getAbsolutePath());
                        startActivity(intent);
                    }
                    */
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            else if (requestCode == TrimmerConstants.REQ_CODE_VIDEO_TRIMMER) {
                String path = data.getStringExtra(TrimmerConstants.TRIMMED_VIDEO_PATH);

                Chnage_Video_size(path, Variables.gallery_resize_video);
            }
        }
    }


    public void Chnage_Video_size(String src_path, String destination_path) {

        try {
            Functions.copyFile(new File(src_path),
                    new File(destination_path));

            File file = new File(src_path);
            if (file.exists())
                file.delete();

            Intent intent = new Intent(Video_Recoder_A.this, GallerySelectedVideo_A.class);
            intent.putExtra("video_path", Variables.gallery_resize_video);
            startActivity(intent);

        } catch (IOException e) {
            e.printStackTrace();
            Log.d(Variables.tag, e.toString());
        }
    }


    public void startTrim(final File src, final File dst, final int startMs, final int endMs) throws IOException {

        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... strings) {
                try {

                    FileDataSourceImpl file = new FileDataSourceImpl(src);
                    Movie movie = MovieCreator.build(file);
                    List<Track> tracks = movie.getTracks();
                    movie.setTracks(new LinkedList<Track>());
                    double startTime = startMs / 1000;
                    double endTime = endMs / 1000;
                    boolean timeCorrected = false;

                    for (Track track : tracks) {
                        if (track.getSyncSamples() != null && track.getSyncSamples().length > 0) {
                            if (timeCorrected) {
                                throw new RuntimeException("The startTime has already been corrected by another track with SyncSample. Not Supported.");
                            }
                            startTime = Functions.correctTimeToSyncSample(track, startTime, false);
                            endTime = Functions.correctTimeToSyncSample(track, endTime, true);
                            timeCorrected = true;
                        }
                    }
                    for (Track track : tracks) {
                        long currentSample = 0;
                        double currentTime = 0;
                        long startSample = -1;
                        long endSample = -1;

                        for (int i = 0; i < track.getSampleDurations().length; i++) {
                            if (currentTime <= startTime) {
                                startSample = currentSample;
                            }
                            if (currentTime <= endTime) {
                                endSample = currentSample;
                            } else {
                                break;
                            }
                            currentTime += (double) track.getSampleDurations()[i] / (double) track.getTrackMetaData().getTimescale();
                            currentSample++;
                        }
                        movie.addTrack(new CroppedTrack(track, startSample, endSample));
                    }

                    Container out = new DefaultMp4Builder().build(movie);
                    MovieHeaderBox mvhd = Path.getPath(out, "moov/mvhd");
                    mvhd.setMatrix(Matrix.ROTATE_180);
                    if (!dst.exists()) {
                        dst.createNewFile();
                    }
                    FileOutputStream fos = new FileOutputStream(dst);
                    WritableByteChannel fc = fos.getChannel();
                    try {
                        out.writeContainer(fc);
                    } finally {
                        fc.close();
                        fos.close();
                        file.close();
                    }

                    file.close();
                    return "Ok";
                } catch (IOException e) {
                    Log.d(Variables.tag, e.toString());
                    return "error";
                }

            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Functions.Show_indeterminent_loader(Video_Recoder_A.this, true, true);
            }

            @Override
            protected void onPostExecute(String result) {
                if (result.equals("error")) {
                    Toast.makeText(Video_Recoder_A.this, "Try Again", Toast.LENGTH_SHORT).show();
                } else {
                    Functions.cancel_indeterminent_loader();
                    // Chnage_Video_size(Variables.gallery_trimed_video, Variables.gallery_resize_video);
                }
            }


        }.execute();
    }


    // this will play the sound with the video when we select the audio
    MediaPlayer audio;

    public void PreparedAudio(String soundPath) {
        File file = new File(soundPath);
        if (file.exists()) {
            audio = new MediaPlayer();
            try {
                audio.setDataSource(soundPath);
                audio.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }

            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(this, Uri.fromFile(file));
            String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            final int file_duration = Integer.parseInt(durationStr);

            App.getSingleton().setVideoLength(durationStr);

            if (file_duration < Variables.max_recording_duration) {
                Variables.recording_duration = file_duration;
                initlize_Video_progress();
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(isDuet){
            camera_layout_duet.start();
        }else {
            cameraView.start();
        }
    }


    @Override
    protected void onDestroy() {
        Release_Resources();
        super.onDestroy();

    }


    public void Release_Resources() {
        try {

            if (audio != null) {
                audio.stop();
                audio.reset();
                audio.release();
            }
            cameraView.stop();

        } catch (Exception e) {

        }
        DeleteFile();
    }


    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this)
                .setTitle("Alert")
                .setMessage("Are you Sure? if you Go back you can't undo this action")
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        Release_Resources();
                        finish();
                        overridePendingTransition(R.anim.in_from_top, R.anim.out_from_bottom);

                    }
                }).show();


    }


    public void Go_To_preview_Activity() {
        Intent intent = new Intent(this, Preview_Video_A.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }


    // this will delete all the video parts that is create during priviously created video
    public void DeleteFile() {

        File output = new File(Variables.outputfile);
        File output2 = new File(Variables.outputfile2);

        File gallery_trimed_video = new File(Variables.gallery_trimed_video);
        File gallery_resize_video = new File(Variables.gallery_resize_video);

        if (output.exists()) {
            output.delete();
        }

        if (output2.exists()) {

            output2.delete();
        }


        if (gallery_trimed_video.exists()) {
            gallery_trimed_video.delete();
        }

        if (gallery_resize_video.exists()) {
            gallery_resize_video.delete();
        }

        for (int i = 0; i <= 12; i++) {

            File file = new File(Variables.app_hided_folder + "myvideo" + (i) + ".mp4");
            if (file.exists()) {
                file.delete();
            }

        }

    }

}
