package in.pureway.cinemaflix.fragments.favorites;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.card.MaterialCardView;
import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.activity.HashTagVideoActivity;
import in.pureway.cinemaflix.adapters.AdapterHashTagsSaved;
import in.pureway.cinemaflix.models.ModelFavoriteHashTag;
import in.pureway.cinemaflix.mvvm.SoundsMvvm;
import in.pureway.cinemaflix.utils.AppConstants;
import in.pureway.cinemaflix.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HastagsSavedFragment extends Fragment {
    private View view;
    private RecyclerView recycler_Saved_hastags;
    private String userId = CommonUtils.userId(getActivity());
    List<ModelFavoriteHashTag.Detail> list = new ArrayList<>();
    private MaterialCardView card_no_hashtags;

    public HastagsSavedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_hastags_saved, container, false);
        findIds();
        setRecycler();
        return view;
    }

    private void setRecycler() {
        SoundsMvvm soundsMvvm = ViewModelProviders.of(HastagsSavedFragment.this).get(SoundsMvvm.class);
        soundsMvvm.favHashList(getActivity(), userId).observe(getActivity(), new Observer<ModelFavoriteHashTag>() {
            @Override
            public void onChanged(ModelFavoriteHashTag modelFavoriteHashTag) {
                if (modelFavoriteHashTag.getSuccess().equalsIgnoreCase("1")) {
                    Log.i("favHashList", modelFavoriteHashTag.getMessage());
                    list = modelFavoriteHashTag.getDetails();
                    AdapterHashTagsSaved adapterHashTagsSaved = new AdapterHashTagsSaved(getActivity(), list, new AdapterHashTagsSaved.Select() {
                        @Override
                        public void moveToHashTag(int position) {
                            startActivity(new Intent(getActivity(), HashTagVideoActivity.class)
                                    .putExtra(AppConstants.HASHTAG_ID, list.get(position).getHashtagId()));
                        }
                    });
                    recycler_Saved_hastags.setAdapter(adapterHashTagsSaved);
                } else {
                    card_no_hashtags.setVisibility(View.VISIBLE);
                    recycler_Saved_hastags.setVisibility(View.GONE);
                }
            }
        });
    }

    private void findIds() {
        card_no_hashtags = view.findViewById(R.id.card_no_hashtags);
        recycler_Saved_hastags = view.findViewById(R.id.recycler_Saved_hastags);
    }
}
