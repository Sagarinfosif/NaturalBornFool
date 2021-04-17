package in.pureway.cinemaflix.activity.sounds.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import in.pureway.cinemaflix.adapters.AdapterDeviceSounds;
import in.pureway.cinemaflix.models.AudioModel;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class SoundsDeviceFragment extends Fragment implements Player.EventListener {
    private View view;
    public static String running_sound_id;
    static boolean active = false;

    public SoundsDeviceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_device, container, false);
        getAllAudioFromDevice();
        return view;
    }

    private void getAllAudioFromDevice() {
        final List<AudioModel> tempAudioList = new ArrayList<>();
        Cursor c = getActivity().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        if (c != null) {
            while (c.moveToNext()) {
                AudioModel audioModel = new AudioModel();
                String path = c.getString(c.getColumnIndex(MediaStore.Audio.AudioColumns.DATA));
                String name = c.getString(c.getColumnIndex(MediaStore.Audio.AudioColumns.TITLE));
                String artist = c.getString(c.getColumnIndex(MediaStore.Audio.ArtistColumns.ARTIST));

                audioModel.setaName(name);
                audioModel.setaArtist(artist);
                audioModel.setaPath(path);
                tempAudioList.add(audioModel);
            }
            c.close();
        }
        RecyclerView rv_sounds_device = view.findViewById(R.id.rv_sounds_device);
        AdapterDeviceSounds adapterDeviceSounds = new AdapterDeviceSounds(getActivity(), tempAudioList, new AdapterDeviceSounds.Select() {
            @Override
            public void playSounds(View view, int position) {

                if (thread != null && !thread.isAlive()) {
                    StopPlaying();
                    playaudio(view, tempAudioList.get(position).getaPath());
                } else if (thread == null) {
                    StopPlaying();
                    playaudio(view, tempAudioList.get(position).getaPath());
                }
            }

            @Override
            public void selectSound(String title, String path) {
                StopPlaying();
                Intent output = new Intent();
                output.putExtra("isSelected", "yes");
                output.putExtra("sound_name", title);
                output.putExtra("sound_id", "");
                output.putExtra("sound_path", path);
                getActivity().setResult(RESULT_OK, output);
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.in_from_top, R.anim.out_from_bottom);
            }
        });
        rv_sounds_device.setAdapter(adapterDeviceSounds);
    }

    public void playaudio(View view, String path) {
        previous_view = view;
        if (previous_url.equals(path)) {
            previous_url = "none";
            running_sound_id = "none";
        } else {
            previous_url = path;
            running_sound_id = path;
            DefaultTrackSelector trackSelector = new DefaultTrackSelector();
            player = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector);
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getActivity(), Util.getUserAgent(getActivity(), "Photofit"));
            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(path));
            player.prepare(videoSource);
            player.addListener(this);
            player.setPlayWhenReady(true);
        }
    }


    View previous_view;
    Thread thread;
    SimpleExoPlayer player;
    String previous_url = "none";

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