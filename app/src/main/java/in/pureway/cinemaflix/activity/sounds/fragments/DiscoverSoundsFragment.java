package in.pureway.cinemaflix.activity.sounds.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import in.pureway.cinemaflix.activity.videoEditor.util.Variables;
import in.pureway.cinemaflix.adapters.AdapterDiscoverForYou;
import in.pureway.cinemaflix.adapters.AdapterDiscoverGenres;
import in.pureway.cinemaflix.models.ModelFavoriteSounds;
import in.pureway.cinemaflix.models.ModelSounds;
import in.pureway.cinemaflix.mvvm.SoundsMvvm;
import in.pureway.cinemaflix.utils.AppConstants;
import in.pureway.cinemaflix.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class DiscoverSoundsFragment extends Fragment implements Player.EventListener {
    private RecyclerView rv_sounds_for_you_discover, rv_discover_cat;
    private View view;
    private SoundsMvvm soundsMvvm;
    private AdapterDiscoverForYou adapterDiscoverForYou;
    private List<ModelSounds.Detail> list = new ArrayList<>();
    DownloadRequest prDownloader;
    static boolean active = false;
    Context context;
    public static String running_sound_id;

    public DiscoverSoundsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_discover_sounds, container, false);
        context = getActivity();
        running_sound_id = "none";
        PRDownloader.initialize(context);
        soundsMvvm = ViewModelProviders.of(DiscoverSoundsFragment.this).get(SoundsMvvm.class);
        findIds();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setForYourecycler();
//        setCatRecycler();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser) {
            StopPlaying();
        }
    }

    private void setCatRecycler() {
        rv_discover_cat.setAdapter(new AdapterDiscoverGenres(getActivity()));
    }

    private void setForYourecycler() {
        soundsMvvm.getSoundsList(getActivity(), CommonUtils.userId(getActivity()), "").observe(getActivity(), new Observer<ModelSounds>() {
            @Override
            public void onChanged(final ModelSounds modelSounds) {
                if (modelSounds.getSuccess().equalsIgnoreCase("1")) {
                    list = modelSounds.getDetails();
                    Log.i("soundsList1", list.toString());
                    adapterDiscoverForYou = new AdapterDiscoverForYou(getActivity(), list, new AdapterDiscoverForYou.Select() {
                        @Override
                        public void playSound(View view, int position) {

                            if (thread != null && !thread.isAlive()) {
                                StopPlaying();
                                playaudio(view, modelSounds.getDetails().get(position));
                            } else if (thread == null) {
                                StopPlaying();
                                playaudio(view, modelSounds.getDetails().get(position));
                            }
                        }

                        @Override
                        public void download(int position) {
                            StopPlaying();
                            Down_load_mp3(list.get(position).getId(), list.get(position).getTitle(), list.get(position).getSound());
                        }

                        @Override
                        public void addFavorite(int position) {
                            addToFavorite(list.get(position).getId(), position);
                        }
                    });
                    rv_sounds_for_you_discover.setAdapter(adapterDiscoverForYou);
                } else {

                }
                Log.i("soundsList", modelSounds.toString());
            }
        });
    }

    private void addToFavorite(String soundId, final int position) {
        soundsMvvm.addFavoriteSounds(getActivity(), CommonUtils.userId(getActivity()), soundId).observe(getActivity(), new Observer<ModelFavoriteSounds>() {
            @Override
            public void onChanged(ModelFavoriteSounds modelFavoriteSounds) {
                if (modelFavoriteSounds.getSuccess().equalsIgnoreCase("1")) {
                    Log.i("favoriteSounds", modelFavoriteSounds.getMessage());
                    Intent intent = new Intent(AppConstants.SOUNDS_FAVORITES);
                    intent.putExtra(AppConstants.POSITION, position);
                    LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
                } else {
                    Log.i("favoriteSounds", modelFavoriteSounds.getMessage());
                }
            }
        });
    }

    private void findIds() {
        rv_sounds_for_you_discover = view.findViewById(R.id.rv_sounds_for_you_discover);
        rv_discover_cat = view.findViewById(R.id.rv_discover_cat);
    }

    View previous_view;
    Thread thread;
    SimpleExoPlayer player;
    String previous_url = "none";

    public void playaudio(View view, ModelSounds.Detail item) {
        previous_view = view;
        if (previous_url.equals(item.getSound())) {
            previous_url = "none";
            running_sound_id = "none";
        } else {
            previous_url = item.getSound();
            running_sound_id = item.getId();
            DefaultTrackSelector trackSelector = new DefaultTrackSelector();
            player = ExoPlayerFactory.newSimpleInstance(context, trackSelector);

            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,
                    Util.getUserAgent(context, "Photofit"));

            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(Uri.parse(item.getSound()));
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
            previous_view.findViewById(R.id.img_select_sound).setVisibility(View.VISIBLE);
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
            previous_view.findViewById(R.id.img_select_sound).setVisibility(View.GONE);
        }
        running_sound_id = "none";
    }

    public void Down_load_mp3(final String id, final String sound_name, String url) {

        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();

        prDownloader = PRDownloader.download(url, Variables.app_folder, Variables.SelectedAudio_AAC)
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

//                File downloadFile = new File(Variables.app_folder + Variables.SelectedAudio_AAC);
//                if (downloadFile.toString().contains(".mp3")) {
//                    convertAudio(downloadFile,sound_name,id);
//                } else {
                Intent output = new Intent();
                output.putExtra("isSelected", "yes");
                output.putExtra("sound_name", sound_name);
                output.putExtra("sound_id", id);
                getActivity().setResult(RESULT_OK, output);
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.in_from_top, R.anim.out_from_bottom);
//                }
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
}
