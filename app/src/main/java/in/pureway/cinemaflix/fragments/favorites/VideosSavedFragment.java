package in.pureway.cinemaflix.fragments.favorites;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.card.MaterialCardView;
import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.activity.SelectedVideoActivity;
import in.pureway.cinemaflix.adapters.AdapterLikedVideos;
import in.pureway.cinemaflix.models.ModelLikedVideos;
import in.pureway.cinemaflix.mvvm.VideoMvvm;
import in.pureway.cinemaflix.utils.App;
import in.pureway.cinemaflix.utils.AppConstants;
import in.pureway.cinemaflix.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideosSavedFragment extends Fragment {
    private View view;
    private RecyclerView recycler_videos_saved;
    private VideoMvvm videoMvvm;
    private List<ModelLikedVideos.Detail> list = new ArrayList<>();
    private MaterialCardView card_no_videos;

    public VideosSavedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_videos_saved, container, false);
        videoMvvm = ViewModelProviders.of(VideosSavedFragment.this).get(VideoMvvm.class);
        findIds();
        setLikedRecycler();
        return view;
    }

    private void setLikedRecycler() {
        String userId = CommonUtils.userId(getActivity());
        videoMvvm.getLikedVideos(getActivity(), userId).observe(getActivity(), new Observer<ModelLikedVideos>() {
            @Override
            public void onChanged(final ModelLikedVideos modelLikedVideos) {
                if (modelLikedVideos.getSuccess().equalsIgnoreCase("1")) {
                    list = modelLikedVideos.getDetails();
                    AdapterLikedVideos adapterLikedVideos = new AdapterLikedVideos(getActivity(), list, new AdapterLikedVideos.Select() {
                        @Override
                        public void video(int position) {
//                            Intent intent = new Intent(getActivity(), SingleVideoPlayerActivity.class);
                            Intent intent = new Intent(getActivity(), SelectedVideoActivity.class);
                            App.getSingleton().setLikedVideoList(list);
                            intent.putExtra(AppConstants.POSITION, position);
                            intent.putExtra(AppConstants.SINGLE_VIDEO_TYPE, AppConstants.TYPE_LIKED);
                            startActivity(intent);
                        }
                    });
                    recycler_videos_saved.setAdapter(adapterLikedVideos);
                } else {
                    recycler_videos_saved.setVisibility(View.GONE);
                    card_no_videos.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void findIds() {
        recycler_videos_saved = view.findViewById(R.id.recycler_videos_saved);
        card_no_videos = view.findViewById(R.id.card_no_videos);
    }
}
