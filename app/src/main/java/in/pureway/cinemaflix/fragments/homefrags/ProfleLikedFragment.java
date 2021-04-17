package in.pureway.cinemaflix.fragments.homefrags;

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

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfleLikedFragment extends Fragment {
    private RecyclerView recycler_liked_videos_profile;
    private View view;
    private List<ModelLikedVideos.Detail> list = new ArrayList<>();
    private VideoMvvm videoMvvm;
    String userId;
    private MaterialCardView card_no_videos;

    public ProfleLikedFragment() {
        // Required empty public constructor
    }

    public ProfleLikedFragment(String userId) {
        this.userId = userId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profle_liked, container, false);
        videoMvvm = ViewModelProviders.of(ProfleLikedFragment.this).get(VideoMvvm.class);
        findiDs();
        return view;
    }

    private void findiDs() {
        card_no_videos = view.findViewById(R.id.card_no_videos);
        recycler_liked_videos_profile = view.findViewById(R.id.recycler_liked_videos_profile);
    }

    @Override
    public void onResume() {
        getUserVisibleHint();
        super.onResume();
    }

    private void setLikedRecycler() {
        videoMvvm.getLikedVideos(getActivity(), userId).observe(getActivity(), new Observer<ModelLikedVideos>() {
            @Override
            public void onChanged(final ModelLikedVideos modelLikedVideos) {
                if (modelLikedVideos.getSuccess().equalsIgnoreCase("1")) {
                    list = modelLikedVideos.getDetails();
                    AdapterLikedVideos adapterLikedVideos = new AdapterLikedVideos(getActivity(), list, new AdapterLikedVideos.Select() {
                        @Override
                        public void video(int position) {
                            Intent intent = new Intent(getActivity(), SelectedVideoActivity.class);
                            App.getSingleton().setLikedVideoList(list);
                            intent.putExtra(AppConstants.POSITION, position);
                            intent.putExtra(AppConstants.SINGLE_VIDEO_TYPE, AppConstants.TYPE_LIKED);
                            startActivity(intent);
                        }
                    });
                    recycler_liked_videos_profile.setAdapter(adapterLikedVideos);
                } else {
                    recycler_liked_videos_profile.setVisibility(View.GONE);
                    card_no_videos.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            setLikedRecycler();
        }
    }
}
