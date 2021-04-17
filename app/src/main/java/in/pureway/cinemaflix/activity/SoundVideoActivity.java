package in.pureway.cinemaflix.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import com.downloader.request.DownloadRequest;
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
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.activity.videoEditor.activity.MyVideoEditorActivity;
import in.pureway.cinemaflix.activity.videoEditor.util.Variables;
import in.pureway.cinemaflix.adapters.AdapterSoundDetails;
import in.pureway.cinemaflix.models.GetSoundDetailsPojo;
import in.pureway.cinemaflix.models.ModelFavoriteSounds;
import in.pureway.cinemaflix.mvvm.SoundsMvvm;
import in.pureway.cinemaflix.utils.App;
import in.pureway.cinemaflix.utils.AppConstants;
import in.pureway.cinemaflix.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

public class SoundVideoActivity extends AppCompatActivity implements View.OnClickListener, Player.EventListener {
    private LinearLayout ll_add_sounds_favorites;
    private ImageView img_shoot_sounds, iv_soundThumbnail, img_star_filled;
    private RecyclerView recycler_sounds;
    private SoundsMvvm soundsMvvm;
    private String userId = "0";
    private Activity activity = SoundVideoActivity.this;
    private TextView soundName, soundOwner, noOfPost, tv_fvrtText;
    private String soundId = "";
    private ImageView img_play, img_pause;
    DownloadRequest prDownloader;
    static boolean active = false;
    Context context;
    public static String running_sound_id;
    private GetSoundDetailsPojo.Details.SoundInfo soundInfo;
    private String soundNameValue = "";
    private int startLimit = 0, limit;
    private List<GetSoundDetailsPojo.Details.SoundVideo> list = new ArrayList<>();
    private boolean isScroll = false, hitStatus = false;
    private GridLayoutManager gridLayoutManager;
    int currentItes, totalItem, scrollOutItems;
    private AdapterSoundDetails adapterSoundDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.changeLanguage(SoundVideoActivity.this);
        setContentView(R.layout.activity_sound_video);

        soundsMvvm = ViewModelProviders.of(SoundVideoActivity.this).get(SoundsMvvm.class);
        if (App.getSharedpref().isLogin(activity)) {
            userId = CommonUtils.userId(activity);
        }
        context = SoundVideoActivity.this;
        running_sound_id = "none";
        PRDownloader.initialize(context);
        findIds();
        getSoundDetails();

        gridLayoutManager = new GridLayoutManager(SoundVideoActivity.this, 3);
        recycler_sounds.setLayoutManager(gridLayoutManager);

        adapterSoundDetails = new AdapterSoundDetails(activity, list, new AdapterSoundDetails.Select() {
            @Override
            public void moveToVideo(int position) {
                StopPlaying();
//                            Intent intent = new Intent(activity, SingleVideoPlayerActivity.class);
                Intent intent = new Intent(activity, SelectedVideoActivity.class);
                App.getSingleton().setSoundDetailList(list);
                intent.putExtra(AppConstants.POSITION, position);
//                            intent.putExtra(AppConstants.CHECK_VIDEO, 2);
                intent.putExtra(AppConstants.SINGLE_VIDEO_TYPE, AppConstants.SOUNDS);
                startActivity(intent);
            }
        });
        recycler_sounds.setAdapter(adapterSoundDetails);

