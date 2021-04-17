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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.activity.OtherUserProfileActivity;
import in.pureway.cinemaflix.adapters.AdapterFollowers;
import in.pureway.cinemaflix.javaClasses.FollowUnfollowUser;
import in.pureway.cinemaflix.models.ModelFollowers;
import in.pureway.cinemaflix.mvvm.FollowMvvm;
import in.pureway.cinemaflix.utils.AppConstants;
import in.pureway.cinemaflix.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class FollowersFragment extends Fragment {

    private RecyclerView recycler_followers;
    private View view;
    private List<ModelFollowers.Detail> list = new ArrayList<>();
    private FollowMvvm followMvvm;
    private AdapterFollowers adapterFollowers;
    private MaterialCardView card_no_followers;
    private String userId= CommonUtils.userId(getActivity());
    private MaterialCardView search_layout;

    public FollowersFragment() {
        // Required empty public constructor
    }
    private EditText et_search_followers;

    private void textwatcher() {
        et_search_followers.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(et_search_followers.getText().length() != 1) {
                    setRecycler();

                }
            }
        });
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_followers, container, false);
        followMvvm = ViewModelProviders.of(FollowersFragment.this).get(FollowMvvm.class);
        findIds();
        textwatcher();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setRecycler();
    }

    private void setRecycler() {
        String searchValue = et_search_followers.getText().toString().trim();
        if(searchValue.length() < 2){
            searchValue = "";
        }else{
            searchValue = et_search_followers.getText().toString().trim();
        }
        followMvvm.getFollowers(getActivity(), userId,searchValue).observe(getActivity(), new Observer<ModelFollowers>() {
            @Override
            public void onChanged(ModelFollowers modelFollowers) {
                if (modelFollowers.getSuccess().equalsIgnoreCase("1")) {
                    Log.i("followers", modelFollowers.getMessage());
                    list = modelFollowers.getDetails();
                    card_no_followers.setVisibility(View.GONE);
                    recycler_followers.setVisibility(View.VISIBLE);
                    adapterFollowers = new AdapterFollowers(getActivity(), list, new AdapterFollowers.Select() {
                        @Override
                        public void removeFollower(int position, View view) {
                            popupMenu(position, view);
                        }

                        @Override
                        public void followBack(int position) {
                            followUser(list.get(position).getUserId(), position);
                        }

                        @Override
                        public void moveToProfile(int position) {
                            Toast.makeText(getActivity(), list.get(position).getUsername(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getActivity(), OtherUserProfileActivity.class).putExtra(AppConstants.OTHER_USER_ID, list.get(position).getUserId()));
                            getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                        }

                    });
                    recycler_followers.setAdapter(adapterFollowers);
                } else {
                    card_no_followers.setVisibility(View.VISIBLE);
                    recycler_followers.setVisibility(View.GONE);
                    search_layout.setVisibility(View.VISIBLE);

                    Log.i("followers", modelFollowers.getMessage());
                }
            }
        });
    }

    private void popupMenu(final int position, View view) {
        final PopupMenu popup = new PopupMenu(getActivity(), view);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_cancel:
                        popup.dismiss();
                        break;

                    case R.id.menu_remove_follower:
                        removeFollower(position, popup);
                        break;
                }
                return true;
            }
        });
        popup.inflate(R.menu.menu_follower);
        popup.show();
    }

    private void removeFollower(final int position, final PopupMenu popupMenu) {
        followMvvm.deleteFollower(getActivity(), list.get(position).getId()).observe(getActivity(), new Observer<Map>() {
            @Override
            public void onChanged(Map map) {
                if (map.get("success").equals("1")) {
                    list.remove(position);
                    adapterFollowers.notifyDataSetChanged();
                    popupMenu.dismiss();

                } else {
                    Log.i("deleteFollower", map.get("message").toString());
                }
            }
        });
    }

    private void findIds() {
        card_no_followers = view.findViewById(R.id.card_no_followers);
        recycler_followers = view.findViewById(R.id.recycler_followers);
        et_search_followers = view.findViewById(R.id.et_search_followers);
        search_layout = view.findViewById(R.id.search_layout);
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