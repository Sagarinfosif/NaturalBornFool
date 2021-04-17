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

import com.google.android.material.card.MaterialCardView;
import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.activity.HomeActivity;
import in.pureway.cinemaflix.activity.OtherUserProfileActivity;
import in.pureway.cinemaflix.adapters.AdapterOtherFollowers;
import in.pureway.cinemaflix.javaClasses.FollowUnfollowUser;
import in.pureway.cinemaflix.models.ModelFollowers;
import in.pureway.cinemaflix.mvvm.FollowMvvm;
import in.pureway.cinemaflix.utils.AppConstants;
import in.pureway.cinemaflix.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class OtherFollowersFragment extends Fragment implements FollowUnfollowUser.followCallBack {
    private RecyclerView recycler_followers;
    private View view;
    private List<ModelFollowers.Detail> list = new ArrayList<>();
    private FollowMvvm followMvvm;
    private AdapterOtherFollowers adapterFollowers;
    String userId = "";
    private MaterialCardView card_no_followers;
    private int myPosition = 0;

    public OtherFollowersFragment(String userId) {
        this.userId = userId;
    }

    public OtherFollowersFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_other_followers, container, false);
        followMvvm = ViewModelProviders.of(OtherFollowersFragment.this).get(FollowMvvm.class);
        findIds();
        setRecycler();
        return view;
    }

    private void setRecycler() {
        followMvvm.otherFollowersList(getActivity(), userId, CommonUtils.userId(getActivity())).observe(requireActivity(), new Observer<ModelFollowers>() {
            @Override
            public void onChanged(ModelFollowers modelFollowers) {
                if (modelFollowers.getSuccess().equalsIgnoreCase("1")) {
                    Log.i("followers", modelFollowers.getMessage());
                    list = modelFollowers.getDetails();
                    adapterFollowers = new AdapterOtherFollowers(getActivity(), list, new AdapterOtherFollowers.Select() {
                        @Override
                        public void followBack(int position) {
                            followUser(list.get(position).getFollowingUserId(), position);
                        }

                        @Override
                        public void moveToProfile(int position) {
                            if (CommonUtils.userId(getActivity()).equalsIgnoreCase(list.get(position).getUserId())) {
                                startActivity(new Intent(getActivity(), HomeActivity.class).putExtra("fragment", AppConstants.VIDEO_POST));
                            } else {
                                startActivity(new Intent(getActivity(), OtherUserProfileActivity.class).putExtra(AppConstants.OTHER_USER_ID, list.get(position).getUserId()));
                                getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                            }
                        }
                    });
                    recycler_followers.setAdapter(adapterFollowers);
                } else {
                    card_no_followers.setVisibility(View.VISIBLE);
                    recycler_followers.setVisibility(View.GONE);
                    Log.i("followers", modelFollowers.getMessage());
                }
            }
        });
    }

    private void findIds() {
        card_no_followers = view.findViewById(R.id.card_no_followers);
        recycler_followers = view.findViewById(R.id.recycler_followers);
    }

    private void followUser(String otheruserId, final int position) {
        myPosition = position;
        FollowUnfollowUser followUnfollowUser = new FollowUnfollowUser(getActivity(), this);
        followUnfollowUser.followUnfollow(otheruserId);
    }

    @Override
    public void followInterfaceCall(Map map) {
        if (map.get("success").equals("1")) {
            if (map.get("following_status").equals(true)) {
                Intent intent = new Intent(AppConstants.FOLLOW_UNFOLLOW);
                intent.putExtra(AppConstants.FOLLOW_UNFOLLOW, AppConstants.FOLLOW);
                intent.putExtra(AppConstants.POSITION, myPosition);
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
            } else {
                Intent intent = new Intent(AppConstants.FOLLOW_UNFOLLOW);
                intent.putExtra(AppConstants.FOLLOW_UNFOLLOW, AppConstants.UNFOLLOW);
                intent.putExtra(AppConstants.POSITION, myPosition);
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
            }
        } else {
            Log.i("follow", map.get("message").toString());
        }
    }
}