        recycler_sounds.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScroll = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItes = gridLayoutManager.getChildCount();
                totalItem = gridLayoutManager.getItemCount();
                scrollOutItems = gridLayoutManager.findFirstVisibleItemPosition();
                if (isScroll && (currentItes + scrollOutItems == limit)) {
//data fetch
                    isScroll = false;
                    if (!hitStatus) {
                        getSoundDetails();
                    } else {
                        Toast.makeText(SoundVideoActivity.this, "Loading more videos", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        StopPlaying();
    }


    private void findIds() {
        img_pause = findViewById(R.id.img_pause);
        img_play = findViewById(R.id.img_play);
        img_star_filled = findViewById(R.id.img_star_filled);
        recycler_sounds = findViewById(R.id.recycler_sounds);
        findViewById(R.id.ll_add_sounds_favorites).setOnClickListener(this);
        findViewById(R.id.img_shoot_sounds).setOnClickListener(this);

        img_pause.setOnClickListener(this);
        img_play.setOnClickListener(this);

        soundName = findViewById(R.id.tv_hashtag_tags);
        soundOwner = findViewById(R.id.tv_soundOwner);
        noOfPost = findViewById(R.id.tv_videos_count);
        iv_soundThumbnail = findViewById(R.id.iv_soundThumbnail);
        tv_fvrtText = findViewById(R.id.tv_fvrtText);
    }

    public void backPress(View view) {
        onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_play:
            case R.id.img_pause:
                if (thread != null && !thread.isAlive()) {
                    StopPlaying();
                    playaudio(v.getRootView(), soundInfo);
                } else if (thread == null) {
                    StopPlaying();
                    playaudio(v.getRootView(), soundInfo);
                }
                break;

            case R.id.img_shoot_sounds:
                Down_load_mp3(soundInfo.getSoundId(), soundInfo.getSoundTitle(), soundInfo.getSoundPath());
                break;

            case R.id.ll_add_sounds_favorites:
                addToFavorite(soundId, 0);
                break;
        }
    }

    private void getSoundDetails() {
        hitStatus = true;
        soundsMvvm.getSoundResults(activity, userId, App.getSingleton().getSoundId(), String.valueOf(startLimit)).observe(SoundVideoActivity.this, new Observer<GetSoundDetailsPojo>() {
            @Override
            public void onChanged(final GetSoundDetailsPojo getSoundDetailsPojo) {
                hitStatus = false;
                if (getSoundDetailsPojo.getSuccess().equalsIgnoreCase("1")) {

                    for (int i = 0; i < getSoundDetailsPojo.getDetails().getSoundVideo().size(); i++) {
                        list.add(getSoundDetailsPojo.getDetails().getSoundVideo().get(i));
                    }
                    limit = list.size();
                    startLimit = limit;

                    soundId = getSoundDetailsPojo.getDetails().getSoundInfo().getSoundId();
                    soundName.setText(getSoundDetailsPojo.getDetails().getSoundInfo().getSoundTitle());
                    soundOwner.setText("Original sound by "+getSoundDetailsPojo.getDetails().getSoundInfo().getUsername());
                    noOfPost.setText(getSoundDetailsPojo.getDetails().getSoundInfo().getCountVideo() + " videos");

                    soundInfo = getSoundDetailsPojo.getDetails().getSoundInfo();
                    if (getSoundDetailsPojo.getDetails().getSoundInfo().getFavoritesStatus().equalsIgnoreCase("1")) {
                        tv_fvrtText.setText(R.string.removeFromFvrt);
                        Glide.with(SoundVideoActivity.this).load(getResources().getDrawable(R.drawable.ic_starfilled)).into(img_star_filled);
                    } else {
                        tv_fvrtText.setText(R.string.addToFvrt);
                    }
                    Glide.with(activity).load(getSoundDetailsPojo.getDetails().getSoundInfo().getVideoPath()).placeholder(getResources().getDrawable(R.drawable.logo)).into(iv_soundThumbnail);


                } else {
                    Toast.makeText(activity, getSoundDetailsPojo.getMessage(), Toast.LENGTH_SHORT).show();
                }

                adapterSoundDetails.notifyDataSetChanged();
            }
        });

    }

    private void addToFavorite(String soundId, final int position) {
        SoundsMvvm soundsMvvm = ViewModelProviders.of(SoundVideoActivity.this).get(SoundsMvvm.class);
        soundsMvvm.addFavoriteSounds(activity, userId, soundId).observe(SoundVideoActivity.this, new Observer<ModelFavoriteSounds>() {
            @Override
            public void onChanged(ModelFavoriteSounds modelFavoriteSounds) {
                if (modelFavoriteSounds.getSuccess().equalsIgnoreCase("1")) {
                    Log.i("favoriteSounds", modelFavoriteSounds.getMessage());
                    if (tv_fvrtText.getText().toString().equalsIgnoreCase(getResources().getString(R.string.addToFvrt))) {
                        tv_fvrtText.setText(getResources().getString(R.string.removeFromFvrt));
                        Glide.with(activity).load(getResources().getDrawable(R.drawable.ic_starfilled)).into(img_star_filled);
                    } else {
                        tv_fvrtText.setText(getResources().getString(R.string.addToFvrt));
                        Glide.with(activity).load(getResources().getDrawable(R.drawable.ic_star)).into(img_star_filled);
                    }
//                    getSoundDetails();
                } else {
                    Log.i("favoriteSounds", modelFavoriteSounds.getMessage());
                }
            }
        });
    }

    View previous_view;
    Thread thread;
    SimpleExoPlayer player;
    String previous_url = "none";

    public void playaudio(View view, GetSoundDetailsPojo.Details.SoundInfo item) {
        previous_view = view;
        if (previous_url.equals(item.getSoundPath())) {
            previous_url = "none";
            running_sound_id = "none";
        } else {
            previous_url = item.getSoundPath();
            running_sound_id = item.getSoundId();
            DefaultTrackSelector trackSelector = new DefaultTrackSelector();
            player = ExoPlayerFactory.newSimpleInstance(context, trackSelector);

            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,
                    Util.getUserAgent(context, "Cinemaflix"));

            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(Uri.parse(item.getSoundPath()));
            player.prepare(videoSource);
            player.addListener(this);
            player.setPlayWhenReady(true);
        }
    }

