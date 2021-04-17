package in.pureway.cinemaflix.fragments.follow.my;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.material.card.MaterialCardView;
import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.activity.OtherUserProfileActivity;
import in.pureway.cinemaflix.adapters.AdapterFollowing;
import in.pureway.cinemaflix.javaClasses.FollowUnfollowUser;
import in.pureway.cinemaflix.models.ModelFollowers;
import in.pureway.cinemaflix.mvvm.FollowMvvm;
import in.pureway.cinemaflix.utils.AppConstants;
import in.pureway.cinemaflix.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FollowingFragment extends Fragment {

    private RecyclerView recycler_following;
    private View view;
    private List<ModelFollowers.Detail> list = new ArrayList<>();
    private FollowMvvm followMvvm;
    private MaterialCardView card_no_following;
    private MaterialCardView search_layout;

//private String userId;

    public FollowingFragment() {
        // Required empty public constructor
    }

    //    public FollowingFragment(String userId) {
//        this.userId = userId;
//    }
    private EditText et_search_following;

    private void textwatcher() {
        et_search_following.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(et_search_following.getText().length() != 1) {
                    setRecycler();

                }
            }
        });
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_following, container, false);
        followMvvm = ViewModelProviders.of(FollowingFragment.this).get(FollowMvvm.class);
        findIds();
        textwatcher();
        return view;
    }

    private void setRecycler() {
        String searchValue = et_search_following.getText().toString().trim();
        if(searchValue.length() < 2){
            searchValue = "";
        }else{
            searchValue = et_search_following.getText().toString().trim();
        }
        followMvvm.getfollowing(getActivity(), CommonUtils.userId(getActivity()),searchValue).observe(getActivity(), new Observer<ModelFollowers>() {
            @Override
            public void onChanged(ModelFollowers modelFollowers) {
                if (modelFollowers.getSuccess().equalsIgnoreCase("1")) {
                    Log.i("followers", modelFollowers.getMessage());
                    card_no_following.setVisibility(View.GONE);
                    recycler_following.setVisibility(View.VISIBLE);
                    list = modelFollowers.getDetails();
                    AdapterFollowing adapterFollowing = new AdapterFollowing(getActivity(), list, new AdapterFollowing.Select() {
                        @Override
                        public void followBack(int position) {
                            followUser(list.get(position).getFollowingUserId(), position);
                        }

                        @Override
                        public void moveToProfile(int position) {

                            startActivity(new Intent(getActivity(), OtherUserProfileActivity.class).putExtra(AppConstants.OTHER_USER_ID, list.get(position).getFollowingUserId()));
                            getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                        }
                    });
                    recycler_following.setAdapter(adapterFollowing);
                } else {
                    card_no_following.setVisibility(View.VISIBLE);
                    recycler_following.setVisibility(View.GONE);
                    search_layout.setVisibility(View.VISIBLE);
                    Log.i("followers", modelFollowers.getMessage());
                }
            }
        });
    }

    @Override
    public void onResume() {
        setRecycler();
        super.onResume();
    }

    private void findIds() {
        card_no_following = view.findViewById(R.id.card_no_following);
        recycler_following = view.findViewById(R.id.recycler_following);
        search_layout = view.findViewById(R.id.search_layout);

        et_search_following = view.findViewById(R.id.et_search_following);
    }

    private void followUser(String otheruserId, final int position) {
        FollowUnfollowUser followUnfollowUser=new FollowUnfollowUser(getActivity(), new FollowUnfollowUser.followCallBack() {
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