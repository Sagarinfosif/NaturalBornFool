package in.pureway.cinemaflix.fragments.follow.other;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.activity.HomeActivity;
import in.pureway.cinemaflix.activity.OtherUserProfileActivity;
import in.pureway.cinemaflix.adapters.AdapterFollowing;
import in.pureway.cinemaflix.adapters.AdapterOtherFollowing;
import in.pureway.cinemaflix.javaClasses.FollowUnfollowUser;
import in.pureway.cinemaflix.models.ModelFollowers;
import in.pureway.cinemaflix.mvvm.FollowMvvm;
import in.pureway.cinemaflix.utils.AppConstants;
import in.pureway.cinemaflix.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OtherFollowingFragment extends Fragment {
    private FollowMvvm followMvvm;
    private View view;
    private List<ModelFollowers.Detail> list = new ArrayList<>();
    private RecyclerView recycler_following;
    String otherUserId;
    private AdapterOtherFollowing adapterFollowing;
    private MaterialCardView card_no_following;

    public OtherFollowingFragment() {
    }

    public OtherFollowingFragment(String otherUserId) {
        this.otherUserId = otherUserId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_other_following, container, false);
        followMvvm = ViewModelProviders.of(OtherFollowingFragment.this).get(FollowMvvm.class);
        findIds();
        return view;
    }

    @Override
    public void onResume() {
        setRecycler();
        super.onResume();
    }

    private void setRecycler() {

        followMvvm.otherFollowingList(getActivity(), otherUserId, CommonUtils.userId(getActivity())).observe(getActivity(), new Observer<ModelFollowers>() {
            @Override
            public void onChanged(ModelFollowers modelFollowers) {
                Log.i("otherFollowingList", modelFollowers.getMessage());
                if (modelFollowers.getSuccess().equalsIgnoreCase("1")) {
                    list = modelFollowers.getDetails();
                    adapterFollowing = new AdapterOtherFollowing(getActivity(), list, new AdapterFollowing.Select() {
                        @Override
                        public void followBack(int position) {
                            followUser(list.get(position).getFollowingUserId(), position);
                        }

                        @Override
                        public void moveToProfile(int position) {
                            if (CommonUtils.userId(getActivity()).equalsIgnoreCase(list.get(position).getFollowingUserId())) {
                                startActivity(new Intent(getActivity(), HomeActivity.class).putExtra("fragment", AppConstants.VIDEO_POST));
                            } else {
                                startActivity(new Intent(getActivity(), OtherUserProfileActivity.class).putExtra(AppConstants.OTHER_USER_ID, list.get(position).getUserId()));
                            }
                        }
                    });
                    recycler_following.setAdapter(adapterFollowing);
                } else {
                    card_no_following.setVisibility(View.VISIBLE);
                    recycler_following.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), modelFollowers.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void findIds() {
        card_no_following = view.findViewById(R.id.card_no_following);
        recycler_following = view.findViewById(R.id.recycler_following);
    }

    private void followUser(String otheruserId, final int position) {
        FollowUnfollowUser followUnfollowUser = new FollowUnfollowUser(getActivity(), new FollowUnfollowUser.followCallBack() {
            @Override
            public void followInterfaceCall(Map map) {
                if (map.get("success").equals("1")) {
                    if (map.get("following_status").equals(true)) {
                        Intent intent = new Intent(AppConstants.FOLLOW_UNFOLLOW);
                        intent.putExtra(AppConstants.FOLLOW_UNFOLLOW, AppConstants.FOLLOW);
                        intent.putExtra(AppConstants.POSITION, position);
                        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
                    } else {
                        Intent intent = new Intent(AppConstants.FOLLOW_UNFOLLOW);
                        intent.putExtra(AppConstants.FOLLOW_UNFOLLOW, AppConstants.UNFOLLOW);
                        intent.putExtra(AppConstants.POSITION, position);
                        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
                    }
                } else {
                    Log.i("follow", map.get("message").toString());
                }
            }
        });
        followUnfollowUser.followUnfollow(otheruserId);
    }
}