    public void StopPlaying() {
        if (player != null) {
            player.setPlayWhenReady(false);
            player.removeListener(this);
            player.release();
        }
        show_Stop_state();
    }

    @Override
    public void onStart() {
        super.onStart();
        active = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        active = false;
        running_sound_id = "null";
        if (player != null) {
            player.setPlayWhenReady(false);
            player.removeListener(this);
            player.release();
        }
        show_Stop_state();
    }

    public void Show_Run_State() {
        if (previous_view != null) {
            previous_view.findViewById(R.id.loading_progress).setVisibility(View.GONE);
            previous_view.findViewById(R.id.img_pause).setVisibility(View.VISIBLE);
        }
    }

    public void Show_loading_state() {
        previous_view.findViewById(R.id.img_play).setVisibility(View.GONE);
        previous_view.findViewById(R.id.loading_progress).setVisibility(View.VISIBLE);
    }

    public void show_Stop_state() {
        if (previous_view != null) {
            previous_view.findViewById(R.id.img_play).setVisibility(View.VISIBLE);
            previous_view.findViewById(R.id.loading_progress).setVisibility(View.GONE);
            previous_view.findViewById(R.id.img_pause).setVisibility(View.GONE);
        }
        running_sound_id = "none";
    }

    public void Down_load_mp3(final String id, final String sound_name, String url) {

        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();

        prDownloader = PRDownloader.download(url, Variables.app_folder, Variables.downloadedmp3File)
                .build()
                .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                    @Override
                    public void onStartOrResume() {

                    }
                })
                .setOnPauseListener(new OnPauseListener() {
                    @Override
                    public void onPause() {

                    }
                })
                .setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel() {

                    }
                })
                .setOnProgressListener(new OnProgressListener() {
                    @Override
                    public void onProgress(Progress progress) {

                    }
                });

        prDownloader.start(new OnDownloadListener() {
            @Override
            public void onDownloadComplete() {
                progressDialog.dismiss();
                startActivity(new Intent(SoundVideoActivity.this, MyVideoEditorActivity.class)
                        .putExtra(AppConstants.SOUND_ID, id)
                        .putExtra(AppConstants.SOUND_NAME, sound_name));
            }

            @Override
            public void onError(Error error) {
                progressDialog.dismiss();
            }
        });

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
            Show_loading_state();
        } else if (playbackState == Player.STATE_READY) {
            Show_Run_State();
        } else if (playbackState == Player.STATE_ENDED) {
            show_Stop_state();
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
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}